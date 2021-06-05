package vip.creatio.gca.type;

import java.lang.reflect.MalformedParameterizedTypeException;

public class ArrayType implements Type {

    private Type type;

    private ArrayType(Type t) {
        this.type = t;
    }

    public static ArrayType make(Type t) {
        return new ArrayType(t);
    }

    public static ArrayType makeMutable(Type t) {
        return new Mutable(t);
    }

    /**
     * Returns a {@code Type} object representing the component type
     * of this array. This method creates the component type of the
     * array.  See the declaration of {@link
     * java.lang.reflect.ParameterizedType ParameterizedType} for the
     * semantics of the creation process for parameterized types and
     * see {@link java.lang.reflect.TypeVariable TypeVariable} for the
     * creation process for type variables.
     *
     * @return  a {@code Type} object representing the component type
     *     of this array
     * @throws TypeNotPresentException if the underlying array type's
     *     component type refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if  the
     *     underlying array type's component type refers to a
     *     parameterized type that cannot be instantiated for any reason
     */
    Type getComponentType() {
        return type;
    }

    @Override
    public String getTypeName() {
        return getInternalName();
    }

    @Override
    public String getSignature() {
        return '[' + type.getSignature();
    }

    @Override
    public String getCanonicalName() {
        return type.getCanonicalName() + "[]";
    }

    @Override
    public String getInternalName() {
        return '[' + type.getInternalName();
    }

    public static class Mutable extends ArrayType {
        private Mutable(Type t) {
            super(t);
        }

        public void setComponentType(Type type) {
            super.type = type;
        }

        @Override
        public boolean mutable() {
            return true;
        }
    }
}
