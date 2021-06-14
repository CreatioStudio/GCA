package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.Const;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.common.ByteVector;

// this will always read a short index
public class ConstOpCode extends OpCode {

    private final OpCodeType type;
    private Const constant;

    ConstOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.type = type;
        int index = buffer.getUShort();
        this.constant = pool.get(index);
    }

    public ConstOpCode(CodeContainer codes, OpCodeType type, Const constant) {
        super(codes);
        this.type = type;
        this.constant = constant;
    }

    public Const getConstant() {
        return constant;
    }

    public void setConstant(Const constant) {
        this.constant = constant;
    }

    @Override
    public OpCodeType type() {
        return type;
    }

    @Override
    void collect(ConstPool pool) {
        pool.acquire(constant);
    }

    @Override
    void write(ConstPool pool, ByteVector buffer) {
        super.write(pool, buffer);
        buffer.putShort(pool.indexOf(constant));
    }

    @Override
    public String toString() {
        return super.toString() + " " + constant;
    }
}
