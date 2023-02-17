package lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

public class IndexLoader {

	private IndexSearcher indexSearcher;

	public IndexLoader() throws IOException {
		this.indexSearcher = this.indexLoader();
	}

	public IndexSearcher indexLoader() throws IOException {
		long start = System.currentTimeMillis();
		Path path = Paths.get(new GlobalVariables().getPathIndex());
		IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
		IndexSearcher searcher = new IndexSearcher(reader);
		searcher.search(new TermQuery(new Term("tableContent", "test")), 20000000);
		long end = System.currentTimeMillis();
		
		System.out.println("Index Loaded. Total Time Taken: " + (end - start) / 1000F + " seconds");
		return searcher;
	}

	public IndexSearcher getIndexSearcher() {
		return this.indexSearcher;
	}
}
