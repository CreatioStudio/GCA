package vip.creatio.gca.type;

public interface AnnotationInfo {

    boolean equals(Object obj);

    int hashCode();

    String toString();

    Type annotationType();

}
