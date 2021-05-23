package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypeAnnotation {

    private final ClassFile file;
    private final ConstPool pool;

    private TargetType target;

    private int index; // type_parameter,

    private String type;

    private final List<Pair<String, ElementValue>> nameValuePairs = new ArrayList<>();

    TypeAnnotation(ClassFile file) {
        this.file = file;
        this.pool = file.constPool();
    }

    public TypeAnnotation(ClassFile file, String type) {
        this(file);
        this.type = type;
    }

    static TypeAnnotation parse(ClassFile file, ClassFileParser pool, ByteVector buffer) {
        TypeAnnotation anno = new TypeAnnotation(file);
        anno.type = pool.getString(buffer.getUShort());
        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            String name = pool.getString(buffer.getUShort());
            anno.nameValuePairs.add(new Pair<>(name, anno.new ElementValue(pool, buffer)));
        }
        return anno;
    }

    public String getClassName() {
        return type;
    }

    public ElementValue addValue(String name, ValueType type) {
        ElementValue value = new ElementValue(type);
        nameValuePairs.add(new Pair<>(name, value));
        return value;
    }

    public ElementValue getValue(String name) {
        for (Pair<String, ElementValue> p : nameValuePairs) {
            if (p.getKey().equals(name)) return p.getValue();
        }
        return null;
    }

    public boolean removeValue(String name) {
        return nameValuePairs.removeIf(p -> p.getKey().equals(name));
    }

    public ElementValue addConstValue(String name, Const.Value value) {
        ElementValue v = new ElementValue(value.valueType());
        v.constant = value;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addClassValue(String name, String clsName) {
        ElementValue v = new ElementValue(ValueType.CLASS);
        v.typeName = clsName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addEnumValue(String name, String enumClass, String enumName) {
        ElementValue v = new ElementValue(ValueType.ENUM);
        v.typeName = enumClass;
        v.enumName = enumName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addAnnotationValue(String name, TypeAnnotation anno) {
        ElementValue v = new ElementValue(ValueType.ANNOTATION);
        v.nested = anno;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addArrayValue(String name, Collection<ElementValue> values) {
        ElementValue v = new ElementValue(ValueType.ARRAY);
        v.values = new ArrayList<>(values);
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addArrayValue(String name, ElementValue... values) {
        return addArrayValue(name, Arrays.asList(values));
    }

    void write(ByteVector buffer) {
        buffer.putShort(constPool().acquireUtf(type).index());
        buffer.putShort(nameValuePairs.size());
        for (Pair<String, ElementValue> p : nameValuePairs) {
            buffer.putShort(constPool().acquireUtf(p.getKey()).index());
            p.getValue().write(buffer);
        }
    }

    void collect() {
        for (Pair<String, ElementValue> p : nameValuePairs) {
            constPool().acquireUtf(p.getKey());
            p.getValue().collect();
        }
    }

    private ConstPool constPool() {
        return pool;
    }

    public class ElementValue {

        private final ValueType type;
        private Const.Value constant;
        private String typeName;    // class name and enum name
        private String enumName;
        private TypeAnnotation nested;
        private List<ElementValue> values;

        ElementValue(ValueType type) {
            this.type = type;
        }

        ElementValue(ClassFileParser pool, ByteVector buffer) {
            type = ValueType.fromTag(buffer.getByte());
            if (type.isValue()) {
                constant = (Const.Value) pool.get(buffer.getShort());
            } else {
                if (type == ValueType.CLASS) {
                    typeName = pool.getString(buffer.getShort());
                } else if (type == ValueType.ENUM) {
                    typeName = pool.getString(buffer.getShort());
                    enumName = pool.getString(buffer.getShort());
                } else if (type == ValueType.ANNOTATION) {
                    nested = parse(file, pool, buffer);
                } else {
                    int num = buffer.getUShort();
                    for (int i = 0; i < num; i++) {
                        values.add(new ElementValue(pool, buffer));
                    }
                }
            }
        }

        public ValueType getType() {
            return type;
        }

        public String getClassValue() {
            checkType(ValueType.CLASS);
            return typeName;
        }

        public void setClassValue(String signature) {
            checkType(ValueType.CLASS);
            this.typeName = signature;
        }

        public Const.Value getConstValue() {
            if (!type.isValue())
                throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
            return constant;
        }

        public void setConstValue(Const.Value c) {
            if (!type.isValue())
                throw new IllegalArgumentException("Only element value with \"value type\" can do this action");
            this.constant = c;
        }

        public String getEnumType() {
            checkType(ValueType.ENUM);
            return typeName;
        }

        public void setEnumType(String signature) {
            checkType(ValueType.ENUM);
            typeName = signature;
        }

        public String getEnumName() {
            checkType(ValueType.ENUM);
            return enumName;
        }

        public void setEnumName(String enumName) {
            checkType(ValueType.ENUM);
            this.enumName = enumName;
        }

        public TypeAnnotation getAnnotation() {
            checkType(ValueType.ANNOTATION);
            return nested;
        }

        public void setAnnotation(TypeAnnotation anno) {
            checkType(ValueType.ANNOTATION);
            this.nested = anno;
        }

        public List<ElementValue> getValues() {
            checkType(ValueType.ARRAY);
            return values;
        }

        public void setValues(Collection<ElementValue> values) {
            checkType(ValueType.ARRAY);
            this.values = new ArrayList<>(values);
        }

        private void collect() {
            if (type.isValue()) {
                constPool().acquire(constant);
            } else {
                if (type == ValueType.CLASS) {
                    constPool().acquireUtf(typeName);
                } else if (type == ValueType.ENUM) {
                    constPool().acquireUtf(typeName);
                    constPool().acquireUtf(enumName);
                } else if (type == ValueType.ANNOTATION) {
                    nested.collect();
                } else {
                    for (ElementValue v : values) {
                        v.collect();
                    }
                }
            }
        }

        private void write(ByteVector buffer) {
            buffer.putByte(type.getTag());
            if (type.isValue()) {
                buffer.putShort(constant.index());
            } else {
                if (type == ValueType.CLASS) {
                    buffer.putShort(constPool().acquireUtf(typeName).index());
                } else if (type == ValueType.ENUM) {
                    buffer.putShort(constPool().acquireUtf(typeName).index());
                    buffer.putShort(constPool().acquireUtf(enumName).index());
                } else if (type == ValueType.ANNOTATION) {
                    nested.write(buffer);
                } else {
                    buffer.putShort(values.size());
                    for (ElementValue v : values) {
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
}
