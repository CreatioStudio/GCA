package vip.creatio.gca.constant;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Cacheable;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.Immutable;

@Immutable
public class UTFConst extends Const implements Cacheable {

    private String cache;
    private final byte[] data;

    UTFConst(ConstPool pool, byte[] data) {
        super(pool, ConstType.UTF8);
        this.data = data;
        recache();
    }

    public UTFConst(ConstPool pool, final ByteVector buffer) {
        super(pool, ConstType.UTF8);
        int size = buffer.getUShort();
        data = new byte[size];
        buffer. getBytes(data, 0, size);
        recache();
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Const copy() {
        return new UTFConst(pool, data);
    }

    @Override
    public int byteSize() {
        return data.length + 3;
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putShort((short) length());
        buffer.putBytes(data);
    }

    public int length() {
        return data.length;
    }

    public String string() {
        if (cache == null)  {
            cache = new String(data);
        }
        return cache;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public void recache() {
        this.cache = new String(data);
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
            return ((UTFConst) obj).pool == pool && string().equals(((UTFConst) obj).string());
        }
        return false;
    }
}
