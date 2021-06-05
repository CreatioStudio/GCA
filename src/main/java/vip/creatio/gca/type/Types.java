package vip.creatio.gca.type;

// util class
public final class Types {

    //TODO
    public static int operandSize(String type) {
        if (type.equals("double") || type.equals("long")) return 2;
        else if (type.equals("void")) return 0;
        return 1;
    }

    public static TypeInfo toType(String signature) {
        //TODO
    }

    public static TypeInfo toType(Class<?> cls) {

    }

    public static TypeInfo toType(Type type) {
        if (type instanceof TypeInfo) {
            return (TypeInfo) type;
        } else if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getRawType();
        } else {
            return toType(type.getTypeName());
        }
    }

    public static Type resolve(String generic) {

    }

}
