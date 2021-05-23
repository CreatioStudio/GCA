package vip.creatio.gca;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public interface AccessFlagContainer {

    EnumSet<AccessFlag> getAccessFlags();

    void setAccessFlags(Collection<AccessFlag> flags);

    default void setAccessFlags(AccessFlag... flags) {
        setAccessFlags(Arrays.asList(flags));
    }

    default void addAccessFlags(AccessFlag... flags) {
        getAccessFlags().addAll(Arrays.asList(flags));
    }

    default void removeAccessFlags(AccessFlag... flags) {
        getAccessFlags().retainAll(Arrays.asList(flags));
    }

    default boolean flaggedAllOf(AccessFlag... flags) {
        return getAccessFlags().containsAll(Arrays.asList(flags));
    }

    default boolean flaggedAnyOf(AccessFlag... flags) {
        for (AccessFlag f : getAccessFlags()) {
            for (AccessFlag flag : flags) {
                if (f == flag) return true;
            }
        }
        return false;
    }

    default boolean flaggedNoneOf(AccessFlag... flags) {
        return !flaggedAnyOf(flags);
    }

    default boolean flaggedPublic() {
        return getAccessFlags().contains(AccessFlag.PUBLIC);
    }

    default boolean flaggedPrivate() {
        return getAccessFlags().contains(AccessFlag.PRIVATE);
    }

    default boolean flaggedProtected() {
        return getAccessFlags().contains(AccessFlag.PROTECTED);
    }

    default boolean flaggedStatic() {
        return getAccessFlags().contains(AccessFlag.STATIC);
    }

    default boolean flaggedFinal() {
        return getAccessFlags().contains(AccessFlag.FINAL);
    }

    default boolean flaggedSuper() {
        return getAccessFlags().contains(AccessFlag.SUPER);
    }

    default boolean flaggedSynchronized() {
        return getAccessFlags().contains(AccessFlag.SYNCHRONIZED);
    }

    default boolean flaggedVolatile() {
        return getAccessFlags().contains(AccessFlag.VOLATILE);
    }

    default boolean flaggedBridge() {
        return getAccessFlags().contains(AccessFlag.BRIDGE);
    }

    default boolean flaggedTransient() {
        return getAccessFlags().contains(AccessFlag.TRANSIENT);
    }

    default boolean flaggedVarargs() {
        return getAccessFlags().contains(AccessFlag.VARARGS);
    }

    default boolean flaggedNative() {
        return getAccessFlags().contains(AccessFlag.NATIVE);
    }

    default boolean flaggedInterface() {
        return getAccessFlags().contains(AccessFlag.INTERFACE);
    }

    default boolean flaggedAbstract() {
        return getAccessFlags().contains(AccessFlag.ABSTRACT);
    }

    default boolean flaggedStrict() {
        return getAccessFlags().contains(AccessFlag.STRICT);
    }

    default boolean flaggedSynthetic() {
        return getAccessFlags().contains(AccessFlag.SYNTHETIC);
    }

    default boolean flaggedAnnotation() {
        return getAccessFlags().contains(AccessFlag.ANNOTATION);
    }

    default boolean flaggedEnum() {
        return getAccessFlags().contains(AccessFlag.ENUM);
    }
}
