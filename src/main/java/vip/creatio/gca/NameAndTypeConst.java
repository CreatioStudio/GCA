package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.Pair;

public class NameAndTypeConst extends Pair<String, String> implements Const {

    public NameAndTypeConst(String name, String descriptor) {
        super(name, descriptor);
    }

    public String toString() {
        return "NameAndType{name=" + getName() + ",descriptor=" + getDescriptor() + '}';
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(getKey()));
        buffer.putShort(pool.indexOf(getValue()));
    }

    void collect(ConstPool pool) {
        pool.acquireUtf(getKey());
        pool.acquireUtf(getValue());
    }

    public String getName() {
        return getKey();
    }

    public void setName(String name) {
        setKey(name);
    }

    public String getDescriptor() {
        return getValue();
    }

    public void setDescriptor(String str) {
        setValue(str);
    }

    @Override
    public ConstType constantType() {
        return ConstType.NAME_AND_TYPE;
    }
}
