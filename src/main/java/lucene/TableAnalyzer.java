package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class TableAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        Tokenizer src = new StandardTokenizer();
        TokenStream  result = new LowerCaseFilter(src);
        CharArraySet enStopSet = EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;
        result = new StopFilter(result, enStopSet);
        CharArraySet itaStopSet = ItalianAnalyzer.getDefaultStopSet();
        result = new StopFilter(result, itaStopSet);


        return new TokenStreamComponents(src, result);

    }
}
