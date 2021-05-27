package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ElementValue {

    private final AbstractAnnotation annotation;
    final ValueType type;

    /*
     * union ElementValue {
     *     Const.Value        constant;     // when type.isValue() is true
     *     String             typeName;     // when type is CLASS and ENUM
     *     Annotation         nested;       // when type is ANNOTATION
     *     List<ElementValue> values;       // when type is ARRAY
     * } union (This Object);
     */
    Object union;   // such java where's my union?
    String enumName;

    ElementValue(AbstractAnnotation annotation, ValueType type) {
        this.annotation = annotation;
        this.type = type;
    }

    ElementValue(AbstractAnnotation annotation, ClassFileParser pool, ByteVector buffer) {
        this.annotation = annotation;
        type = ValueType.fromTag(buffer.getByte());
        if (type.isValue()) {
            union = (Const.Value) pool.get(buffer.getShort());
        } else {
            if (type == ValueType.CLASS) {
                union = pool.getString(buffer.getShort());
            } else if (type == ValueType.ENUM) {
                union = pool.getString(buffer.getShort());
                enumName = pool.getString(buffer.getShort());
            } else if (type == ValueType.ANNOTATION) {
                union = Annotation.parse(annotation.file, pool, buffer);
            } else {
                int num = buffer.getUShort();
                List<ElementValue> v = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    v.add(new ElementValue(annotation, pool, buffer));
                }
                union = v;
            }
        }
    }

    public ValueType getType() {
        return type;
    }

    public String getClassValue() {
        checkType(ValueType.CLASS);
        return (String) union;
    }

    public void setClassValue(String signature) {
        checkType(ValueType.CLASS);
        this.union = signature;
    }

    public Const.Value getConstValue() {
        if (!type.isValue())
            throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
        return (Const.Value) union;
    }

    public void setConstValue(Const.Value c) {
        if (!type.isValue())
            throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
        this.union = c;
    }

    public String getEnumType() {
        checkType(ValueType.ENUM);
        return (String) union;
    }

    public void setEnumType(String signature) {
        checkType(ValueType.ENUM);
        union = signature;
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
    void collect() {
        if (type.isValue()) {
            annotation.constPool().acquire((Const) union);
        } else {
            if (type == ValueType.CLASS) {
                annotation.constPool().acquireUtf((String) union);
            } else if (type == ValueType.ENUM) {
                annotation.constPool().acquireUtf((String) union);
                annotation.constPool().acquireUtf(enumName);
            } else if (type == ValueType.ANNOTATION) {
                ((AbstractAnnotation) union).collect();
            } else {
                for (ElementValue v : (List<ElementValue>) union) {
                    v.collect();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    void write(ByteVector buffer) {
        buffer.putByte(type.getTag());
        if (type.isValue()) {
            buffer.putShort(((Const) union).index());
        } else {
            if (type == ValueType.CLASS) {
                buffer.putShort(annotation.constPool().acquireUtf((String) union).index());
            } else if (type == ValueType.ENUM) {
                buffer.putShort(annotation.constPool().acquireUtf((String) union).index());
                buffer.putShort(annotation.constPool().acquireUtf(enumName).index());
            } else if (type == ValueType.ANNOTATION) {
                ((AbstractAnnotation) union).write(buffer);
            } else {
                List<ElementValue> list = (List<ElementValue>) union;
                buffer.putShort(list.size());
                for (ElementValue v : list) {
                    v.write(buffer);
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
