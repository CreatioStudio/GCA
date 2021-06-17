package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.TypeInfo;
import vip.creatio.gca.ValueType;

import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.common.ByteVector;
import java.util.HashMap;
import java.util.Map;

// newarray and anewarray
public class NewArrayOpCode extends OpCode {

    private static final Map<TypeInfo, Byte> ARRAY_TYPE_CODE = new HashMap<>();
    static {
        ARRAY_TYPE_CODE.put(Types.BOOL, (byte) 4);
        ARRAY_TYPE_CODE.put(Types.CHAR, (byte) 5);
        ARRAY_TYPE_CODE.put(Types.FLOAT, (byte) 6);
        ARRAY_TYPE_CODE.put(Types.DOUBLE, (byte) 7);
        ARRAY_TYPE_CODE.put(Types.BYTE, (byte) 8);
        ARRAY_TYPE_CODE.put(Types.SHORT, (byte) 9);
        ARRAY_TYPE_CODE.put(Types.INT, (byte) 10);
        ARRAY_TYPE_CODE.put(Types.LONG, (byte) 11);
    }

    private final boolean isPrimitive;
    private TypeInfo refType;
    private TypeInfo primitiveType;
    private int dimension = 1;

    public NewArrayOpCode(CodeContainer codes, TypeInfo type) {
        this(codes, type, 1);
    }

    public NewArrayOpCode(CodeContainer codes, TypeInfo type, int dimension) {
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
                primitiveType = Types.BYTE;
                break;
            case CHAR:
                primitiveType = Types.CHAR;
                break;
            case DOUBLE:
                primitiveType = Types.DOUBLE;
                break;
            case FLOAT:
                primitiveType = Types.FLOAT;
                break;
            case INT:
                primitiveType = Types.INT;
                break;
            case LONG:
                primitiveType = Types.LONG;
                break;
            case SHORT:
                primitiveType = Types.SHORT;
                break;
            case BOOLEAN:
                primitiveType = Types.BOOL;
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
            for (Map.Entry<TypeInfo, Byte> entry : ARRAY_TYPE_CODE.entrySet()) {
                if (entry.getValue() == rawType) {
                    this.primitiveType = entry.getKey();
                    break;
                }
            }
        } else if (type == OpCodeType.ANEWARRAY) {
            this.isPrimitive = false;
            this.refType = (TypeInfo) pool.get(buffer.getShort());
        } else if (type == OpCodeType.MULTIANEWARRAY) {
            this.isPrimitive = false;
            this.refType = (TypeInfo) pool.get(buffer.getShort());
            this.dimension = buffer.getByte() & 0xFF;
        } else {
            throw new ClassFormatError("Invalid type: " + type);
        }
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public TypeInfo getArrayType() {
        return refType;
    }

    public void setArrayType(TypeInfo refType) {
        this.refType = refType;
    }

    public TypeInfo getPrimitiveType() {
        return primitiveType;
    }

    public void setPrimitiveType(TypeInfo primitiveType) {
        if (primitiveType instanceof Types.Primitive && primitiveType != Types.VOID) {
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
    public void write(ConstPool pool, ByteVector buffer) {
        if (isPrimitive) {
            buffer.putByte(OpCodeType.NEWARRAY.getTag());
            buffer.putByte(ARRAY_TYPE_CODE.get(primitiveType));
        } else {
            if (dimension == 1) {
                buffer.putByte(OpCodeType.ANEWARRAY.getTag());
                buffer.putShort(pool.indexOf(refType));
            } else {
                buffer.putByte(OpCodeType.MULTIANEWARRAY.getTag());
                buffer.putShort(pool.indexOf(refType));
                buffer.putByte(dimension);
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
