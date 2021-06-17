package vip.creatio.gca.type;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

public class WildcardType implements Type {
    private Type[] upperBounds;
    private Type[] lowerBounds;

    private WildcardType(Type[] upper, Type[] lower) {
        this.upperBounds = upper;
        this.lowerBounds = lower;
    }

    public static WildcardType make(Collection<Type> upper, Collection<Type> lower) {
        return new WildcardType(upper.toArray(new Type[0]), lower.toArray(new Type[0]));
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
    public Type[] getUpperBounds() {
        return Arrays.copyOf(upperBounds, upperBounds.length);
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
    public Type[] getLowerBounds() {
        return Arrays.copyOf(lowerBounds, lowerBounds.length);
    }

    @Override
    public String getName() {
        return "java.lang.Object";
    }

    @Override
    public String getSignature() {
        return toString();
    }

    @Override
    public String getInternalSignature() {
        return Type.super.getInternalSignature();
    }

    @Override
    public String toString() {
        Type[] lowerBounds = getLowerBounds();
        Type[] bounds = lowerBounds;
        StringBuilder sb = new StringBuilder();

        if (lowerBounds.length > 0)
            sb.append("? super ");
        else {
            Type[] upperBounds = getUpperBounds();
            if (upperBounds.length > 0 && !upperBounds[0].getCanonicalName().equals("java.lang.Object") ) {
                bounds = upperBounds;
                sb.append("? extends ");
            } else
                return "?";
        }

        StringJoiner sj = new StringJoiner(" & ");
        for(Type bound: bounds) {
            sj.add(bound.getCanonicalName());
        }
        sb.append(sj);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WildcardType) {
            WildcardType that = (WildcardType) o;
            return
                    Arrays.equals(this.getLowerBounds(),
                            that.getLowerBounds()) &&
                            Arrays.equals(this.getUpperBounds(),
                                    that.getUpperBounds());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        Type[] lowerBounds = getLowerBounds();
        Type[] upperBounds = getUpperBounds();

        return Arrays.hashCode(lowerBounds) ^ Arrays.hashCode(upperBounds);
    }
}
