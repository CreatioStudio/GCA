package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.type.MethodInfo;

public class InvocationOpCode extends OpCode {

    private final OpCodeType type;
    private MethodInfo refConst;

    InvocationOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.type = type;
        this.refConst = (MethodInfo) pool.get(buffer.getShort());
        if (type == OpCodeType.INVOKEINTERFACE) buffer.getShort();  // jump 2 bytes
    }

    public InvocationOpCode(CodeContainer codes, OpCodeType type, MethodInfo constant) {
        super(codes);
        this.type = type;
        this.refConst = constant;
    }

    public MethodInfo getRef() {
        return refConst;
    }

    public void setRef(MethodInfo refConst) {
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
    void collect(ConstPool pool) {
        pool.acquire(refConst);
    }

    @Override
    void write(ConstPool pool, ByteVector buffer) {
        super.write(pool, buffer);
        buffer.putShort(pool.indexOf(refConst));
        if (type == OpCodeType.INVOKEINTERFACE) {
            buffer.putByte((byte) (refConst.getParameterCount() + 1));
            buffer.putByte((byte) 0x00);
        }
    }
}
