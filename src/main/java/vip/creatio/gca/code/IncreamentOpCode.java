package vip.creatio.gca.code;

import vip.creatio.gca.util.ByteVector;

//iinc
public class IncreamentOpCode extends OpCode {

    private int index;
    private int value;

    public IncreamentOpCode(CodeContainer codes, int index, int value) {
        super(codes);
        this.index = index;
        this.value = value;
    }

    IncreamentOpCode(CodeContainer codes, ByteVector buffer, boolean wide) {
        super(codes);
        if (wide) {
            this.index = buffer.getShort() & 0xFFFF;
            this.value = buffer.getShort();
        } else {
            this.index = buffer.getByte() & 0xFF;
            this.value = buffer.getByte();
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void serialize(ByteVector buffer) {
        if (index > 0xFF || value > 0xFF || value < -0xFF) {
            buffer.putByte(OpCodeType.WIDE.getTag());
            super.serialize(buffer);

            buffer.putShort((short) index);
            buffer.putShort((short) value);
        } else {
            super.serialize(buffer);

            buffer.putByte((byte) index);
            buffer.putByte((byte) value);
        }
    }

    @Override
    public int byteSize() {
        if (index > 0xFF || value > 0xFF || value < -0xFF) {
            return 6;
        } else {
            return super.byteSize();
        }
    }

    @Override
    public OpCodeType type() {
        return OpCodeType.IINC;
    }

    @Override
    public String toString() {
        return "iinc " + index + ", " + value;
    }
}
