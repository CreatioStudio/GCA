package vip.creatio.gca.util.common;

import java.nio.InvalidMarkException;

/**
 * An extendable data list, something like a java NIO's buffer
 */
public abstract class Vector {

    // Invariants: mark <= position <= capacity
    protected int mark = -1;
    protected int position = 0;
    protected int capacity;

    protected int maxPosition = 0;

    public Vector mark() {
        mark = position;
        return this;
    }

    public Vector reset() {
        int m = mark;
        if (m < 0)
            throw new InvalidMarkException();
        position = m;
        if (position > maxPosition) maxPosition = position;
        return this;
    }

    public Vector clear() {
        position = 0;
        maxPosition = 0;
        capacity = 0;
        mark = -1;
        return this;
    }

    public abstract Object array();

    public int size() {
        return maxPosition;
    }

    public final int capacity() {
        return capacity;
    }

    public int position() {
        return position;
    }

    public Vector position(int newPosition) {
        checkIndex(newPosition);
        if (newPosition < 0)
            throw createPositionException(newPosition);
        position = newPosition;
        if (mark > position) mark = -1;
        return this;
    }

    public Vector flip() {
        position = 0;
        mark = -1;
        return this;
    }

    private IllegalArgumentException createPositionException(int newPosition) {
        String msg;

        if (newPosition > capacity) {
            msg = "newPosition > limit: (" + newPosition + " > " + capacity + ")";
        } else { // assume negative
            assert newPosition < 0 : "newPosition expected to be negative";
            msg = "newPosition < 0: (" + newPosition + " < 0)";
        }

        return new IllegalArgumentException(msg);
    }

    protected int checkIndex(int i) {
        if ((i < 0) || (i >= capacity))
            throw new IndexOutOfBoundsException();
        return i;
    }

    protected static void checkBounds(int off, int len, int size) {
        if ((off | len | (off + len) | (size - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }

}
