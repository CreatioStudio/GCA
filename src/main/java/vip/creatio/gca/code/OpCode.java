package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.Serializer;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.util.BiHashMap;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import static vip.creatio.gca.code.OpCodeType.*;

public abstract class OpCode implements Serializer {

    public static final int FLAG_JUMP =             0b0000_0000_0000_0001;
    public static final int FLAG_INVOKE =           0b0000_0000_0000_0010;
    public static final int FLAG_LOAD =             0b0000_0000_0000_0100;
    public static final int FLAG_STORE =            0b0000_0000_0000_1000;
    public static final int FLAG_MATH =             0b0000_0000_0001_0000;
    public static final int FLAG_IF =               0b0000_0000_0010_0000;

    final CodeContainer codes;

    OpCode(CodeContainer codes) {
        this.codes = codes;
    }

    public abstract OpCodeType type();

    @Override
    public int byteSize() {
        return type().byteSize();
    }

    public final int offset() {
        return codes.offsetOf(this);
    }

    public final int index() {
        return codes.indexOf(this);
    }

    @Override
    public void serialize(ByteVector buffer) {
        buffer.putByte(type().getTag());
    }

    @Override
    public String toString() {
        return type().name().toLowerCase();
    }

    public OpCode next() {
        return codes.next(this);
    }

    public OpCode prev() {
        return codes.prev(this);
    }

    public boolean hasNext() {
        return codes.hasNext(this);
    }

    public boolean hasPrev() {
        return codes.hasPrev(this);
    }

    // get or create label that attached on this opcode
    public Label visitLabel() {
        return codes.visitLabel(this);
    }


    // internals

    final CodeContainer getCodes() {
        return codes;
    }

    // called when all opcodes of a method is parsed
    void parse() {}

    void collect() {}

    final ConstPool constPool() {
        return codes.constPool();
    }

    static OpCode parse(ClassFileParser pool,
                        CodeContainer codes,
                        BiHashMap<Integer, OpCode> offsetMap,
                        int startingOffset,
                        ByteVector buffer) {
        int offset = buffer.position() - startingOffset;
        int op = buffer.getByte() & 0xFF;
        OpCode code;
        try{
            switch (op) {
                case 0x10://bipush
                case 0x15://iload
                case 0x16://lload
                case 0x17://fload
                case 0x18://dload
                case 0x19://aload
                case 0x36://istore
                case 0x37://lstore
                case 0x38://fstore
                case 0x39://dstore
                case 0x3A://astore
                case 0xA9://ret     //TODO: wtf this is
                    code = new NumberOpCode(codes, fromCode(op), buffer, false);
                    break;
                case 0x11://sipush
                    code = new NumberOpCode(codes, SIPUSH, buffer, false);
                    break;
                case 0x12://ldc
                    code = new LoadConstOpCode(codes, LDC, pool, buffer);
                    break;
                case 0x13://ldc_w
                    code = new LoadConstOpCode(codes, LDC_W, pool, buffer);
                    break;
                case 0x14://ldc2_w
                    code = new LoadConstOpCode(codes, LDC2_W, pool, buffer);
                    break;
                case 0x84://iinc
                    code = new IncreamentOpCode(codes, buffer, false);
                    break;
                // process control instruction
                case 0x99://ifeq
                case 0x9A://ifne
                case 0x9B://iflt
                case 0x9C://ifge
                case 0x9D://ifgt
                case 0x9E://ifle
                case 0x9f://if_icmpeq
                case 0xA0://if_icmpne
                case 0xA1://if_icmplt
                case 0xA2://if_icmpge
                case 0xA3://if_icmpgt
                case 0xA4://if_icmple
                case 0xA5://if_acmpeq
                case 0xA6://if_acmpne
                case 0xA7://goto
                case 0xA8://jsr
                case 0xC6://ifnull
                case 0xC7://ifnonnull
                    // 4 byte long address
                case 0xC8://goto_w
                case 0xC9://jsr_w
                    code = new LabelOpCode(codes, fromCode(op), buffer);
                    break;
                // constant
                case 0xC0://checkcast
                case 0xC1://instanceof
                case 0xB2://getstatic
                case 0xB3://putstatic
                case 0xB4://getfield
                case 0xB5://putfield
                case 0xBB://new
                    code = new ConstOpCode(codes, fromCode(op), pool, buffer);
                    break;
                case 0xBC://newarray
                    code = new NewArrayOpCode(codes, NEWARRAY, pool, buffer);
                    break;
                case 0xBD://anewarray
                    code = new NewArrayOpCode(codes, ANEWARRAY, pool, buffer);
                    break;
                case 0xC5://multianewarray
                    code = new NewArrayOpCode(codes, MULTIANEWARRAY, pool, buffer);
                    break;
                case 0xB6://invokevirtual
                case 0xB7://invokespecial
                case 0xB8://invokestatic
                case 0xB9://invokeinterface
                    code = new InvocationOpCode(codes, fromCode(op), pool, buffer);
                    break;
                case 0xBA://invokedynamic
                    code = new InvokeDynamicOpCode(codes, pool, buffer);
                    break;
                case 0xAA://tableswitch
                    code = new TableSwitchOpCode(codes, startingOffset, buffer);
                    break;
                case 0xAB://lookupswitch
                    code = new LookupSwitchOpCode(codes, startingOffset, buffer);
                    break;
                case 0xC4://wide
                    byte b = buffer.getByte();
                    switch (b) {
                        case 0x15://iload
                        case 0x16://lload
                        case 0x17://float
                        case 0x18://dload
                        case 0x19://aload
                        case 0x36://istore
                        case 0x37://lstore
                        case 0x38://fstore
                        case 0x39://dstore
                        case 0x3A://astore
                        case (byte) 0xA9://ret
                            code = new NumberOpCode(codes, fromCode(b), buffer, true);
                            break;
                        case (byte) 0x84://iinc
                            code = new IncreamentOpCode(codes, buffer, true);
                            break;
                        default:
                            throw new BytecodeException(codes, offset, "Unsupported type after a WIDE opcode: 0x" + Util.toHex(b));
                    }
                    break;
                default:
                    OpCodeType type = fromCode(op);
                    if (type == null) {
                        throw new BytecodeException(codes, offset, "Unknown opcode type: 0x" + Util.toHex(op));
                    }
                    code = new UnaryOpCode(codes, type);
                    break;
                //Unary
            }
        } catch (Exception e) {
            throw new BytecodeException(codes, offset, e, "Exception while parsing opcode " + fromCode(op));
        }
        offsetMap.put(offset, code);
        return code; //TODO
    }

}
