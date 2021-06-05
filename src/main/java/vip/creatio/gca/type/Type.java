package vip.creatio.gca.type;

import vip.creatio.gca.util.ClassUtil;

public interface Type {

    //java.lang.String / [Ljava/lang/String; // java binary name which can be accepted by Class::forName
    String getTypeName();

    //java.lang.String / java.lang.String[][] / int[]
    default String getCanonicalName() {
        return getTypeName();
    }

    // might be a generic signature, abc.exm.Generic<abc.d.C> or [Labc/exm/Generic<Labc/d/C;>;
    default String getSignature() {
        return getTypeName();
    }

    // always a raw type name, Ljava/lang/String; / [Ljava/lang/String;
    default String getInternalName() {
        return ClassUtil.toSignature(getTypeName());
    }

    default String getInternalSignature() {
        return ClassUtil.toSignature(getSignature());
    }

    default boolean mutable() {
        return false;
    }

    default String getSimpleName() {
        String name = getCanonicalName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

}
