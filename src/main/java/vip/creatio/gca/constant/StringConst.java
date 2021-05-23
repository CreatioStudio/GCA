package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class StringConst extends Const.Value {
    private /* final */ String string;

    public StringConst(ConstPool pool, String string) {
        super(pool, ConstType.STRING);
        this.string = string;
    }

    public StringConst(ConstPool pool) {
        super(pool, ConstType.STRING);
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new StringConst(pool, string);
    }

    @Override
    public int byteSize() {
        return 3;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(pool.acquireUtf(string).index());
    }

    @Override
    public ValueType valueType() {
        return ValueType.STRING;
    }

    @Override
    public void parse(ClassFileParser pool, ByteVector buffer) {
        this.string = pool.getString(buffer.getShort());
    }

    @Override
    void collect() {
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
            return ((StringConst) obj).pool == pool
                    && ((StringConst) obj).string.equals(string);
        }
        return super.equals(obj);
    }
}
