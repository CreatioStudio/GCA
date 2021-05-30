package vip.creatio.gca.attr;

import vip.creatio.gca.Attribute;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

/**
 * The Signature attribute records generic signature information for
 * any class, interface, constructor or member whose generic signature
 * in the Java programming language would include references to type
 * variables or parameterized types.
 */
public class Signature extends Attribute {

    private String signature;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
