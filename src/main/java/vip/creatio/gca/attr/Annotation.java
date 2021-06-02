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

public class Annotation extends AbstractAnnotation {

    private String type;

    private final List<Pair<String, ElementValue>> nameValuePairs = new ArrayList<>();

    Annotation(ConstPool pool) {
        super(pool);
    }

    public Annotation(ConstPool pool, String type) {
        this(pool);
        this.type = type;
    }

    static Annotation parse(ClassFile file, ClassFileParser pool, ByteVector buffer) {
        Annotation anno = new Annotation(file.constPool());
        anno.type = pool.getString(buffer.getUShort());
        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            String name = pool.getString(buffer.getUShort());
            anno.nameValuePairs.add(new Pair<>(name, new ElementValue(file, pool, buffer)));
        }
        return anno;
    }

    public String getClassName() {
        return type;
    }

    public ElementValue addValue(String name, ValueType type) {
        ElementValue value = new ElementValue(constPool(), type);
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
        ElementValue v = new ElementValue(constPool(), value.valueType());
        v.union = value;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addClassValue(String name, String clsName) {
        ElementValue v = new ElementValue(constPool(), ValueType.CLASS);
        v.union = clsName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addEnumValue(String name, String enumClass, String enumName) {
        ElementValue v = new ElementValue(constPool(), ValueType.ENUM);
        v.union = enumClass;
        v.enumName = enumName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addAnnotationValue(String name, Annotation anno) {
        ElementValue v = new ElementValue(constPool(), ValueType.ANNOTATION);
        v.union = anno;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addArrayValue(String name, Collection<ElementValue> values) {
        ElementValue v = new ElementValue(constPool(), ValueType.ARRAY);
        v.union = new ArrayList<>(values);
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
        constPool().acquireUtf(type);
        for (Pair<String, ElementValue> p : nameValuePairs) {
            constPool().acquireUtf(p.getKey());
            p.getValue().collect();
        }
    }

    @Override
    AbstractAnnotation copy() {
        return new Annotation(pool, type);
    }
}
