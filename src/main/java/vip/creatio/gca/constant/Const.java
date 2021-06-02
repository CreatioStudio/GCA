package vip.creatio.gca.constant;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.util.ByteVector;

@SuppressWarnings("unused")
public abstract class Const {

    protected ConstPool pool;
    protected final ConstType type;
    
    Const(ConstPool pool, ConstType type) {
        this.pool = pool;
        this.type = type;
    }

    public abstract int byteSize();

    public final ConstType type() {
        return type;
    }

    public final ConstPool constPool() {
        return pool;
    }

    public final byte tag() {
        return type().tag;
    }

    public abstract Const copy();

    public abstract boolean isImmutable();

    // starts from 1, might be a negative, use index() & 0xFFFF to get positive
    public final int index() {
        return pool.indexOf(this);
    }

    public final ClassFile classFile() {
        return pool.classFile();
    }



    void parse(ClassFileParser pool, ByteVector buffer) {}

    void collect() {}

    abstract void write(ByteVector buffer);



    // marker class which indicates this constant can be accepted in ConstantValue
    public static abstract class Value extends Const {
        Value(ConstPool pool, ConstType type) {
            super(pool, type);
        }

        public abstract Object value();

        public abstract ValueType valueType();
    }

    // marker interface which indicates this constant takes 2 slots
    public interface DualSlot {}
}
