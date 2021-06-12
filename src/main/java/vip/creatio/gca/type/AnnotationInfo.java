package vip.creatio.gca.type;

public interface AnnotationInfo {

    Type annotationType();

    Object getValue(String name);

}
