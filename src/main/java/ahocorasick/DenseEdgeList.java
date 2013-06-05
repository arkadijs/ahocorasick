package ahocorasick;

/**
 * Represents an EdgeList by using a single array. Very fast lookup (just an
 * array access), but expensive in terms of memory.
 */
class DenseEdgeList implements EdgeList {

    final State[] array = new State[256];

    /**
     * Helps in converting to dense representation.
     */
    public static DenseEdgeList fromSparse(SparseEdgeList list) {
        byte[] keys = list.keys();
        DenseEdgeList newInstance = new DenseEdgeList();
        for (int i = 0; i < keys.length; i++) {
            newInstance.put(keys[i], list.get(keys[i]));
        }
        return newInstance;
    }

    @Override
    public State get(byte b) {
        return this.array[(int) b & 0xFF];
    }

    @Override
    public void put(byte b, State s) {
        this.array[(int) b & 0xFF] = s;
    }

    @Override
    public byte[] keys() {
        int length = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null)
                length++;
        }
        byte[] result = new byte[length];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                result[j] = (byte) i;
                j++;
            }
        }
        return result;
    }
}
