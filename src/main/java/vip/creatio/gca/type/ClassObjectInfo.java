package vip.creatio.gca.type;

import java.lang.annotation.Annotation;

//TODO
public class ClassObjectInfo extends ClassInfo {

    private final Class<?> clazz;

    // cachable
    private AnnotationInfo[] annotations;
    private MethodInfo[] methods;
    private FieldInfo[] fields;

    protected ClassObjectInfo(Class<?> clazz) {
        this.clazz = clazz;
        super.name = clazz.getName();
    }

    public static ClassObjectInfo make(Class<?> clazz) {
        return new ClassObjectInfo(clazz);
    }

    @Override
    public AnnotationInfo[] getAnnotations() {
        if (annotations == null) {
            Annotation[] annos = clazz.getAnnotations();
            annotations = new AnnotationInfo[annos.length];
            for (int i = 0; i < annos.length; i++) {
                annotations[i] = new AnnotationObjectInfo(this, annos[i]);
            }
        }
        return annotations;
    }

    public Class<?> getClassObject() {
        return clazz;
    }

    @Override
    public String getTypeName() {
        return clazz.getName();
    }

    @Override
    public String getCanonicalName() {
        return clazz.getCanonicalName();
    }

    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public FieldInfo[] getFields() {
        if (fields == null) {

        }
        return null;
    }

    @Override
    public MethodInfo[] getMethods() {
        return super.getMethods();
    }

    @Override
    public MethodInfo getEnclosingMethod() {
        if (methods == null);
        return null;
    }

    @Override
    public Type getDeclaringClass() {
        return null;
    }

    @Override
    public Type[] getClasses() {
        return new Type[0];
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        return new TypeVariable[0];
    }
}
