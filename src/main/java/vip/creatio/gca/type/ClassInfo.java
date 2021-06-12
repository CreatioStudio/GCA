package vip.creatio.gca.type;

import vip.creatio.gca.AccessFlag;

import java.util.EnumSet;
import java.util.List;

// representing a Class object or a ClassFile
public abstract class ClassInfo extends TypeInfo implements GenericInfo {

    protected Type superType;
    protected List<Type> interfaces;
    protected EnumSet<AccessFlag> accessFlags;

    protected List<FieldInfo> fields;
    protected List<MethodInfo> methods;

    // getTypeParameters()

    public Type getSuperclass() {
        return superType;
    }

    public Type[] getInterfaces() {
        return interfaces == null ? null : interfaces.toArray(new Type[0]);
    }

    public EnumSet<AccessFlag> getAccessFlags() {
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
}
