package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class StringConst extends Const.Value {
    private final String string;

    StringConst(String string) {
        this.string = string;
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(string));
    }

    @Override
    public ConstType constantType() {
        return ConstType.STRING;
    }

    @Override
    public ValueType valueType() {
        return ValueType.STRING;
    }

    void collect(ConstPool pool) {
        pool.acquireUtf(string);
    }

    public String getString() {
        return string;
    }

    @Override
    public Object value() {
        return getString();
    }

    public String toString() {
        return "String{string=" + string + '}';
    }

    @Override
    public int hashCode() {
        return string.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringConst) {
            return ((StringConst) obj).string.equals(string);
        }
        return super.equals(obj);
    }
}
