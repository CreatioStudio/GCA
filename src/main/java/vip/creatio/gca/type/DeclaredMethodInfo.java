package vip.creatio.gca.type;

import vip.creatio.gca.TypeInfo;

import static vip.creatio.gca.util.AccessFlags.*;

public interface DeclaredMethodInfo extends MethodInfo, GenericDeclaration {

    TypeInfo[] getExceptionTypes();

    Type[] getGenericExceptionTypes();

    Type[] getGenericParameterTypes();

    Type getGenericReturnType();

    TypeVariable[] getTypeParameters();

    AnnotationInfo[][] getParameterAnnotations();
    
    int getAccessFlags();

    /* access flags */

    default boolean hasAllAccessFlags(int flag) {
        return (getAccessFlags() & flag) == flag;
    }

    default boolean hasAnyAccessFlags(int flag) {
        return (getAccessFlags() & flag) != 0;
    }

    default boolean isPublic() {
        return (getAccessFlags() & PUBLIC) != 0;
    }

    default boolean isPrivate() {
        return (getAccessFlags() & PRIVATE) != 0;
    }

    default boolean isProtected() {
        return (getAccessFlags() & PROTECTED) != 0;
    }

    default boolean isStatic() {
        return (getAccessFlags() & STATIC) != 0;
    }

    default boolean isFinal() {
        return (getAccessFlags() & FINAL) != 0;
    }

    default boolean isSynchronized() {
        return (getAccessFlags() & SYNCHRONIZED) != 0;
    }

    default boolean isBridge() {
        return (getAccessFlags() & BRIDGE) != 0;
    }

    default boolean isVarargs() {
        return (getAccessFlags() & VARARGS) != 0;
    }

    default boolean isNative() {
        return (getAccessFlags() & NATIVE) != 0;
    }

    default boolean isAbstract() {
        return (getAccessFlags() & ABSTRACT) != 0;
    }

    default boolean isStrict() {
        return (getAccessFlags() & STRICT) != 0;
    }

    default boolean isSynthetic() {
        return (getAccessFlags() & SYNTHETIC) != 0;
    }

}
