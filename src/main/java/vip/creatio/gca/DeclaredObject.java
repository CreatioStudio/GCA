package vip.creatio.gca;

import vip.creatio.gca.attr.Signature;
import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.type.Types;

import vip.creatio.gca.util.ByteVector;

import java.util.*;

abstract class DeclaredObject
implements AttributeContainer, AccessFlagContainer {

    protected final ClassFile classFile;
    protected EnumSet<AccessFlag> accessFlags;

    protected String name;
    protected TypeInfo descriptor;
    protected List<Attribute> attributes = new ArrayList<>();

    protected RefConst constRef;

    DeclaredObject(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        this.classFile = bc;
        this.accessFlags = resolveFlags(buffer.getShort());
        int s = buffer.getUShort();
        this.name = pool.getString(s);
        this.descriptor = Types.toType(pool.getString(buffer.getShort()));
        int attrCount = buffer.getUShort();

        for (int i = 0; i < attrCount; i++) {
            try {
                attributes.add(pool.resolveAttribute(this, buffer));
            } catch (BytecodeException e) {
                e.setLocation(name + descriptor.getCanonicalName());
                throw e;
            } catch (Exception e) {
                System.err.println("Exception while parsing attributes in "
                        + (this instanceof DeclaredMethod ? "method" : "field") + ' ' + name);
                throw e;
            }
        }
    }

    DeclaredObject(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   TypeInfo descriptor,
                   Attribute... attributes) {
        this.classFile = bc;
        this.accessFlags = flags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = new ArrayList<>(Arrays.asList(attributes));
    }

    public String getName() {
        return name;
    }

    public TypeInfo getDescriptor() {
        return descriptor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeInfo descriptor) {
        this.descriptor = descriptor;
    }

    public void setType(String descriptor) {
        this.descriptor = Types.toType(descriptor);
        Signature sig = getAttribute("Signature");
        if (sig != null) sig.setGenericType((Type) null);
    }

    public void setGenericType(Type s) {
        getOrAddAttribute("Signature",
                () -> new Signature(classFile))
                .setGenericType(s);
        descriptor = Types.toType(s);
    }

    public void setGenericType(String s) {
        getOrAddAttribute("Signature",
                () -> new Signature(classFile))
                .setGenericType(s);
        descriptor = Types.toType(s);
    }

    @Override //TODO
    public AttributeContainer copy() {
        return null;
    }

    public Type getGenericType() {
        Signature sig = getAttribute("Signature");
        return sig == null ? null : sig.getGenericType();
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
        buffer.putShort(constPool().acquireUtf(descriptor.getInternalName()).index());

        try {
            AttributeContainer.super.writeAttributes(buffer);
        } catch (BytecodeException e) {
            e.setLocation(name + descriptor);
            throw e;
        }
    }

    protected void collect() {
        constPool().acquireUtf(this.name);
        constPool().acquireUtf(descriptor.getInternalName());
        collectConstants();
    }
}
