package vip.creatio.gca.util;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    public static String toBytecodeName(String binaryName) {
        return binaryName.replace('.', '/');
    }

    public static String toBytecodeName(Class<?> cls) {
        return toBytecodeName(cls.getName());
    }

    /**
     *
     * Node: the returned primitive types, such as int and byte,
     * cannot obtain its primitive class using Class.forName
     * Make a map for conversion!
     *
     * I -> int
     * J -> long
     * [[I -> [[I
     * java/lang/String -> java.lang.String
     * [Ljava/lang/String; -> [Ljava.lang.String;
     */
    public static String toBinaryName(String bytecodeName) {
        char[] chars = bytecodeName.toCharArray();
        // array
        if (chars[0] == '[') return bytecodeName.replace('/', '.');
        if (chars.length == 1) {
            switch (chars[0]) {
                case 'I':
                    return "int";
                case 'C':
                    return "char";
                case 'S':
                    return "short";
                case 'Z':
                    return "boolean";
                case 'F':
                    return "float";
                case 'D':
                    return "double";
                case 'V':
                    return "void";
                case 'J':
                    return "long";
                case 'B':
                    return "byte";
                default:
                    throw new RuntimeException("Unknown token: " + chars[0]);
            }
        } else if (chars[chars.length - 1] == ';') {
            if (chars[0] == 'L') {
                return new String(chars, 1, chars.length - 2)
                        .replace('/', '.');
            } else {
                throw new RuntimeException("Invalid name: " + bytecodeName);
            }
        } else {
            return bytecodeName.replace('/', '.');
        }
    }

    public static String readBytecodeName(final char[] chars, final int index, int[] length) {
        if (length == null) length = new int[1];
        int dim = 0;
        while (chars[index + dim] == '[') ++dim;
        int ptr = index + dim;
        switch (chars[ptr]) {
            case 'I':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "int";
                else
                    return "[".repeat(dim) + "I";
            case 'C':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "char";
                else
                    return "[".repeat(dim) + "C";
            case 'S':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "short";
                else
                    return "[".repeat(dim) + "S";
            case 'Z':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "boolean";
                else
                    return "[".repeat(dim) + "Z";
            case 'F':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "float";
                else
                    return "[".repeat(dim) + "F";
            case 'D':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "double";
                else
                    return "[".repeat(dim) + "D";
            case 'V':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "void";
                else
                    return "[".repeat(dim) + "V";
            case 'J':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "long";
                else
                    return "[".repeat(dim) + "J";
            case 'B':
                length[0] = 1 + dim;
                if (dim == 0)
                    return "byte";
                else
                    return "[".repeat(dim) + "B";
            case 'L':
                while (chars[++ptr] != ';');
                ptr++;
                length[0] = ptr - index;
                return new String(chars, index, ptr - index);
            default:
                throw new RuntimeException("Unknown token: '" + chars[0] + "' @ " + index + " in " + new String(chars));
        }
    }

    public static String getSignature(Class<?> rType, Class<?>... pType) {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> c : pType) {
            sb.append(toSignature(c.getName()));
        }
        sb.append(')');
        return sb.append(toSignature(rType.getName())).toString().replace('.', '/');
    }

    public static String getSignature(String rtype, String... ptype) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 1; i < ptype.length; i++) {
            sb.append(toSignature(ptype[i]));
        }
        sb.append(')');
        return sb.append(toSignature(rtype)).toString().replace('.', '/');
    }

    public static String getSignature(String... signatures) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 1; i < signatures.length; i++) {
            sb.append(toSignature(signatures[i]));
        }
        sb.append(')');
        return sb.append(toSignature(signatures[0])).toString().replace('.', '/');
    }

    public static String getSignature(String sig) {
        return toSignature(sig);
    }

    public static String getSignature(Method mth) {
        return getSignature(mth.getReturnType(), mth.getParameterTypes());
    }

    public static String getSignature(Field field) {
        return toSignature(field.getType().getName());
    }

    public static String getSignature(MethodType type) {
        return getSignature(type.returnType(), type.parameterArray());
    }

    /**
     * Convert bytecode signature(field or method) to java class name
     *
     * For field, the returned array will always have one element
     * For method, the first element of array will be the return type
     * of method, then the resting will be parameter types
     *
     * Node: the returned primitive types, such as int and byte,
     * cannot obtain its primitive class using Class.forName
     * Make a map for conversion!
     *
     * (IIBIB)V -> [void, int, int, byte, int, byte]
     * I -> [int]
     * Ljava/lang/Object; -> [java.lang.Object]
     * Ljava.lang.String; -> [java.lang.String]
     * (I[Ljava/lang/String;[I)Z -> [boolean, int, [Ljava.lang.String;, [I]
     */
    public static String[] fromSignature(String signature) {
        List<String> item = new ArrayList<>();
        char[] arr = signature.toCharArray();
        if (arr[0] == '(') {
            int index = 1;
            int[] len = new int[1];
            while (arr[index] != ')') {
                item.add(toBinaryName(readBytecodeName(arr, index, len)));
                index += len[0];
            }
            index++;
            item.add(0, toBinaryName(readBytecodeName(arr, index, len)));
        } else {
            item.add(toBinaryName(readBytecodeName(arr, 0, null)));
        }
        return item.toArray(new String[0]);
    }

    public static String toSignature(String binaryName) {
        if (binaryName.startsWith("[")) return binaryName;
        char c = toBasicChar(binaryName);
        if (c == 'L') return c + binaryName + ';';
        else return "" + c;
    }

    public static char toBasicChar(String t) {
        switch (t) {
            case "int":
                return 'I';
            case "char":
                return 'C';
            case "short":
                return 'S';
            case "boolean":
                return 'Z';
            case "float":
                return 'F';
            case "double":
                return 'D';
            case "void":
                return 'V';
            case "long":
                return 'J';
            case "byte":
                return 'B';
            default:
                return 'L';
        }
    }

    public static Class<?> toPrimitiveClass(char c) {
        switch (c) {
            case 'I':
                return int.class;
            case 'C':
                return char.class;
            case 'S':
                return short.class;
            case 'Z':
                return boolean.class;
            case 'F':
                return float.class;
            case 'D':
                return double.class;
            case 'V':
                return void.class;
            case 'J':
                return long.class;
            case 'B':
                return byte.class;
            default:
                throw new RuntimeException("Unknown token: " + c);
        }
    }
}
