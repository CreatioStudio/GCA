package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.InvokeDynamicConst;

import vip.creatio.gca.util.common.ByteVector;

public class InvokeDynamicOpCode extends OpCode {

    private InvokeDynamicConst ref;

    InvokeDynamicOpCode(CodeContainer codes, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.ref = (InvokeDynamicConst) pool.get(buffer.getShort());
        buffer.getShort();      // skip 2 bytes
    }

    public InvokeDynamicOpCode(CodeContainer codes, InvokeDynamicConst ref) {
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
    void write(ConstPool pool, ByteVector buffer) {
        super.write(pool, buffer);
        buffer.putShort(pool.indexOf(ref));
        buffer.putShort((short) 0x0000);
    }
}
