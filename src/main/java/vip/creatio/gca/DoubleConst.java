package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class DoubleConst extends Const.Value implements Const.DualSlot {

    private final double data;

    DoubleConst(double data) {
        this.data = data;
    }

    DoubleConst(ByteVector buffer) {
        data = buffer.getDouble();
    }

    public double getDouble() {
        return data;
    }

    void write(ByteVector buffer) {
        buffer.putDouble(data);
    }

    @Override
    public ConstType constantType() {
        return ConstType.DOUBLE;
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
            return ((DoubleConst) obj).data == data;
        }
        return false;
    }
}
