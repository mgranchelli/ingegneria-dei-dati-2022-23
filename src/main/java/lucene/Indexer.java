package lucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Cell;
import models.Table;

public class Indexer {

	private int tablesNumber;
	private GlobalVariables gv;
	
	public Indexer() throws IOException {
		this.tablesNumber = 0;
		this.gv = new GlobalVariables();
		
        Path path = Paths.get(this.gv.getPathIndex());

        try (Directory directory = FSDirectory.open(path)) {
            indexDocs(directory, new SimpleTextCodec());
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void indexDocs(Directory directory, Codec codec) {
		long startTime = System.currentTimeMillis();
		long endTime;

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();

		System.out.println("Indexing...");
		try (BufferedReader br = new BufferedReader(new FileReader(this.gv.getPathInputFile()))) {
			Iterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
			Analyzer defaultAnalyzer = new StandardAnalyzer();
			Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
			perFieldAnalyzers.put("tableContent", new TableAnalyzer());

			Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			if (codec != null) {
				config.setCodec(codec);
			}
			IndexWriter writer = new IndexWriter(directory, config);

			writer.deleteAll();

			value.forEachRemaining((u) -> {
				if (this.tablesNumber % 2500 == 0) {
					if (this.tablesNumber % 10000 == 0) {
						System.out.println("Current table: " + this.tablesNumber);
					}
					System.out.println("...");
				}
				
				StringBuilder stringBuilder = new StringBuilder();

				for (Cell c : u.getCells()) {
					stringBuilder.append(c.getCleanedText());
					stringBuilder.append(" ");
				}
				
				Document doc = new Document();
                doc.add(new TextField("tableContent", stringBuilder.toString(), Field.Store.YES));
                try {
					writer.addDocument(doc);
				} catch (IOException e) {
					e.printStackTrace();
				}

                this.tablesNumber++;

			});

			System.out.println("Total processed tables: " + this.tablesNumber);

			writer.commit();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		endTime = System.currentTimeMillis();
		System.out.println("Time to indexing: " + (endTime - startTime) / 1000F + " seconds");
	}

}
