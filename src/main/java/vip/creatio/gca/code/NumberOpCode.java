package vip.creatio.gca.code;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.common.ByteVector;

// operator with a const number value follow
public class NumberOpCode extends OpCode {

    private final OpCodeType type;
    private final int data;

    public NumberOpCode(CodeContainer codes, OpCodeType type, int data) {
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
    public void write(ConstPool pool, ByteVector buffer) {
        int size = type.byteSize();
        if (size == 2) {
            if (type != OpCodeType.BIPUSH && (data > 255 || data < -256)) {
                buffer.putByte(OpCodeType.WIDE.getTag());
                super.write(pool, buffer);
                buffer.putShort((short) data);
            } else {
                super.write(pool, buffer);
                buffer.putByte((byte) data);
            }
        } else if (size == 3) {
            super.write(pool, buffer);
            buffer.putShort((short) data);
        } else {
            super.write(pool, buffer);
            buffer.putInt(data);
        }
    }

    public int data() {
        return data;
    }

    @Override
    public int byteSize() {
        if (type != OpCodeType.BIPUSH && (data > 255 || data < -256)) {
            return 4;
        } else {
            return super.byteSize();
        }
    }

    @Override
    public String toString() {
        return super.toString() + ' ' + data;
    }
}
