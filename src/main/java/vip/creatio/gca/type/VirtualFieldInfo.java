package vip.creatio.gca.type;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.ConstType;
import vip.creatio.gca.TypeInfo;

// method that only declared in a class file
public class VirtualFieldInfo implements FieldInfo {

    private TypeInfo decl;
    private TypeInfo type;
    private String name;

    // implementation direct
    private DeclaredFieldInfo impl;

    public VirtualFieldInfo(TypeInfo decl, String name, TypeInfo type) {
        this.decl = decl;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getDescriptor() {
        if (impl != null) return impl.getDescriptor();
        return type.getInternalName();
    }

    @Override
    public TypeInfo getDeclaringClass() {
        if (impl != null) return impl.getDeclaringClass();
        return decl;
    }

    @Override
    public String getName() {
        if (impl != null) return impl.getName();
        return name;
    }

    public void setName(String name) {
        if (impl != null) throw new UnsupportedOperationException("Delegated FieldInfo cannot be modified directly");
        this.name = name;
    }

    @Override
    public TypeInfo getType() {
        return type;
    }

    public void setType(TypeInfo type) {
        if (impl != null) throw new UnsupportedOperationException("Delegated FieldInfo cannot be modified directly");
        this.type = type;
    }

    @Override
    public ConstType constantType() {
        return ConstType.FIELDREF;
    }

    public DeclaredFieldInfo getImpl() {
        return impl;
    }

    public void setDeclaringClass(TypeInfo type) {
        if (impl != null) throw new UnsupportedOperationException("Delegated FieldInfo cannot be modified directly");
        this.decl = type;
    }

    public void setImpl(@NotNull DeclaredFieldInfo impl) {
        this.impl = impl;
        // chear all properties
        this.decl = null;
        this.name = null;
        this.type = null;
    }
}
