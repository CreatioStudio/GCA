package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.type.ClassInfo;
import vip.creatio.gca.type.Type;
import vip.creatio.gca.util.ClassUtil;

// representing a class info which may not exist.
public abstract class TypeInfo implements Type, Const {

    protected String name;

    public TypeInfo(String name) {
        this.name = name;
    }

    protected TypeInfo() {}

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConstType constantType() {
        return ConstType.CLASS;
    }

    public ClassInfo getImpl() {
        if (this instanceof ClassInfo)
            return (ClassInfo) this;
        return null;
    }

    final void write(ConstPool pool, ByteVector vector) {
        vector.putShort(pool.indexOf(ClassUtil.toBytecodeName(name)));
    }

    final void collect(ConstPool pool) {
        pool.acquireUtf(ClassUtil.toBytecodeName(name));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TypeInfo) {
            return ((TypeInfo) obj).name.equals(name);
        }
        return false;
    }

    public interface Mutable {
        void setName(String name);
    }
}
