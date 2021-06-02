package vip.creatio.gca.attr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;
import vip.creatio.gca.code.*;
import vip.creatio.gca.constant.*;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * A Code attribute contains the Java Virtual Machine instructions
 * and auxiliary information for a single method, instance
 * initialization method, or class or interface initialization method.
 * Every Java Virtual Machine implementation must recognize Code
 * attributes. If the method is either native or abstract, its method_info
 * structure must not have a Code attribute. Otherwise, its method_info
 * structure must have exactly one Code attribute.
 */
public class Code extends Attribute implements AttributeContainer, Iterable<OpCode> {

    private final CodeContainer codes = new CodeContainer(method());
    private final List<ExceptionTable> tables = new ArrayList<>();
    private final List<Attribute> attributes = new ArrayList<>();

    private Code(AttributeContainer container) {
        super(container);
    }

    public Code(DeclaredMethod mth) {
        this((AttributeContainer) mth);
    }

    public static Code parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        Code c = new Code(container);

        c.codes.parse(pool, buffer);

        {
            int tableSize = buffer.getShort();
            for (int i = 0; i < tableSize; i++) {
                c.tables.add(c.new ExceptionTable(pool, buffer));
            }
        }

        {
            int attrSize = buffer.getShort();
            for (int i = 0; i < attrSize; i++) {
                c.attributes.add(pool.resolveAttribute(c, buffer));
            }
        }

        c.codes.parseFinished();
        return c;
    }

    public int maxStack() {
        return codes.getMaxStack();
    }

    public int maxLocals() {
        return codes.getMaxLocals();
    }

    public List<ExceptionTable> exceptionTables() {
        return tables;
    }

    public List<Attribute> attributes() {
        return attributes;
    }

    @NotNull
    @Override
    public Iterator<OpCode> iterator() {
        return codes.iterator();
    }

    public int offsetOf(OpCode code) {
        int sum = 0;
        for (OpCode op : codes) {
            if (op == code) return sum;
            else sum += op.byteSize();
        }
        return sum;
    }

    public @Nullable OpCode fromOffset(int offset) {
        return codes.fromOffset(offset);
    }

    public @Nullable OpCode fromOffsetNearest(int offset) {
        return codes.fromOffsetNearest(offset);
    }

    public int indexOf(OpCode code) {
        return codes.indexOf(code);
    }

    public OpCode get(int index) {
        return codes.fromIndex(index);
    }

    public OpCode getLast() {
        return codes.getLast();
    }

    public OpCode getFirst() {
        return codes.getFirst();
    }

    public Label getLabel(OpCode code) {
        return codes.getLabel(code);
    }

    public Label visitLabel(OpCode code) {
        Label l = getLabel(code);
        if (l == null) l = codes.addLabel(code);
        return l;
    }

    public Label addLabel(Label lb) {
        return codes.addLabel(lb);
    }

    public void add(int index, OpCode code) {
        codes.add(index, code);
    }

    public void add(OpCode code) {
        codes.add(code);
    }

    public void clear() {
        codes.clear();
        removeAttribute("StackMapTable");
        removeAttribute("LineNumberTable");
        removeAttribute("LocalVariableTable");
        removeAttribute("LocalVariableTypeTable");
        removeAttribute("RuntimeVisibleTypeAnnotations");
        removeAttribute("RuntimeInvisibleTypeAnnotations");
    }

    @Override
    public Code copy() {
        return null;//TODO
    }

    public OpCode emitNop() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.NOP);
        codes.add(c);
        return c;
    }

    // give a null to emit ACONST_NULL
    public OpCode emitConst(@Nullable Const.Value value) {
        OpCode c;
        if (value == null) {
            c = new UnaryOpCode(codes, OpCodeType.ACONST_NULL);
        } else {
            switch (value.valueType()) {
                case DOUBLE:
                case FLOAT:
                case INT:
                case LONG:
                case STRING:
                    c = new LoadConstOpCode(codes, value);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupport type: " + value.valueType());
            }
        }
        codes.add(c);
        return c;
    }

    public OpCode emitConst(int v) {
        OpCode c;
        switch (v) {
            case -1:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_M1);
                break;
            case 0:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_0);
                break;
            case 1:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_1);
                break;
            case 2:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_2);
                break;
            case 3:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_3);
                break;
            case 4:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_4);
                break;
            case 5:
                c = new UnaryOpCode(codes, OpCodeType.ICONST_5);
                break;
            default:
                if (v <= 0xFF && v >= -0xFF) {
                    c = new NumberOpCode(codes, OpCodeType.BIPUSH, v);
                } else if (v <= 0xFFFF && v >= -0xFFFF) {
                    c = new NumberOpCode(codes, OpCodeType.SIPUSH, v);
                } else {
                    c = new LoadConstOpCode(codes, new IntegerConst(constPool(), v));
                }
        }
        codes.add(c);
        return c;
    }

    public OpCode emitConst(boolean v) {
        return v ? emitConst(0) : emitConst(1);
    }

    public OpCode emitConst(long v) {
        OpCode c;
        if (v == 0L) {
            c = new UnaryOpCode(codes, OpCodeType.LCONST_0);
        } else if (v == 1L) {
            c = new UnaryOpCode(codes, OpCodeType.LCONST_1);
        } else {
            c = new LoadConstOpCode(codes, new LongConst(constPool(), v));
        }
        codes.add(c);
        return c;
    }

    public OpCode emitConst(float v) {
        OpCode c;
        if (v == 0F) {
            c = new UnaryOpCode(codes, OpCodeType.FCONST_0);
        } else if (v == 1F) {
            c = new UnaryOpCode(codes, OpCodeType.FCONST_1);
        } else if (v == 2F) {
            c = new UnaryOpCode(codes, OpCodeType.FCONST_2);
        } else {
            c = new LoadConstOpCode(codes, new FloatConst(constPool(), v));
        }
        codes.add(c);
        return c;
    }

    public OpCode emitConst(double v) {
        OpCode c;
        if (v == 0F) {
            c = new UnaryOpCode(codes, OpCodeType.DCONST_0);
        } else if (v == 1F) {
            c = new UnaryOpCode(codes, OpCodeType.DCONST_1);
        } else {
            c = new LoadConstOpCode(codes, new DoubleConst(constPool(), v));
        }
        codes.add(c);
        return c;
    }

    public OpCode emitConst(String s) {
        OpCode c = new LoadConstOpCode(codes, new StringConst(constPool(), s));
        codes.add(c);
        return c;
    }

    // set t to null to load a reference
    public OpCode emitLoad(@Nullable ValueType t, int index) {
        checkIndex(index);
        OpCode c = null;
        if (t != null) {
            switch (t) {
                case BYTE:
                case CHAR:
                case SHORT:
                case BOOLEAN:
                case INT:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.ILOAD_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.ILOAD_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.ILOAD_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.ILOAD_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.ILOAD, index);
                    }
                    break;
                case DOUBLE:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.DLOAD_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.DLOAD_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.DLOAD_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.DLOAD_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.DLOAD, index);
                    }
                    break;
                case FLOAT:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.FLOAD_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.FLOAD_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.FLOAD_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.FLOAD_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.FLOAD, index);
                    }
                    break;
                case LONG:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.LLOAD_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.LLOAD_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.LLOAD_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.LLOAD_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.LLOAD, index);
                    }
                    break;
            }
        }
        if (c == null) {
            if (index == 0) {
                c = new UnaryOpCode(codes, OpCodeType.ALOAD_0);
            } else if (index == 1) {
                c = new UnaryOpCode(codes, OpCodeType.ALOAD_1);
            } else if (index == 2) {
                c = new UnaryOpCode(codes, OpCodeType.ALOAD_2);
            } else if (index == 3) {
                c = new UnaryOpCode(codes, OpCodeType.ALOAD_3);
            } else {
                c = new NumberOpCode(codes, OpCodeType.ALOAD, index);
            }
        }
        codes.add(c);
        return c;
    }

    // load a reference
    public OpCode emitLoad(int index) {
        return emitLoad(null, index);
    }

    public OpCode emitArrayLoad(@Nullable ValueType t) {
        OpCode c = null;
        if (t != null) {
            switch (t) {
                case BOOLEAN:
                case BYTE:
                    c = new UnaryOpCode(codes, OpCodeType.BALOAD);
                    break;
                case CHAR:
                    c = new UnaryOpCode(codes, OpCodeType.CALOAD);
                    break;
                case DOUBLE:
                    c = new UnaryOpCode(codes, OpCodeType.DALOAD);
                    break;
                case FLOAT:
                    c = new UnaryOpCode(codes, OpCodeType.FALOAD);
                    break;
                case INT:
                    c = new UnaryOpCode(codes, OpCodeType.IALOAD);
                    break;
                case LONG:
                    c = new UnaryOpCode(codes, OpCodeType.LALOAD);
                    break;
                case SHORT:
                    c = new UnaryOpCode(codes, OpCodeType.SALOAD);
                    break;
            }
        }
        if (c == null) {
            c = new UnaryOpCode(codes, OpCodeType.AALOAD);
        }
        codes.add(c);
        return c;
    }

    public OpCode emitArrayLoad() {
        return emitArrayLoad(null);
    }

    // set t to null to load a reference
    public OpCode emitStore(@Nullable ValueType t, int index) {
        checkIndex(index);
        OpCode c = null;
        if (t != null) {
            switch (t) {
                case BYTE:
                case CHAR:
                case SHORT:
                case BOOLEAN:
                case INT:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.ISTORE_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.ISTORE_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.ISTORE_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.ISTORE_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.ISTORE, index);
                    }
                    break;
                case DOUBLE:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.DSTORE_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.DSTORE_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.DSTORE_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.DSTORE_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.DSTORE, index);
                    }
                    break;
                case FLOAT:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.FSTORE_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.FSTORE_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.FSTORE_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.FSTORE_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.FSTORE, index);
                    }
                    break;
                case LONG:
                    if (index == 0) {
                        c = new UnaryOpCode(codes, OpCodeType.LSTORE_0);
                    } else if (index == 1) {
                        c = new UnaryOpCode(codes, OpCodeType.LSTORE_1);
                    } else if (index == 2) {
                        c = new UnaryOpCode(codes, OpCodeType.LSTORE_2);
                    } else if (index == 3) {
                        c = new UnaryOpCode(codes, OpCodeType.LSTORE_3);
                    } else {
                        c = new NumberOpCode(codes, OpCodeType.LSTORE, index);
                    }
                    break;
            }
        }
        if (c == null) {
            if (index == 0) {
                c = new UnaryOpCode(codes, OpCodeType.ASTORE_0);
            } else if (index == 1) {
                c = new UnaryOpCode(codes, OpCodeType.ASTORE_1);
            } else if (index == 2) {
                c = new UnaryOpCode(codes, OpCodeType.ASTORE_2);
            } else if (index == 3) {
                c = new UnaryOpCode(codes, OpCodeType.ASTORE_3);
            } else {
                c = new NumberOpCode(codes, OpCodeType.ASTORE, index);
            }
        }
        codes.add(c);
        return c;
    }

    // store a reference
    public OpCode emitStore(int index) {
        return emitStore(null, index);
    }

    public OpCode emitArrayStore(@Nullable ValueType t) {
        OpCode c = null;
        if (t != null) {
            switch (t) {
                case BOOLEAN:
                case BYTE:
                    c = new UnaryOpCode(codes, OpCodeType.BASTORE);
                    break;
                case CHAR:
                    c = new UnaryOpCode(codes, OpCodeType.CASTORE);
                    break;
                case DOUBLE:
                    c = new UnaryOpCode(codes, OpCodeType.DASTORE);
                    break;
                case FLOAT:
                    c = new UnaryOpCode(codes, OpCodeType.FASTORE);
                    break;
                case INT:
                    c = new UnaryOpCode(codes, OpCodeType.IASTORE);
                    break;
                case LONG:
                    c = new UnaryOpCode(codes, OpCodeType.LASTORE);
                    break;
                case SHORT:
                    c = new UnaryOpCode(codes, OpCodeType.SASTORE);
                    break;
            }
        }
        if (c == null) {
            c = new UnaryOpCode(codes, OpCodeType.AASTORE);
        }
        codes.add(c);
        return c;
    }

    // store reference to array
    public OpCode emitArrayStore() {
        return emitArrayStore(null);
    }

    public OpCode emitInt2Long() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2L);
        codes.add(c);
        return c;
    }

    public OpCode emitInt2Float() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2F);
        codes.add(c);
        return c;
    }

    public OpCode emitInt2Double() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2D);
        codes.add(c);
        return c;
    }

    public OpCode emitLong2Int() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.L2I);
        codes.add(c);
        return c;
    }

    public OpCode emitLong2Float() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.L2F);
        codes.add(c);
        return c;
    }

    public OpCode emitLong2Double() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.L2D);
        codes.add(c);
        return c;
    }

    public OpCode emitFloat2Int() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.F2I);
        codes.add(c);
        return c;
    }

    public OpCode emitFloat2Long() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.F2L);
        codes.add(c);
        return c;
    }

    public OpCode emitFloat2Double() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.F2D);
        codes.add(c);
        return c;
    }

    public OpCode emitDouble2Int() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.D2I);
        codes.add(c);
        return c;
    }

    public OpCode emitDouble2Long() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.D2L);
        codes.add(c);
        return c;
    }

    public OpCode emitDouble2Float() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.D2F);
        codes.add(c);
        return c;
    }

    public OpCode emitInt2Byte() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2B);
        codes.add(c);
        return c;
    }

    public OpCode emitInt2Char() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2C);
        codes.add(c);
        return c;
    }

    public OpCode emitInt2Short() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.I2S);
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitAdd(ValueType t) {
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DADD);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FADD);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.IADD);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LADD);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitSub(ValueType t) { // subtract
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DSUB);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FSUB);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.ISUB);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LSUB);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitMul(ValueType t) { // multiply
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DMUL);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FMUL);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.IMUL);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LMUL);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitDiv(ValueType t) { // division
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DDIV);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FDIV);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.IDIV);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LDIV);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitRem(ValueType t) { // remainder
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DREM);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FREM);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.IREM);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LREM);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long, float, double
    public OpCode emitNeg(ValueType t) { // negative
        OpCode c;
        switch (t) {
            case DOUBLE:
                c = new UnaryOpCode(codes, OpCodeType.DNEG);
                break;
            case FLOAT:
                c = new UnaryOpCode(codes, OpCodeType.FNEG);
                break;
            case INT:
                c = new UnaryOpCode(codes, OpCodeType.INEG);
                break;
            case LONG:
                c = new UnaryOpCode(codes, OpCodeType.LNEG);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitShLeft(ValueType t) { // shift left
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.ISHL);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LSHL);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitShRight(ValueType t) { // shift right
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.ISHR);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LSHR);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitUShRight(ValueType t) { // unsigned(logical) shift right
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.IUSHR);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LUSHR);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitAnd(ValueType t) { // bitwise AND
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.IAND);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LAND);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitOr(ValueType t) { // bitwise OR
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.IOR);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LOR);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports int, long
    public OpCode emitXor(ValueType t) { // bitwise XOR
        OpCode c;
        if (t == ValueType.INT) {
            c = new UnaryOpCode(codes, OpCodeType.IXOR);
        } else if (t == ValueType.LONG) {
            c = new UnaryOpCode(codes, OpCodeType.LXOR);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // only work for int local variable
    public OpCode emitIncrement(int index, int value) {
        checkIndex(index);
        OpCode c = new IncreamentOpCode(codes, index, value);
        codes.add(c);
        return c;
    }

    // supports float, double
    public OpCode emitCompareLess(ValueType t) {
        OpCode c;
        if (t == ValueType.FLOAT) {
            c = new UnaryOpCode(codes, OpCodeType.FCMPL);
        } else if (t == ValueType.DOUBLE) {
            c = new UnaryOpCode(codes, OpCodeType.DCMPL);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    // supports float, double
    public OpCode emitCompareGreater(ValueType t) {
        OpCode c;
        if (t == ValueType.FLOAT) {
            c = new UnaryOpCode(codes, OpCodeType.FCMPG);
        } else if (t == ValueType.DOUBLE) {
            c = new UnaryOpCode(codes, OpCodeType.DCMPG);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + t);
        }
        codes.add(c);
        return c;
    }

    public OpCode emitIfEqualZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFEQ, jumpTo);
        codes.add(c);
        return c;
    }

    public OpCode emitIfNotEqualZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFNE, jumpTo);
        codes.add(c);
        return c;
    }

    public OpCode emitIfLessThanZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFLT, jumpTo);
        codes.add(c);
        return c;
    }

    public OpCode emitIfGreaterThanZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFGT, jumpTo);
        codes.add(c);
        return c;
    }

    public OpCode emitIfLessOrEqualZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFLE, jumpTo);
        codes.add(c);
        return c;
    }

    public OpCode emitIfGreaterOrEqualZero(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFGE, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPEQ, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfNotEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPNE, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfLessThan(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPLT, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfGreaterThan(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPGT, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfLessOrEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPLE, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on int
    public OpCode emitIfGreaterOrEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ICMPGE, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on reference
    public OpCode emitIfRefEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ACMPEQ, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on reference
    public OpCode emitIfRefNotEqual(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IF_ACMPNE, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on reference
    public OpCode emitIfNull(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFNULL, jumpTo);
        codes.add(c);
        return c;
    }

    // only works on reference
    public OpCode emitIfNotNull(Label jumpTo) {
        OpCode c = new LabelOpCode(codes, OpCodeType.IFNONNULL, jumpTo);
        codes.add(c);
        return c;
    }

    /**
     * if (comparator_type) {
     *     succeed
     * } else {
     *     failed
     * }
     * outer
     */
    public OpCode emitIf(OpCodeType type, Consumer<Code> succeed, Consumer<Code> failed, Consumer<Code> outer) {
        if ((type.getFlag() & OpCode.FLAG_IF) == 0) throw new UnsupportedOperationException("Unsupported type: " + type);
        LabelOpCode c = new LabelOpCode(codes, type, null);
        add(c);
        LabelOpCode go1 = new LabelOpCode(codes, OpCodeType.GOTO, null);
        LabelOpCode go2 = new LabelOpCode(codes, OpCodeType.GOTO, null);
        failed.accept(this);
        add(go1);
        succeed.accept(this);
        add(go2);
        outer.accept(this);
        c.setLabel(go1.next().visitLabel());
        go1.setLabel(go2.next().visitLabel());
        go2.setLabel(go2.next().visitLabel());
        return c;
    }

    public ConstOpCode emitCheckcast(ClassConst clazz) {
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.CHECKCAST, clazz);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitInstanceOf(ClassConst clazz) {
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.INSTANCEOF, clazz);
        codes.add(c);
        return c;
    }

    public OpCode emitPop() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.POP);
        codes.add(c);
        return c;
    }

    public OpCode emitPop2() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.POP2);
        codes.add(c);
        return c;
    }

    public OpCode emitDup() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP);
        codes.add(c);
        return c;
    }

    public OpCode emitDupX1() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP_X1);
        codes.add(c);
        return c;
    }

    public OpCode emitDupX2() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP_X2);
        codes.add(c);
        return c;
    }

    public OpCode emitDup2() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP2);
        codes.add(c);
        return c;
    }

    public OpCode emitDup2X1() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP2_X1);
        codes.add(c);
        return c;
    }

    public OpCode emitDup2X2() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.DUP2_X2);
        codes.add(c);
        return c;
    }

    public OpCode emitSwap() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.SWAP);
        codes.add(c);
        return c;
    }

    public OpCode emitGoto(Label label) {
        OpCode c = new LabelOpCode(codes, OpCodeType.GOTO, label);
        codes.add(c);
        return c;
    }

    // synchronize start
    public OpCode emitMonitorEnter() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.MONITORENTER);
        codes.add(c);
        return c;
    }

    // synchronize end
    public OpCode emitMonitorExit() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.MONITOREXIT);
        codes.add(c);
        return c;
    }

    public OpCode emitReturn(@Nullable ValueType t) {
        OpCode c = null;
        if (t != null) {
            switch (t) {
                case DOUBLE:
                    c = new UnaryOpCode(codes, OpCodeType.DRETURN);
                    break;
                case FLOAT:
                    c = new UnaryOpCode(codes, OpCodeType.FRETURN);
                    break;
                case BYTE:
                case CHAR:
                case SHORT:
                case BOOLEAN:
                case INT:
                    c = new UnaryOpCode(codes, OpCodeType.IRETURN);
                    break;
                case LONG:
                    c = new UnaryOpCode(codes, OpCodeType.LRETURN);
                    break;
            }
        }
        if (c == null) {
            c = new UnaryOpCode(codes, OpCodeType.ARETURN);
        }
        codes.add(c);
        return c;
    }

    public OpCode emitReturn() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.RETURN);
        codes.add(c);
        return c;
    }

    public TableSwitchOpCode emitTableSwitch() {
        TableSwitchOpCode c = new TableSwitchOpCode(codes);
        codes.add(c);
        return c;
    }

    public LookupSwitchOpCode emitLookupSwitch() {
        LookupSwitchOpCode c = new LookupSwitchOpCode(codes);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitGetStatic(RefConst field) {
        if (field.type() != ConstType.FIELDREF) throw new UnsupportedOperationException("need a field");
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.GETSTATIC, field);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitGetStatic(String clazz, String name, String descriptor) {
        RefConst field = constPool().acquireFieldRef(clazz, name, descriptor);
        return emitGetStatic(field);
    }

    public ConstOpCode emitPutStatic(RefConst field) {
        if (field.type() != ConstType.FIELDREF) throw new UnsupportedOperationException("need a field");
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.PUTFIELD, field);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitPutStatic(String clazz, String name, String descriptor) {
        RefConst field = constPool().acquireFieldRef(clazz, name, descriptor);
        return emitPutStatic(field);
    }

    public ConstOpCode emitGetField(RefConst field) {
        if (field.type() != ConstType.FIELDREF) throw new UnsupportedOperationException("need a field");
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.GETFIELD, field);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitGetField(String clazz, String name, String descriptor) {
        RefConst field = constPool().acquireFieldRef(clazz, name, descriptor);
        return emitGetField(field);
    }

    public ConstOpCode emitPutField(RefConst field) {
        if (field.type() != ConstType.FIELDREF) throw new UnsupportedOperationException("need a field");
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.PUTFIELD, field);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitPutField(String clazz, String name, String descriptor) {
        RefConst field = constPool().acquireFieldRef(clazz, name, descriptor);
        return emitPutField(field);
    }

    public InvocationOpCode emitInvokeVirtual(RefConst mth) {
        if (mth.type() != ConstType.METHODREF) throw new UnsupportedOperationException("need a method");
        InvocationOpCode c = new InvocationOpCode(codes, OpCodeType.INVOKEVIRTUAL, mth);
        codes.add(c);
        return c;
    }

    public InvocationOpCode emitInvokeVirtual(String clazz, String name, String... descriptors) {
        RefConst mth = constPool().acquireMethodRef(clazz, name, ClassUtil.getSignature(descriptors));
        return emitInvokeVirtual(mth);
    }

    public InvocationOpCode emitInvokeSpecial(RefConst mth) {
        if (mth.type() != ConstType.METHODREF
         && mth.type() != ConstType.INTERFACE_METHODREF)
            throw new UnsupportedOperationException("need a method or interface method");
        InvocationOpCode c = new InvocationOpCode(codes, OpCodeType.INVOKESPECIAL, mth);
        codes.add(c);
        return c;
    }

    public InvocationOpCode emitInvokeSpecial(String clazz, String name, String... descriptors) {
        RefConst mth = constPool().acquireMethodRef(clazz, name, ClassUtil.getSignature(descriptors));
        return emitInvokeSpecial(mth);
    }

    public InvocationOpCode emitInvokeStatic(RefConst mth) {
        if (mth.type() != ConstType.METHODREF) throw new UnsupportedOperationException("need a method");
        InvocationOpCode c = new InvocationOpCode(codes, OpCodeType.INVOKESTATIC, mth);
        codes.add(c);
        return c;
    }

    public InvocationOpCode emitInvokeStatic(String clazz, String name, String... descriptors) {
        RefConst mth = constPool().acquireMethodRef(clazz, name, ClassUtil.getSignature(descriptors));
        return emitInvokeStatic(mth);
    }

    public InvocationOpCode emitInvokeInterface(RefConst mth) {
        if (mth.type() != ConstType.INTERFACE_METHODREF)
            throw new UnsupportedOperationException("need a interface method");
        InvocationOpCode c = new InvocationOpCode(codes, OpCodeType.INVOKEINTERFACE, mth);
        codes.add(c);
        return c;
    }

    public InvocationOpCode emitInvokeInterface(String clazz, String name, String... descriptors) {
        RefConst mth = constPool().acquireInterfaceMethodRef(clazz, name, ClassUtil.getSignature(descriptors));
        return emitInvokeInterface(mth);
    }

    public InvokeDynamicOpCode emitInvokeDynamic(InvokeDynamicConst dyn) {
        InvokeDynamicOpCode c = new InvokeDynamicOpCode(codes, dyn);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitNew(ClassConst cls) {
        ConstOpCode c = new ConstOpCode(codes, OpCodeType.NEW, cls);
        codes.add(c);
        return c;
    }

    public ConstOpCode emitNew(String cls) {
        ClassConst clazz = constPool().acquireClass(cls);
        return emitNew(clazz);
    }

    // create a new array of primitive type
    public NewArrayOpCode emitNewArray(ValueType type) {
        NewArrayOpCode c = new NewArrayOpCode(codes, type);
        codes.add(c);
        return c;
    }

    public NewArrayOpCode emitNewArray(ClassConst type, int dimension) {
        NewArrayOpCode c = new NewArrayOpCode(codes, type, dimension);
        codes.add(c);
        return c;
    }

    public NewArrayOpCode emitNewArray(ClassConst type) {
        NewArrayOpCode c = new NewArrayOpCode(codes, type);
        codes.add(c);
        return c;
    }

    public NewArrayOpCode emitNewArray(String cls) {
        ClassConst clazz = constPool().acquireClass(cls);
        return emitNewArray(clazz);
    }

    public NewArrayOpCode emitNewArray(String cls, int dimension) {
        ClassConst clazz = constPool().acquireClass(cls);
        return emitNewArray(clazz, dimension);
    }

    public OpCode emitArrayLength() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.ARRAYLENGTH);
        codes.add(c);
        return c;
    }

    public OpCode emitThrow() {
        OpCode c = new UnaryOpCode(codes, OpCodeType.ATHROW);
        codes.add(c);
        return c;
    }


    private void checkIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index);
        }
    }


    public Label getLabel(String name) {
        return codes.getLabel(name);
    }

    DeclaredMethod method() {
        return (DeclaredMethod) container;
    }

    @Override
    protected void collect() {
        super.collect();
        codes.collect();
        collectConstants();
    }

    @Override
    public void writeData(ByteVector buffer) {
        buffer.putShort(maxStack());
        buffer.putShort(maxLocals());
        int pos = buffer.position();
        buffer.skip(4);
        codes.write(buffer);
        int len = buffer.position() - pos - 4;
        buffer.putInt(pos, len);
        buffer.putShort((short) tables.size());
        for (ExceptionTable tab : tables) {
            tab.write(buffer);
        }
        AttributeContainer.super.writeAttributes(buffer);
    }

    public LocalVariableTable variableTable() {
        return getOrAddAttribute("LocalVariableTable", () -> new LocalVariableTable(this));
    }

    public LineNumberTable lineNumberTable() {
        return getOrAddAttribute("LineNumberTable", () -> new LineNumberTable(this));
    }

    public StackMapTable stackMapTable() {
        return getOrAddAttribute("StackMapTable", () -> new StackMapTable(this));
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, DeclaredMethod.class);
    }

    @Override
    public String name() {
        return "Code";
    }

    @Override
    public String toString() {
        return "Code{max_stack=" + maxStack() + ",max_locals=" + maxLocals() + ",codes=" + codes +
                ",exception_table=" + tables + ",attributes=" + attributes + '}';
    }

    public final class ExceptionTable {

        private int startPc;
        private int endPc;
        private int handlerPc;
        // null means catch anything(keyword finally)
        private ClassConst catchType = null;

        ExceptionTable(int start, int end, int handler, ClassConst type) {
            this.startPc = start;
            this.endPc = end;
            this.handlerPc = handler;
            this.catchType = type;
        }

        ExceptionTable(ClassFileParser pool, ByteVector buffer) {
            this.startPc = buffer.getShort() & 0xFFFF;
            this.endPc = buffer.getShort() & 0xFFFF;
            this.handlerPc = buffer.getShort() & 0xFFFF;
            this.catchType = (ClassConst) pool.get(buffer.getShort() & 0xFFFF);
        }

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public int getEndPc() {
            return endPc;
        }

        public void setEndPc(int endPc) {
            this.endPc = endPc;
        }

        public int getHandlerPc() {
            return handlerPc;
        }

        public void setHandlerPc(int handlerPc) {
            this.handlerPc = handlerPc;
        }

        public ClassConst getCatchType() {
            return catchType;
        }

        public void setCatchType(ClassConst catchType) {
            this.catchType = catchType;
        }

        public int getIndex() {
            return tables.indexOf(this);
        }

        private void write(ByteVector buffer) {
            buffer.putShort((short) startPc);
            buffer.putShort((short) endPc);
            buffer.putShort((short) handlerPc);
            buffer.putShort(catchType == null ? 0 : catchType.index());
        }

        @Override
        public String toString() {
            return "ExceptionTable{start_pc=" + startPc +
                    ",end_pc=" + endPc +
                    ",handler_pc=" + handlerPc +
                    ", catch_type=" + (catchType == null ? "any" : catchType) + '}';
        }
    }
}
