package vip.creatio.gca.type;

public interface MethodSignature extends Signature {

    Type getReturnType();

    Type[] getParameterType();

}
