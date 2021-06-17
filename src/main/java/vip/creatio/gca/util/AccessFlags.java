package vip.creatio.gca.util;

import java.lang.reflect.Modifier;

public final class AccessFlags {
    
    public static final int PUBLIC =            0x0001;      // class, inner, field, method
    public static final int PRIVATE =           0x0002;      //        inner, field, method
    public static final int PROTECTED =         0x0004;      //        inner, field, method
    public static final int STATIC =            0x0008;      //        inner, field, method
    public static final int FINAL =             0x0010;      // class, inner, field, method
    public static final int SUPER =             0x0020;      // class
    public static final int SYNCHRONIZED =      0x0020;      //                      method
    public static final int VOLATILE =          0x0040;      //               field
    public static final int BRIDGE =            0x0040;      //                      method
    public static final int TRANSIENT =         0x0080;      //               field
    public static final int VARARGS =           0x0080;      //                      method
    public static final int NATIVE =            0x0100;      //                      method
    public static final int INTERFACE =         0x0200;      // class, inner
    public static final int ABSTRACT =          0x0400;      // class, inner,        method
    public static final int STRICT =            0x0800;      //                      method
    public static final int SYNTHETIC =         0x1000;      // class, inner, field, method
    public static final int ANNOTATION =        0x2000;      // class, inner
    public static final int ENUM =              0x4000;      // class, inner, field
    public static final int MANDATED =          0x8000;      //                      method parameter
    public static final int MODULE =            0x8000;      // class

    public static int classModifiers() {
        return Modifier.classModifiers();
    }

    public static int interfaceModifiers() {
        return Modifier.interfaceModifiers();
    }

    public static int constructorModifiers() {
        return Modifier.constructorModifiers();
    }

    public static int methodModifiers() {
        return Modifier.methodModifiers();
    }

    public static int fieldModifiers() {
        return Modifier.fieldModifiers();
    }

    public static int parameterModifiers() {
        return Modifier.parameterModifiers();
    }
}
