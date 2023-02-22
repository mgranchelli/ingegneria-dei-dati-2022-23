package lucenex;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class QueryIndex {

	public void generateQuery(String key, String inputQuery) throws Exception {

		Indexer indexer = new Indexer();
		Path path = Paths.get(Indexer.pathIndex);
		QueryParser parser;
		Query query;

		if ((inputQuery.charAt(0) == '"') & (inputQuery.charAt(inputQuery.length() - 1) == '"')) {
			System.out.println("Phrase query");
		}
		
		if (key.equals("nome")) {
			parser = new QueryParser(key, Indexer.analyzerNome);
		}
		else {
			parser = new QueryParser(key, Indexer.analyzerContenuto);
		}

		query = parser.parse(inputQuery);
//		System.out.println(query);

		try (Directory directory = FSDirectory.open(path)) {
			indexer.indexDocs(directory, null);
			try (IndexReader reader = DirectoryReader.open(directory)) {
				IndexSearcher searcher = new IndexSearcher(reader);
				runQuery(searcher, query, false);
			} finally {
				directory.close();
			}

		}
	}


	private void runQuery(IndexSearcher searcher, Query query, boolean explain) throws IOException {
		
		long startTime = System.currentTimeMillis();
		TopDocs hits = searcher.search(query, 10);
		long endTime = System.currentTimeMillis();
		System.out.println("Time to query: " + (endTime - startTime) / 1000F + " seconds");
		if (hits.scoreDocs.length > 0) {
			System.out.println("\nTotal docs: " + hits.scoreDocs.length);
		}
		else {
			System.out.println("\nNo result!");;
		}
		
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.println("- doc" + scoreDoc.doc + ": " + doc.get("nome") + " (" + scoreDoc.score + ")");
			if (explain) {
				Explanation explanation = searcher.explain(query, scoreDoc.doc);
				System.out.println(explanation);
			}
		}
	}
	
	
	/*************************** TEST ***************************/
	
	@Test
    public void testIndexingAndSearchAllWithCodec() throws Exception {
        Path path = Paths.get(Indexer.pathIndex);
        Indexer indexer = new Indexer();
        Query query = new MatchAllDocsQuery();

        try (Directory directory = FSDirectory.open(path)) {
            indexer.indexDocs(directory, new SimpleTextCodec());
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher, query, false);
            } finally {
                directory.close();
            }

        }
    }
}
