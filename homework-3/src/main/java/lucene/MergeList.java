package lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import statistics.SortMapByValue;

public class MergeList {
	
	private IndexSearcher searcher;
	
	public MergeList(IndexSearcher searcher) {
		this.searcher = searcher;
	}
	
	public Integer searchAzienda(String aziendaName) throws IOException, ParseException {
		// long start = System.currentTimeMillis();
		
		QueryParser parser = new QueryParser("tableContent", new StandardAnalyzer());
        Query query = parser.parse(aziendaName.toLowerCase().toString());
        TopDocs hits = searcher.search(query, 2000000);
        
        System.out.println("Totale dei documenti trovati: " + hits.scoreDocs.length);
        
        if (hits.scoreDocs.length != 0) {
        	System.out.println("Il documento [" + hits.scoreDocs[0].doc + "] ha score più alta!");
        	// System.out.println("Total Time Taken : " + (System.currentTimeMillis() - start) + " ms\n");
        	return hits.scoreDocs[0].doc;
        }
        else {
        	return null;
        }  
	}
	
	public void run(String inputString, int K) throws IOException {
		long start = System.currentTimeMillis();

		HashMap<String, List<Integer>> invertedIndex = generateInvertedIndex(inputString);
		Map<Integer, Integer> sortedSet2count = getSortedSet2count(invertedIndex);

		System.out.println("Total Time Taken : " + (System.currentTimeMillis() - start) + " ms\n");

		for (Map.Entry<Integer, Integer> entry : sortedSet2count.entrySet()) {
			if (entry.getValue() >= K) {
				System.out.println("Table (document) number: " + entry.getKey().toString() + 
						" K: " + entry.getValue().toString());
			} else {
				break;
			}

		}
	}

	private Set<String> tokenizeString(TableAnalyzer analyzer, String inputString) {
		Set<String> tokens = new HashSet<>();
		try (TokenStream tokenStream = analyzer.tokenStream("tableContent", new StringReader(inputString))) {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				tokens.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
			}
		} catch (IOException e) {
			new RuntimeException(e);
		}
		return tokens;
	}

	public HashMap<String, List<Integer>> generateInvertedIndex(String inputString) throws IOException {
		Set<String> tokens = tokenizeString(new TableAnalyzer(), inputString);


		HashMap<String, List<Integer>> map = new HashMap<>();
		System.out.println("\nTokens: " + tokens + "\n");

		for (String token : tokens) {
			System.out.println("Executing token: " + token);

			TopDocs hits = this.searcher.search(new TermQuery(new Term("tableContent", token)), 20000000);
			System.out.println("Number of table found for token (\"" + token + "\"): " + hits.scoreDocs.length);

			List<Integer> docList = new ArrayList<>();
			for (ScoreDoc hit : hits.scoreDocs) {
				docList.add(hit.doc);
				map.put(token, docList);
			}
		}
		return map;
	}

	public Map<Integer, Integer> getSortedSet2count(HashMap<String, List<Integer>> invertedIndex) {
		HashMap<Integer, Integer> set2count = new HashMap<>();

		for (String token : invertedIndex.keySet()) {
			List<Integer> docList = invertedIndex.get(token);
			for (Integer doc : docList) {

				Integer count = set2count.get(doc);
				if (count == null) {
					set2count.put(doc, 1);
				} else {
					set2count.put(doc, count + 1);
				}
			}
		}

		return SortMapByValue.sortByValue(set2count, false);

	}


}
