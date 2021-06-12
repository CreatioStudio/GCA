package vip.creatio.gca.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;

public final class Util {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHex(long num) {
        return new String(new char[]{HEX_CHARS[(int)(num >>> 60)], HEX_CHARS[(int)(num >>> 56) & 0xF],
                HEX_CHARS[(int)(num >>> 52) & 0xF], HEX_CHARS[(int)(num >>> 48) & 0xF], HEX_CHARS[(int)(num >>> 44) & 0xF],
                HEX_CHARS[(int)(num >>> 40) & 0xFF], HEX_CHARS[(int)(num >>> 36) & 0xF], HEX_CHARS[(int)(num >>> 32) & 0xF],
                HEX_CHARS[(int)(num >>> 28) & 0xF], HEX_CHARS[(int)(num >>> 24) & 0xF], HEX_CHARS[(int)(num >>> 20) & 0xF],
                HEX_CHARS[(int)(num >>> 16) & 0xF], HEX_CHARS[(int)(num >>> 12) & 0xF], HEX_CHARS[(int)(num >>> 8) & 0xF],
                HEX_CHARS[(int)(num >>> 4) & 0xF], HEX_CHARS[(int)(num & 0xF)]});
    }

    public static String toHex(int num) {
        return new String(new char[]{HEX_CHARS[num >>> 28], HEX_CHARS[(num >>> 24) & 0xF], HEX_CHARS[(num >>> 20) & 0xF],
                HEX_CHARS[(num >>> 16) & 0xF], HEX_CHARS[(num >>> 12) & 0xF], HEX_CHARS[(num >>> 8) & 0xF],
                HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static String toHex(short num) {
        return new String(new char[]{HEX_CHARS[num >>> 12], HEX_CHARS[(num >>> 8) & 0xF], HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static String toHex(byte num) {
        return new String(new char[]{HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static int align(int v) {
        return (v + 3) & ~3;
    }

    @SafeVarargs
    public static <T> T[] merge(IntFunction<T[]> supplier, T[]... arrays) {
        int sum = 0;
        for (T[] ts : arrays) {
            sum += ts.length;
        }
        T[] array = supplier.apply(sum);
        sum = 0;
        for (T[] ts : arrays) {
            System.arraycopy(ts, 0, array, sum, ts.length);
            sum += ts.length;
        }
        return array;
    }

    public static <T> T[] concat(IntFunction<T[]> supplier, T front, T[] follow) {
        T[] array = supplier.apply(follow.length + 1);
        array[0] = front;
        System.arraycopy(follow, 0, array, 1, follow.length);
        return array;
    }

    public static <T> T[] concat(IntFunction<T[]> supplier, T[] front, T follow) {
        T[] array = supplier.apply(front.length + 1);
        array[front.length] = follow;
        System.arraycopy(front, 0, array, 0, front.length);
        return array;
    }

    public static <T, R> R[] map(Function<T, R> mapStrategy, IntFunction<R[]> supplier, T... items) {
        R[] target = supplier.apply(items.length);
        for (int i = 0; i < target.length; i++) {
            target[i] = mapStrategy.apply(items[i]);
        }
        return target;
    }

    @SafeVarargs
    public static <E> List<E> merge(List<E> shell, List<E>... items) {
        for (List<E> item : items) {
            shell.addAll(item);
        }
        return shell;
    }

    //TODO: TEST!
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <E> List<E> mergeOnEach(BinaryOperator<E> mergeStrategy, List<E>... items) {
        Iterator<E>[] itrs = (Iterator<E>[]) map(List::iterator, Iterator[]::new, items);
        List<E> cpy = new ArrayList<>();
        boolean shouldContinue = true;
        int index = 0;
        while (shouldContinue) {
            shouldContinue = false;
            for (Iterator<E> itr : itrs) {
                boolean hasNext = itr.hasNext();
                shouldContinue |= hasNext;
                if (hasNext) {
                    if (index >= cpy.size()) {
                        cpy.add(itr.next());
                    } else {
                        cpy.set(index, mergeStrategy.apply(cpy.get(index), itr.next()));
                    }
                }
            }
            index++;
        }
        return cpy;
    }
}
