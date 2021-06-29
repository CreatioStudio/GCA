package vip.creatio.gca;

import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.common.ByteVector;

abstract class AbstractTypeConst implements Const {
    private String name;
    private String bcName;

    AbstractTypeConst(String name) {
        this.name = name;
        this.bcName = Types.toBytecodeName(name);
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(bcName));
    }

    void collect(ConstPool pool) {
        pool.acquireUtf(bcName);
    }

    public void setName(String name) {
        this.name = name;
        this.bcName = Types.toBytecodeName(name);
    }

    public String getName() {
        return name;
    }

    public String getBytecodeName() {
        return bcName;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass().isAssignableFrom(obj.getClass())) {
            return obj.hashCode() == hashCode();
        }
        return false;
    }
}
