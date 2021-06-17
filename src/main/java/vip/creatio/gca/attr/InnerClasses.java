package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;

import vip.creatio.gca.util.common.ByteVector;
import java.util.Arrays;
import java.util.EnumSet;

import static vip.creatio.gca.util.AccessFlags.*;
import static vip.creatio.gca.util.AccessFlags.MODULE;

/**
 * If the constant pool of a class or interface C contains a CONSTANT_Class_info
 * entry which represents a class or interface that is not a member of a package,
 * then C's ClassFile structure must have exactly one InnerClasses attribute in
 * its attributes table.
 */
public class InnerClasses extends TableAttribute<InnerClasses.Class> {

    private InnerClasses(AttributeContainer c) {
        super(c);
    }

    public InnerClasses(ClassFile classFile) {
        this((AttributeContainer) classFile);
    }

    public static InnerClasses parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        InnerClasses inst = new InnerClasses(container);

        int num = buffer.getUShort();
        for (int i = 0; i < num; i++) {
            inst.items.add(new Class(pool, buffer) /* an ridiculous way to construct object... */ );
        }
        return inst;
    }

    public void add(TypeInfo clazz, String innerName, int flags) {
        // anti duplication
        for (Class i : items) {
            if (i.inner.equals(clazz)) return;
        }
        Class i = new Class(clazz, (ClassFile) container, innerName, flags);
        items.add(i);
    }

    public void add(ClassFile classFile) {
        String name = classFile.getName();
        name = name.substring(name.lastIndexOf('/') + 1);
        add(classFile, name, classFile.getAccessFlags());
    }

    public void remove(TypeInfo clazz) {
        items.removeIf(i -> i.inner.equals(clazz));
    }

    public void remove(String innerName) {
        items.removeIf(i -> i.innerName.equals(innerName));
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, ClassFile.class);
    }

    @Override
    public String name() {
        return "InnerClasses";
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        for (Class item : items) {
            pool.acquire(item.getInner());
            pool.acquire(item.getOuter());
            pool.acquireUtf(item.getInnerName());
        }
    }

    @Override
    public void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Class i : items) {
            i.write(pool, buffer);
        }
    }

    public static final class Class {

        private TypeInfo inner;
        private TypeInfo outer;
        private String innerName;
        private int innerAccessFlags;

        Class(TypeInfo inner,
              TypeInfo outer,
              String innerName,
              int innerAccessFlags) {
            this.inner = inner;
            this.outer = outer;
            this.innerName = innerName;
            this.innerAccessFlags = innerAccessFlags;
        }

        Class(ClassFileParser pool, ByteVector buffer) {
            this.inner = (TypeInfo) pool.get(buffer.getUShort());
            this.outer = (TypeInfo) pool.get(buffer.getUShort());
            this.innerName = pool.getString(buffer.getUShort());
            this.innerAccessFlags = buffer.getUShort();
        }

        public TypeInfo getInner() {
            return inner;
        }

        public void setInner(TypeInfo inner) {
            this.inner = inner;
        }

        public TypeInfo getOuter() {
            return outer;
        }

        public void setOuter(TypeInfo outer) {
            this.outer = outer;
        }

        public String getInnerName() {
            return innerName;
        }

        public void setInnerName(@Nullable String innerName) {
            this.innerName = innerName;
        }

        public int getInnerAccessFlags() {
            return innerAccessFlags;
        }

        public void setInnerAccessFlags(int flags) {
            this.innerAccessFlags = flags;
        }

        /* access flags */

        public boolean hasAllAccessFlags(int flag) {
            return (innerAccessFlags & flag) == flag;
        }

        public boolean hasAnyAccessFlags(int flag) {
            return (innerAccessFlags & flag) != 0;
        }

        public boolean isPublic() {
            return (innerAccessFlags & PUBLIC) != 0;
        }

        public boolean isPrivate() {
            return (innerAccessFlags & PRIVATE) != 0;
        }

        public boolean isProtected() {
            return (innerAccessFlags & PROTECTED) != 0;
        }

        public boolean isStatic() {
            return (innerAccessFlags & STATIC) != 0;
        }

        public boolean isFinal() {
            return (innerAccessFlags & FINAL) != 0;
        }

        public boolean isInterface() {
            return (innerAccessFlags & INTERFACE) != 0;
        }

        public boolean isAbstract() {
            return (innerAccessFlags & ABSTRACT) != 0;
        }

        public boolean isSynthetic() {
            return (innerAccessFlags & SYNTHETIC) != 0;
        }

        public boolean isAnnotation() {
            return (innerAccessFlags & ANNOTATION) != 0;
        }

        public boolean isEnum() {
            return (innerAccessFlags & ENUM) != 0;
        }

        private void write(ConstPool pool, ByteVector buffer) {
            buffer.putShort(pool.indexOf(inner));
            buffer.putShort(pool.indexOf(outer));
            buffer.putShort(innerName == null ? 0 : pool.indexOf(innerName));
            buffer.putShort(innerAccessFlags);
        }

        @Override
        public String toString() {
            return "{inner=" + inner + ",outer=" + outer + ",inner_name=" + innerName + ",access_flags=" + innerAccessFlags + '}';
        }
    }
}
