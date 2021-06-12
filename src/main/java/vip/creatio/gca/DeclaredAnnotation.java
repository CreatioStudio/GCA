package vip.creatio.gca;

import vip.creatio.gca.type.AnnotationInfo;
import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.PairMap;

import java.util.Collection;
import java.util.Map;

public abstract class DeclaredAnnotation implements AnnotationInfo {

    public TypeInfo annotationType() {
        return type;
    }

    public boolean visible() {
        return visible;
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

    public ElementValue putValue(String name, TypeInfo clazz) {
        return putValue(name, ElementValue.of(clazz));
    }

    public ElementValue putValue(String name, TypeInfo enumClass, String enumName) {
        return putValue(name, ElementValue.of(enumClass, enumName));
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

    public abstract DeclaredAnnotation copy();


    /* internals */
    protected final PairMap<String, ElementValue> values = new PairMap<>();
    protected final boolean visible;

    protected TypeInfo type;

    protected DeclaredAnnotation(boolean visible) {
        this.visible = visible;
    }

    protected DeclaredAnnotation(boolean visible, TypeInfo type) {
        this.visible = visible;
        this.type = type;
    }

    protected abstract void write(ConstPool pool, ByteVector buffer);

    protected abstract void collect(ConstPool pool);

    protected final void copyValues(DeclaredAnnotation copy) {
        for (Map.Entry<String, ElementValue> entry : values.entrySet()) {
            copy.values.put(entry.getKey(), entry.getValue().copy());
        }
    }

}
