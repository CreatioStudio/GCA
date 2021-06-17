package vip.creatio.gca.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ConstType;
import vip.creatio.gca.TypeInfo;

import java.util.Arrays;

// method that only declared in a class file
public class VirtualMethodInfo implements MethodInfo {

    private TypeInfo decl;
    private TypeInfo rtype;
    private TypeInfo[] ptype;
    private String name;
    private final boolean isInterface;

    // implementation direct
    private DeclaredMethodInfo impl;

    public VirtualMethodInfo(boolean isInterface, TypeInfo decl, String name, TypeInfo rtype, TypeInfo... ptype) {
        this.decl = decl;
        this.name = name;
        this.rtype = rtype;
        this.ptype = ptype;
        this.isInterface = isInterface;
    }

    public VirtualMethodInfo(boolean isInterface, TypeInfo decl, String name, TypeInfo... types) {
        this(isInterface, decl, name, types[0], Arrays.copyOfRange(types, 1, types.length));
    }


    @Override
    public String getDescriptor() {
        if (impl != null) return impl.getDescriptor();
        return Types.toMethodSignature(rtype, ptype);
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
        if (impl != null) throw new UnsupportedOperationException("Delegated MethodInfo cannot be modified directly");
        this.name = name;
    }

    @Override
    public TypeInfo getReturnType() {
        if (impl != null) return impl.getReturnType();
        return rtype;
    }

    public void setReturnType(TypeInfo type) {
        if (impl != null) throw new UnsupportedOperationException("Delegated MethodInfo cannot be modified directly");
        this.rtype = type;
    }

    @Override
    public TypeInfo[] getParameterTypes() {
        if (impl != null) return impl.getParameterTypes();
        return ptype;
    }

    public void setParameterTypes(TypeInfo... types) {
        if (impl != null) throw new UnsupportedOperationException("Delegated MethodInfo cannot be modified directly");
        this.ptype = types;
    }

    @Override
    public ConstType constantType() {
        return isInterface ? ConstType.INTERFACE_METHODREF : ConstType.METHODREF;
    }

    public void setDeclaringClass(TypeInfo type) {
        if (impl != null) throw new UnsupportedOperationException("Delegated MethodInfo cannot be modified directly");
        this.decl = type;
    }

    @Override
    public @Nullable DeclaredMethodInfo getImpl() {
        return impl;
    }

    public void setImpl(@NotNull DeclaredMethodInfo impl) {
        this.impl = impl;
        // chear all properties
        this.decl = null;
        this.rtype = null;
        this.ptype = null;
        this.name = null;
    }
}
