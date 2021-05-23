package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class LongConst extends Const.Value implements Const.DualSlot {

    private final long data;

    LongConst(ConstPool pool, long data) {
        super(pool, ConstType.LONG);
        this.data = data;
    }

    public LongConst(ConstPool pool, ByteVector buffer) {
        super(pool, ConstType.LONG);
        data = buffer.getLong();
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new LongConst(pool, data);
    }

    @Override
    public int byteSize() {
        return 9;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putLong(data);
    }

    @Override
    public ValueType valueType() {
        return ValueType.LONG;
    }

    public long getLong() {
        return data;
    }

    @Override
    public Object value() {
        return getLong();
    }

    @Override
    public String toString() {
        return "Long{" + getLong() + '}';
    }

    @Override
    public int hashCode() {
        return ((int) data * 31) ^ 0x12345678;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LongConst) {
            return ((LongConst) obj).pool == pool && ((LongConst) obj).data == data;
        }
        return false;
    }
}
