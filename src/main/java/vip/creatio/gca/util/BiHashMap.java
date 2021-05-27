
package vip.creatio.gca.util;

import java.util.HashMap;
import java.util.Map;

public class BiHashMap<K, V> extends HashMap<K, V> {
    private final BiHashMap<V, K> rev;

    public BiHashMap() {
        this.rev = new BiHashMap<>(this);
    }

    private BiHashMap(BiHashMap<V, K> rev) {
        this.rev = rev;
    }

    public BiHashMap<V, K> reverse() {
        return this.rev;
    }

    public V put(K key, V value) {
        this.rev.put0(value, key);
        return super.put(key, value);
    }

    private void put0(K key, V value) {
        super.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.rev.put0(e.getValue(), e.getKey());
        }
        super.putAll(m);
    }

    public V remove(Object key) {
        V v = super.remove(key);
        if (v != null) {
            this.rev.remove0(v);
        }

        return v;
    }

    private void remove0(Object key) {
        super.remove(key);
    }

    public void clear() {
        this.rev.clear0();
        super.clear();
    }

    private void clear0() {
        super.clear();
    }

    public boolean remove(Object key, Object value) {
        this.rev.remove0(value, key);
        return super.remove(key, value);
    }

    private void remove0(Object key, Object value) {
        super.remove(key, value);
    }

    public V putIfAbsent(K key, V value) {
        this.rev.putIfAbsent0(value, key);
        return super.putIfAbsent(key, value);
    }

    private void putIfAbsent0(K key, V value) {
        super.putIfAbsent(key, value);
    }
}
