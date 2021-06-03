package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.*;
import vip.creatio.gca.util.ClassUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import vip.creatio.gca.util.ByteVector;
import java.util.*;

@SuppressWarnings("unused")
public class ClassFile implements AttributeContainer, AccessFlagContainer {

    public static final int MAGIC = 0xCAFE_BABE;

    int minorVer;
    int majorVer;

    ConstPool constPool;
    EnumSet<AccessFlag> accessFlags;

    ClassConst thisClass;
    ClassConst superClass;

    List<ClassConst> interfaces = new ArrayList<>();

    List<DeclaredField> fields = new ArrayList<>();
    List<DeclaredMethod> methods = new ArrayList<>();

    List<Attribute> attributes = new ArrayList<>();

    private ClassFile() {}

    public static ClassFile parse(ByteVector buffer, @Nullable Map<String, AttributeResolver> resolvers) {
        ClassFile code = new ClassFile();
        code.parse0(buffer, resolvers);
        return code;
    }

    public static ClassFile parse(ByteVector buffer) {
        return parse(buffer, null);
    }

    public static ClassFile parse(byte[] bytecodes) {
        return parse(ByteVector.asVector(bytecodes));
    }

    public static ClassFile parse(InputStream stream) throws IOException {
        return parse(stream.readAllBytes());
    }

    private ClassFileParser getParser(ByteVector buffer, Map<String, AttributeResolver> resolvers) {
        int size = buffer.getUShort();
        return new ClassFileParser(this, size, resolvers);
    }

    private void parse0(ByteVector buffer, Map<String, AttributeResolver> resolvers) throws ClassFormatError {
        // validate magic number
        {
            int magic = buffer.getInt();
            if (magic != MAGIC) throw new
                    ClassFormatError("Invalid magic: 0x" + BigInteger.valueOf(magic).toString(16));
        }

        // get class versions
        minorVer = buffer.getUShort();
        majorVer = buffer.getUShort();

        // read constants
        constPool = new ConstPool(this);
        ClassFileParser pool = getParser(buffer, resolvers);
        ConstPool.parse(pool, buffer);

        // read class access flags
        accessFlags = AccessFlag.resolveClass(buffer.getShort());

        // read this class and super class object
        thisClass = (ClassConst) pool.get(buffer.getUShort());
        superClass = (ClassConst) pool.get(buffer.getUShort());

        // read interfaces
        {
            int iCount = buffer.getUShort();
            for (int i = 0; i < iCount; i++) {
                interfaces.add((ClassConst) pool.get(buffer.getUShort()));
            }
        }

        // read fields
        {
            int fieldCount = buffer.getUShort();
            for (int i = 0; i < fieldCount; i++) {
                fields.add(new DeclaredField(this, pool, buffer));
            }
        }

        // read methods
        {
            int methodCount = buffer.getUShort();
            for (int i = 0; i < methodCount; i++) {
                methods.add(new DeclaredMethod(this, pool, buffer));
            }
        }

        // read attributes
        {
            int attrCount = buffer.getUShort();
            for (int i = 0; i < attrCount; i++) {
                attributes.add(pool.resolveAttribute(buffer));
            }
        }

        // parse and link constants in pool
        pool.parse(buffer);
    }

    public ClassFile classFile() {
        return this;
    }

    public int getMinorVer() {
        return minorVer;
    }

    public void setMinorVer(int minorVer) {
        this.minorVer = minorVer;
    }

    public int getMajorVer() {
        return majorVer;
    }

    public void setMajorVer(int majorVer) {
        if (majorVer < 52 /* java 8 */ ) throw new RuntimeException("Invalid version: " + majorVer);
        this.majorVer = majorVer;
    }

    public EnumSet<AccessFlag> getAccessFlags() {
        return accessFlags;
    }

    @Override
    public void setAccessFlags(Collection<AccessFlag> flags) {
        accessFlags = EnumSet.noneOf(AccessFlag.class);
        accessFlags.addAll(flags);
    }

    public ClassConst getThisClass() {
        return thisClass;
    }

    public void setThisClass(ClassConst thisClass) {
        this.thisClass = thisClass;
    }

    public ClassConst getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassConst superClass) {
        this.superClass = superClass;
    }

    public ConstPool constPool() {
        return constPool;
    }

    public List<DeclaredField> getFields() {
        return fields;
    }

    public List<DeclaredMethod> getMethods() {
        return methods;
    }

    public List<ClassConst> getInterfaces() {
        return interfaces;
    }

    public DeclaredField getField(String name) {
        for (DeclaredField f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    // Get ot create field
    public @NotNull DeclaredField visitField(String name, String type) {
        DeclaredField field = getField(name);
        if (field != null) return field;

        EnumSet<AccessFlag> flags = EnumSet.of(AccessFlag.PRIVATE);
        field = new DeclaredField(this, flags, name, type);
        fields.add(field);
        return field;
    }

    public DeclaredMethod getMethod(String name, String... signatures) {
        for (DeclaredMethod method : methods) {
            if (method.getName().equals(name) && Arrays.equals(method.getDescriptors(), signatures))
                return method;
        }
        return null;
    }

    public @NotNull List<DeclaredMethod> getMethods(String name) {
        List<DeclaredMethod> mths = new ArrayList<>();
        for (DeclaredMethod mth : methods) {
            if (mth.getName().equals(name)) mths.add(mth);
        }
        return mths;
    }

    // Get or create method
    public @NotNull DeclaredMethod visitMethod(String name, String... signatures) {
        DeclaredMethod method = getMethod(name, signatures);
        if (method != null) return method;

        String signature = ClassUtil.getSignature(signatures);
        EnumSet<AccessFlag> flags = EnumSet.of(AccessFlag.PUBLIC);
        method = new DeclaredMethod(this, flags, name, signature, signatures);
        methods.add(method);
        return method;
    }

    public ClassConst getInterface(String clsName) {
        for (ClassConst c : interfaces) {
            if (c.getName().equals(clsName)) return c;
        }
        return null;
    }

    public @NotNull ClassConst visitInterface(String clsName) {
        ClassConst c = getInterface(clsName);
        if (c != null) return c;

        c = constPool.acquireClass(clsName);
        return c;
    }

    @Override
    public List<Attribute> attributes() {
        return attributes;
    }

    public @Nullable String getSourceFile() {
        SourceFile sf = getAttribute("SourceFile");
        return sf == null ? null : sf.getSource();
    }

    public void setSourceFile(@Nullable String name) {
        SourceFile sf = getOrAddAttribute("SourceFile", () -> new SourceFile(this));
        sf.setSource(name);
    }

    public @NotNull InnerClasses innerClasses() {
        return getOrAddAttribute("InnerClasses",
                () -> new InnerClasses(this));
    }

    public @NotNull EnclosingMethod enclosingMethod() {
        return getOrAddAttribute("EnclosingMethod",
                () -> new EnclosingMethod(this));
    }

    public boolean isSynthetic() {
        return hasAttribute("Synthetic");
    }

    public @Nullable String getSignature() {
        Signature sig = getAttribute("Signature");
        return sig == null ? null : sig.getGenericSignature() == null ? null : sig.getGenericSignature();
    }

    public void setSignature(@Nullable String s) {
        getOrAddAttribute("Signature",
                () -> new Signature(this))
                .setGenericSignature(s);
    }

    public boolean isDeprecated() {
        return hasAttribute("Deprecated");
    }

    public @NotNull BootstrapMethods bootstrapMethods() {
        return getOrAddAttribute("BootstrapMethods",
                () -> new BootstrapMethods(this));
    }

    public String getClassName() {
        return thisClass.getName();
    }

    public void write(ByteVector buffer) {
        // collect all used constants
        constPool.collect();
        constPool.recacheMap();

        buffer.putInt(MAGIC);

        buffer.putShort(minorVer);
        buffer.putShort(majorVer);

        constPool.write(buffer);

        buffer.putShort(AccessFlag.serialize(accessFlags));

        buffer.putShort(thisClass.index());
        buffer.putShort(superClass.index());

        buffer.putShort((short) interfaces.size());
        interfaces.forEach(i -> buffer.putShort(i.index()));

        buffer.putShort((short) fields.size());
        fields.forEach(f -> f.write(buffer));

        buffer.putShort((short) methods.size());
        methods.forEach(m -> m.write(buffer));

        AttributeContainer.super.writeAttributes(buffer);
        constPool.setWriting(false);
    }

    public void collect() {
        boolean previous = constPool.isWriting();
        constPool.setWriting(true);
        constPool.collect();
        constPool.acquire(thisClass, superClass);
        constPool.acquire(interfaces);
        for (DeclaredField f : fields) {
            f.collect();
        }
        for (DeclaredMethod mth : methods) {
            mth.collect();
        }
        AttributeContainer.super.collectConstants();
        constPool.setWriting(previous);
    }

    public ClassFile copy() {
        ClassFile c = new ClassFile();
        c.majorVer = majorVer;
        c.minorVer = minorVer;
        c.accessFlags = EnumSet.copyOf(accessFlags);
        c.constPool = new ConstPool(this);
        for (Const constant : constPool) {
            c.constPool.add(constant.copy());
        }
        //TODO

        return c;
    }

    public byte[] toByteArray() {
        ByteVector buffer = new ByteVector();
        write(buffer);
        return buffer.array();
    }
}
