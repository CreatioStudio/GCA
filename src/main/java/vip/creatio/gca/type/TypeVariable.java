package vip.creatio.gca.type;

import vip.creatio.gca.TypeAnnotation;

import java.util.*;

public class TypeVariable implements Type, AnnotatedInfo {

    private Type[] bounds;
    private GenericDeclaration decl;
    private String name;
    //TODO:
    private final List<TypeAnnotation> annotations = new ArrayList<>();

    private TypeVariable(GenericDeclaration decl, String name, Type[] bounds) {
        this.bounds = bounds;
        this.name = name;
        this.decl = decl;
    }

    public static TypeVariable make(GenericDeclaration decl, String name, Collection<Type> bounds) {
        return new TypeVariable(decl, name, bounds.toArray(new Type[0]));
    }

    public static TypeVariable make(GenericDeclaration decl, String name, Type bound) {
        return new TypeVariable(decl, name, new Type[]{bound});
    }

    public Type[] getBounds() {
        return Arrays.copyOf(bounds, bounds.length);
    }

    public GenericDeclaration getGenericDeclaration() {
        return decl;
    }

    @Override
    public TypeAnnotation[] getAnnotations() {
        return annotations.toArray(new TypeAnnotation[0]);
    }

    @Override
    public String getTypeName() {
        if (bounds.length == 0) return Types.OBJECT.getTypeName();
        return bounds[0].getTypeName();     // first bound can be a non-interface class
    }

    public String getName() {
        return name;
    }

    @Override
    public String getInternalName() {
        return "T" + name + ";";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (bounds.length != 0) {
            StringJoiner sj = new StringJoiner(" & ");
            for (Type bound : bounds) {
                sj.add(bound.toString());
            }
            sb.append(sj);
        }
        return sb.toString();
    }
}
