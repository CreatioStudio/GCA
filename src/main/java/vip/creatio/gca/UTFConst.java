package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;

public class UTFConst implements Const {

    private final byte[] data;

    UTFConst(byte[] data) {
        this.data = data;
    }

    UTFConst(String s) {
        this.data = s.getBytes();
    }

    UTFConst(final ByteVector buffer) {
        int size = buffer.getUShort();
        data = new byte[size];
        buffer. getBytes(data, 0, size);
    }

    @Override
    public ConstType constantType() {
        return ConstType.UTF8;
    }

    void write(ByteVector buffer) {
        buffer.putShort(length());
        buffer.putBytes(data);
    }

    public int length() {
        return data.length;
    }

    public String string() {
        return new String(data);
    }

    public byte[] data() {
        return data;
    }

    @Override
    public String toString() {
        return string();
    }

    @Override
    public int hashCode() {
        return string().hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UTFConst) {
            return string().equals(((UTFConst) obj).string());
        }
        return false;
    }
}
