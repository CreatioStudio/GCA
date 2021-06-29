package vip.creatio.gca;

import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.type.ClassInfo;
import vip.creatio.gca.type.Type;

// representing a class info which may not exist.
public abstract class TypeInfo implements Type, Const {

    //TODO: split into pkg name and class name
    protected String name;

    public TypeInfo(String name) {
        this.name = name;
    }

    protected TypeInfo() {}

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTypeName() {
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
        vector.putShort(pool.indexOf(Types.toBytecodeName(name)));
    }

    final void collect(ConstPool pool) {
        pool.acquireUtf(Types.toBytecodeName(name));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof TypeInfo) {
            return ((TypeInfo) obj).getTypeName().equals(getTypeName());
        }
        return false;
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    public interface Mutable {
        void setName(String name);
    }
}
