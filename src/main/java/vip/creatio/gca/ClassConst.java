package vip.creatio.gca;

import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;

public class ClassConst extends Const {

    private TypeInfo type;

    ClassConst(ConstPool pool, String name) {
        super(pool, ConstType.CLASS);
        this.type = Types.toType(name);
    }

    ClassConst(ConstPool pool, TypeInfo type) {
        super(pool, ConstType.CLASS);
        this.type = type;
    }

    ClassConst(ConstPool pool) {
        super(pool, ConstType.CLASS);
    }

    public String toString() {
        return "Class{name=" + getTypeName() + '}';
    }

    public String getTypeName() {
        return type.getTypeName();
    }

    public TypeInfo getTypeInfo() {
        return type;
    }

    @Override
    public Const copy() {
        return new ClassConst(pool, type);
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    int byteSize() {
        return 3;
    }

    @Override
    void write(ByteVector buffer) {
        buffer.putByte(type().tag);
        buffer.putShort(pool.acquireUtf(type.getInternalName()).index());
    }

    @Override
    void collect() {
        pool.acquireUtf(type.getInternalName());
    }

    @Override
    void parse(ClassFileParser pool, ByteVector buffer) {
        this.type = Types.toType(pool.getString(buffer.getShort()));
    }
}
