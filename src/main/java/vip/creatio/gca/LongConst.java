package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class LongConst extends Const.Value implements Const.DualSlot {

    private final long data;

    public LongConst(long data) {
        this.data = data;
    }

    LongConst(ByteVector buffer) {
        data = buffer.getLong();
    }

    void write(ByteVector buffer) {
        buffer.putLong(data);
    }

    @Override
    public ValueType valueType() {
        return ValueType.LONG;
    }

    @Override
    public ConstType constantType() {
        return ConstType.LONG;
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
            return ((LongConst) obj).data == data;
        }
        return false;
    }
}
