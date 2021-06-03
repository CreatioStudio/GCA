package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ValueType;
import vip.creatio.gca.ClassConst;

import vip.creatio.gca.util.ByteVector;
import java.util.HashMap;
import java.util.Map;

// newarray and anewarray
public class NewArrayOpCode extends OpCode {

    private static final Map<Class<?>, Byte> ARRAY_TYPE_CODE = new HashMap<>();
    static {
        ARRAY_TYPE_CODE.put(boolean.class, (byte) 4);
        ARRAY_TYPE_CODE.put(char.class, (byte) 5);
        ARRAY_TYPE_CODE.put(float.class, (byte) 6);
        ARRAY_TYPE_CODE.put(double.class, (byte) 7);
        ARRAY_TYPE_CODE.put(byte.class, (byte) 8);
        ARRAY_TYPE_CODE.put(short.class, (byte) 9);
        ARRAY_TYPE_CODE.put(int.class, (byte) 10);
        ARRAY_TYPE_CODE.put(long.class, (byte) 11);
    }

    private final boolean isPrimitive;
    private ClassConst refType;
    private Class<?> primitiveType;
    private int dimension = 1;

    public NewArrayOpCode(CodeContainer codes, ClassConst type) {
        this(codes, type, 1);
    }

    public NewArrayOpCode(CodeContainer codes, ClassConst type, int dimension) {
        super(codes);
        this.isPrimitive = false;
        this.refType = type;
        this.dimension = dimension;
    }

    public NewArrayOpCode(CodeContainer codes, ValueType type) {
        super(codes);
        isPrimitive = true;
        switch (type) {
            case BYTE:
                primitiveType = byte.class;
                break;
            case CHAR:
                primitiveType = byte.class;
                break;
            case DOUBLE:
                primitiveType = double.class;
                break;
            case FLOAT:
                primitiveType = float.class;
                break;
            case INT:
                primitiveType = int.class;
                break;
            case LONG:
                primitiveType = long.class;
                break;
            case SHORT:
                primitiveType = short.class;
                break;
            case BOOLEAN:
                primitiveType = boolean.class;
                break;
            default:
                throw new UnsupportedOperationException("Unsupport type: " + type);
        }
    }

    NewArrayOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        if (type == OpCodeType.NEWARRAY) {
            this.isPrimitive = true;
            byte rawType = buffer.getByte();
            for (Map.Entry<Class<?>, Byte> entry : ARRAY_TYPE_CODE.entrySet()) {
                if (entry.getValue() == rawType) {
                    this.primitiveType = entry.getKey();
                    break;
                }
            }
        } else if (type == OpCodeType.ANEWARRAY) {
            this.isPrimitive = false;
            this.refType = (ClassConst) pool.get(buffer.getShort());
        } else if (type == OpCodeType.MULTIANEWARRAY) {
            this.isPrimitive = false;
            this.refType = (ClassConst) pool.get(buffer.getShort());
            this.dimension = buffer.getByte() & 0xFF;
        } else {
            throw new ClassFormatError("Invalid type: " + type);
        }
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public ClassConst getArrayType() {
        return refType;
    }

    public void setArrayType(ClassConst refType) {
        this.refType = refType;
    }

    public Class<?> getPrimitiveType() {
        return primitiveType;
    }

    public void setPrimitiveType(Class<?> primitiveType) {
        if (primitiveType.isPrimitive() && primitiveType != void.class) {
            this.primitiveType = primitiveType;
        } else {
            throw new UnsupportedOperationException("Invalid type: " + primitiveType);
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        if (isPrimitive) throw new UnsupportedOperationException("Primitive array cannot have dimension");
        this.dimension = dimension;
    }

    @Override
    public OpCodeType type() {
        if (isPrimitive) {
            return OpCodeType.NEWARRAY;
        } else {
            if (dimension == 1) {
                return OpCodeType.ANEWARRAY;
            } else {
                return OpCodeType.MULTIANEWARRAY;
            }
        }
    }

    @Override
    public void serialize(ByteVector buffer) {
        if (isPrimitive) {
            buffer.putByte(OpCodeType.NEWARRAY.getTag());
            buffer.putByte(ARRAY_TYPE_CODE.get(primitiveType));
        } else {
            if (dimension == 1) {
                buffer.putByte(OpCodeType.ANEWARRAY.getTag());
                buffer.putShort(refType.index());
            } else {
                buffer.putByte(OpCodeType.MULTIANEWARRAY.getTag());
                buffer.putShort(refType.index());
                buffer.putByte((byte) dimension);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + ' '
                + (isPrimitive ? primitiveType.getName() : refType.toString())
                + (dimension == 1 ? "" : ", " + dimension);
    }
}
