package vip.creatio.gca.constant;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.util.ClassUtil;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.Descriptor;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

public class MethodTypeConst extends Const implements Descriptor {
    private String descriptor;
    private String[] descriptors;

    MethodTypeConst(ConstPool pool, String nameAndType) {
        super(pool, ConstType.METHOD_TYPE);
        this.descriptor = nameAndType;
        recache();
    }

    public MethodTypeConst(ConstPool pool) {
        super(pool, ConstType.METHOD_TYPE);
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Const copy() {
        return new MethodTypeConst(pool, descriptor);
    }

    @Override
    public int byteSize() {
        return 3;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(pool.acquireUtf(descriptor).index());
    }

    @Override
    void collect() {
        pool.acquireUtf(descriptor);
    }

    @Override
    public void parse(ClassFileParser pool, ByteVector buffer) {
        descriptor = pool.getString(buffer.getShort());
        recache();
    }

    public String toString() {
        return "MethodType{discriptor=" + descriptor + '}';
    }

    @Override
    public void recache() {
        this.descriptors = ClassUtil.fromSignature(descriptor);
    }

    @Override
    public @Nullable String[] getDescriptors() {
        return descriptors;
    }

    @Override
    public @Nullable String getDescriptor() {
        return this.descriptor;
    }

    @Override
    public void setDescriptor(String str) {
        this.descriptor = str;
    }

    @Override
    public int hashCode() {
        return descriptor.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodTypeConst) {
            return ((MethodTypeConst) obj).pool == pool && ((MethodTypeConst) obj).descriptor.equals(descriptor);
        }
        return false;
    }
}
