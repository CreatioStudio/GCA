package vip.creatio.gca;

import vip.creatio.gca.attr.TableAttribute;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;

public class Annotations extends TableAttribute<Annotation> {

    private boolean runtimeVisible;

    public Annotations(AttributeContainer container) {
        super(container);
    }

    Annotations(Annotations pair) {
        super(pair.container);
        super.items = pair.items;
        this.runtimeVisible = !pair.runtimeVisible;
    }

    public void add(Annotation anno) {
        remove(anno.annotationType());
        items.add(anno);
    }

    public Annotation add(String className) {
        return add(classFile().toType(className));
    }

    public Annotation add(TypeInfo type) {
        Annotation anno = new Annotation(constPool(), runtimeVisible, type);
        add(anno);
        return anno;
    }

    public boolean remove(TypeInfo type) {
        return items.removeIf(annotation -> annotation.annotationType().equals(type));
    }

    public boolean remove(String className) {
        return items.removeIf(annotation -> annotation.annotationType().getTypeName().equals(className));
    }

    public Annotation get(String typeName) {
        for (Annotation item : items) {
            if (item.annotationType().getTypeName().equals(typeName)) return item;
        }
        return null;
    }
    
    public Annotation get(TypeInfo type) {
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
        Annotations inst = new Annotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(Annotation.parse(container.classFile(), pool, buffer, visible));
        }
        return inst;
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
