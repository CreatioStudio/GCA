package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

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

    static AnnotationDefault parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        AnnotationDefault inst = new AnnotationDefault(container);

        inst.value = ElementValue.parse(container.classFile(), pool, buffer, true);
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
    public void writeData(ConstPool pool, ByteVector buffer) {
        value.write(pool, buffer);
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        pool.acquireValue(value);
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
