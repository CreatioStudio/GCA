package vip.creatio.gca;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.PairMap;

import java.util.Collection;

public abstract class AnnotationInfo {

    protected final ConstPool pool;

    protected final PairMap<String, ElementValue> values = new PairMap<>();

    protected Type type;

    protected AnnotationInfo(ConstPool pool) {
        this.pool = pool;
    }

    protected AnnotationInfo(ConstPool pool, Type type) {
        this.pool = pool;
        this.type = type;
    }

    public Type annotationType() {
        return type;
    }

    /**
     * Put a value with name to value list, replace old value if name already existed
     * or append a new value if not present.
     */
    public ElementValue putValue(String name, ElementValue v) {
        values.put(name, v);
        return v;
    }

    public ElementValue putValue(String name, int v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, short v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, byte v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, boolean v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, char v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, float v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, double v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, long v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, String v) {
        return putValue(name, ElementValue.of(v));
    }

    public ElementValue putValue(String name, Type clazz) {
        return putValue(name, ElementValue.of(clazz));
    }

    public ElementValue putValue(String name, Class<?> clazz) {
        return putValue(name, ElementValue.of(clazz));
    }

    public ElementValue putValue(String name, Type enumClass, String enumName) {
        return putValue(name, ElementValue.of(enumClass, enumName));
    }

    public ElementValue putValue(String name, Enum<?> enumConstant) {
        return putValue(name, ElementValue.of(enumConstant));
    }

    public ElementValue putValue(String name, Annotation anno) {
        return putValue(name, ElementValue.of(anno));
    }

    public ElementValue putValue(String name, ElementValue... array) {
        return putValue(name, ElementValue.of(array));
    }

    public ElementValue putValue(String name, Collection<ElementValue> array) {
        return putValue(name, ElementValue.of(array));
    }

    public ElementValue getValue(String name) {
        return values.get(name);
    }

    public ElementValue getValue(int index) {
        return values.get(index);
    }

    public ElementValue removeValue(String name) {
        return values.remove(name);
    }

    public ElementValue removeValue(int index) {
        return values.remove(index);
    }


    /* internals */

    protected final ConstPool constPool() {
        return pool;
    }

    protected abstract void write(ByteVector buffer);

    protected abstract void collect();

    protected abstract AnnotationInfo copy();

}
