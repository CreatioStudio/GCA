package vip.creatio.gca.util.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteVector extends Vector {

    private static final int DEFAULT_SEG_DIGIT = 10;

    private final int segmentDigit;
    private final int segmentSize;       // pow of 2
    private final int segmentRem;

    final List<byte[]> segments = new ArrayList<>();

    public ByteVector(int initSegmentCount, int segmentSizeDigit) {
        if (segmentSizeDigit < 3) throw new IllegalArgumentException("digit too small");
        else if (segmentSizeDigit > 31) throw new IllegalArgumentException("digit too big");
        segmentDigit = segmentSizeDigit;
        segmentSize = 1 << segmentDigit;
        segmentRem = segmentSize - 1;
        for (int i = 0; i < initSegmentCount; i++) {
            segments.add(new byte[segmentSize]);
        }
        this.capacity = initSegmentCount * segmentSize;
    }

    public ByteVector(int initSegmentCount) {
        this(initSegmentCount, DEFAULT_SEG_DIGIT);
    }

    public ByteVector() {
        this(1);
    }

    public static ByteVector asVector(byte[] bytes) {
        ByteVector vec = new ByteVector();
        vec.putBytes(bytes).flip();
        return vec;
    }

    private void expandOneSegment() {
        segments.add(new byte[segmentSize]);
        capacity += segmentSize;
    }

    private void ensureSize(int newSize) {
        while (capacity <= newSize) {
            expandOneSegment();
        }
    }

    @Override
    public ByteVector clear() {
        position = 0;
        maxPosition = 0;
        capacity = segments.size() * segmentSize;
        mark = -1;
        return this;
    }

    public ByteVector skip(int offset) {
        int newIndex = position + offset;
        ensureSize(newIndex);
        position = newIndex;
        return this;
    }

    public byte[] array() {
        int chunkCount = maxPosition >>> segmentDigit;
        int incompleteSize = maxPosition & segmentRem;
        byte[] sum = new byte[maxPosition];
        for (int i = 0; i < chunkCount; i++) {
            System.arraycopy(segments.get(i), 0, sum, segmentSize * i, segmentSize);
        }
        if (incompleteSize > 0) {
            System.arraycopy(segments.get(chunkCount), 0, sum, chunkCount << segmentDigit, incompleteSize);
        }
        return sum;
    }

    public byte getByte() {
        return getByte(position++);
    }

    public byte getByte(int index) {
        byte[] seg = segments.get(checkIndex(index) >>> segmentDigit);
        return seg[index & segmentRem];
    }

    public int getUByte(int index) {
        return getByte(index) & 0xFF;
    }

    public int getUByte() {
        return getByte() & 0xFF;
    }

    public ByteVector putByte(int b) {
        putByte(position, b);
        if (++position > maxPosition) maxPosition = position;
        return this;
    }

    public ByteVector putByte(int index, int b) {
        ensureSize(index);
        byte[] seg = segments.get(index >>> segmentDigit);
        seg[index & segmentRem] = (byte) b;
        return this;
    }

    public ByteVector getBytes(byte[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        checkIndex(position + length);
        int end = offset + length;
        for (int i = offset; i < end; i++)
            dst[i] = getByte();
        return this;
    }

    public ByteVector getBytes(byte[] dst) {
        return getBytes(dst, 0, dst.length);
    }

    public ByteVector putBytes(byte[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        int end = offset + length;
        for (int i = offset; i < end; i++)
            this.putByte(src[i]);
        return this;
    }

    public ByteVector putBytes(byte[] src) {
        return putBytes(src, 0, src.length);
    }



    public short getShort(int index) {
        checkIndex(index + 1);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos == 0) {
            byte[] seg2 = segments.get((index >>> segmentDigit) + 1);
            return (short) ((seg[pos] << 8) | (seg2[0] & 0xFF));
        }
        return (short) ((seg[pos] << 8) | (seg[pos + 1] & 0xFF));
    }

    public short getShort() {
        short v = getShort(position);
        position += 2;
        return v;
    }

    public int getUShort(int index) {
        return getShort(index) & 0xFFFF;
    }

    public int getUShort() {
        return getShort() & 0xFFFF;
    }

    public ByteVector putShort(int index, int s) {
        ensureSize(index + 1);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos == 0) {
            byte[] next = segments.get((index >>> segmentDigit) + 1);
            seg[pos] = (byte) (s >>> 8);
            next[0] = (byte) (s);
        } else {
            seg[pos] = (byte) (s >>> 8);
            seg[pos + 1] = (byte) (s);
        }
        return this;
    }

    public ByteVector putShort(int s) {
        putShort(position, s);
        if ((position += 2) > maxPosition) maxPosition = position;
        return this;
    }



    public char getChar(int index) {
        return (char) getShort(index);
    }

    public char getChar() {
        return (char) getShort();
    }

    public ByteVector putChar(int index, int c) {
        return putShort(index, c);
    }

    public ByteVector putChar(int c) {
        return putShort(c);
    }



    public int getInt(int index) {
        checkIndex(index + 3);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos < 3) {
            int lack = segmentRem - pos;
            byte[] seg2 = segments.get((index >>> segmentDigit) + 1);
            if (lack == 0) {
                return (seg[pos] << 24) | ((seg2[0] & 0xFF) << 16) | ((seg2[1] & 0xFF) << 8) | (seg2[2] & 0xFF);
            } else if (lack == 1) {
                return (seg[pos] << 24) | ((seg[pos + 1] & 0xFF) << 16) | ((seg2[0] & 0xFF) << 8) | (seg2[1] & 0xFF);
            } else {
                return (seg[pos] << 24) | ((seg[pos + 1] & 0xFF) << 16) | ((seg[pos + 2] & 0xFF) << 8) | (seg2[0] & 0xFF);
            }
        }
        return (seg[pos] << 24) | (((seg[pos + 1] & 0xFF) & 0xFF) << 16) | (((seg[pos + 2] & 0xFF) & 0xFF) << 8) | ((seg[pos + 3] & 0xFF) & 0xFF);
    }

    public int getInt() {
        int v = getInt(position);
        position += 4;
        return v;
    }

    public long getUInt(int index) {
        return (long) getInt(index) & 0xFFFFFFFFL;
    }

    public long getUInt() {
        return (long) getInt() & 0xFFFFFFFFL;
    }

    public ByteVector putInt(int index, int i) {
        ensureSize(index + 3);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos < 3) {
            int lack = segmentRem - pos;
            byte[] next = segments.get((index >>> segmentDigit) + 1);
            if (lack == 0) {
                seg[pos] = (byte) (i >>> 24);
                next[0] = (byte) (i >>> 16);
                next[1] = (byte) (i >>> 8);
                next[2] = (byte) (i);
            } else if (lack == 1) {
                seg[pos] = (byte) (i >>> 24);
                seg[pos + 1] = (byte) (i >>> 16);
                next[0] = (byte) (i >>> 8);
                next[1] = (byte) (i);
            } else {
                seg[pos] = (byte) (i >>> 24);
                seg[pos + 1] = (byte) (i >>> 16);
                seg[pos + 2] = (byte) (i >>> 8);
                next[0] = (byte) (i);
            }
        } else {
            seg[pos] = (byte) (i >>> 24);
            seg[pos + 1] = (byte) (i >>> 16);
            seg[pos + 2] = (byte) (i >>> 8);
            seg[pos + 3] = (byte) (i);
        }
        return this;
    }

    public ByteVector putInt(int i) {
        putInt(position, i);
        if ((position += 4) > maxPosition) maxPosition = position;
        return this;
    }




    // 3 bytes integer
    public int getMedium(int index) {
        checkIndex(index + 2);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos < 2) {
            int lack = segmentRem - pos;
            byte[] seg2 = segments.get((index >>> segmentDigit) + 1);
            if (lack == 0) {
                return (seg[pos] << 16) | ((seg2[0] & 0xFF) << 8) | ((seg2[1] & 0xFF));
            } else {
                return (seg[pos] << 16) | ((seg[pos + 1] & 0xFF) << 8) | ((seg2[0] & 0xFF));
            }
        }
        return (seg[pos] << 16) | ((seg[pos + 1] & 0xFF) << 8) | ((seg[pos + 2] & 0xFF));
    }

    public int getMedium() {
        int v = getMedium(position);
        position += 3;
        return v;
    }

    public int getUMedium(int index) {
        return getMedium(index) & 0xFFFFFF;
    }

    public int getUMedium() {
        return getMedium() & 0xFFFFFF;
    }

    public ByteVector putMedium(int index, int i) {
        ensureSize(index + 2);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos < 2) {
            int lack = segmentRem - pos;
            byte[] next = segments.get((index >>> segmentDigit) + 1);
            if (lack == 0) {
                seg[pos] = (byte) (i >>> 16);
                next[0] = (byte) (i >>> 8);
                next[1] = (byte) (i);
            } else {
                seg[pos] = (byte) (i >>> 24);
                seg[pos + 1] = (byte) (i >>> 16);
                next[0] = (byte) (i >>> 8);
            }
        } else {
            seg[pos] = (byte) (i >>> 16);
            seg[pos + 1] = (byte) (i >>> 8);
            seg[pos + 2] = (byte) (i);
        }
        return this;
    }

    public ByteVector putMedium(int i) {
        putMedium(position, i);
        if ((position += 3) > maxPosition) maxPosition = position;
        return this;
    }




    public long getLong(int index) {
        checkIndex(index + 7);
        byte[] seg = segments.get(index >>> segmentDigit);
        int pos = index & segmentRem;
        if (segmentRem - pos < 7) {
            byte[] next = segments.get((index >>> segmentDigit) + 1);
            switch (segmentRem - pos) {
                case 0:
                    return ((long) seg[pos] << 56) | ((long) (next[0] & 0xFF) << 48)
                            | ((long) (next[1] & 0xFF) << 40) | ((long) (next[2] & 0xFF) << 32)
                            | ((long) (next[3] & 0xFF) << 24) | ((long) (next[4] & 0xFF) << 16)
                            | ((long) (next[5] & 0xFF) << 8) | (long) (next[6] & 0xFF);
                case 1:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (next[0] & 0xFF) << 40) | ((long) (next[1] & 0xFF) << 32)
                            | ((long) (next[2] & 0xFF) << 24) | ((long) (next[3] & 0xFF) << 16)
                            | ((long) (next[4] & 0xFF) << 8) | (long) (next[5] & 0xFF);
                case 2:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (seg[pos + 2] & 0xFF) << 40) | ((long) (next[0] & 0xFF) << 32)
                            | ((long) (next[1] & 0xFF) << 24) | ((long) (next[2] & 0xFF) << 16)
                            | ((long) (next[3] & 0xFF) << 8) | (long) (next[4] & 0xFF);
                case 3:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (seg[pos + 2] & 0xFF) << 40) | ((long) (seg[pos + 3] & 0xFF) << 32)
                            | ((long) (next[0] & 0xFF) << 24) | ((long) (next[1] & 0xFF) << 16)
                            | ((long) (next[2] & 0xFF) << 8) | (long) (next[3] & 0xFF);
                case 4:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (seg[pos + 2] & 0xFF) << 40) | ((long) (seg[pos + 3] & 0xFF) << 32)
                            | ((long) (seg[pos + 4] & 0xFF) << 24) | ((long) (next[0] & 0xFF) << 16)
                            | ((long) (next[1] & 0xFF) << 8) | (long) (next[2] & 0xFF);
                case 5:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (seg[pos + 2] & 0xFF) << 40) | ((long) (seg[pos + 3] & 0xFF) << 32)
                            | ((long) (seg[pos + 4] & 0xFF) << 24) | ((long) (seg[pos + 5] & 0xFF) << 16)
                            | ((long) (next[0] & 0xFF) << 8) | (long) (next[1] & 0xFF);
                case 6:
                    return ((long) seg[pos] << 56) | ((long) (seg[pos + 1] & 0xFF) << 48)
                            | ((long) (seg[pos + 2] & 0xFF) << 40) | ((long) (seg[pos + 3] & 0xFF) << 32)
                            | ((long) (seg[pos + 4] & 0xFF) << 24) | ((long) (seg[pos + 5] & 0xFF) << 16)
                            | ((long) (seg[pos + 6] & 0xFF) << 8) | (long) (next[0] & 0xFF);
            }
        }
        return ((long) ((seg[pos] << 24) | ((seg[pos + 1] & 0xFF) << 16) + ((seg[pos + 2] & 0xFF) << 8) | (seg[pos + 3] & 0xFF)) << 32) |
               ((long) (seg[pos + 4] & 0xFF) << 24) | ((seg[pos + 5] & 0xFF) << 16) | ((seg[pos + 6] & 0xFF) << 8) | (seg[pos + 7] & 0xFF);
    }

    public long getLong() {
        long v = getLong(position);
        position += 8;
        return v;
    }

    public ByteVector putLong(int index, long l) {
        ensureSize(index + 3);
        putByte(index, (int) (l >>> 56));
        putByte(index + 1, (int) (l >>> 48));
        putByte(index + 2, (int) (l >>> 40));
        putByte(index + 3, (int) (l >>> 32));
        putByte(index + 4, (int) (l >>> 24));
        putByte(index + 5, (int) (l >>> 16));
        putByte(index + 6, (int) (l >>> 8));
        putByte(index + 7, (int) (l));
        return this;
    }

    public ByteVector putLong(long l) {
        putLong(position, l);
        if ((position += 8) > maxPosition) maxPosition = position;
        return this;
    }



    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public ByteVector putFloat(int index, float f) {
        return putInt(index, Float.floatToRawIntBits(f));
    }

    public ByteVector putFloat(float f) {
        return putInt(Float.floatToRawIntBits(f));
    }



    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public ByteVector putDouble(int index, double d) {
        return putLong(index, Double.doubleToRawLongBits(d));
    }

    public ByteVector putDouble(double d) {
        return putLong(Double.doubleToRawLongBits(d));
    }



    public String getString(int index, int len) {
        checkIndex(index + len);
        byte[] data = new byte[len];
        int end = len + index;
        for (int i = index; i < end; i++)
            data[i] = getByte();
        return new String(data).intern();
    }

    public String getString(int len) {
        return getString(position, len);
    }

    public ByteVector putString(int index, String s) {
        byte[] data = s.getBytes();
        int end = index + data.length;
        for (int i = index; i < end; i++)
            putByte(data[i]);
        return this;
    }

    public ByteVector putString(String s) {
        return putString(position, s);
    }

    public String toString() {
        return getClass().getName() +
                "{pos=" +
                position() +
                " cap=" +
                capacity() +
                "}";
    }

    public int hashCode() {
        int h = 1;
        int p = position();
        for (int i = capacity() - 1; i >= p; i--)
            h = 31 * h + (int) getByte(i);
        return h;
    }

    public boolean equals(Object ob) {
        if (this == ob)
            return true;
        if (!(ob instanceof ByteVector))
            return false;
        ByteVector that = (ByteVector) ob;
        if (this.capacity() != that.capacity())
            return false;
        for (int i = 0; i < segments.size(); i++) {
            if (!Arrays.equals(segments.get(i), that.segments.get(i))) return false;
        }
        return true;
    }
}
