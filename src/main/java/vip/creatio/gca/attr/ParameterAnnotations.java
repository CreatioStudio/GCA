package vip.creatio.gca.attr;

import vip.creatio.gca.Annotation;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParameterAnnotations extends TableAttribute<List<Annotation>> {

    private boolean runtimeVisible;

    public ParameterAnnotations(AttributeContainer container) {
        super(container);
    }

    public static ParameterAnnotations parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer, boolean visible) {
        ParameterAnnotations inst = new ParameterAnnotations(container);
        inst.runtimeVisible = visible;
        int len = buffer.getUByte();
        for (int i = 0; i < len; i++) {
            int num = buffer.getUShort();
            List<Annotation> anno = new ArrayList<>();
            for (int j = 0; j < num; j++) {
                anno.add(Annotation.parse(container.classFile(), pool, buffer));
            }
            inst.items.add(anno);
        }
        return inst;
    }

    public List<Annotation> addParameter(Collection<Annotation> anno) {
        List<Annotation> list = new ArrayList<>(anno);
        items.add(list);
        return list;
    }

    public void addAnnotation(int index, Annotation anno) {
        List<Annotation> list = items.get(index);
        if (list == null) return;
        list.add(anno);
    }

    @Override
    public String name() {
        return runtimeVisible ? "RuntimeVisibleParameterAnnotations" : "RuntimeInvisibleParameterAnnotations";
    }

    @Override
    protected void collect() {
        super.collect();
        for (List<Annotation> item : items) {
            for (Annotation anno : item) {
                anno.collect();
            }
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putByte(items.size());
        for (List<Annotation> item : items) {
            buffer.putShort(item.size());
            for (Annotation anno : item) {
                anno.write(buffer);
            }
        }
    }
}
