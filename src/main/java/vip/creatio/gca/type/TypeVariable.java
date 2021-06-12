package vip.creatio.gca.type;

import vip.creatio.gca.DeclaredAnnotation;

public class TypeVariable implements Type, AnnotatedInfo {

    @Override
    public DeclaredAnnotation[] getAnnotations() {
        return new DeclaredAnnotation[0];
    }

    @Override
    public String getTypeName() {
        return null;
    }
}
