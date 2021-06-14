package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.Pair;

public class Annotation extends DeclaredAnnotation {

    Annotation(AttributeContainer container, boolean visible) {
        super(container, visible);
    }

    public Annotation(AttributeContainer container, TypeInfo type, boolean visible) {
        this(container, visible);
        this.type = type;
    }

    static Annotation parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        ClassFile file = container.classFile();
        Annotation anno = new Annotation(container, visible);
        anno.type = file.repository().toType(pool.getString(buffer.getUShort()));
        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            String name = pool.getString(buffer.getUShort());
            anno.values.put(name, ElementValue.parse(file, pool, buffer, visible));
        }
        return anno;
    }

    @Override
    protected void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(type.getInternalName()));
        buffer.putShort(values.size());
        for (Pair<String, ElementValue> p : values) {
            buffer.putShort(pool.indexOf(p.getKey()));
            p.getValue().write(pool, buffer);
        }
    }

    @Override
    protected void collect(ConstPool pool) {
        pool.acquireUtf(type.getInternalName());
        for (Pair<String, ElementValue> p : values) {
            pool.acquireUtf(p.getKey());
            p.getValue().collect(pool);
        }
    }

    @Override
    public Annotation copy(AttributeContainer container) {
        Annotation copy = new Annotation(container, type, visible);
        copyValues(copy);
        return copy;
    }

    @Override
    public Annotation copy() {
        return (Annotation) super.copy();
    }
}
