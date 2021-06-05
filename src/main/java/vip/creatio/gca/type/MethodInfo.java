package vip.creatio.gca.type;

public interface MethodInfo {

    TypeInfo getDeclaringClass();

    TypeInfo getReturnType();

    TypeInfo[] getParametersType();

    String getName();

}
