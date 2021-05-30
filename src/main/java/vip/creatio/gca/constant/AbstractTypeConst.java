package vip.creatio.gca.constant;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.ByteVector;

abstract class AbstractTypeConst extends Const {
    private String name;

    AbstractTypeConst(ConstPool pool, ConstType type, String name) {
        super(pool, type);
        this.name = name;
    }

    AbstractTypeConst(ConstPool pool, ConstType type) {
        super(pool, type);
    }

    @Override
    public int byteSize() {
        return 3;
    }

    @Override
    protected void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(pool.acquireUtf(name).index());
    }

    @Override
    void collect() {
        pool.acquireUtf(name);
    }

    @Override
    void parse(ClassFileParser pool, ByteVector buffer) {
        name = pool.getString(buffer.getShort());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass().isAssignableFrom(obj.getClass())) {
            return ((AbstractTypeConst) obj).pool == pool && obj.hashCode() == hashCode();
        }
        return false;
    }
}
