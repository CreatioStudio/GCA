package vip.creatio.gca.code;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.constant.Const;

import vip.creatio.gca.util.ByteVector;

// ldc, ldc_w and ldc2_w
public class LoadConstOpCode extends OpCode {

    private Const constant;

    LoadConstOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        short index;
        if (type == OpCodeType.LDC) {
            index = (short) (buffer.getByte() & 0xFF);
        } else if (type == OpCodeType.LDC_W || type == OpCodeType.LDC2_W) {
            index = (short) (buffer.getShort() & 0xFFFF);
        } else throw new UnsupportedOperationException("Invalid type: " + type);
        constant = pool.get(index);
    }

    LoadConstOpCode(CodeContainer codes, Const constant) {
        super(codes);
        this.constant = constant;
    }

    @Override
    public OpCodeType type() {
        if (constant instanceof Const.DualSlot) {
            return OpCodeType.LDC2_W;
        } else {
            // might be LDC_W, depends on constant's index size
            return OpCodeType.LDC;
        }
    }

    public @NotNull Const getConstant() {
        return constant;
    }

    public void setConstant(@NotNull Const constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return super.toString() + ' ' + constant;
    }

    @Override
    void collect() {
        constPool().acquire(constant);
    }

    @Override
    public int byteSize() {
        if (constant instanceof Const.DualSlot) {
            return OpCodeType.LDC2_W.byteSize();
        } else {
            int index = constant.index();
            if (index > 255) {
                return OpCodeType.LDC_W.byteSize();
            }
            return OpCodeType.LDC.byteSize();
        }
    }

    @Override
    public void serialize(ByteVector buffer) {
        int index = constant.index();
        if (constant instanceof Const.DualSlot) {
            buffer.putByte(OpCodeType.LDC2_W.getTag());
            buffer.putShort((short) index);
        } else {
            if (index > 255) {
                buffer.putByte(OpCodeType.LDC_W.getTag());
                buffer.putShort((short) index);
            } else {
                buffer.putByte(OpCodeType.LDC.getTag());
                buffer.putByte((byte) index);
            }
        }
    }
}
