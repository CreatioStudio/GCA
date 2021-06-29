package vip.creatio.gca.type;

import java.lang.reflect.MalformedParameterizedTypeException;

public class WildcardType implements Type {
    public static final WildcardType ANY = new WildcardType(Types.OBJECT, true);

    private Type bound;
    private final boolean upper;

    private WildcardType(Type b, boolean isUpper) {
        this.bound = b;
        this.upper = isUpper;
    }

    public static WildcardType makeUpper(Type upper) {
        return new WildcardType(upper, true);
    }

    public static WildcardType makeLower(Type lower) {
        return new WildcardType(lower, false);
    }

    /**
     * Returns an array of {@code Type} objects representing the  upper
     * bound(s) of this type variable.  If no upper bound is
     * explicitly declared, the upper bound is {@code Object}.
     *
     * <p>For each upper bound B :
     * <ul>
     *  <li>if B is a parameterized type or a type variable, it is created,
     *  (see {@link java.lang.reflect.ParameterizedType ParameterizedType}
     *  for the details of the creation process for parameterized types).
     *  <li>Otherwise, B is resolved.
     * </ul>
     *
     * @return an array of Types representing the upper bound(s) of this
     *     type variable
     * @throws TypeNotPresentException if any of the
     *     bounds refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if any of the
     *     bounds refer to a parameterized type that cannot be instantiated
     *     for any reason
     */
    public Type getUpperBound() {
        return upper ? bound : Types.OBJECT;
    }

    /**
     * Returns an array of {@code Type} objects representing the
     * lower bound(s) of this type variable.  If no lower bound is
     * explicitly declared, the lower bound is the type of {@code null}.
     * In this case, a zero length array is returned.
     *
     * <p>For each lower bound B :
     * <ul>
     *   <li>if B is a parameterized type or a type variable, it is created,
     *  (see {@link java.lang.reflect.ParameterizedType ParameterizedType}
     *  for the details of the creation process for parameterized types).
     *   <li>Otherwise, B is resolved.
     * </ul>
     *
     * @return an array of Types representing the lower bound(s) of this
     *     type variable
     * @throws TypeNotPresentException if any of the
     *     bounds refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if any of the
     *     bounds refer to a parameterized type that cannot be instantiated
     *     for any reason
     */
    public Type getLowerBound() {
        return upper ? Types.OBJECT : bound;
    }

    public Type getBound() {
        return bound;
    }

    @Override
    public String getTypeName() {
        return getBound().getTypeName();
    }

    @Override
    public String getInternalName() {
        if (upper) {
            if (!bound.equals(Types.OBJECT)) {
                return "+" + bound.getInternalName();
            } else
                return "*";
        } else {
            return "-" + bound.getInternalName();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (upper) {
            if (!bound.equals(Types.OBJECT)) {
                sb.append("? extends ");
            } else
                return "?";
        } else {
            sb.append("? super ");
        }

        sb.append(bound.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WildcardType) {
            WildcardType that = (WildcardType) o;
            return that.upper == this.upper && that.bound.equals(this.bound);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return bound.hashCode() ^ (upper ? 0x12343210 : 0x43210123);
    }
}
