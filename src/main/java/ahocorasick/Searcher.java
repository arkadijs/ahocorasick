package ahocorasick;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator returns a list of Search matches.
 */
class Searcher implements Iterator<SearchResult>, Iterable<SearchResult> {

    private SearchResult currentResult;
    private AhoCorasick tree;

    Searcher(AhoCorasick tree, SearchResult result) {
        this.tree = tree;
        this.currentResult = result;
    }

    @Override
    public boolean hasNext() {
        return currentResult != null;
    }

    @Override
    public SearchResult next() {
        if (!hasNext())
            throw new NoSuchElementException();
        SearchResult result = currentResult;
        currentResult = tree.continueSearch(currentResult);
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<SearchResult> iterator() {
        return this;
    }
}
