package vip.creatio.gca;

import vip.creatio.gca.attr.TableAttribute;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;

public class Annotations extends TableAttribute<Annotation> {

    private boolean runtimeVisible;

    public Annotations(AttributeContainer container) {
        super(container);
    }

    static Annotations
    parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        Annotations inst = new Annotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(Annotation.parse(container.classFile(), pool, buffer));
        }
        return inst;
    }

    public void add(Annotation anno) {
        remove(anno.annotationType());
        items.add(anno);
    }

    public Annotation add(String className) {
        Annotation anno = new Annotation(container.classFile().constPool(), Types.toType(className));
        add(anno);
        return anno;
    }

    public boolean remove(String className) {
        return items.removeIf(annotation -> annotation.annotationType().equals(className));
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
