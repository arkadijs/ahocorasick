package ahocorasick;

/**
 * <p>An implementation of the Aho-Corasick string searching automaton. This
 * implementation of the <a
 * href="http://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_string_matching_algorithm"
 * target="_blank">Aho-Corasick</a> string matching algorithm is optimized to work with
 * bytes.</p>
 *
 * <p>
 * Example usage:
 * <code><pre>
 * AhoCorasick tree = new AhoCorasick();
 * tree.add("hello".getBytes(), "hello");
 * tree.add("world".getBytes(), "world");
 *
 * Iterator searcher = tree.search("hello world".getBytes());
 * while (searcher.hasNext()) {
 *    SearchResult result = searcher.next();
 *    System.out.println(result.getOutputs());
 *    System.out.println("Found at index: " + result.getLastIndex());
 * }
 * </pre></code>
 * </p>
 */
public class AhoCorasick {

    private State root = new State(0);
    private boolean prepared = false;

    protected State getRoot() {
        return root;
    }

    /**
     * Adds a new keyword with the given output. During search, if the keyword
     * is matched, output will be one of the yielded elements in
     * SearchResults.getOutputs().
     */
    public void add(byte[] keyword, Object output) {
        if (prepared)
            throw new IllegalStateException("Can't add keywords after search is called");
        State lastState = root.extendAll(keyword);
        lastState.addOutput(output);
    }

    /**
     * Starts a new search, and returns an Iterator of SearchResults.
     */
    public Searcher search(byte[] bytes) {
        return new Searcher(this, startSearch(bytes));
    }

    /**
     * Initializes the fail transitions of all states except for the root.
     */
    protected void prepare() {
        Queue q = new Queue();
        for (int i = 0; i < 256; i++) {
            if (root.get((byte) i) != null) {
                root.get((byte) i).setFail(root);
                q.add(root.get((byte) i));
            }
        }
        prepareRoot();
        while (!q.isEmpty()) {
            State state = q.pop();
            byte[] keys = state.keys();
            for (int i = 0; i < keys.length; i++) {
                State r = state;
                byte a = keys[i];
                State s = r.get(a);
                q.add(s);
                r = r.getFail();
                while (r.get(a) == null)
                    r = r.getFail();
                s.setFail(r.get(a));
                s.getOutputs().addAll(r.get(a).getOutputs());
            }
        }
    }

    /**
     * Sets all the out transitions of the root to itself, if no transition yet
     * exists at this point.
     */
    private void prepareRoot() {
        for (int i = 0; i < 256; i++) {
            if (root.get((byte) i) == null) {
                root.put((byte) i, root);
            }
        }
    }

    /**
     * Begins a new search using the raw interface.
     */
    public SearchResult startSearch(byte[] bytes) {
        if (!prepared) {
            prepare();
            prepared = true;
        }
        return continueSearch(new SearchResult(this.root, bytes, 0));
    }

    /**
     * Continues the search, given the initial state described by the
     * lastResult.
     */
    SearchResult continueSearch(SearchResult lastResult) {
        byte[] bytes = lastResult.bytes;
        State state = lastResult.lastMatchedState;
        for (int i = lastResult.lastIndex; i < bytes.length; i++) {
            byte b = bytes[i];
            State state2;
            while ((state2 = state.edgeList.get(b)) == null)
                state = state.getFail();
            state = state2;
            if (!state.getOutputs().isEmpty())
                return new SearchResult(state, bytes, i + 1);
        }
        return null;
    }
}
