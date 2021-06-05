package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

/**
 * The Signature attribute records generic signature information for
 * any class, interface, constructor or member whose generic signature
 * in the Java programming language would include references to type
 * variables or parameterized types.
 */
public class Signature extends Attribute {

    private Type signature;

    public Signature(AttributeContainer container) {
        super(container);
    }

    public Signature(AttributeContainer container, String signature) {
        this(container);
        this.signature = Types.resolve(signature);
    }

    public static Signature parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        Signature inst = new Signature(container);
        inst.signature = Types.resolve(pool.getString(buffer.getUShort()));
        return inst;
    }

    @Override
    public String name() {
        return "Signature";
    }

    public Type getGenericType() {
        return signature;
    }

    public void setGenericType(String signature) {
        this.signature = Types.resolve(signature);
    }

    public void setGenericType(Type signature) {
        this.signature = signature;
    }

    @Override
    protected void collect() {
        super.collect();
        constPool().acquireUtf(signature.getInternalSignature());
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(constPool().acquireUtf(signature.getInternalSignature()).index());
    }

    @Override
    public String toString() {
        return "Signature{signature=" + signature + '}';
    }

    @Override
    public boolean isEmpty() {
        return signature == null;
    }
}
