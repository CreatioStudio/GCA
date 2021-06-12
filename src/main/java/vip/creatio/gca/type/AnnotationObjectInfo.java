package vip.creatio.gca.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class AnnotationObjectInfo implements AnnotationInfo {

    private final Type type;
    private final Annotation anno;
    private Map<String, Object> cachedObjects;

    AnnotationObjectInfo(Type t, Annotation anno) {
        this.type = t;
        this.anno = anno;
        try {
            cachedObjects = new HashMap<>();
            for (Method mth : anno.annotationType().getMethods()) {
                if (!Modifier.isStatic(mth.getModifiers())) {
                    cachedObjects.put(mth.getName(), mth.invoke(anno));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Type annotationType() {
        return type;
    }

    @Override
    public Object getValue(String name) {
        return cachedObjects.get(name);
    }

    public Annotation getAnnotation() {
        return anno;
    }
}
