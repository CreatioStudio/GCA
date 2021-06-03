package vip.creatio.gca.attr;

import vip.creatio.gca.*;
import vip.creatio.gca.Const;

import vip.creatio.gca.util.ByteVector;

/**
 * A ConstantValue attribute represents the value of a constant field.
 * There can be no more than one ConstantValue attribute in the attributes
 * table of a given field_info structure. If the field is static (that is,
 * the ACC_STATIC flag in the access_flags item of the field_info structure
 * is set) then the constant field represented by the field_info structure
 * is assigned the value referenced by its ConstantValue attribute as part
 * of the initialization of the class or interface declaring the constant
 * field. This occurs prior to the invocation of the class or interface
 * initialization method of that class or interface.
 */
public class ConstantValue extends Attribute {

    // can be Integer, Float, Double, Long and String
    private Object constantValue;

    private ConstantValue(AttributeContainer container) {
        super(container);
    }

    public ConstantValue(DeclaredField field) {
        this((AttributeContainer) field);
    }

    public ConstantValue(DeclaredField field, Object constantValue) {
        this((AttributeContainer) field);
        this.constantValue = constantValue;
    }

    public static ConstantValue parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        ConstantValue inst = new ConstantValue(container);

        inst.constantValue = ((Const.Value) pool.get(buffer.getShort())).value();
        return inst;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, DeclaredField.class);
    }

    @Override
    public String name() {
        return "ConstantValue";
    }

    @Override
    public boolean isEmpty() {
        return constantValue == null;
    }

    @Override
    public void writeData(ByteVector buffer) {
        buffer.putShort(constPool().acquireValue(constantValue).index());
    }

    @Override
    protected void collect() {
        super.collect();
        constPool().acquireValue(constantValue);
    }

    public Object getValue() {
        return constantValue;
    }

    public void setValue(Object value) {
        this.constantValue = value;
    }

    @Override
    public String toString() {
        return "ConstantValue{value=" + constantValue.toString() + '}';
    }
}
