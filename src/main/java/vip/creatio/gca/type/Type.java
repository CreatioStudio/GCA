package vip.creatio.gca.type;

import vip.creatio.gca.util.ClassUtil;

public interface Type {

    //java.lang.String / [Ljava/lang/String; // java binary name which can be accepted by Class::forName
    String getName();

    //java.lang.String / java.lang.String[][] / int[]
    default String getCanonicalName() {
        return getName();
    }

    // might be a generic signature, abc.exm.Generic<abc.d.C> or [Labc/exm/Generic<Labc/d/C;>;
    default String getSignature() {
        return getName();
    }

    // always a raw type name, Ljava/lang/String; / [Ljava/lang/String;
    default String getInternalName() {
        return ClassUtil.toBytecodeName(ClassUtil.toSignature(getName()));
    }

    default String getInternalSignature() {
        return getInternalName();
    }

    default String getSimpleName() {
        String name = getCanonicalName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

}
