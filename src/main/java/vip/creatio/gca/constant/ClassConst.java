package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

public class ClassConst extends Const {
    private String name;

    public ClassConst(ConstPool pool, String name) {
        super(pool, ConstType.CLASS);
        this.name = name;
    }

    public ClassConst(ConstPool pool) {
        super(pool, ConstType.CLASS);
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

    public String toString() {
        return "Class{name=" + getName() + '}';
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
    public Const copy() {
        return new ClassConst(pool, name);
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
        if (obj instanceof ClassConst) {
            return ((ClassConst) obj).pool == pool && obj.hashCode() == hashCode();
        }
        return false;
    }
}
