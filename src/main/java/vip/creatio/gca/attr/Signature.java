package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

/**
 * The Signature attribute records generic signature information for
 * any class, interface, constructor or member whose generic signature
 * in the Java programming language would include references to type
 * variables or parameterized types.
 */
//TODO: make signatures into something like GenericType[]
public class Signature extends Attribute implements GenericSignature {

    private String signature;

    private @Nullable String[] signatures;

    public Signature(AttributeContainer container) {
        super(container);
    }

    public Signature(AttributeContainer container, String signature) {
        this(container);
        this.signature = signature;
    }

    public static Signature parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        Signature inst = new Signature(container);
        inst.signature = pool.getString(buffer.getUShort());
        return inst;
    }

    @Override
    public String name() {
        return "Signature";
    }

    public String getGenericSignature() {
        return signature;
    }

    public void setGenericSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public @Nullable String[] getGenericSignatures() {
        return signatures;
    }

    @Override
    public void recache() {
        if (signature != null) {
            signatures = ClassUtil.fromSignature(signature);
        }
    }

    @Override
    protected void collect() {
        super.collect();
        constPool().acquireUtf(signature);
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(constPool().acquireUtf(signature).index());
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
