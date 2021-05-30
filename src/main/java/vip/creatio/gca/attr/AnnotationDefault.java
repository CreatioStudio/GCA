package vip.creatio.gca.attr;

import vip.creatio.gca.*;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;

public class AnnotationDefault extends Attribute {

    // can be Integer, Float, Double, Long and String
    private ElementValue value;

    private AnnotationDefault(AttributeContainer container) {
        super(container);
    }

    public AnnotationDefault(DeclaredMethod mth) {
        this((AttributeContainer) mth);
    }

    public AnnotationDefault(DeclaredMethod mth, ElementValue value) {
        this((AttributeContainer) mth);
        this.value = value;
    }

    public static AnnotationDefault parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        AnnotationDefault inst = new AnnotationDefault(container);

        inst.value = new ElementValue(container.classFile(), pool, buffer);
        return inst;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, DeclaredMethod.class);
    }

    @Override
    public String name() {
        return "AnnotationDefault";
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public void writeData(ByteVector buffer) {
        value.write(buffer);
    }

    @Override
    protected void collect() {
        super.collect();
        constPool().acquireValue(value);
    }

    public ElementValue getValue() {
        return value;
    }

    public void setValue(ElementValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AnnotationDefault{value=" + value.toString() + '}';
    }
}
