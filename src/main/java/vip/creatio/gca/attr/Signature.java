package vip.creatio.gca.attr;

import vip.creatio.gca.*;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.common.ByteVector;

/**
 * The Signature attribute records generic signature information for
 * any class, interface, constructor or member whose generic signature
 * in the Java programming language would include references to type
 * variables or parameterized types.
 */
public class Signature extends Attribute {

    private String signature;

    private Type[] cachedType;

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

    public String getGenericType() {
        return signature;
    }

    public void setGenericType(String signature) {
        this.signature = signature;
    }

    public void setCachedGenericType(Type[] signature) {
        this.cachedType = signature;
        this.signature = null;
    }

    public Type[] getCachedGenericType() {
        return cachedType;
    }

    private void ensureSignatureNotNull() {
        if (signature == null) {
            if (container instanceof DeclaredMethod) {
                signature = Types.toMethodSignature(cachedType);
            } else {
                signature = cachedType[0].getInternalSignature();
            }

        }
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        ensureSignatureNotNull();
        pool.acquireUtf(signature);
    }

    @Override
    protected void writeData(ConstPool pool, ByteVector buffer) {
        ensureSignatureNotNull();
        buffer.putShort(pool.indexOf(signature));
    }

    @Override
    public String toString() {
        return "Signature{signature=" + signature + '}';
    }

    @Override
    public boolean isEmpty() {
        return signature == null && cachedType == null;
    }
}
