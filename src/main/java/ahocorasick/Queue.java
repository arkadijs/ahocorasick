package ahocorasick;

import java.util.ArrayList;

/**
 * Quick-and-dirty queue class. Essentially uses two lists to represent a queue.
 */
class Queue {

    final ArrayList<State> l1 = new ArrayList<>();
    final ArrayList<State> l2 = new ArrayList<>();

    public void add(State s) {
        l2.add(s);
    }

    public boolean isEmpty() {
        return l1.isEmpty() && l2.isEmpty();
    }

    public State pop() {
        if (isEmpty())
            throw new IllegalStateException("Queue is empty");
        if (l1.isEmpty()) {
            for (int i = l2.size() - 1; i >= 0; i--)
                l1.add(l2.remove(i));
            assert l2.isEmpty();
            assert !l1.isEmpty();
        }
        return l1.remove(l1.size() - 1);
    }
}
