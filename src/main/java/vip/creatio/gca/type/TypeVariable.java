package vip.creatio.gca.type;

import vip.creatio.gca.TypeAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypeVariable implements Type, AnnotatedInfo {

    private Type[] bounds;
    private GenericDeclaration decl;
    private String name;
    //TODO:
    private List<TypeAnnotation> annotations = new ArrayList<>();

    private TypeVariable(GenericDeclaration decl, String name, Type[] bounds) {
        this.bounds = bounds;
        this.name = name;
        this.decl = decl;
    }

    public static TypeVariable make(GenericDeclaration decl, String name, Collection<Type> bounds) {
        return new TypeVariable(decl, name, bounds.toArray(new Type[0]));
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
    public String getName() {
        return name;
    }
}
