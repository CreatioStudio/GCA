package vip.creatio.gca.type;

import vip.creatio.gca.TypeInfo;

public class VirtualClassInfo extends TypeInfo implements TypeInfo.Mutable {

    protected ClassInfo impl;

    public VirtualClassInfo(String name) {
        super(name);
    }

    public VirtualClassInfo(ClassInfo impl) {
        this.impl = impl;
    }

    @Override
    public ClassInfo getImpl() {
        return super.getImpl();
    }

    public void setImpl(ClassInfo cls) {
        this.impl = cls;
    }

    @Override
    public String getName() {
        if (impl != null) return impl.getName();
        return super.getName();
    }

    @Override
    public String getCanonicalName() {
        if (impl != null) return impl.getCanonicalName();
        return super.getCanonicalName();
    }

    @Override
    public String getSignature() {
        if (impl != null) return impl.getSignature();
        return super.getSignature();
    }

    @Override
    public String getInternalName() {
        if (impl != null) return impl.getInternalName();
        return super.getInternalName();
    }

    @Override
    public String getInternalSignature() {
        if (impl != null) return impl.getInternalSignature();
        return super.getInternalSignature();
    }

    @Override
    public String getSimpleName() {
        if (impl != null) return impl.getSimpleName();
        return super.getSimpleName();
    }

    @Override
    public void setName(String name) {
        if (impl != null && impl instanceof TypeInfo.Mutable) {
            ((Mutable) impl).setName(name);
        } else {
            super.setName(name);
        }
    }
}
