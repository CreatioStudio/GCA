package vip.creatio.gca.type;

import vip.creatio.gca.TypeInfo;

import java.util.Arrays;
import java.util.List;

import static vip.creatio.gca.util.AccessFlags.*;

// representing a Class object or a ClassFile
public abstract class ClassInfo extends TypeInfo implements GenericInfo, GenericDeclaration {

    protected TypeInfo superType;
    protected List<TypeInfo> interfaces;
    //protected EnumSet<AccessFlag> accessFlags;
    protected int accessFlags;

    protected List<FieldInfo> fields;
    protected List<MethodInfo> methods;

    protected TypeVariable[] typeVars;

    // getTypeParameters()

    public TypeInfo getSuperclass() {
        return superType;
    }

    public TypeInfo[] getInterfaces() {
        return interfaces == null ? null : interfaces.toArray(new TypeInfo[0]);
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public abstract MethodInfo getEnclosingMethod();

    public abstract Type getDeclaringClass();

    public boolean isLocalOrAnonymousClass() {
        return getEnclosingMethod() != null;
    }

    public abstract Type[] getClasses();

    public FieldInfo[] getFields() {
        return fields.toArray(new FieldInfo[0]);
    }

    public MethodInfo[] getMethods() {
        return methods.toArray(new MethodInfo[0]);
    }

    public FieldInfo getField(String name) {
        for (FieldInfo f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        return Arrays.copyOf(typeVars, typeVars.length);
    }

    /* access flags */

    public boolean hasAllAccessFlags(int flag) {
        return (accessFlags & flag) == flag;
    }

    public boolean hasAnyAccessFlags(int flag) {
        return (accessFlags & flag) != 0;
    }

    public boolean isPublic() {
        return (accessFlags & PUBLIC) != 0;
    }

    public boolean isFinal() {
        return (accessFlags & FINAL) != 0;
    }

    public boolean isSuper() {
        return (accessFlags & SUPER) != 0;
    }

    public boolean isInterface() {
        return (accessFlags & INTERFACE) != 0;
    }

    public boolean isAbstract() {
        return (accessFlags & ABSTRACT) != 0;
    }

    public boolean isSynthetic() {
        return (accessFlags & SYNTHETIC) != 0;
    }

    public boolean isAnnotation() {
        return (accessFlags & ANNOTATION) != 0;
    }

    public boolean isEnum() {
        return (accessFlags & ENUM) != 0;
    }

    public boolean isModule() {
        return (accessFlags & MODULE) != 0;
    }
}
