package vip.creatio.gca.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

// An map ordered in add time
public class PairMap<K, V> extends AbstractMap<K, V> implements Iterable<Pair<K, V>> {

    private final ArrayList<Pair<K, V>> items = new ArrayList<>();
    private final HashMap<K, V> map = new HashMap<>();

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    public V get(int index) {
        Pair<K, V> pair = items.get(index);
        return pair == null ? null : pair.getValue();
    }

    @Override
    public V put(K key, V value) {
        return put(items.size(), key, value);
    }

    public V put(int index, K key, V value) {
        boolean flag = false;
        for (Pair<K, V> item : items) {
            if (item.getKey().equals(key)) {
                item.setValue(value);
                flag = true;
                break;
            }
        }
        if (!flag) items.add(index, new Pair<>(key, value));
        return map.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        V v = map.get(key);
        Pair<K, V> pair = new Pair<>((K) key, v);
        items.remove(pair);
        return v;
    }

    public V remove(int index) {
        Pair<K, V> pair = items.get(index);
        if (pair == null) return null;
        map.remove(pair.getKey());
        items.remove(index);
        return pair.getValue();
    }

    @Override
    public void clear() {
        map.clear();
        items.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return new ConvertorSet<>(Pair::getKey);
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return new ConvertorSet<>(Pair::getValue);
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        class OrderedSet extends AbstractSet<Entry<K, V>> {

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public @NotNull Iterator<Entry<K, V>> iterator() {
                return (Iterator) PairMap.this.iterator();
            }

            @Override
            public int size() {
                return items.size();
            }
        }
        return new OrderedSet();
    }

    @NotNull
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return items.iterator();
    }

    private class ConvertorSet<T> extends AbstractSet<T> {

        private final Function<Pair<K, V>, T> convertor;

        private ConvertorSet(Function<Pair<K, V>, T> convertor) {
            this.convertor = convertor;
        }

        @Override
        public @NotNull Iterator<T> iterator() {
            return new Iterator<>() {
                private final Iterator<Pair<K, V>> itr = items.iterator();

                @Override
                public boolean hasNext() {
                    return itr.hasNext();
                }

                @Override
                public T next() {
                    return convertor.apply(itr.next());
                }
            };
        }

        @Override
        public int size() {
            return items.size();
        }
    }
}
