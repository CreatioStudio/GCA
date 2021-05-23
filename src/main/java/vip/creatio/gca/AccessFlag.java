package vip.creatio.gca;

import java.util.EnumSet;

public enum AccessFlag {
    PUBLIC      (0x0001, 0b1111, "public"),         // class, inner, field, method
    PRIVATE     (0x0002, 0b0111, "private"),        //        inner, field, method
    PROTECTED   (0x0004, 0b0111, "protected"),      //        inner, field, method
    STATIC      (0x0008, 0b0111, "static"),         //        inner, field, method
    FINAL       (0x0010, 0b1111, "final"),          // class, inner, field, method
    SUPER       (0x0020, 0b1000, "super"),          // class
    SYNCHRONIZED(0x0020, 0b0010, "synchronized"),   //                      method
    VOLATILE    (0x0040, 0b0100, "volatile"),       //               field
    BRIDGE      (0x0040, 0b0010, "/* bridge */"),   //                      method
    TRANSIENT   (0x0080, 0b0100, "transient"),      //               field
    VARARGS     (0x0080, 0b0010, "/* varargs */"),  //                      method
    NATIVE      (0x0100, 0b0010, "native"),         //                      method
    INTERFACE   (0x0200, 0b1001, "interface"),      // class, inner
    ABSTRACT    (0x0400, 0b1011, "abstract"),       // class, inner,        method
    STRICT      (0x0800, 0b0010, "strictfp"),       //                      method
    SYNTHETIC   (0x1000, 0b1111, "/* synthetic */"),// class, inner, field, method
    ANNOTATION  (0x2000, 0b1001, "@interface"),     // class, inner
    ENUM        (0x4000, 0b1101, "enum"),           // class, inner, field
    MANDATED    (0x8000, 0b0010, "/* mandated */"), //                      method parameter
    MODULE      (0x8000, 0b1000, "module");         // class

    final int mask;
    final byte flag;        // 1st bit - for class, 2nd - for field, 3rd - for method, 4th - for inner class
    final String string;

    AccessFlag(int mask, int flag, String string) {
        this.mask = mask;
        this.flag = (byte) flag;
        this.string = string;
    }

    public static EnumSet<AccessFlag> resolveField(short flag) {
        return resolve(flag, 0b0100);
    }

    public static EnumSet<AccessFlag> resolveClass(short flag) {
        return resolve(flag, 0b1000);
    }

    public static EnumSet<AccessFlag> resolveMethod(short flag) {
        return resolve(flag, 0b0010);
    }

    public static EnumSet<AccessFlag> resolveInnerClass(short flag) {
        return resolve(flag, 0b0001);
    }

    public static short serialize(EnumSet<AccessFlag> flags) {
        int stack = 0;
        for (AccessFlag flag : flags) {
            stack |= flag.mask;
        }
        return (short) stack;
    }

    private static EnumSet<AccessFlag> resolve(short flag, int restrict) {
        EnumSet<AccessFlag> set = EnumSet.noneOf(AccessFlag.class);
        for (AccessFlag flags : values()) {
            if ((flags.flag & restrict) != 0 && (flag & flags.mask) == flags.mask)
                set.add(flags);
        }
        return set;
    }

    public static String toString(EnumSet<AccessFlag> flags) {
        StringBuilder builder = new StringBuilder();
        for (AccessFlag flag : flags) {
            builder.append(flag.string).append(' ');
        }
        return builder.toString().trim();
    }
}
