package vip.creatio.gca;

import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Pair;

public class Annotation extends DeclaredAnnotation {

    Annotation(boolean visible) {
        super(visible);
    }

    public Annotation(TypeInfo type, boolean visible) {
        this(visible);
        this.type = type;
    }

    static Annotation parse(ClassFile file, ClassFileParser pool, ByteVector buffer, boolean visible) {
        Annotation anno = new Annotation(visible);
        anno.type = file.toType(pool.getString(buffer.getUShort()));
        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            String name = pool.getString(buffer.getUShort());
            anno.values.put(name, ElementValue.parse(file, pool, buffer, visible));
        }
        return anno;
    }

    @Override
    protected void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.acquireUtf(type.getInternalName()).index());
        buffer.putShort(values.size());
        for (Pair<String, ElementValue> p : values) {
            buffer.putShort(pool.acquireUtf(p.getKey()).index());
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
    public Annotation copy() {
        Annotation copy = new Annotation(type, visible);
        copyValues(copy);
        return copy;
    }
}
