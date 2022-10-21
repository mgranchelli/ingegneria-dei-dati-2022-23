package lucenex;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class Indexer {
	
	private static CharArraySet enStopSet = EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;
	private static CharArraySet stopWords = new CharArraySet(Arrays.asList("txt"), true);
	public static final StandardAnalyzer analyzerContenuto = new StandardAnalyzer(enStopSet);
	public static final StopAnalyzer analyzerNome = new StopAnalyzer(stopWords);
	public static final String pathIndex = "target/idx";
	
	
	public void indexDocs(Directory directory, Codec codec) throws IOException {
		long startTime = System.currentTimeMillis();
		long endTime;
		
		System.out.println("Indexing...");
		
		Path docDir = Paths.get("inputFiles");
		Analyzer defaultAnalyzer = new StandardAnalyzer();
		Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
		perFieldAnalyzers.put("contenuto", new StandardAnalyzer(enStopSet));
		perFieldAnalyzers.put("nome", new StopAnalyzer(stopWords));

		Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		if (codec != null) {
			config.setCodec(codec);
		}
		IndexWriter writer = new IndexWriter(directory, config);
		writer.deleteAll();

		// Is directory?
		if (Files.isDirectory(docDir)) {

			// Iterate directory
			Files.walkFileTree(docDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					// Index this file
					indexDoc(writer, file);
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			// Index this file
			indexDoc(writer, docDir);
		}

		writer.commit();
		writer.close();
		
		endTime = System.currentTimeMillis();
		System.out.println("Time to indexing: " + (endTime - startTime) / 1000F + " seconds");
	}

	private void indexDoc(IndexWriter writer, Path file) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {
			// Create lucene Document
			Document doc = new Document();
			doc.add(new TextField("nome", file.getFileName().toString(), Field.Store.YES));
			doc.add(new TextField("contenuto", new String(Files.readAllBytes(file)), Field.Store.YES));
			writer.addDocument(doc);
		}
	}
	
	/*************************** TEST ***************************/
	
	// Analyzer generated tokens 
	private List<String> analyze(String text, Analyzer analyzer) throws IOException {
        List<String> result = new ArrayList<String>();
        TokenStream tokenStream = analyzer.tokenStream("test", text);
        CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            result.add(attr.toString());
        }
        return result;
	}
	
	@Test
	public void testGeneratedTokens() throws Exception {
		CharArraySet stopWords = new CharArraySet(Arrays.asList("txt"), true);
		List<String> result = analyze("Ant-Man.And.The.Wasp.txt", new StopAnalyzer(stopWords));
        List<String> test = Arrays.asList("ant", "man", "and", "the", "wasp");
        
        assertEquals(result, test);
	}
	
	
	@Test
	public void testIndexStatistics() throws Exception {
		Path path = Paths.get(pathIndex);

		try (Directory directory = FSDirectory.open(path)) {
			indexDocs(directory, new SimpleTextCodec());
			try (IndexReader reader = DirectoryReader.open(directory)) {
				IndexSearcher searcher = new IndexSearcher(reader);
				Collection<String> indexedFields = FieldInfos.getIndexedFields(reader);
				for (String field : indexedFields) {
					System.out.println(searcher.collectionStatistics(field));
				}
			} finally {
				directory.close();
			}

		}
	}

}
