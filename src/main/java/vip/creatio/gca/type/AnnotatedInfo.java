package vip.creatio.gca.type;

import java.util.Objects;

public interface AnnotatedInfo {

    default boolean isAnnotationPresent(Type annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    default AnnotationInfo getAnnotation(Type annotationClass) {
        Objects.requireNonNull(annotationClass);
        for (AnnotationInfo annotation : getAnnotations()) {
            if (annotationClass.getTypeName().equals(annotation.annotationType().getTypeName())) {
                return annotation;
            }
        }
        return null;
    }

    AnnotationInfo[] getAnnotations();

}
