package vip.creatio.gca.code;

import vip.creatio.gca.util.ByteVector;

// operator with a const number value follow
public class NumberOpCode extends OpCode {

    private final OpCodeType type;
    private final int data;

    NumberOpCode(CodeContainer codes, OpCodeType type, int data) {
        super(codes);
        this.type = type;
        this.data = data;
    }

    NumberOpCode(CodeContainer codes, OpCodeType type, ByteVector buf, boolean wide) {
        super(codes);
        this.type = type;
        int size = type.byteSize();
        if (type == OpCodeType.BIPUSH)
            this.data = buf.getByte();
        else if (type == OpCodeType.SIPUSH)
            this.data = buf.getShort();
        else if (size == 2) {
            if (wide) {
                this.data = buf.getShort() & 0xFFFF;
            } else {
                this.data = buf.getByte() & 0xFF;
            }
        } else
            this.data = buf.getInt();
    }

    @Override
    public OpCodeType type() {
        return type;
    }

    @Override
    public void serialize(ByteVector buffer) {
        int size = type.byteSize();
        if (size == 2) {
            if (type != OpCodeType.BIPUSH && (data > 255 || data < -256)) {
                buffer.putByte(OpCodeType.WIDE.getTag());
                super.serialize(buffer);
                buffer.putShort((short) data);
            } else {
                super.serialize(buffer);
                buffer.putByte((byte) data);
            }
        } else if (size == 3) {
            super.serialize(buffer);
            buffer.putShort((short) data);
        } else {
            super.serialize(buffer);
            buffer.putInt(data);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ' ' + data;
    }
}
