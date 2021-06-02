package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class DoubleConst extends Const.Value implements Const.DualSlot {

    private final double data;

    public DoubleConst(ConstPool pool, double data) {
        super(pool, ConstType.DOUBLE);
        this.data = data;
    }

    public DoubleConst(ConstPool pool, ByteVector buffer) {
        super(pool, ConstType.DOUBLE);
        data = buffer.getDouble();
    }

    @Override
    public int byteSize() {
        return 9;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putDouble(data);
    }

    public double getDouble() {
        return data;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new DoubleConst(pool, data);
    }

    @Override
    public Object value() {
        return getDouble();
    }

    @Override
    public String toString() {
        return "Double{" + getDouble() + '}';
    }

    @Override
    public ValueType valueType() {
        return ValueType.DOUBLE;
    }

    @Override
    public int hashCode() {
        return ((int) Double.doubleToRawLongBits(data) * 31) ^ 0x12345678;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleConst) {
            return ((DoubleConst) obj).pool == pool && ((DoubleConst) obj).data == data;
        }
        return false;
    }
}
