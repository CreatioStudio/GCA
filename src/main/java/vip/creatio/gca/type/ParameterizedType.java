package vip.creatio.gca.type;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

public class ParameterizedType implements Type {
    private List<Type> typeArguments;
    private TypeInfo   rawType;
    private Type       ownerType;

    private ParameterizedType(TypeInfo rawType,
                              Collection<Type> typeArguments,
                              Type ownerType) {
        this.typeArguments = new ArrayList<>(typeArguments);
        this.rawType             = rawType;
        this.ownerType = ownerType;
    }

    public static ParameterizedType make(TypeInfo rawType,
                                         Collection<Type> typeArguments,
                                         Type ownerType) {
        return new ParameterizedType(rawType, typeArguments, ownerType);
    }

    public static ParameterizedType make(TypeInfo rawType,
                                         Type[] typeArguments,
                                         Type ownerType) {
        return new ParameterizedType(rawType, Arrays.asList(typeArguments),
                ownerType);
    }

    public static ParameterizedType makeMutable(TypeInfo rawType,
                                         Collection<Type> typeArguments,
                                         Type ownerType) {
        return new Mutable(rawType, typeArguments, ownerType);
    }

    public static ParameterizedType makeMutable(TypeInfo rawType,
                                         Type[] typeArguments,
                                         Type ownerType) {
        return new Mutable(rawType, Arrays.asList(typeArguments),
                ownerType);
    }

    /**
     * Returns an array of {@code Type} objects representing the actual type
     * arguments to this type.
     *
     * <p>Note that in some cases, the returned array be empty. This can occur
     * if this type represents a non-parameterized type nested within
     * a parameterized type.
     *
     * @return an array of {@code Type} objects representing the actual type
     *     arguments to this type
     * @throws TypeNotPresentException if any of the
     *     actual type arguments refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if any of the
     *     actual type parameters refer to a parameterized type that cannot
     *     be instantiated for any reason
     * @since 1.5
     */
    public Type[] getTypeArguments() {
        return typeArguments.toArray(new Type[0]);
    }

    /**
     * Returns the {@code Type} object representing the class or interface
     * that declared this type.
     *
     * @return the {@code Type} object representing the class or interface
     *     that declared this type
     * @since 1.5
     */
    public TypeInfo getRawType() {
        return rawType;
    }

    /**
     * Returns a {@code Type} object representing the type that this type
     * is a member of.  For example, if this type is {@code O<T>.I<S>},
     * return a representation of {@code O<T>}.
     *
     * <p>If this type is a top-level type, {@code null} is returned.
     *
     * @return a {@code Type} object representing the type that
     *     this type is a member of. If this type is a top-level type,
     *     {@code null} is returned
     * @throws TypeNotPresentException if the owner type
     *     refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if the owner type
     *     refers to a parameterized type that cannot be instantiated
     *     for any reason
     * @since 1.5
     */
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public String getTypeName() {
        return rawType.getTypeName();
    }

    @Override
    public String getSignature() {
        return toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (ownerType != null) {
            sb.append(ownerType.getCanonicalName());

            sb.append("$");

            if (ownerType instanceof ParameterizedType) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.getCanonicalName().replace( ((ParameterizedType) ownerType).rawType.getCanonicalName() + "$",
                        ""));
            } else
                sb.append(rawType.getSimpleName());
        } else
            sb.append(rawType.getCanonicalName());

        if (typeArguments != null) {
            StringJoiner sj = new StringJoiner(", ", "<", ">");
            sj.setEmptyValue("");
            for(Type t: typeArguments) {
                sj.add(t.getCanonicalName());
            }
            sb.append(sj);
        }

        return sb.toString();
    }

    public static class Mutable extends ParameterizedType {

        private Mutable(TypeInfo rawType, Collection<Type> actualTypeArguments, Type ownerType) {
            super(rawType, actualTypeArguments, ownerType);
        }

        public void setTypeArguments(Collection<Type> typeArguments) {
            super.typeArguments = new ArrayList<>(typeArguments);
        }

        public void setRawType(TypeInfo t) {
            super.rawType = t;
        }

        public void setOwnerType(Type t) {
            super.ownerType = t;
        }

        @Override
        public boolean mutable() {
            return true;
        }
    }
}
