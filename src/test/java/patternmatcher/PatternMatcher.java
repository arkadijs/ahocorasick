package patternmatcher;

import java.nio.charset.Charset;
import ahocorasick.AhoCorasick;
import ahocorasick.SearchResult;

/**
 * This class should contain your pattern matcher implementation.
 */
public class PatternMatcher implements IPatternMatcher {

    private AhoCorasick a = new AhoCorasick();

    private static final Charset utf8 = Charset.forName("UTF-8");

    @Override
    public void addPattern(String pattern) {
        a.add(pattern.getBytes(utf8), pattern);
    }

    @Override
    public String checkText(String text) {
        SearchResult r = a.startSearch(text.getBytes(utf8));
        if (r == null || r.getOutputs().isEmpty()) {
            return null;
        }
        return (String) r.getOutputs().iterator().next();
    }
}
