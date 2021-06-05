package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;
import vip.creatio.gca.ClassConst;

import vip.creatio.gca.util.ByteVector;
import java.util.Arrays;
import java.util.EnumSet;

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
            inst.items.add(inst.new Class(pool, buffer) /* an ridiculous way to construct object... */ );
        }
        return inst;
    }

    // innerName: the original simple name of class
    public void add(ClassConst clazz,
                    @Nullable String innerName, /* set to null to make anonymous class */
                    AccessFlag... flags) {
        EnumSet<AccessFlag> es = EnumSet.noneOf(AccessFlag.class);
        es.addAll(Arrays.asList(flags));
        add(clazz, innerName, es);
    }

    public void add(ClassConst clazz, String innerName, EnumSet<AccessFlag> flags) {
        // anti duplication
        for (Class i : items) {
            if (i.inner.equals(clazz)) return;
        }
        Class i = new Class(clazz, ((ClassFile) container).getThisClass(), innerName, flags);
        items.add(i);
    }

    public void add(ClassFile classFile) {
        ClassConst c = this.constPool().acquireClass(classFile.getThisClass());
        String name = c.getTypeName();
        name = name.substring(name.lastIndexOf('/') + 1);
        add(c, name, classFile.getAccessFlags());
    }

    public void remove(ClassConst clazz) {
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
    protected void collect() {
        super.collect();
        for (Class item : items) {
            constPool().acquire(item.getInner());
            constPool().acquire(item.getOuter());
            constPool().acquireUtf(item.getInnerName());
        }
    }

    @Override
    public void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Class i : items) {
            i.write(buffer);
        }
    }

    public final class Class {

        private ClassConst inner;
        private ClassConst outer;
        private String innerName;
        private EnumSet<AccessFlag> innerAccessFlags;

        Class(ClassConst inner,
              ClassConst outer,
              String innerName,
              EnumSet<AccessFlag> innerAccessFlags) {
            this.inner = inner;
            this.outer = outer;
            this.innerName = innerName;
            this.innerAccessFlags = innerAccessFlags;
        }

        Class(ClassFileParser pool, ByteVector buffer) {
            this.inner = (ClassConst) pool.get(buffer.getShort());
            this.outer = (ClassConst) pool.get(buffer.getShort());
            this.innerName = pool.getString(buffer.getShort());
            this.innerAccessFlags = AccessFlag.resolveInnerClass(buffer.getShort());
        }

        public ClassConst getInner() {
            return inner;
        }

        public void setInner(ClassConst inner) {
            this.inner = inner;
        }

        public ClassConst getOuter() {
            return outer;
        }

        public void setOuter(ClassConst outer) {
            this.outer = outer;
        }

        public String getInnerName() {
            return innerName;
        }

        public void setInnerName(@Nullable String innerName) {
            this.innerName = innerName;
        }

        public EnumSet<AccessFlag> getInnerAccessFlags() {
            return innerAccessFlags;
        }

        public void setInnerAccessFlags(AccessFlag... flags) {
            this.innerAccessFlags = EnumSet.noneOf(AccessFlag.class);
            this.innerAccessFlags.addAll(Arrays.asList(flags));
        }

        private void write(ByteVector buffer) {
            buffer.putShort(inner.index());
            buffer.putShort(outer.index());
            buffer.putShort(innerName == null ? 0 : constPool().acquireUtf(innerName).index());
            buffer.putShort(AccessFlag.serialize(innerAccessFlags));
        }

        @Override
        public String toString() {
            return "{inner=" + inner + ",outer=" + outer + ",inner_name=" + innerName + ",access_flags=" + innerAccessFlags + '}';
        }
    }
}
