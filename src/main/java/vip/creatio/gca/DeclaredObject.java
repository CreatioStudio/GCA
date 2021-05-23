package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.Signature;
import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.constant.RefConst;
import vip.creatio.gca.constant.UTFConst;
import vip.creatio.gca.util.ClassUtil;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import java.util.*;

abstract class DeclaredObject
implements AttributeContainer, AccessFlagContainer, DeclaredSignature {

    protected final ClassFile classFile;
    protected EnumSet<AccessFlag> accessFlags;

    protected String name;
    protected String descriptor;
    protected List<Attribute> attributes = new ArrayList<>();

    protected String[] signatures;
    protected RefConst constRef;

    DeclaredObject(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        this.classFile = bc;
        this.accessFlags = resolveFlags(buffer.getShort());
        int s = buffer.getUShort();
        System.out.println("INDEX_OFFSET: " + Util.toHex(buffer.position()));
        this.name = pool.getString(s);
        this.descriptor = pool.getString(buffer.getShort());
        System.out.println("Parsed: " + name);
        int attrCount = buffer.getShort() & 0xFFFF;
        for (int i = 0; i < attrCount; i++) {
            try {
                attributes.add(pool.resolveAttribute(buffer));
            } catch (BytecodeException e) {
                e.setMethodName(name);
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

    public @Nullable String getSignature() {
        Signature sig = getAttribute("Signature");
        return sig == null ? null : sig.getSignature() == null ? null : sig.getSignature();
    }

    public void setSignature(@Nullable String s) {
        getOrAddAttribute("Signature",
                () -> new Signature(classFile))
                .setSignature(s);
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
        UTFConst name = constPool().acquireUtf(this.name);
        UTFConst desc = constPool().acquireUtf(descriptor);
        buffer.putShort(name.index());
        buffer.putShort(desc.index());

        AttributeContainer.super.writeAttributes(buffer);
    }

    @Override
    public void recache() {
        this.signatures = ClassUtil.fromSignature(descriptor);
    }

    @Override
    public String[] getSignatures() {
        return signatures;
    }
}
