package vip.creatio.gca;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Immutable;

@SuppressWarnings("unused")
public abstract class Const {

    protected ConstPool pool;
    protected final ConstType type;
    
    Const(ConstPool pool, ConstType type) {
        this.pool = pool;
        this.type = type;
    }

    public final ConstType type() {
        return type;
    }

    public final ConstPool constPool() {
        return pool;
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

    final byte tag() {
        return type().tag;
    }

    abstract int byteSize();

    abstract void write(ByteVector buffer);



    // marker class which indicates this constant can be accepted in ConstantValue
    @Immutable
    public static abstract class Value extends Const {
        Value(ConstPool pool, ConstType type) {
            super(pool, type);
        }

        public abstract Object value();

        public abstract ValueType valueType();

        public static Value of(ConstPool pool, Object v) {
            ValueType type = ValueType.getValueType(v);
            switch (type) {
                case BYTE:
                    return new IntegerConst(pool, (Byte) v);
                case CHAR:
                    return new IntegerConst(pool, (Character) v);
                case DOUBLE:
                    return new DoubleConst(pool, (Double) v);
                case FLOAT:
                    return new FloatConst(pool, (Float) v);
                case INT:
                    return new IntegerConst(pool, (Integer) v);
                case LONG:
                    return new LongConst(pool, (Long) v);
                case SHORT:
                    return new IntegerConst(pool, (Short) v);
                case BOOLEAN:
                    return new IntegerConst(pool, ((boolean) v) ? 1 : 0);
                case STRING:
                    return new StringConst(pool, (String) v);
                default:
                    throw new UnsupportedOperationException(v.getClass().getName());
            }
        }
    }

    // marker interface which indicates this constant takes 2 slots
    public interface DualSlot {}
}
