package vip.creatio.gca;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

abstract class AbstractTypeConst extends Const {
    private String name;
    private String bcName;

    AbstractTypeConst(ConstPool pool, ConstType type, String name) {
        super(pool, type);
        this.name = name;
        this.bcName = ClassUtil.toBytecodeName(name);
    }

    AbstractTypeConst(ConstPool pool, ConstType type) {
        super(pool, type);
    }

    @Override
    int byteSize() {
        return 3;
    }

    @Override
    protected void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(pool.acquireUtf(bcName).index());
    }

    @Override
    void collect() {
        pool.acquireUtf(bcName);
    }

    @Override
    void parse(ClassFileParser pool, ByteVector buffer) {
        bcName = pool.getString(buffer.getShort());
        name = ClassUtil.toBinaryName(bcName);
    }

    public void setName(String name) {
        this.name = name;
        this.bcName = ClassUtil.toBytecodeName(name);
    }

    public String getName() {
        return name;
    }

    public String getBytecodeName() {
        return bcName;
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
