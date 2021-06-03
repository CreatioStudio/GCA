package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.RefConst;

import vip.creatio.gca.util.ByteVector;

public class InvocationOpCode extends OpCode {

    private final OpCodeType type;
    private RefConst refConst;

    InvocationOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.type = type;
        this.refConst = (RefConst) pool.get(buffer.getShort());
        if (type == OpCodeType.INVOKEINTERFACE) buffer.getShort();  // jump 2 bytes
    }

    public InvocationOpCode(CodeContainer codes, OpCodeType type, RefConst constant) {
        super(codes);
        this.type = type;
        this.refConst = constant;
    }

    public RefConst getRef() {
        return refConst;
    }

    public void setRef(RefConst refConst) {
        this.refConst = refConst;
    }

    @Override
    public OpCodeType type() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + ' ' + refConst;
    }

    @Override
    void collect() {
        constPool().acquire(refConst);
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        buffer.putShort(refConst.index());
        if (type == OpCodeType.INVOKEINTERFACE) {
            buffer.putByte((byte) (refConst.getParameterCount() + 1));
            buffer.putByte((byte) 0x00);
        }
    }
}
