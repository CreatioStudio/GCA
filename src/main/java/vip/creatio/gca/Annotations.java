package vip.creatio.gca;

import vip.creatio.gca.attr.TableAttribute;
import vip.creatio.gca.util.common.ByteVector;

class Annotations extends TableAttribute<Annotation> {

    private final boolean runtimeVisible;

    Annotations(AttributeContainer container, boolean runtimeVisible) {
        super(container);
        this.runtimeVisible = true;
    }

    void add(Annotation anno) {
        remove(anno.annotationType());
        items.add(anno);
    }

    boolean remove(TypeInfo type) {
        return items.removeIf(annotation -> annotation.annotationType().equals(type));
    }

    boolean remove(String className) {
        return items.removeIf(annotation -> annotation.annotationType().getTypeName().equals(className));
    }

    Annotation get(String typeName) {
        for (Annotation item : items) {
            if (item.annotationType().getTypeName().equals(typeName)) return item;
        }
        return null;
    }
    
    Annotation get(TypeInfo type) {
        for (Annotation item : items) {
            if (item.annotationType().equals(type)) return item;
        }
        return null;
    }

    @Override
    public String name() {
        return runtimeVisible ? "RuntimeVisibleAnnotations" : "RuntimeInvisibleAnnotations";
    }



    /* internals */

    static Annotations
    parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        Annotations inst = new Annotations(container, visible);
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(Annotation.parse(container.classFile(), pool, buffer, visible));
        }
        return inst;
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        for (Annotation item : items) {
            item.collect(pool);
        }
    }

    @Override
    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort(items.size());
        for (Annotation item : items) {
            item.write(pool, buffer);
        }
    }
}
