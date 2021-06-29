package vip.creatio.gca;

import vip.creatio.gca.code.BytecodeException;

import vip.creatio.gca.type.MemberInfo;
import vip.creatio.gca.util.common.ByteVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class DeclaredObject
implements AttributeContainer, MemberInfo {

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

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int flags) {
        this.accessFlags = flags;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public boolean isDeprecated() {
        return getAttribute("Deprecated") != null;
    }

    public DeclaredAnnotation[] getAnnotations() {
        return annotations.values().toArray(new DeclaredAnnotation[0]);
    }

    public void removeAnnotation(TypeInfo type) {
        annotations.remove(type);
    }

    public void removeAnnotation(String type) {
        removeAnnotation(classFile.repository().toType(type));
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
        return getAnnotation(classFile.repository().toType(type));
    }

    public Annotation addAnnotation(TypeInfo type, boolean visible) {
        Annotation anno = new Annotation(this, type, visible);
        annotations.put(type, anno);
        return anno;
    }




    // Internals

    private final ClassFile classFile;
    private int accessFlags;

    private String name;
    protected HashMap<String, Attribute> attributes = new HashMap<>();
    protected Map<TypeInfo, DeclaredAnnotation> annotations = new HashMap<>();



    abstract void setDescriptor(String s);

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(accessFlags);
        buffer.putShort(pool.indexOf(this.name));
        buffer.putShort(pool.indexOf(getDescriptor()));

        try {
            AttributeContainer.super.writeAttributes(pool, buffer);
        } catch (BytecodeException e) {
            e.setLocation(name + getDescriptor());
            throw e;
        }
    }

    void collect(ConstPool pool) {
        pool.acquireUtf(this.name);
        System.out.println("Descriptor: " + getDescriptor());
        pool.acquireUtf(getDescriptor());
        collectConstants(pool);
        annotations.values().forEach(a -> a.collect(pool));
    }


    // Constructors

    DeclaredObject(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        this.classFile = bc;
        this.accessFlags = buffer.getUShort();
        int s = buffer.getUShort();
        this.name = pool.getString(s);
        setDescriptor(pool.getString(buffer.getUShort()));
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
                   int flags,
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
