package vip.creatio.gca.constant;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.Descriptor;
import vip.creatio.gca.attr.BootstrapMethods;

import vip.creatio.gca.util.ByteVector;

public class InvokeDynamicConst extends Const implements Descriptor {
    private BootstrapMethods.Method method;
    private NameAndTypeConst pair;

    public InvokeDynamicConst(ConstPool pool, BootstrapMethods.Method bootstrap, String mthName, String mthDescriptor) {
        super(pool, ConstType.INVOKE_DYNAMIC);
        this.method = bootstrap;
        this.pair = new NameAndTypeConst(pool, mthName, mthDescriptor);
        recache();
    }

    public InvokeDynamicConst(ConstPool pool) {
        super(pool, ConstType.INVOKE_DYNAMIC);
    }

    @Override
    public Const copy() {
        return new InvokeDynamicConst(pool, method, pair.getName(), pair.getDescriptor());
    }

    @Override
    public int byteSize() {
        return 5;
    }

    public String toString() {
        return "InvokeDynamic{bootstrap_method_attr=" + method
                + ",mth_name=" + getMethodName() + ",mth_descriptor=" + getMethodDescriptor() + '}';
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort(method.index());
        buffer.putShort(pool.acquire(pair).index());
    }

    @Override
    void collect() {
        pool.acquire(pair);
    }

    @Override
    void parse(ClassFileParser pool, ByteVector buffer) {
        this.method = classFile().bootstrapMethods().get(buffer.getUShort());
        this.pair = (NameAndTypeConst) pool.get(buffer.getShort());
    }

    public BootstrapMethods.Method getMethod() {
        return method;
    }

    public void setMethod(BootstrapMethods.Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return pair.getName();
    }

    public void setMethodName(String methodName) {
        this.pair = new NameAndTypeConst(pool,methodName, pair.getDescriptor());
    }

    public String getMethodDescriptor() {
        return pair.getDescriptor();
    }

    public void setMethodDescriptor(String methodDescriptor) {
        this.pair = new NameAndTypeConst(pool, pair.getName(), methodDescriptor);
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public int hashCode() {
        return  method.hashCode() +
                pair.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvokeDynamicConst) {
            return ((InvokeDynamicConst) obj).pool == pool
                    && ((InvokeDynamicConst) obj).method.equals(method)
                    && ((InvokeDynamicConst) obj).pair.equals(pair);
        }
        return false;
    }

    @Override
    public @Nullable String[] getDescriptors() {
        return pair.getDescriptors();
    }

    @Override
    public @Nullable String getDescriptor() {
        return pair.getDescriptor();
    }

    @Override
    public void setDescriptor(String str) {
        pair.setDescriptor(str);
    }

    @Override
    public void recache() {
        pair.recache();
    }
}
