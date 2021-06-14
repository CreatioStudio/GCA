package vip.creatio.gca.type;

import vip.creatio.gca.ConstType;
import vip.creatio.gca.TypeInfo;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;

public class FieldObjectInfo implements DeclaredFieldInfo {

    private final TypeFactory factory;
    private final Field field;
    private final TypeInfo decl;
    private final TypeInfo type;

    // caches
    private SoftReference<AnnotationInfo[]> annotations;
    private SoftReference<Type>             genericType;

    public FieldObjectInfo(TypeFactory factory, Field field) {
        this.factory = factory;
        this.decl = factory.toType(field.getDeclaringClass());
        this.type = factory.toType(field.getType());
        this.field = field;
    }

    @Override
    public TypeInfo getDeclaringClass() {
        return decl;
    }

    @Override
    public TypeInfo getType() {
        return type;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    public Field getField() {
        return field;
    }

    @Override
    public AnnotationInfo[] getAnnotations() {
        AnnotationInfo[] result = annotations == null ? null : annotations.get();
        if (result == null) {
            Annotation[] annos = field.getAnnotations();
            result = new AnnotationInfo[annos.length];
            for (int i = 0; i < annos.length; i++) {
                result[i] = new AnnotationObjectInfo(factory.toType(annos[i].annotationType()), annos[i]);
            }
            annotations = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public Type getGenericType() {
        Type result = genericType == null ? null : genericType.get();
        if (result == null) {
            result = factory.toGeneric(field.getGenericType());
            genericType = new SoftReference<>(result);
        }
        return result;
    }
}
