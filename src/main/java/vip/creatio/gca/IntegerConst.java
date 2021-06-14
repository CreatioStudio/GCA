package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class IntegerConst extends Const.Value {

    private final int data;

    public IntegerConst(int data) {
        this.data = data;
    }

    IntegerConst(ByteVector buffer) {
        data = buffer.getInt();
    }

    @Override
    public ConstType constantType() {
        return ConstType.INTEGER;
    }

    @Override
    public ValueType valueType() {
        return ValueType.INT;
    }

    public int getInt() {
        return data;
    }

    void write(ByteVector buffer) {
        buffer.putInt(data);
    }

    @Override
    public Object value() {
        return getInt();
    }

    @Override
    public String toString() {
        return "Integer{" + getInt() + '}';
    }

    @Override
    public int hashCode() {
        return data * 31 ^ 0x12345678;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerConst) {
            return ((IntegerConst) obj).data == data;
        }
        return false;
    }
}
