package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.*;
import vip.creatio.gca.type.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import vip.creatio.gca.util.common.ByteVector;

import java.util.*;

@SuppressWarnings("unused")
public class ClassFile extends ClassInfo implements AttributeContainer, AccessFlagContainer, TypeInfo.Mutable {

    public static final int MAGIC = 0xCAFE_BABE;

    private int minorVer;
    private int majorVer;

    private EnumSet<AccessFlag> accessFlags;

    private Map<String, Attribute> attributes = new HashMap<>();

    private Map<TypeInfo, DeclaredAnnotation> annotations = new HashMap<>();

    private Repository repository;

    ClassFile() {
        // private class map, no need to synchronize
        this(new Repository());
    }

    ClassFile(Repository remote) {
        // class map from ClassPool, have to be a ConcurrentHashMap for thread-safe consideration.
        this.repository = remote;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.interfaces = new ArrayList<>();
    }

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
        ClassFileParser pool = getParser(buffer, resolvers);
        pool.parse(buffer);

        // read class access flags
        accessFlags = AccessFlag.resolveClass(buffer.getShort());

        // read this class and super class object
        name = ((TypeInfo) pool.get(buffer.getUShort())).getTypeName();
        superType = (TypeInfo) pool.get(buffer.getUShort());

        // read interfaces
        {
            int iCount = buffer.getUShort();
            for (int i = 0; i < iCount; i++) {
                interfaces.add((TypeInfo) pool.get(buffer.getUShort()));
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
                Attribute attr = pool.resolveAttribute(buffer);
                if (attr != null) {
                    attributes.put(attr.name(), attr);
                }
            }
        }

        // link methods in BootstrapMethods
        pool.parseDynamics();
    }

    //TODO: void setPool(ClassPool)

    public ClassFile classFile() {
        return this;
    }

    public Repository repository() {
        return repository;
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

    @Override
    public void setName(String binName) {
        name = binName;
    }

    public void setSuperClass(TypeInfo superClass) {
        this.superType = superClass;
    }

    // Get ot create field
    public @NotNull DeclaredField visitField(String name, String type) {
        DeclaredField field = (DeclaredField) getField(name);
        if (field != null) return field;

        EnumSet<AccessFlag> flags = EnumSet.of(AccessFlag.PRIVATE);
        field = new DeclaredField(this, flags, name, repository.toType(type));
        fields.add(field);
        return field;
    }

    public DeclaredField getField(String name) {
        for (FieldInfo field : fields) {
            if (field.getName().equals(name)) return (DeclaredField) field;
        }
        return null;
    }

    public DeclaredMethod getMethod(String name, String rtype, String... ptype) {
        return getMethod(name, repository().toType(rtype), repository().toType(ptype));
    }

    public DeclaredMethod getMethod(String name, TypeInfo rtype, TypeInfo... ptype) {
        for (MethodInfo mth : methods) {
            if (mth.getName().equals(name) && Types.withSignature(mth, rtype, ptype)) {
                return (DeclaredMethod) mth;
            }
        }
        return null;
    }

    public DeclaredMethod getmethod(String name, String descriptor) {
        for (MethodInfo mth : methods) {
            if (mth.getName().equals(name) && mth.getDescriptor().equals(descriptor))
                return (DeclaredMethod) mth;
        }
        return null;
    }

    public @NotNull List<DeclaredMethod> getMethods(String name) {
        List<DeclaredMethod> mths = new ArrayList<>();
        for (MethodInfo mth : methods) {
            if (mth.getName().equals(name)) mths.add((DeclaredMethod) mth);
        }
        return mths;
    }

    // Get or create method
    public @NotNull DeclaredMethod visitMethod(String name, String rtype, String... ptype) {
        DeclaredMethod method = getMethod(name, rtype, ptype);
        if (method != null) return method;

        EnumSet<AccessFlag> flags = EnumSet.of(AccessFlag.PUBLIC);
        method = new DeclaredMethod(this, flags, name, repository.toType(rtype), repository.toType(ptype));
        methods.add(method);
        return method;
    }

    public Type getInterface(String clsName) {
        for (Type c : interfaces) {
            if (c.getTypeName().equals(clsName)) return c;
        }
        return null;
    }

    public @NotNull Type visitInterface(String clsName) {
        Type c = getInterface(clsName);
        if (c != null) return c;

        c = repository.toType(clsName);
        interfaces.add((TypeInfo) c);
        return c;
    }

    @Override
    public Map<String, Attribute> getAttributes() {
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
        return sig == null ? null : sig.getGenericType() == null ? null : sig.getGenericType();
    }

    public void setSignature(@Nullable String s) {
        getOrAddAttribute("Signature",
                () -> new Signature(this))
                .setGenericType(s);
    }

    public boolean isDeprecated() {
        return hasAttribute("Deprecated");
    }

    public @NotNull BootstrapMethods bootstrapMethods() {
        return getOrAddAttribute("BootstrapMethods",
                () -> new BootstrapMethods(this));
    }

    public void write(ByteVector buffer) {
        // collect all used constants
        ConstPool pool = new ConstPool(this); // new ClassCollectorPool
        collectFile(pool);

        buffer.putInt(MAGIC);

        buffer.putShort(minorVer);
        buffer.putShort(majorVer);

        pool.write(buffer);

        buffer.putShort(AccessFlag.serialize(accessFlags));

        buffer.putShort(pool.indexOf(this));
        buffer.putShort(pool.indexOf(superType));

        buffer.putShort((short) interfaces.size());
        interfaces.forEach(i -> buffer.putShort(pool.indexOf(i)));

        buffer.putShort((short) fields.size());
        fields.forEach(f -> ((DeclaredField) f).write(pool, buffer));

        buffer.putShort((short) methods.size());
        methods.forEach(m -> ((DeclaredMethod) m).write(pool, buffer));

        AttributeContainer.super.writeAttributes(pool, buffer);
    }

    private void collectFile(ConstPool pool) {
        pool.collect();
        pool.acquireClass(this);
        pool.acquireClass(superType);
        interfaces.forEach(pool::acquireClass);
        for (FieldInfo f : fields) {
            ((DeclaredField) f).collect(pool);
        }
        for (MethodInfo mth : methods) {
            ((DeclaredMethod) mth).collect(pool);
        }
        AttributeContainer.super.collectConstants(pool);
        for (DeclaredAnnotation value : annotations.values()) {
            value.collect(pool);
            if (value instanceof Annotation) {
                Annotations anno;
                if (value.visible()) {
                    anno = getOrAddAttribute("RuntimeVisibleAnnotations", () -> new Annotations(this, true));
                } else {
                    anno = getOrAddAttribute("RuntimeInvisibleAnnotations", () -> new Annotations(this, false));
                }
                anno.add((Annotation) value);
            } else if (value instanceof TypeAnnotation) {
                TypeAnnotations anno;
                if (value.visible()) {
                    anno = getOrAddAttribute("RuntimeVisibleTypeAnnotations", () -> new TypeAnnotations(this, true));
                } else {
                    anno = getOrAddAttribute("RuntimeInvisibleTypeAnnotations", () -> new TypeAnnotations(this, false));
                }
                anno.add((TypeAnnotation) value);
            }
        }
    }

    public ClassFile copy() {
        ClassFile c = new ClassFile();
        c.majorVer = majorVer;
        c.minorVer = minorVer;
        c.accessFlags = EnumSet.copyOf(accessFlags);
        //TODO

        return c;
    }

    public byte[] toByteArray() {
        ByteVector buffer = new ByteVector();
        write(buffer);
        return buffer.array();
    }

    @Override
    public DeclaredAnnotation[] getAnnotations() {
        return annotations.values().toArray(new DeclaredAnnotation[0]);
    }

    public void removeAnnotation(TypeInfo type) {
        annotations.remove(type);
    }

    public void removeAnnotation(String type) {
        removeAnnotation(repository.toType(type));
    }

    public void clearAnnotations() {
        annotations.clear();
    }

    public void addAnnotation(DeclaredAnnotation anno) {
        annotations.put(anno.annotationType(), anno);
    }

    public DeclaredAnnotation getAnnotation(TypeInfo type) {
        return annotations.get(type);
    }

    public DeclaredAnnotation getAnnotation(String type) {
        return getAnnotation(repository.toType(type));
    }

    public Annotation addAnnotation(TypeInfo type, boolean visible) {
        Annotation anno = new Annotation(this, type, visible);
        annotations.put(type, anno);
        return anno;
    }

    @Override
    public MethodInfo getEnclosingMethod() {
        return null;
    }

    @Override
    public Type getDeclaringClass() {
        return null;
    }

    @Override
    public Type[] getClasses() {
        return new Type[0];
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        return new TypeVariable[0];
    }

    //TypeAnnotation: enhancement required



}
