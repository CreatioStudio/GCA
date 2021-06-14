package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;
import vip.creatio.gca.NameAndTypeConst;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.type.MethodInfo;

/**
 * A class must have an EnclosingMethod attribute if and only if it
 * is a local class or an anonymous class. A class may have no more
 * than one EnclosingMethod attribute.
 */
public class EnclosingMethod extends Attribute {

    private TypeInfo clazz;
    // If the current class is not immediately enclosed by a method
    // or constructor, then method must be null.
    @Nullable
    private MethodInfo method;

    private EnclosingMethod(AttributeContainer container) {
        super(container);
    }

    public EnclosingMethod(ClassFile file) {
        this((AttributeContainer) file);
    }

    public EnclosingMethod(ClassFile classFile,
                           TypeInfo clazz,
                           @Nullable MethodInfo method) {
        this((AttributeContainer) classFile);
        this.clazz = clazz;
        this.method = method;
    }

    public static EnclosingMethod parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        EnclosingMethod inst = new EnclosingMethod(container);

        inst.clazz = (TypeInfo) pool.get(buffer.getUShort());
        NameAndTypeConst pair = (NameAndTypeConst) pool.get(buffer.getUShort());
        Repository repo = ((ClassFile) container).repository();
        inst.method = repo.toMethod(inst.clazz, pair.getName(), repo.resolveMethodDescriptor(pair.getDescriptor()));
        return inst;
    }

    public TypeInfo getClazz() {
        return clazz;
    }

    public void setClazz(TypeInfo clazz) {
        this.clazz = clazz;
    }

    public @Nullable MethodInfo getMethod() {
        return method;
    }

    public void setMethod(@Nullable MethodInfo mth) {
        this.method = mth;
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
    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(clazz));
        buffer.putShort(method == null ? 0 : pool.indexOf(method.getName(), method.getDescriptor()));
    }

    @Override
    protected void collect(ConstPool pool) {
        if (method != null) pool.acquireNameAndType(method.getName(), method.getDescriptor());
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
