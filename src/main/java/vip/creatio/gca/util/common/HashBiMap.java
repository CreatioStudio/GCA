
package vip.creatio.gca.util.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * Modified from Google Guava's HashBiMap
 */
public final class HashBiMap<K, V> extends IteratorBasedAbstractMap<K, V>
        implements BiMap<K, V>, Serializable {

    /** Returns a new, empty {@code HashBiMap} with the default initial capacity (16). */
    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    /**
     * Constructs a new, empty bimap with the specified expected size.
     *
     * @param expectedSize the expected number of entries
     * @throws IllegalArgumentException if the specified expected size is negative
     */
    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap<>(expectedSize);
    }

    /**
     * Constructs a new bimap containing initial values from {@code map}. The bimap is created with an
     * initial capacity sufficient to hold the mappings in the specified map.
     */
    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    private static final class BiEntry<K, V> extends AbstractMapEntry<K, V> {
        final @Nullable K key;
        final @Nullable V value;
        final int keyHash;
        final int valueHash;

        // All BiEntry instances are strongly reachable from owning HashBiMap through
        // "HashBiMap.hashTableKToV" and "BiEntry.nextInKToVBucket" references.
        // Under that assumption, the remaining references can be safely marked as @Weak.
        // Using @Weak is necessary to avoid retain-cycles between BiEntry instances on iOS,
        // which would cause memory leaks when non-empty HashBiMap with cyclic BiEntry
        // instances is deallocated.
        @Nullable BiEntry<K, V> nextInKToVBucket;
        @Nullable BiEntry<K, V> nextInVToKBucket;

        @Nullable BiEntry<K, V> nextInKeyInsertionOrder;
        @Nullable BiEntry<K, V> prevInKeyInsertionOrder;

        BiEntry(@Nullable K key, int keyHash, @Nullable V value, int valueHash) {
            this.key = key;
            this.value = value;
            this.keyHash = keyHash;
            this.valueHash = valueHash;
        }

        @Override
        public final @Nullable K getKey() {
            return key;
        }

        @Override
        public final @Nullable V getValue() {
            return value;
        }
    }

    private static final double LOAD_FACTOR = 1.0;

    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    private transient @Nullable BiEntry<K, V> firstInKeyInsertionOrder;
    private transient @Nullable BiEntry<K, V> lastInKeyInsertionOrder;
    private transient int size;
    private transient int mask;
    private transient int modCount;

    private HashBiMap(int expectedSize) {
        init(expectedSize);
    }

    private void init(int expectedSize) {
        if (expectedSize < 0) {
            throw new IllegalArgumentException("expectedSize cannot be negative but was: " + expectedSize);
        }

        // Get the recommended table size.
        // Round down to the nearest power of 2.
        expectedSize = Math.max(expectedSize, 2);
        int tableSize = Integer.highestOneBit(expectedSize);
        // Check to make sure that we will not exceed the maximum load factor.
        if (expectedSize > (int) (LOAD_FACTOR * tableSize)) {
            tableSize <<= 1;
            tableSize = (tableSize > 0) ? tableSize : MAX_TABLE_SIZE;
        }

        this.hashTableKToV = createTable(tableSize);
        this.hashTableVToK = createTable(tableSize);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.size = 0;
        this.mask = tableSize - 1;
        this.modCount = 0;
    }

    public static final int MAX_TABLE_SIZE = 1 << (Integer.SIZE - 2);

    /**
     * Finds and removes {@code entry} from the bucket linked lists in both the key-to-value direction
     * and the value-to-key direction.
     */
    private void delete(BiEntry<K, V> entry) {
        int keyBucket = entry.keyHash & mask;
        BiEntry<K, V> prevBucketEntry = null;
        for (BiEntry<K, V> bucketEntry = hashTableKToV[keyBucket];
             true;
             bucketEntry = bucketEntry.nextInKToVBucket) {
            if (bucketEntry == entry) {
                if (prevBucketEntry == null) {
                    hashTableKToV[keyBucket] = entry.nextInKToVBucket;
                } else {
                    prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
                }
                break;
            }
            prevBucketEntry = bucketEntry;
        }

        int valueBucket = entry.valueHash & mask;
        prevBucketEntry = null;
        for (BiEntry<K, V> bucketEntry = hashTableVToK[valueBucket];
             true;
             bucketEntry = bucketEntry.nextInVToKBucket) {
            if (bucketEntry == entry) {
                if (prevBucketEntry == null) {
                    hashTableVToK[valueBucket] = entry.nextInVToKBucket;
                } else {
                    prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
                }
                break;
            }
            prevBucketEntry = bucketEntry;
        }

        if (entry.prevInKeyInsertionOrder == null) {
            firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        } else {
            entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        }

        if (entry.nextInKeyInsertionOrder == null) {
            lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        } else {
            entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        }

        size--;
        modCount++;
    }

    private void insert(BiEntry<K, V> entry, @Nullable BiEntry<K, V> oldEntryForKey) {
        int keyBucket = entry.keyHash & mask;
        entry.nextInKToVBucket = hashTableKToV[keyBucket];
        hashTableKToV[keyBucket] = entry;

        int valueBucket = entry.valueHash & mask;
        entry.nextInVToKBucket = hashTableVToK[valueBucket];
        hashTableVToK[valueBucket] = entry;

        if (oldEntryForKey == null) {
            entry.prevInKeyInsertionOrder = lastInKeyInsertionOrder;
            entry.nextInKeyInsertionOrder = null;
            if (lastInKeyInsertionOrder == null) {
                firstInKeyInsertionOrder = entry;
            } else {
                lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            lastInKeyInsertionOrder = entry;
        } else {
            entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
            if (entry.prevInKeyInsertionOrder == null) {
                firstInKeyInsertionOrder = entry;
            } else {
                entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
            if (entry.nextInKeyInsertionOrder == null) {
                lastInKeyInsertionOrder = entry;
            } else {
                entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
            }
        }

        size++;
        modCount++;
    }

    private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
        for (BiEntry<K, V> entry = hashTableKToV[keyHash & mask];
             entry != null;
             entry = entry.nextInKToVBucket) {
            if (keyHash == entry.keyHash && Objects.equals(key, entry.key)) {
                return entry;
            }
        }
        return null;
    }

    private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
        for (BiEntry<K, V> entry = hashTableVToK[valueHash & mask];
             entry != null;
             entry = entry.nextInVToKBucket) {
            if (valueHash == entry.valueHash && Objects.equals(value, entry.value)) {
                return entry;
            }
        }
        return null;
    }

    private static int smearedHash(@Nullable Object o) {
        return 0x1b873593 * Integer.rotateLeft(o == null ? 0 : o.hashCode() * 0xcc9e2d51, 15);
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return seekByKey(key, smearedHash(key)) != null;
    }

    /**
     * Returns {@code true} if this BiMap contains an entry whose value is equal to {@code value} (or,
     * equivalently, if this inverse view contains a key that is equal to {@code value}).
     *
     * <p>Due to the property that values in a BiMap are unique, this will tend to execute in
     * faster-than-linear time.
     *
     * @param value the object to search for in the values of this BiMap
     * @return true if a mapping exists from a key to the specified value
     */
    @Override
    public boolean containsValue(@Nullable Object value) {
        return seekByValue(value, smearedHash(value)) != null;
    }

    @Override
    public @Nullable V get(@Nullable Object key) {
        BiEntry<K, V> entry = seekByKey(key, smearedHash(key));
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public V put(@Nullable K key, @Nullable V value) {
        return put(key, value, false);
    }

    private V put(@Nullable K key, @Nullable V value, boolean force) {
        int keyHash = smearedHash(key);
        int valueHash = smearedHash(value);

        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForKey != null
                && valueHash == oldEntryForKey.valueHash
                && Objects.equals(value, oldEntryForKey.value)) {
            return value;
        }

        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        if (oldEntryForValue != null) {
            if (force) {
                delete(oldEntryForValue);
            } else {
                throw new IllegalArgumentException("value already present: " + value);
            }
        }

        BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
        if (oldEntryForKey != null) {
            delete(oldEntryForKey);
            insert(newEntry, oldEntryForKey);
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
            return oldEntryForKey.value;
        } else {
            insert(newEntry, null);
            rehashIfNecessary();
            return null;
        }
    }

    @Override
    public @Nullable V forcePut(@Nullable K key, @Nullable V value) {
        return put(key, value, true);
    }

    private @Nullable K putInverse(@Nullable V value, @Nullable K key, boolean force) {
        int valueHash = smearedHash(value);
        int keyHash = smearedHash(key);

        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForValue != null
                && keyHash == oldEntryForValue.keyHash
                && Objects.equals(key, oldEntryForValue.key)) {
            return key;
        } else if (oldEntryForKey != null && !force) {
            throw new IllegalArgumentException("key already present: " + key);
        }

        /*
         * The ordering here is important: if we deleted the key entry and then the value entry,
         * the key entry's prev or next pointer might point to the dead value entry, and when we
         * put the new entry in the key entry's position in iteration order, it might invalidate
         * the linked list.
         */

        if (oldEntryForValue != null) {
            delete(oldEntryForValue);
        }

        if (oldEntryForKey != null) {
            delete(oldEntryForKey);
        }

        BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
        insert(newEntry, oldEntryForKey);

        if (oldEntryForKey != null) {
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
        }
        if (oldEntryForValue != null) {
            oldEntryForValue.prevInKeyInsertionOrder = null;
            oldEntryForValue.nextInKeyInsertionOrder = null;
        }
        rehashIfNecessary();
        return (oldEntryForValue == null) ? null : oldEntryForValue.getKey();
    }

    private void rehashIfNecessary() {
        BiEntry<K, V>[] oldKToV = hashTableKToV;
        if (size > LOAD_FACTOR * oldKToV.length && oldKToV.length < MAX_TABLE_SIZE) {
            int newTableSize = oldKToV.length * 2;

            this.hashTableKToV = createTable(newTableSize);
            this.hashTableVToK = createTable(newTableSize);
            this.mask = newTableSize - 1;
            this.size = 0;

            for (BiEntry<K, V> entry = firstInKeyInsertionOrder;
                 entry != null;
                 entry = entry.nextInKeyInsertionOrder) {
                insert(entry, entry);
            }
            this.modCount++;
        }
    }

    @SuppressWarnings("unchecked")
    private BiEntry<K, V>[] createTable(int length) {
        return new BiEntry[length];
    }

    @Override
    public @Nullable V remove(@Nullable Object key) {
        BiEntry<K, V> entry = seekByKey(key, smearedHash(key));
        if (entry == null) {
            return null;
        } else {
            delete(entry);
            entry.prevInKeyInsertionOrder = null;
            entry.nextInKeyInsertionOrder = null;
            return entry.value;
        }
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(hashTableKToV, null);
        Arrays.fill(hashTableVToK, null);
        firstInKeyInsertionOrder = null;
        lastInKeyInsertionOrder = null;
        modCount++;
    }

    @Override
    public int size() {
        return size;
    }

    abstract class Itr<T> implements Iterator<T> {
        BiEntry<K, V> next = firstInKeyInsertionOrder;
        BiEntry<K, V> toRemove = null;
        int expectedModCount = modCount;
        int remaining = size();

        @Override
        public boolean hasNext() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return next != null && remaining > 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            BiEntry<K, V> entry = next;
            next = entry.nextInKeyInsertionOrder;
            toRemove = entry;
            remaining--;
            return output(entry);
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (toRemove == null) {
                throw new IllegalStateException(String.valueOf("no calls to next() since the last call to remove()"));
            }
            delete(toRemove);
            expectedModCount = modCount;
            toRemove = null;
        }

        abstract T output(BiEntry<K, V> entry);
    }

    @Override
    public @NotNull Set<K> keySet() {
        return new KeySet();
    }

    private final class KeySet extends MapKeySet<K, V> {
        KeySet() {
            super(HashBiMap.this);
        }

        @Override
        public @NotNull Iterator<K> iterator() {
            return new Itr<K>() {
                @Override
                K output(BiEntry<K, V> entry) {
                    return entry.key;
                }
            };
        }

        @Override
        public boolean remove(@Nullable Object o) {
            BiEntry<K, V> entry = seekByKey(o, smearedHash(o));
            if (entry == null) {
                return false;
            } else {
                delete(entry);
                entry.prevInKeyInsertionOrder = null;
                entry.nextInKeyInsertionOrder = null;
                return true;
            }
        }
    }

    @Override
    public @NotNull Set<V> values() {
        return inverse().keySet();
    }

    @Override
    Iterator<Entry<K, V>> entryIterator() {
        return new Itr<Entry<K, V>>() {
            @Override
            Entry<K, V> output(BiEntry<K, V> entry) {
                return new MapEntry(entry);
            }

            class MapEntry extends AbstractMapEntry<K, V> {
                BiEntry<K, V> delegate;

                MapEntry(BiEntry<K, V> entry) {
                    this.delegate = entry;
                }

                @Override
                public K getKey() {
                    return delegate.key;
                }

                @Override
                public V getValue() {
                    return delegate.value;
                }

                @Override
                public V setValue(V value) {
                    V oldValue = delegate.value;
                    int valueHash = smearedHash(value);
                    if (valueHash == delegate.valueHash && Objects.equals(value, oldValue)) {
                        return value;
                    }
                    if (seekByValue(value, valueHash) != null) {
                        throw new IllegalArgumentException("value already present: " + value);
                    }
                    delete(delegate);
                    BiEntry<K, V> newEntry = new BiEntry<>(delegate.key, delegate.keyHash, value, valueHash);
                    insert(newEntry, delegate);
                    delegate.prevInKeyInsertionOrder = null;
                    delegate.nextInKeyInsertionOrder = null;
                    expectedModCount = modCount;
                    if (toRemove == delegate) {
                        toRemove = newEntry;
                    }
                    delegate = newEntry;
                    return oldValue;
                }
            }
        };
    }

    @Override
    public void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        for (BiEntry<K, V> entry = firstInKeyInsertionOrder;
             entry != null;
             entry = entry.nextInKeyInsertionOrder) {
            action.accept(entry.key, entry.value);
        }
    }

    @Override
    public void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
        BiEntry<K, V> oldFirst = firstInKeyInsertionOrder;
        clear();
        for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
            put(entry.key, function.apply(entry.key, entry.value));
        }
    }

    private transient @Nullable BiMap<V, K> inverse;

    @Override
    public BiMap<V, K> inverse() {
        BiMap<V, K> result = inverse;
        return (result == null) ? inverse = new Inverse() : result;
    }

    private final class Inverse extends IteratorBasedAbstractMap<V, K>
            implements BiMap<V, K>, Serializable {
        BiMap<K, V> forward() {
            return HashBiMap.this;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            forward().clear();
        }

        @Override
        public boolean containsKey(@Nullable Object value) {
            return forward().containsValue(value);
        }

        @Override
        public K get(@Nullable Object value) {
            BiEntry<K, V> entry = seekByValue(value, smearedHash(value));
            return (entry == null) ? null : entry.getKey();
        }

        @Override
        public @Nullable K put(@Nullable V value, @Nullable K key) {
            return putInverse(value, key, false);
        }

        @Override
        public @Nullable K forcePut(@Nullable V value, @Nullable K key) {
            return putInverse(value, key, true);
        }

        @Override
        public @Nullable K remove(@Nullable Object value) {
            BiEntry<K, V> entry = seekByValue(value, smearedHash(value));
            if (entry == null) {
                return null;
            } else {
                delete(entry);
                entry.prevInKeyInsertionOrder = null;
                entry.nextInKeyInsertionOrder = null;
                return entry.key;
            }
        }

        @Override
        public BiMap<K, V> inverse() {
            return forward();
        }

        @Override
        public @NotNull Set<V> keySet() {
            return new InverseKeySet();
        }

        private final class InverseKeySet extends MapKeySet<V, K> {
            InverseKeySet() {
                super(Inverse.this);
            }

            @Override
            public boolean remove(@Nullable Object o) {
                BiEntry<K, V> entry = seekByValue(o, smearedHash(o));
                if (entry == null) {
                    return false;
                } else {
                    delete(entry);
                    return true;
                }
            }

            @Override
            public @NotNull Iterator<V> iterator() {
                return new Itr<V>() {
                    @Override
                    V output(BiEntry<K, V> entry) {
                        return entry.value;
                    }
                };
            }
        }

        @Override
        public @NotNull Set<K> values() {
            return forward().keySet();
        }

        @Override
        Iterator<Entry<V, K>> entryIterator() {
            return new Itr<Entry<V, K>>() {
                @Override
                Entry<V, K> output(BiEntry<K, V> entry) {
                    return new InverseEntry(entry);
                }

                class InverseEntry extends AbstractMapEntry<V, K> {
                    BiEntry<K, V> delegate;

                    InverseEntry(BiEntry<K, V> entry) {
                        this.delegate = entry;
                    }

                    @Override
                    public V getKey() {
                        return delegate.value;
                    }

                    @Override
                    public K getValue() {
                        return delegate.key;
                    }

                    @Override
                    public K setValue(K key) {
                        K oldKey = delegate.key;
                        int keyHash = smearedHash(key);
                        if (keyHash == delegate.keyHash && Objects.equals(key, oldKey)) {
                            return key;
                        }
                        if (seekByValue(key, keyHash) != null) {
                            throw new IllegalArgumentException("value already present: " + key);
                        }
                        delete(delegate);
                        BiEntry<K, V> newEntry =
                                new BiEntry<>(key, keyHash, delegate.value, delegate.valueHash);
                        delegate = newEntry;
                        insert(newEntry, null);
                        expectedModCount = modCount;
                        return oldKey;
                    }
                }
            };
        }

        @Override
        public void forEach(@NotNull BiConsumer<? super V, ? super K> action) {
            HashBiMap.this.forEach((k, v) -> action.accept(v, k));
        }

        @Override
        public void replaceAll(@NotNull BiFunction<? super V, ? super K, ? extends K> function) {
            BiEntry<K, V> oldFirst = firstInKeyInsertionOrder;
            clear();
            for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
                put(entry.value, function.apply(entry.value, entry.key));
            }
        }

        Object writeReplace() {
            return new InverseSerializedForm<>(HashBiMap.this);
        }
    }

    private static final class InverseSerializedForm<K, V> implements Serializable {
        private final HashBiMap<K, V> bimap;

        InverseSerializedForm(HashBiMap<K, V> bimap) {
            this.bimap = bimap;
        }

        Object readResolve() {
            return bimap.inverse();
        }
    }

    /**
     * @serialData the number of entries, first key, first value, second key, second value, and so on.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size());
        for (Map.Entry<K, V> entry : entrySet()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int size = stream.readInt();
        init(16); // resist hostile attempts to allocate gratuitous heap
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked") // reading data stored by writeMap
            K key = (K) stream.readObject();
            @SuppressWarnings("unchecked") // reading data stored by writeMap
            V value = (V) stream.readObject();
            put(key, value);
        }
    }

    private static final long serialVersionUID = 0;
}



abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {

    @Override
    public abstract K getKey();

    @Override
    public abstract V getValue();

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry<?, ?> that = (Map.Entry<?, ?>) object;
            return Objects.equals(this.getKey(), that.getKey())
                    && Objects.equals(this.getValue(), that.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        K k = getKey();
        V v = getValue();
        return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
    }

    /** Returns a string representation of the form {@code {key}={value}}. */
    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}



abstract class IteratorBasedAbstractMap<K, V> extends AbstractMap<K, V> {
    @Override
    public abstract int size();

    abstract Iterator<Entry<K, V>> entryIterator();

    Spliterator<Entry<K, V>> entrySpliterator() {
        return Spliterators.spliterator(
                entryIterator(), size(), Spliterator.SIZED | Spliterator.DISTINCT);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K,V>>() {
            Map<K, V> map() {
                return IteratorBasedAbstractMap.this;
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return entryIterator();
            }

            @Override
            public Spliterator<Entry<K, V>> spliterator() {
                return entrySpliterator();
            }

            @Override
            public void forEach(Consumer<? super Entry<K, V>> action) {
                forEachEntry(action);
            }

            @Override
            public int size() {
                return map().size();
            }

            @Override
            public void clear() {
                map().clear();
            }

            @Override
            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    Object key = entry.getKey();
                    V value;
                    try {
                        value = map().get(key);
                    } catch (ClassCastException | NullPointerException e) {
                        value = null;
                    }
                    return Objects.equals(value, entry.getValue()) && (value != null || map().containsKey(key));
                }
                return false;
            }

            @Override
            public boolean isEmpty() {
                return map().isEmpty();
            }

            @Override
            public boolean remove(Object o) {
                if (contains(o)) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    return map().keySet().remove(entry.getKey());
                }
                return false;
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                try {
                    return super.removeAll(c);
                } catch (UnsupportedOperationException e) {
                    // if the iterators don't support remove
                    boolean changed = false;
                    for (Object o : c) {
                        changed |= remove(o);
                    }
                    return changed;
                }
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                try {
                    return super.retainAll(c);
                } catch (UnsupportedOperationException e) {
                    // if the iterators don't support remove
                    int expectedSize = c.size();
                    if (expectedSize < 3) {
                        expectedSize++;
                    } else if (expectedSize < HashBiMap.MAX_TABLE_SIZE) {
                        // This is the calculation used in JDK8 to resize when a putAll
                        // happens; it seems to be the most conservative calculation we
                        // can make.  0.75 is the default load factor.
                        expectedSize = (int) ((float) expectedSize / 0.75F + 1.0F);
                    } else {
                        expectedSize = Integer.MAX_VALUE; // any large value
                    }
                    Set<Object> keys = new HashSet<>(expectedSize);
                    for (Object o : c) {
                        if (contains(o)) {
                            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                            keys.add(entry.getKey());
                        }
                    }
                    return map().keySet().retainAll(keys);
                }
            }
        };
    }

    void forEachEntry(Consumer<? super Entry<K, V>> action) {
        entryIterator().forEachRemaining(action);
    }

    @Override
    public void clear() {
        Iterator<Entry<K, V>> iterator = entryIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}



class MapKeySet<K, V> extends AbstractSet<K> {

    final Map<K, V> map;

    MapKeySet(@NotNull Map<K, V> map) {
        this.map = map;
    }

    Map<K, V> map() {
        return map;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<>() {
            final Iterator<Map.Entry<K, V>> backingIterator = map().entrySet().iterator();

            @Override
            public final boolean hasNext() {
                return backingIterator.hasNext();
            }

            @Override
            public final K next() {
                return backingIterator.next().getKey();
            }

            @Override
            public final void remove() {
                backingIterator.remove();
            }
        };
    }

    @Override
    public void forEach(@NotNull Consumer<? super K> action) {
        // avoids entry allocation for those maps that allocate entries on iteration
        map.forEach((k, v) -> action.accept(k));
    }

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public boolean isEmpty() {
        return map().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map().containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            map().remove(o);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        map().clear();
    }
}