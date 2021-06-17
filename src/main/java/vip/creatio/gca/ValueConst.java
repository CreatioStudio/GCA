package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.util.common.BiMap;
import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.common.HashBiMap;

// int, float, long, double, String
public class ValueConst implements Const {

    private static final BiMap<ValueType, ConstType> CONST_TYPE_PAIR = HashBiMap.create(5);
    static {
        CONST_TYPE_PAIR.put(ValueType.INT, ConstType.INTEGER);
        CONST_TYPE_PAIR.put(ValueType.FLOAT, ConstType.FLOAT);
        CONST_TYPE_PAIR.put(ValueType.DOUBLE, ConstType.DOUBLE);
        CONST_TYPE_PAIR.put(ValueType.LONG, ConstType.LONG);
        CONST_TYPE_PAIR.put(ValueType.STRING, ConstType.STRING);
    }

    private final @NotNull Object val;
    private final @NotNull ValueType type;

    ValueConst(ClassFileParser pool, ConstType type, ByteVector buf) {
        this.type = CONST_TYPE_PAIR.inverse().get(type);
        switch (this.type) {
            case DOUBLE:
                this.val = buf.getDouble();
                break;
            case FLOAT:
                this.val = buf.getFloat();
                break;
            case INT:
                this.val = buf.getInt();
                break;
            case LONG:
                this.val = buf.getLong();
                break;
            case STRING:
                this.val = pool.getString(buf.getUShort());
                break;
            default:
                throw new IllegalArgumentException("unknown: " + this.type);
        }
    }

    ValueConst(@NotNull Object val, @NotNull ValueType type) {
        this.type = type;
        this.val = val;
    }

    ValueConst(int val) {
        this(val, ValueType.INT);
    }

    ValueConst(long val) {
        this(val, ValueType.LONG);
    }

    ValueConst(float val) {
        this(val, ValueType.FLOAT);
    }

    ValueConst(double val) {
        this(val, ValueType.DOUBLE);
    }

    ValueConst(String val) {
        this(val, ValueType.STRING);
    }

    public Object value() {
        return val;
    }

    public ValueType valueType() {
        return type;
    }

    @Override
    public ConstType constantType() {
        return CONST_TYPE_PAIR.get(type);
    }

    void collect(ConstPool pool) {
        if (type == ValueType.STRING)
            pool.acquireUtf((String) val);
    }

    void write(ConstPool pool, ByteVector buffer) {
        switch (type) {
            case DOUBLE:
                buffer.putDouble((Double) val);
                break;
            case FLOAT:
                buffer.putFloat((Float) val);
                break;
            case INT:
                buffer.putInt((Integer) val);
                break;
            case LONG:
                buffer.putLong((Long) val);
                break;
            case STRING:
                buffer.putShort(pool.indexOf((String) val));
                break;
            default:
                throw new IllegalArgumentException(type.name());
        }
    }

    public static ValueConst of(Object v) {
        ValueType type = ValueType.getValueType(v);
        if (type.isValue()) {
            switch (type) {
                case INT:
                case LONG:
                case STRING:
                case DOUBLE:
                case FLOAT:
                    return new ValueConst(v, type);
                case BYTE:
                    return new ValueConst((Byte) v);
                case CHAR:
                    return new ValueConst((Character) v);
                case SHORT:
                    return new ValueConst((Short) v);
                case BOOLEAN:
                    return new ValueConst(((boolean) v) ? 1 : 0);
            }
        }
        throw new UnsupportedOperationException(v.getClass().getName());
    }
}
