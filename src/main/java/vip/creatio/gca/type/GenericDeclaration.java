package vip.creatio.gca.type;

import java.lang.reflect.GenericSignatureFormatError;

public interface GenericDeclaration extends AnnotatedInfo {
    /**
     * Returns an array of {@code TypeVariable} objects that
     * represent the type variables declared by the generic
     * declaration represented by this {@code GenericDeclaration}
     * object, in declaration order.  Returns an array of length 0 if
     * the underlying generic declaration declares no type variables.
     *
     * @return an array of {@code TypeVariable} objects that represent
     *     the type variables declared by this generic declaration
     * @throws GenericSignatureFormatError if the generic
     *     signature of this generic declaration does not conform to
     *     the format specified in
     *     <cite>The Java&trade; Virtual Machine Specification</cite>
     */
    TypeVariable[] getTypeParameters();

    default TypeVariable getTypeParameter(String name) {
        for (TypeVariable t : getTypeParameters()) {
            if (t.getTypeName().equals(name)) return t;
        }
        return null;
    }
}
