package vip.creatio.gca.type;

import vip.creatio.gca.ConstType;
import vip.creatio.gca.TypeInfo;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodObjectInfo implements DeclaredMethodInfo {

    private final TypeFactory   factory;
    private final Executable    method;
    private final TypeInfo      decl;
    private final TypeInfo      returnType;

    // caches
    private SoftReference<TypeInfo[]>           parameterTypes;
    private SoftReference<TypeInfo[]>           exceptionTypes;
    private SoftReference<AnnotationInfo[]>     annotations;
    private SoftReference<AnnotationInfo[][]>   parameterAnnotations;
    private SoftReference<Type>                 genericReturnType;
    private SoftReference<Type[]>               genericParameterTypes;
    private SoftReference<Type[]>               genericExceptionTypes;

    public MethodObjectInfo(TypeFactory factory, Executable method) {
        this.factory = factory;
        this.decl = factory.toType(method.getDeclaringClass());
        this.returnType = method instanceof Method
                ? factory.toType(((Method) method).getReturnType())
                : factory.toType(method.getDeclaringClass());
        this.method = method;

    }

    @Override
    public TypeInfo getDeclaringClass() {
        return decl;
    }

    @Override
    public TypeInfo getReturnType() {
        return returnType;
    }

    @Override
    public TypeInfo[] getParameterTypes() {
        TypeInfo[] result = parameterTypes == null ? null : parameterTypes.get();
        if (result == null) {
            result = factory.toType(method.getParameterTypes());
            parameterTypes = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    public Executable getMethod() {
        return method;
    }

    @Override
    public AnnotationInfo[] getAnnotations() {
        AnnotationInfo[] result = annotations == null ? null : annotations.get();
        if (result == null) {
            Annotation[] annos = method.getAnnotations();
            result = new AnnotationInfo[annos.length];
            for (int i = 0; i < annos.length; i++) {
                result[i] = new AnnotationObjectInfo(factory.toType(annos[i].annotationType()), annos[i]);
            }
            annotations = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public TypeInfo[] getExceptionTypes() {
        TypeInfo[] result = exceptionTypes == null ? null : exceptionTypes.get();
        if (result == null) {
            result = factory.toType(method.getExceptionTypes());
            exceptionTypes = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public Type[] getGenericExceptionTypes() {
        Type[] result = genericExceptionTypes == null ? null : genericExceptionTypes.get();
        if (result == null) {
            result = factory.toGeneric(method.getGenericExceptionTypes());
            genericExceptionTypes = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public Type[] getGenericParameterTypes() {
        Type[] result = genericParameterTypes == null ? null : genericParameterTypes.get();
        if (result == null) {
            result = factory.toGeneric(method.getGenericParameterTypes());
            genericParameterTypes = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public Type getGenericReturnType() {
        Type result = genericReturnType == null ? null : genericReturnType.get();
        if (result == null) {
            result = method instanceof Method
                    ? factory.toGeneric(((Method) method).getGenericReturnType())
                    : returnType;
            genericReturnType = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        //TODO
        return new TypeVariable[0];
    }

    @Override
    public AnnotationInfo[][] getParameterAnnotations() {
        AnnotationInfo[][] result = parameterAnnotations == null ? null : parameterAnnotations.get();
        if (result == null) {
            Annotation[][] annos = method.getParameterAnnotations();
            result = new AnnotationInfo[annos.length][];
            for (int i = 0; i < annos.length; i++) {
                result[i] = new AnnotationInfo[annos[i].length];
                for (int j = 0; j < annos[i].length; j++) {
                    result[i][j] = new AnnotationObjectInfo(factory.toType(annos[i][j].annotationType()), annos[i][j]);
                }
            }
            parameterAnnotations = new SoftReference<>(result);
        }
        return result;
    }

    @Override
    public String getDescriptor() {
        return Types.toMethodSignature(getReturnType(), getParameterTypes());
    }

    // access flags


    @Override
    public ConstType constantType() {
        return Modifier.isInterface(method.getModifiers()) ? ConstType.INTERFACE_METHODREF : ConstType.METHODREF;
    }

    @Override
    public int getAccessFlags() {
        return method.getModifiers();
    }
}
