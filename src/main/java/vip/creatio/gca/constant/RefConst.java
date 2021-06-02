package vip.creatio.gca.constant;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.Descriptor;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

public class RefConst extends Const implements Descriptor {
    private ClassConst clazz;
    private NameAndTypeConst pair;

    public RefConst(ConstPool pool, ConstType type, ClassConst clazz, String name, String descriptor) {
        super(pool, type);
        this.clazz = clazz;
        this.pair = new NameAndTypeConst(pool, name, ClassUtil.toBytecodeName(descriptor));
        recache();
    }

    public RefConst(ConstPool pool, ConstType type) {
        super(pool, type);
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Const copy() {
        return new RefConst(pool, type, clazz, pair.getName(), pair.getDescriptor());
    }

    @Override
    public int byteSize() {
        return 5;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(getDeclaringClass().index());
        buffer.putShort(pool.acquire(pair).index());
    }

    @Override
    void collect() {
        pool.acquire(pair);
    }

    @Override
    public void parse(ClassFileParser pool, ByteVector buffer) {
        clazz = (ClassConst) pool.get(buffer.getUShort());
        pair = (NameAndTypeConst) pool.get(buffer.getUShort());
    }

    public ClassConst getDeclaringClass() {
        return clazz;
    }

    public String getName() {
        return pair.getName();
    }

    public void setName(String name) {
        this.pair = new NameAndTypeConst(pool, name, pair.getDescriptor());
    }

    public String getDescriptor() {
        return pair.getDescriptor();
    }

    public void setDescriptor(String descriptor) {
        this.pair = new NameAndTypeConst(pool, pair.getName(), descriptor);
    }

    public void setClazz(ClassConst clazz) {
        this.clazz = clazz;
    }

    //TODO: DeclaredObject getReference()

    @Override
    public int hashCode() {
        return clazz.hashCode() + pair.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "Ref{type=" + type + ",class=" + clazz + ",name=" + getName() + ",descriptor=" + getDescriptor() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RefConst) {
            return ((RefConst) obj).pool == pool
                    && ((RefConst) obj).clazz.equals(clazz)
                    && ((RefConst) obj).pair.equals(pair);
        }
        return false;
    }

    @Override
    public void recache() {
        pair.recache();
    }

    @Override
    public @Nullable String[] getDescriptors() {
        return pair.getDescriptors();
    }
}
