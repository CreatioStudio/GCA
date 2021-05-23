package vip.creatio.gca.util;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {

    private K key;

    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K setKey(K key) {
        K old = this.key;
        this.key = key;
        return old;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            return ((Pair<?, ?>) obj).key.equals(key)
                    && ((Pair<?, ?>) obj).value.equals(value);
        }
        return false;
    }

    @Override
    public String toString() {
        return key.toString() + "=" + value.toString();
    }
}
