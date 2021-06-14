package vip.creatio.gca.type;

import vip.creatio.gca.TypeInfo;

public interface DeclaredMethodInfo extends MethodInfo, AnnotatedInfo {

    TypeInfo[] getExceptionTypes();

    Type[] getGenericExceptionTypes();

    Type[] getGenericParameterTypes();

    Type getGenericReturnType();

    TypeVariable[] getTypeParameters();

    AnnotationInfo[][] getParameterAnnotations();

}
