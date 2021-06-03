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
            if (v == null) throw new NullPointerException();
            if (v instanceof String)
                return new StringConst(pool, (String) v);
            if (v instanceof Integer)
                return new IntegerConst(pool, (Integer) v);
            if (v instanceof Long)
                return new LongConst(pool, (Long) v);
            if (v instanceof Double)
                return new DoubleConst(pool, (Double) v);
            if (v instanceof Float)
                return new FloatConst(pool, (Float) v);
            if (v instanceof Short)
                return new IntegerConst(pool, (Short) v);
            if (v instanceof Byte)
                return new IntegerConst(pool, (Byte) v);
            if (v instanceof Character)
                return new IntegerConst(pool, (Character) v);
            throw new UnsupportedOperationException(v.getClass().getName());
        }
    }

    public static boolean isValue(Object obj) {
        return obj instanceof String || obj instanceof Integer || obj instanceof Long
                || obj instanceof Double || obj instanceof Float || obj instanceof Short
                || obj instanceof Byte || obj instanceof Character;
    }

    // marker interface which indicates this constant takes 2 slots
    public interface DualSlot {}
}
