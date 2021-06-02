package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;
import vip.creatio.gca.constant.ClassConst;
import vip.creatio.gca.constant.NameAndTypeConst;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

/**
 * A class must have an EnclosingMethod attribute if and only if it
 * is a local class or an anonymous class. A class may have no more
 * than one EnclosingMethod attribute.
 */
public class EnclosingMethod extends Attribute implements Descriptor {

    private ClassConst clazz;
    // If the current class is not immediately enclosed by a method
    // or constructor, then method must be null.
    @Nullable
    private NameAndTypeConst method;

    private EnclosingMethod(AttributeContainer container) {
        super(container);
    }

    public EnclosingMethod(ClassFile file) {
        this((AttributeContainer) file);
    }

    public EnclosingMethod(ClassFile classFile,
                           ClassConst clazz,
                           @Nullable NameAndTypeConst method) {
        this((AttributeContainer) classFile);
        this.clazz = clazz;
        this.method = method;
    }

    public static EnclosingMethod parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        EnclosingMethod inst = new EnclosingMethod(container);

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
    public @Nullable String getDescriptor() {
        return method == null ? null : method.getDescriptor();
    }

    @Override
    public void setDescriptor(String str) {
        if (method != null) method.setDescriptor(str);
    }

    @Override
    public @Nullable String[] getDescriptors() {
        return method == null ? null : method.getDescriptors();
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
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, ClassFile.class);
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
