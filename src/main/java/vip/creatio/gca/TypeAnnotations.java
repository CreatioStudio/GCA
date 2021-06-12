package vip.creatio.gca;

import vip.creatio.gca.attr.TableAttribute;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.util.ByteVector;

public class TypeAnnotations extends TableAttribute<TypeAnnotation> {

    private boolean runtimeVisible;

    public TypeAnnotations(AttributeContainer container) {
        super(container);
    }

    public void add(TypeAnnotation anno) {
        remove(anno.annotationType());
        items.add(anno);
    }

    public TypeAnnotation add(String className) {
        return add(classFile().toType(className));
    }

    public TypeAnnotation add(TypeInfo type) {
        TypeAnnotation anno = new TypeAnnotation(constPool(), type);
        add(anno);
        return anno;
    }

    public boolean remove(TypeInfo type) {
        return items.removeIf(annotation -> annotation.annotationType().equals(type));
    }

    public boolean remove(String className) {
        return items.removeIf(annotation -> annotation.annotationType().getTypeName().equals(className));
    }

    public TypeAnnotation get(String typeName) {
        for (TypeAnnotation item : items) {
            if (item.annotationType().getTypeName().equals(typeName)) return item;
        }
        return null;
    }

    public TypeAnnotation get(TypeInfo type) {
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
        TypeAnnotations inst = new TypeAnnotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(TypeAnnotation.parse(container, pool, buffer));
        }
        return inst;
    }

    @Override
    protected void collect() {
        super.collect();
        for (TypeAnnotation item : items) {
            item.collect();
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(items.size());
        for (TypeAnnotation item : items) {
            item.write(buffer);
        }
    }
}
