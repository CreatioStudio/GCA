package vip.creatio.gca;

import vip.creatio.gca.attr.Signature;
import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.type.AnnotationInfo;
import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.type.Types;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class DeclaredObject
implements AttributeContainer, AccessFlagContainer {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override //TODO
    public AttributeContainer copy() {
        return null;
    }

    public ClassFile classFile() {
        return classFile;
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

    public Map<String, Attribute> getAttributes() {
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

    // get all annotations
    public DeclaredAnnotation[] getAnnotations() {
        return Util.merge(DeclaredAnnotation[]::new,
                getAnnotations(true),
                getAnnotations(false),
                getTypeAnnotations(true),
                getTypeAnnotations(false));
    }

    public Annotation[] getAnnotations(boolean visible) {
        Annotations anno;
        if (visible) {
            anno = getAttribute("RuntimeVisibleAnnotations");
        } else {
            anno = getAttribute("RuntimeInvisibleAnnotations");
        }
        return anno == null ? new Annotation[0] : anno.getTable().toArray(new Annotation[0]);
    }

    public void setAnnotations(boolean visible, Annotation... annos) {
        if (visible) {

        }
    }

    public TypeAnnotation[] getTypeAnnotations(boolean visible) {
        TypeAnnotations anno;
        if (visible) {
            anno = getAttribute("RuntimeVisibleTypeAnnotations");
        } else {
            anno = getAttribute("RuntimeInvisibleTypeAnnotations");
        }
        return anno == null ? new TypeAnnotation[0] : anno.getTable().toArray(new TypeAnnotation[0]);
    }

    public Annotation getAnnotation(String typeName) {
        return getAnnotation0(anno -> anno.get(typeName));
    }

    public Annotation getAnnotation(TypeInfo type) {
        return getAnnotation0(anno -> anno.get(type));
    }

    private Annotation getAnnotation0(Function<Annotations, Annotation> func) {
        Annotations annos = getAttribute("RuntimeVisibleAnnotations");
        if (annos == null) {
            annos = getAttribute("RuntimeInvisibleAnnotations");
            if (annos == null) return null;
            return func.apply(annos);
        }
        Annotation anno = func.apply(annos);
        if (anno == null) {
            annos = getAttribute("RuntimeInvisibleAnnotations");
            if (annos == null) return null;
            anno = func.apply(annos);
        }
        return anno;
    }

    public TypeAnnotation getTypeAnnotation(String typeName) {
        return getTypeAnnotation0(anno -> anno.get(typeName));
    }

    public TypeAnnotation getTypeAnnotation(TypeInfo type) {
        return getTypeAnnotation0(anno -> anno.get(type));
    }

    private TypeAnnotation getTypeAnnotation0(Function<TypeAnnotations, TypeAnnotation> func) {
        TypeAnnotations annos = getAttribute("RuntimeVisibleAnnotations");
        if (annos == null) {
            annos = getAttribute("RuntimeInvisibleAnnotations");
            if (annos == null) return null;
            return func.apply(annos);
        }
        TypeAnnotation anno = func.apply(annos);
        if (anno == null) {
            annos = getAttribute("RuntimeInvisibleAnnotations");
            if (annos == null) return null;
            anno = func.apply(annos);
        }
        return anno;
    }




    // Internals

    final ClassFile classFile;
    EnumSet<AccessFlag> accessFlags;

    String name;
    HashMap<String, Attribute> attributes = new HashMap<>();

    RefConst constRef;



    ConstPool constPool() {
        return classFile.constPool;
    }

    abstract EnumSet<AccessFlag> resolveFlags(short flags);

    abstract String getDescriptor();

    abstract void setDescriptor(String s);

    void write(ByteVector buffer) {
        buffer.putShort(AccessFlag.serialize(accessFlags));
        buffer.putShort(constPool().acquireUtf(this.name).index());
        buffer.putShort(constPool().acquireUtf(getDescriptor()).index());

        try {
            AttributeContainer.super.writeAttributes(buffer);
        } catch (BytecodeException e) {
            e.setLocation(name + getDescriptor());
            throw e;
        }
    }

    void collect() {
        constPool().acquireUtf(this.name);
        constPool().acquireUtf(getDescriptor());
        collectConstants();
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


    // Constructors

    DeclaredObject(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        this.classFile = bc;
        this.accessFlags = resolveFlags(buffer.getShort());
        int s = buffer.getUShort();
        this.name = pool.getString(s);
        setDescriptor(pool.getString(buffer.getShort()));
        int attrCount = buffer.getUShort();

        for (int i = 0; i < attrCount; i++) {
            try {
                Attribute attr = pool.resolveAttribute(this, buffer);
                if (attr != null) {
                    attributes.put(attr.name(), attr);
                }
            } catch (BytecodeException e) {
                e.setLocation(name + getDescriptor());
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
                   Attribute... attributes) {
        this.classFile = bc;
        this.accessFlags = flags;
        this.name = name;
        this.attributes = attributes.length == 0
                ? new HashMap<>()
                : (HashMap<String, Attribute>) Arrays.stream(attributes)
                .collect(Collectors.toMap(Attribute::name, Function.identity()));
    }
}
