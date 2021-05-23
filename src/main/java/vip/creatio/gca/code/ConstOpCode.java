package vip.creatio.gca.code;

import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;

// this will always read a short index
public class ConstOpCode extends OpCode {

    private final OpCodeType type;
    private Const constant;

    ConstOpCode(CodeContainer codes, OpCodeType type, ClassFileParser pool, ByteVector buffer) {
        super(codes);
        this.type = type;
        this.constant = pool.get(buffer.getShort());
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
    void collect() {
        constPool().acquire(constant);
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        buffer.putShort(constant.index());
    }

    @Override
    public String toString() {
        return super.toString() + " " + constant;
    }
}
