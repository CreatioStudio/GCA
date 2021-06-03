package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.util.ClassUtil;
import vip.creatio.gca.util.Immutable;

import vip.creatio.gca.util.ByteVector;

@Immutable
public class NameAndTypeConst extends Const implements Descriptor {
    private /* final */ String name;
    private String descriptor;

    private String[] descriptors;

    NameAndTypeConst(ConstPool pool) {
        super(pool, ConstType.NAME_AND_TYPE);
    }

    public NameAndTypeConst(ConstPool pool, String name, String descriptor) {
        super(pool, ConstType.NAME_AND_TYPE);
        this.name = name;
        this.descriptor = descriptor;
        recache();
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new NameAndTypeConst(pool, name, descriptor);
    }

    @Override
    int byteSize() {
        return 5;
    }

    public String toString() {
        return "NameAndType{name=" + getName() + ",discriptor=" + getDescriptor() + '}';
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(pool.acquireUtf(name).index());
        buffer.putShort(pool.acquireUtf(descriptor).index());
    }

    @Override
    public void parse(ClassFileParser pool, ByteVector buffer) {
        name = pool.getString(buffer.getShort());
        descriptor = pool.getString(buffer.getShort());
        recache();
    }

    @Override
    void collect() {
        pool.acquireUtf(name);
        pool.acquireUtf(descriptor);
    }

    public String getName() {
        return name;
    }

    @Override
    public @Nullable String getDescriptor() {
        return descriptor;
    }

    @Override
    public void setDescriptor(String str) {
        this.descriptor = str;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + descriptor.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NameAndTypeConst) {
            return ((NameAndTypeConst) obj).pool == pool
                    && ((NameAndTypeConst) obj).name.equals(name)
                    && ((NameAndTypeConst) obj).descriptor.equals(descriptor);
        }
        return false;
    }

    @Override
    public void recache() {
        this.descriptors = ClassUtil.fromSignature(descriptor);
    }

    @Override
    public @Nullable String[] getDescriptors() {
        return descriptors;
    }
}
