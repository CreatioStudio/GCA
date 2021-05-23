package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.Attribute;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.DeclaredSignature;
import vip.creatio.gca.constant.ClassConst;
import vip.creatio.gca.constant.NameAndTypeConst;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

/**
 * A class must have an EnclosingMethod attribute if and only if it
 * is a local class or an anonymous class. A class may have no more
 * than one EnclosingMethod attribute.
 */
public class EnclosingMethod extends Attribute implements DeclaredSignature {

    private ClassConst clazz;
    // If the current class is not immediately enclosed by a method
    // or constructor, then method must be null.
    @Nullable
    private NameAndTypeConst method;

    public EnclosingMethod(ClassFile file) {
        super(file);
    }

    public EnclosingMethod(ClassFile classFile,
                           ClassConst clazz,
                           @Nullable NameAndTypeConst method) {
        this(classFile);
        this.clazz = clazz;
        this.method = method;
    }

    public static EnclosingMethod parse(ClassFile file, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        EnclosingMethod inst = new EnclosingMethod(file);
        inst.clazz = (ClassConst) pool.get(buffer.getShort());
        inst.method = (NameAndTypeConst) pool.get(buffer.getShort());
        return inst;
    }

    public ClassConst getClazz() {
        return clazz;
    }

    public void setClazz(ClassConst clazz) {
        this.clazz = clazz;
    }

    public @Nullable String getMethodName() {
        return method == null ? null : method.getName();
    }

    public @Nullable String getMethodDescriptor() {
        return method == null ? null : method.getDescriptor();
    }

    @Override
    public @Nullable String[] getSignatures() {
        return method == null ? null : method.getSignatures();
    }

    @Override
    public void recache() {
        if (method != null) method.recache();
    }

    public void setMethod(@Nullable String name, String descriptor) {
        this.method = new NameAndTypeConst(constPool(), name, descriptor);
    }

    public void setMethod(@Nullable String name, String... signatures) {
        setMethod(name, ClassUtil.getSignature(signatures));
    }

    @Override
    public String name() {
        return "EnclosingMethod";
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(clazz.index());
        buffer.putShort(method == null ? 0 : method.index());
    }

    @Override
    protected void collect() {
        if (method != null) constPool().acquire(method);
    }

    @Override
    public String toString() {
        return "EnclosingMethod{class=" + clazz.toString() + ",method=" + (method == null ? "null" : method) + '}';
    }

    @Override
    public boolean isEmpty() {
        return clazz == null;
    }
}
