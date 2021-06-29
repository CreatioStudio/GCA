package vip.creatio.gca.type;

import vip.creatio.gca.util.ClassUtil;

public interface Type {

    // java.lang.String / [Ljava/lang/String; java binary name which can be accepted by Class::forName
    String getTypeName();

    //java.lang.String / java.lang.String[][] / int[]
    //default String getCanonicalName() {
    //    return getName();
    //}

    // might be a generic signature, abc.exm.Gener ic<abc.d.C> or [Labc/exm/Generic<Labc/d/C;>;
//    default String getSignature() {
//        return getName();
//    }

    // Ljava/lang/String; / [Ljava/lang/String; / TGenericName; / [Ljava/util/function/Consumer<Ljava/lang/String;>;
    default String getInternalName() {
        return Types.toBytecodeName(ClassUtil.toSignature(getTypeName()));
    }

//    default String getInternalSignature() {
//        return getInternalName();
//    }
//
//    default String getSimpleName() {
//        String name = getCanonicalName();
//        return name.substring(name.lastIndexOf('.') + 1);
//    }

    //java.lang.String, java.util.function.Consumer<? extends java.lang.Integer>[]
    String toString();

}
