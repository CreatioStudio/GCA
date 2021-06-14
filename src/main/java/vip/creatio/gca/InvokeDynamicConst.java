package vip.creatio.gca;

import vip.creatio.gca.attr.BootstrapMethods;

import vip.creatio.gca.util.common.ByteVector;

public class InvokeDynamicConst implements Const {
    private BootstrapMethods.Method method;
    private NameAndTypeConst pair;      //TODO: wtf this pair actually used for, confusing

    InvokeDynamicConst(BootstrapMethods.Method bootstrap, String mthName, String mthDescriptor) {
        this.method = bootstrap;
        this.pair = new NameAndTypeConst(mthName, mthDescriptor);
    }

    // temporary
    InvokeDynamicConst(NameAndTypeConst pair) {
        this.pair = pair;
    }

    public String toString() {
        return "InvokeDynamic{bootstrap_method_attr=" + method
                + ",mth_name=" + getMethodName() + ",mth_descriptor=" + getMethodDescriptor() + '}';
    }

    @Override
    public ConstType constantType() {
        return ConstType.INVOKE_DYNAMIC;
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(method.index());
        buffer.putShort(pool.indexOf(pair));
    }

    void collect(ConstPool pool) {
        pool.acquire(pair);
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
        this.pair = new NameAndTypeConst(methodName, pair.getDescriptor());
    }

    public String getMethodDescriptor() {
        return pair.getDescriptor();
    }

    public void setMethodDescriptor(String methodDescriptor) {
        this.pair = new NameAndTypeConst(pair.getName(), methodDescriptor);
    }

    @Override
    public int hashCode() {
        return  method.hashCode() +
                pair.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvokeDynamicConst) {
            return ((InvokeDynamicConst) obj).method.equals(method)
                    && ((InvokeDynamicConst) obj).pair.equals(pair);
        }
        return false;
    }
}
