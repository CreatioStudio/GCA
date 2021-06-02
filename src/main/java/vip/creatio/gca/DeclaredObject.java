package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.Signature;
import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.constant.RefConst;
import vip.creatio.gca.constant.UTFConst;
import vip.creatio.gca.util.ClassUtil;

import vip.creatio.gca.util.ByteVector;

import java.util.*;

abstract class DeclaredObject
implements AttributeContainer, AccessFlagContainer, Descriptor, GenericSignature {

    protected final ClassFile classFile;
    protected EnumSet<AccessFlag> accessFlags;

    protected String name;
    protected String descriptor;
    protected List<Attribute> attributes = new ArrayList<>();

    protected String[] descriptors;
    protected RefConst constRef;

    DeclaredObject(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        this.classFile = bc;
        this.accessFlags = resolveFlags(buffer.getShort());
        int s = buffer.getUShort();
        this.name = pool.getString(s);
        this.descriptor = pool.getString(buffer.getShort());
        int attrCount = buffer.getShort() & 0xFFFF;

        for (int i = 0; i < attrCount; i++) {
            try {
                attributes.add(pool.resolveAttribute(this, buffer));
            } catch (BytecodeException e) {
                e.setLocation(name + descriptor);
                throw e;
            } catch (Exception e) {
                System.err.println("Exception while parsing attributes in "
                        + (this instanceof DeclaredMethod ? "method" : "field") + ' ' + name);
                throw e;
            }
        }
        recache();
    }

    DeclaredObject(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   String descriptor,
                   Attribute... attributes) {
        this.classFile = bc;
        this.accessFlags = flags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = new ArrayList<>(Arrays.asList(attributes));
        recache();
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        recache();
    }

    @Override
    public String[] getDescriptors() {
        return descriptors;
    }

    public @Nullable String getGenericSignature() {
        Signature sig = getAttribute("Signature");
        return sig == null ? null : sig.getGenericSignature() == null ? null : sig.getGenericSignature();
    }

    public void setGenericSignature(@Nullable String s) {
        getOrAddAttribute("Signature",
                () -> new Signature(classFile))
                .setGenericSignature(s);
    }

    @Override
    public int getParameterCount() {
        return Descriptor.super.getParameterCount();
    }

    @Override //TODO
    public AttributeContainer copy() {
        return null;
    }

    @Override
    public @Nullable String[] getGenericSignatures() {
        Signature sig = getAttribute("Signature");
        return sig == null ? null : sig.getGenericSignatures();
    }

    public ClassFile classFile() {
        return classFile;
    }

    protected ConstPool constPool() {
        return classFile.constPool;
    }

    public EnumSet<AccessFlag> getAccessFlags() {
        return accessFlags;
    }

    @Override
    public void setAccessFlags(Collection<AccessFlag> flags) {
        if (accessFlags == null) accessFlags = EnumSet.noneOf(AccessFlag.class);
        else accessFlags.clear();
        accessFlags.addAll(flags);
    }

    abstract EnumSet<AccessFlag> resolveFlags(short flags);

    public List<Attribute> attributes() {
        return attributes;
    }

    public boolean isSynthetic() {
        return getAttribute("Synthetic") != null;
    }

    public boolean isDeprecated() {
        return getAttribute("Deprecated") != null;
    }

    // create a new reference const in constant pool
    public RefConst getReference() {
        if (constRef == null) addRefConst();
        return constRef;
    }

    private void addRefConst() {
        for (Const c : classFile.constPool()) {
            if (c instanceof RefConst) {
                if (((RefConst) c).getDeclaringClass().equals(classFile.thisClass)
                        && ((RefConst) c).getName().equals(name)) {
                    this.constRef = (RefConst) c;
                    return;
                }
            }
        }
        if (this instanceof DeclaredField) {
            this.constRef = constPool().acquireFieldRef(classFile.thisClass, name, descriptor);
        } else {
            this.constRef = constPool().acquireMethodRef(classFile.thisClass, name, descriptor);
        }
    }

    public void write(ByteVector buffer) {
        buffer.putShort(AccessFlag.serialize(accessFlags));
        buffer.putShort(constPool().acquireUtf(this.name).index());
        buffer.putShort(constPool().acquireUtf(descriptor).index());

        try {
            AttributeContainer.super.writeAttributes(buffer);
        } catch (BytecodeException e) {
            e.setLocation(name + descriptor);
            throw e;
        }
    }

    protected void collect() {
        constPool().acquireUtf(this.name);
        constPool().acquireUtf(descriptor);
        collectConstants();
    }

    @Override
    public void recache() {
        this.descriptors = ClassUtil.fromSignature(descriptor);
        //Signature sig = getAttribute("Signature");//TODO
        //if (sig != null) sig.recache();
    }
}
