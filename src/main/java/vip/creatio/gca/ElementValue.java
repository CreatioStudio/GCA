package vip.creatio.gca;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ElementValue {

    /*
     * union ElementValue {
     *     Object             constant;     // when type.isValue() is true
     *     Type               type;         // when type is CLASS and ENUM
     *     Annotation         nested;       // when type is ANNOTATION
     *     List<ElementValue> values;       // when type is ARRAY
     * } union (This Object);
     */
    private Object union;   // such java where's my union?
    private String enumName;

    private final ValueType type;

    private ElementValue(ValueType type) {
        this.type = type;
    }

    static ElementValue parse(ClassFile file, ClassFileParser pool, ByteVector buffer) {
        ValueType t = ValueType.fromTag(buffer.getByte());
        ElementValue e = new ElementValue(t);
        if (t.isValue()) {
            e.union = ((Const.Value) pool.get(buffer.getShort())).value();
        } else {
            if (t == ValueType.CLASS) {
                e.union = pool.getString(buffer.getShort());
            } else if (t == ValueType.ENUM) {
                e.union = pool.getString(buffer.getShort());
                e.enumName = pool.getString(buffer.getShort());
            } else if (t == ValueType.ANNOTATION) {
                e.union = Annotation.parse(file, pool, buffer);
            } else {
                int num = buffer.getUShort();
                List<ElementValue> v = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    v.add(ElementValue.parse(file, pool, buffer));
                }
                e.union = v;
            }
        }
        return e;
    }

    public static ElementValue of(int v) {
        ElementValue ev = new ElementValue(ValueType.INT);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(byte v) {
        ElementValue ev = new ElementValue(ValueType.BYTE);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(char v) {
        ElementValue ev = new ElementValue(ValueType.CHAR);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(short v) {
        ElementValue ev = new ElementValue(ValueType.SHORT);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(long v) {
        ElementValue ev = new ElementValue(ValueType.LONG);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(boolean v) {
        ElementValue ev = new ElementValue(ValueType.BOOLEAN);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(float v) {
        ElementValue ev = new ElementValue(ValueType.FLOAT);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(double v) {
        ElementValue ev = new ElementValue(ValueType.DOUBLE);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(String v) {
        ElementValue ev = new ElementValue(ValueType.STRING);
        ev.union = v;
        return ev;
    }

    public static ElementValue of(Type clazz) {
        ElementValue ev = new ElementValue(ValueType.CLASS);
        ev.union = clazz;
        return ev;
    }

    public static ElementValue of(Class<?> clazz) {
        ElementValue ev = new ElementValue(ValueType.CLASS);
        ev.union = Types.toType(clazz);
        return ev;
    }

    public static ElementValue of(Type enumClass, String enumName) {
        ElementValue ev = new ElementValue(ValueType.ENUM);
        ev.union = enumClass;
        ev.enumName = enumName;
        return ev;
    }

    public static ElementValue of(Enum<?> enumConstant) {
        return of(Types.toType(enumConstant.getDeclaringClass()), enumConstant.name());
    }

    public static ElementValue of(Annotation anno) {
        ElementValue ev = new ElementValue(ValueType.ANNOTATION);
        ev.union = anno;
        return ev;
    }

    public static ElementValue of(Collection<ElementValue> array) {
        ElementValue ev = new ElementValue(ValueType.ARRAY);
        ev.union = new ArrayList<>(array);
        return ev;
    }

    public static ElementValue of(ElementValue... array) {
        return of(Arrays.asList(array));
    }

    public ValueType getType() {
        return type;
    }

    public Type getClassValue() {
        checkType(ValueType.CLASS);
        return (Type) union;
    }

    public void setClassValue(String signature) {
        checkType(ValueType.CLASS);
        this.union = Types.toType(signature);
    }

    public void setClassValue(Type type) {
        checkType(ValueType.CLASS);
        this.union = type;
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
        this.union = v;
    }

    public Type getEnumTypeValue() {
        checkType(ValueType.ENUM);
        return (Type) union;
    }

    public void setEnumTypeValue(String signature) {
        checkType(ValueType.ENUM);
        union = Types.toType(signature);
    }

    public void setEnumTypeValue(Type type) {
        checkType(ValueType.ENUM);
        union = type;
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
        return (Annotation) union;
    }

    public void setAnnotation(Annotation anno) {
        checkType(ValueType.ANNOTATION);
        this.union = anno;
    }

    @SuppressWarnings("unchecked")
    public List<ElementValue> getValues() {
        checkType(ValueType.ARRAY);
        return (List<ElementValue>) union;
    }

    public void setValues(Collection<ElementValue> values) {
        checkType(ValueType.ARRAY);
        this.union = new ArrayList<>(values);
    }

    @SuppressWarnings("unchecked")
    void collect(ConstPool pool) {
        if (type.isValue()) {
            pool.acquire(Const.Value.of(pool, union));
        } else {
            if (type == ValueType.CLASS) {
                pool.acquireUtf((String) union);
            } else if (type == ValueType.ENUM) {
                pool.acquireUtf((String) union);
                pool.acquireUtf(enumName);
            } else if (type == ValueType.ANNOTATION) {
                ((AnnotationInfo) union).collect();
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
            buffer.putShort(pool.acquireValue(union).index());
        } else {
            if (type == ValueType.CLASS) {
                buffer.putShort(pool.acquireUtf((String) union).index());
            } else if (type == ValueType.ENUM) {
                buffer.putShort(pool.acquireUtf((String) union).index());
                buffer.putShort(pool.acquireUtf(enumName).index());
            } else if (type == ValueType.ANNOTATION) {
                ((AnnotationInfo) union).write(buffer);
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
