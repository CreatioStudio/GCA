package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class FloatConst extends Const.Value {

    private final float data;

    FloatConst(float data) {
        this.data = data;
    }

    FloatConst(ByteVector buffer) {
        this.data = buffer.getFloat();
    }

    public float getFloat() {
        return data;
    }

    @Override
    public ConstType constantType() {
        return ConstType.FLOAT;
    }

    void write(ByteVector buffer) {
        buffer.putFloat(data);
    }

    @Override
    public Object value() {
        return getFloat();
    }

    @Override
    public ValueType valueType() {
        return ValueType.FLOAT;
    }

    @Override
    public String toString() {
        return "Float{" + getFloat() + '}';
    }

    @Override
    public int hashCode() {
        return Float.floatToRawIntBits(data) * 31 ^ 0x12345678;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloatConst) {
            return ((FloatConst) obj).data == data;
        }
        return false;
    }
}
