package vip.creatio.gca.type;

import static vip.creatio.gca.util.AccessFlags.*;

public interface DeclaredFieldInfo extends FieldInfo, AnnotatedInfo {

    Type getGenericType();

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

    default boolean isVolatile() {
        return (getAccessFlags() & VOLATILE) != 0;
    }

    default boolean isTransient() {
        return (getAccessFlags() & TRANSIENT) != 0;
    }

    default boolean isSynthetic() {
        return (getAccessFlags() & SYNTHETIC) != 0;
    }

    default boolean isEnum() {
        return (getAccessFlags() & ENUM) != 0;
    }
}
