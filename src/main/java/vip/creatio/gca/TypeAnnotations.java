package vip.creatio.gca;

import vip.creatio.gca.attr.TableAttribute;
import vip.creatio.gca.util.common.ByteVector;

class TypeAnnotations extends TableAttribute<TypeAnnotation> {

    private final boolean runtimeVisible;

    TypeAnnotations(AttributeContainer container, boolean visible) {
        super(container);
        this.runtimeVisible = visible;
    }

    void add(TypeAnnotation anno) {
        remove(anno.annotationType());
        items.add(anno);
    }

    boolean remove(TypeInfo type) {
        return items.removeIf(annotation -> annotation.annotationType().equals(type));
    }

    boolean remove(String className) {
        return items.removeIf(annotation -> annotation.annotationType().getTypeName().equals(className));
    }

    TypeAnnotation get(String typeName) {
        for (TypeAnnotation item : items) {
            if (item.annotationType().getTypeName().equals(typeName)) return item;
        }
        return null;
    }

    TypeAnnotation get(TypeInfo type) {
        for (TypeAnnotation item : items) {
            if (item.annotationType().equals(type)) return item;
        }
        return null;
    }

    @Override
    public String name() {
        return runtimeVisible ? "RuntimeVisibleTypeAnnotations" : "RuntimeInvisibleTypeAnnotations";
    }


    /* internals */

    static TypeAnnotations
    parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        TypeAnnotations inst = new TypeAnnotations(container, visible);
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(TypeAnnotation.parse(container, pool, buffer, visible));
        }
        return inst;
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        for (TypeAnnotation item : items) {
            item.collect(pool);
        }
    }

    @Override
    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort(items.size());
        for (TypeAnnotation item : items) {
            item.write(pool, buffer);
        }
    }
}
