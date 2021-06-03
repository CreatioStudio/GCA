package vip.creatio.gca;

import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class FloatConst extends Const.Value {

    private final float data;

    FloatConst(ConstPool pool, float data) {
        super(pool, ConstType.FLOAT);
        this.data = data;
    }

    FloatConst(ConstPool pool, ByteVector buffer) {
        super(pool, ConstType.FLOAT);
        data = buffer.getFloat();
    }

    public float getFloat() {
        return data;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new FloatConst(pool, data);
    }

    @Override
    public Object value() {
        return getFloat();
    }

    @Override
    int byteSize() {
        return 5;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putFloat(data);
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
            return ((FloatConst) obj).pool == pool && ((FloatConst) obj).data == data;
        }
        return false;
    }
}
