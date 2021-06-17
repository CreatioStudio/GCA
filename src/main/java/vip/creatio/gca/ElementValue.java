package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.Union;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ElementValue {

    /*
     * union ElementValue {
     *     Object             constant;     // when type.isValue() is true
     *     Type               type;         // when type is CLASS and ENUM
     *     Annotation         nested;       // when type is ANNOTATION
     *     List<ElementValue> values;       // when type is ARRAY
     * } union (This Object);
     */
    private final Union<?> union = Union.create();   // such java where's my union?
    private String enumName;

    private final ValueType type;

    private ElementValue(ValueType type) {
        this.type = type;
    }

    static ElementValue parse(ClassFile file, ClassFileParser pool, ByteVector buffer, boolean inheritedVisibility) {
        ValueType t = ValueType.fromTag(buffer.getByte());
        ElementValue e = new ElementValue(t);
        if (t.isValue()) {
            e.union.set(((ValueConst) pool.get(buffer.getShort())).value());
        } else {
            if (t == ValueType.CLASS) {
                e.union.set(file.repository().toType(pool.getString(buffer.getShort())));
            } else if (t == ValueType.ENUM) {
                e.union.set(file.repository().toType(pool.getString(buffer.getShort())));
                e.enumName = pool.getString(buffer.getShort());
            } else if (t == ValueType.ANNOTATION) {
                e.union.set(Annotation.parse(file, pool, buffer, inheritedVisibility));
            } else {
                int num = buffer.getUShort();
                List<ElementValue> v = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    v.add(ElementValue.parse(file, pool, buffer, inheritedVisibility));
                }
                e.union.set(v);
            }
        }
        return e;
    }

    public static ElementValue of(int v) {
        ElementValue ev = new ElementValue(ValueType.INT);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(byte v) {
        ElementValue ev = new ElementValue(ValueType.BYTE);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(char v) {
        ElementValue ev = new ElementValue(ValueType.CHAR);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(short v) {
        ElementValue ev = new ElementValue(ValueType.SHORT);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(long v) {
        ElementValue ev = new ElementValue(ValueType.LONG);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(boolean v) {
        ElementValue ev = new ElementValue(ValueType.BOOLEAN);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(float v) {
        ElementValue ev = new ElementValue(ValueType.FLOAT);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(double v) {
        ElementValue ev = new ElementValue(ValueType.DOUBLE);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(String v) {
        ElementValue ev = new ElementValue(ValueType.STRING);
        ev.union.set(v);
        return ev;
    }

    public static ElementValue of(TypeInfo clazz) {
        ElementValue ev = new ElementValue(ValueType.CLASS);
        ev.union.set(clazz);
        return ev;
    }

    public static ElementValue of(TypeInfo enumClass, String enumName) {
        ElementValue ev = new ElementValue(ValueType.ENUM);
        ev.union.set(enumClass);
        ev.enumName = enumName;
        return ev;
    }

    public static ElementValue of(Annotation anno) {
        ElementValue ev = new ElementValue(ValueType.ANNOTATION);
        ev.union.set(anno);
        return ev;
    }

    public static ElementValue of(Collection<ElementValue> array) {
        ElementValue ev = new ElementValue(ValueType.ARRAY);
        ev.union.set(new ArrayList<>(array));
        return ev;
    }

    public static ElementValue of(ElementValue... array) {
        return of(Arrays.asList(array));
    }

    public ValueType getType() {
        return type;
    }

    public TypeInfo getClassValue() {
        checkType(ValueType.CLASS);
        return union.get();
    }

    public void setClassValue(TypeInfo type) {
        checkType(ValueType.CLASS);
        this.union.set(type);
    }

    public Object getConstValue() {
        if (!type.isValue())
            throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
        return union;
    }

    public void setConstValue(Object v) {
        if (!type.isValue())
            throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
        if (!ValueType.isValue(v))
            throw new IllegalArgumentException("Invalid type of constant value: " + v.getClass().getName());
        union.set(v);
    }

    public TypeInfo getEnumTypeValue() {
        checkType(ValueType.ENUM);
        return union.get();
    }

    public void setEnumTypeValue(TypeInfo type) {
        checkType(ValueType.ENUM);
        union.set(type);
    }

    public String getEnumName() {
        checkType(ValueType.ENUM);
        return enumName;
    }

    public void setEnumName(String enumName) {
        checkType(ValueType.ENUM);
        this.enumName = enumName;
    }

    public Annotation getAnnotation() {
        checkType(ValueType.ANNOTATION);
        return union.get();
    }

    public void setAnnotation(Annotation anno) {
        checkType(ValueType.ANNOTATION);
        union.set(anno);
    }

    @SuppressWarnings("unchecked")
    public List<ElementValue> getValues() {
        checkType(ValueType.ARRAY);
        return (List<ElementValue>) union;
    }

    public void setValues(Collection<ElementValue> values) {
        checkType(ValueType.ARRAY);
        union.set(new ArrayList<>(values));
    }
    
    public ElementValue copy() {
        ElementValue copy = new ElementValue(type);
        if (type == ValueType.ANNOTATION) {
            Annotation nested = union.get();
            copy.union.set(nested.copy());
        } else if (type == ValueType.ARRAY) {
            @SuppressWarnings("unchecked")
            List<ElementValue> list = (List<ElementValue>) union;
            copy.union.set(list.stream().map(ElementValue::copy).collect(Collectors.toList()));
        } else {
            copy.union.set(union);
        }
        copy.enumName = enumName;
        return copy;
    }

    @SuppressWarnings("unchecked")
    void collect(ConstPool pool) {
        if (type.isValue()) {
            pool.acquireValue(union.get());
        } else {
            if (type == ValueType.CLASS) {
                pool.acquireUtf(union.get(TypeInfo.class).getName());
            } else if (type == ValueType.ENUM) {
                pool.acquireUtf(union.get(TypeInfo.class).getName());
                pool.acquireUtf(enumName);
            } else if (type == ValueType.ANNOTATION) {
                union.get(DeclaredAnnotation.class).collect(pool);
            } else {
                for (ElementValue v : (List<ElementValue>) union) {
                    v.collect(pool);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    void write(ConstPool pool, ByteVector buffer) {
        buffer.putByte(type.getTag());
        if (type.isValue()) {
            buffer.putShort(pool.indexOfValue(union.get()));
        } else {
            if (type == ValueType.CLASS) {
                buffer.putShort(pool.indexOf(union.get(TypeInfo.class).getName()));
            } else if (type == ValueType.ENUM) {
                buffer.putShort(pool.indexOf(union.get(TypeInfo.class).getName()));
                buffer.putShort(pool.indexOf(enumName));
            } else if (type == ValueType.ANNOTATION) {
                union.get(DeclaredAnnotation.class).write(pool, buffer);
            } else {
                List<ElementValue> list = (List<ElementValue>) union;
                buffer.putShort(list.size());
                for (ElementValue v : list) {
                    v.write(pool, buffer);
                }
            }
        }
    }

    private void checkType(ValueType type) {
        if (this.type != type) {
            throw new IllegalArgumentException("Only element value with type " + type + " can do this action");
        }
    }

}
