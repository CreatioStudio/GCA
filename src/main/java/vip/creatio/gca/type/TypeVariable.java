package vip.creatio.gca.type;

import vip.creatio.gca.AnnotationInfo;

public class TypeVariable implements Type, AnnotatedInfo {

    @Override
    public AnnotationInfo[] getAnnotations() {
        return new AnnotationInfo[0];
    }

    @Override
    public String getTypeName() {
        return null;
    }
}
