package lucenex;

import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.pattern.SimplePatternSplitTokenizer;

public class NomeAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		CharArraySet stopWords = new CharArraySet(Arrays.asList("txt"), true);
		
		Tokenizer source = new SimplePatternSplitTokenizer("[^a-zA-Z0-9]+");
		TokenStream result = new StopFilter(source, stopWords);
		result = new LowerCaseFilter(result);
		return new TokenStreamComponents(source, result);
	}
}
