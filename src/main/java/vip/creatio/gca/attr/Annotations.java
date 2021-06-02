package vip.creatio.gca.attr;

import vip.creatio.gca.Attribute;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.util.ByteVector;

public class Annotations extends TableAttribute<Annotation> {

    private boolean runtimeVisible;

    public Annotations(AttributeContainer container) {
        super(container);
    }

    public static Annotations parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        Annotations inst = new Annotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(Annotation.parse(container.classFile(), pool, buffer));
        }
        return inst;
    }

    public void add(Annotation anno) {
        remove(anno.getClassName());
        items.add(anno);
    }

    public Annotation add(String className) {
        Annotation anno = new Annotation(container.classFile().constPool(), className);
        add(anno);
        return anno;
    }

    public boolean remove(String className) {
        return items.removeIf(annotation -> annotation.getClassName().equals(className));
    }

    //@Override
    public Attribute copy() {
        return null;
    }

    @Override
    public String name() {
        return runtimeVisible ? "RuntimeVisibleAnnotations" : "RuntimeInvisibleAnnotations";
    }

    @Override
    protected void collect() {
        super.collect();
        for (Annotation item : items) {
            item.collect();
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(items.size());
        for (Annotation item : items) {
            item.write(buffer);
        }
    }
}
