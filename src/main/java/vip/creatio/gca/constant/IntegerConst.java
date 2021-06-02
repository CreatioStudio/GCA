package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class IntegerConst extends Const.Value {

    private final int data;

    public IntegerConst(ConstPool pool, int data) {
        super(pool, ConstType.INTEGER);
        this.data = data;
    }

    public IntegerConst(ConstPool pool, ByteVector buffer) {
        super(pool, ConstType.INTEGER);
        data = buffer.getInt();
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new IntegerConst(pool, data);
    }

    @Override
    public int byteSize() {
        return 5;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putInt(data);
    }

    @Override
    public ValueType valueType() {
        return ValueType.INT;
    }

    public int getInt() {
        return data;
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
            return ((IntegerConst) obj).pool == pool && ((IntegerConst) obj).data == data;
        }
        return false;
    }
}
