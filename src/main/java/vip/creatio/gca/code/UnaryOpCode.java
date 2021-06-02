package vip.creatio.gca.code;

public class UnaryOpCode extends OpCode {

    private final OpCodeType type;

    public UnaryOpCode(CodeContainer codes, OpCodeType type) {
        super(codes);
        this.type = type;
    }

    @Override
    public OpCodeType type() {
        return type;
    }
}
