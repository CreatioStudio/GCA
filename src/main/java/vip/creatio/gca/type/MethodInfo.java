package vip.creatio.gca.type;

public interface MethodInfo {

    TypeInfo getDeclaringClass();

    TypeInfo getReturnType();

    TypeInfo[] getParameterTypes();

    String getName();

    default int getParameterCount() {
        return getParameterTypes().length;
    }
}
