package vip.creatio.gca.code;

import vip.creatio.gca.util.ByteVector;

// opcode that uses Label as coordinator, this always read short index
public class LabelOpCode extends OpCode {

    private final OpCodeType type;
    private Label label;
    private int offset;

    LabelOpCode(CodeContainer codes, OpCodeType type, Label label) {
        super(codes);
        this.type = type;
        this.label = label;
    }

    LabelOpCode(CodeContainer codes, OpCodeType type, ByteVector buffer) {
        super(codes);
        this.type = type;
        if (type == OpCodeType.GOTO_W || type == OpCodeType.JSR_W) {
            this.offset = buffer.getInt();
        } else {
            // signed short, might be a negative number
            this.offset = buffer.getShort();
        }
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public OpCodeType type() {
        return type;
    }

    @Override
    void parse() {
        this.label = codes.addLabel(codes.fromOffset(offset() + offset));
    }

    @Override
    public String toString() {
        return super.toString() + ' ' +  + label.getAnchor().offset();
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        if (type == OpCodeType.GOTO_W || type == OpCodeType.JSR_W) {
            buffer.putInt(label.getOffset() - offset());
        } else {
            buffer.putShort((short) (label.getOffset() - offset()));
        }
    }
}
