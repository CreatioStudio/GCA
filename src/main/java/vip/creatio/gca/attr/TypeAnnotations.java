package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.util.ByteVector;

public class TypeAnnotations extends TableAttribute<TypeAnnotation> {

    private boolean runtimeVisible;

    public TypeAnnotations(AttributeContainer container) {
        super(container);
    }

    public static TypeAnnotations parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        TypeAnnotations inst = new TypeAnnotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(TypeAnnotation.parse(container, pool, buffer));
        }
        return inst;
    }

    public void add(TypeAnnotation anno) {
        remove(anno.getClassName());
        items.add(anno);
    }

    public TypeAnnotation add(String className) {
        TypeAnnotation anno = new TypeAnnotation(container.classFile().constPool(), className);
        add(anno);
        return anno;
    }

    public boolean remove(String className) {
        return items.removeIf(annotation -> annotation.getClassName().equals(className));
    }

    @Override
    public String name() {
        return runtimeVisible ? "RuntimeVisibleTypeAnnotations" : "RuntimeInvisibleTypeAnnotations";
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
