package vip.creatio.gca;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Pair;

public class Annotation extends AnnotationInfo {

    Annotation(ConstPool pool) {
        super(pool);
    }

    public Annotation(ConstPool pool, Type type) {
        this(pool);
        this.type = type;
    }

    public static Annotation parse(ClassFile file, ClassFileParser pool, ByteVector buffer) {
        Annotation anno = new Annotation(file.constPool());
        anno.type = Types.toType(pool.getString(buffer.getUShort()));
        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            String name = pool.getString(buffer.getUShort());
            anno.values.put(name, ElementValue.parse(file, pool, buffer));
        }
        return anno;
    }

    protected void write(ByteVector buffer) {
        buffer.putShort(constPool().acquireUtf(type.getInternalName()).index());
        buffer.putShort(values.size());
        for (Pair<String, ElementValue> p : values) {
            buffer.putShort(constPool().acquireUtf(p.getKey()).index());
            p.getValue().write(pool, buffer);
        }
    }

    protected void collect() {
        constPool().acquireUtf(type.getInternalName());
        for (Pair<String, ElementValue> p : values) {
            constPool().acquireUtf(p.getKey());
            p.getValue().collect(pool);
        }
    }

    @Override
    protected AnnotationInfo copy() {
        return new Annotation(pool, type);
    }
}
