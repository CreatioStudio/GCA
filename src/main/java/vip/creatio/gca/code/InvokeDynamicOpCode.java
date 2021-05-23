package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.constant.InvokeDynamicConst;

import vip.creatio.gca.util.ByteVector;

public class InvokeDynamicOpCode extends OpCode {

    private InvokeDynamicConst ref;

    InvokeDynamicOpCode(CodeContainer codes, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.ref = (InvokeDynamicConst) pool.get(buffer.getShort());
        buffer.getShort();      // skip 2 bytes
    }

    InvokeDynamicOpCode(CodeContainer codes, InvokeDynamicConst ref) {
        super(codes);
        this.ref = ref;
    }

    public InvokeDynamicConst getRef() {
        return ref;
    }

    public void setRef(InvokeDynamicConst ref) {
        this.ref = ref;
    }

    @Override
    public OpCodeType type() {
        return OpCodeType.INVOKEDYNAMIC;
    }

    @Override
    public String toString() {
        return "invokedynamic " + ref;
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        buffer.putShort(ref.index());
        buffer.putShort((short) 0x0000);
    }
}
