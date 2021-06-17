package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.attr.*;

import vip.creatio.gca.type.*;
import vip.creatio.gca.util.ClassUtil;
import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.Util;

import java.util.*;

public class DeclaredMethod extends DeclaredObject implements DeclaredMethodInfo {

    private TypeInfo returnType;
    private TypeInfo[] parameterTypes;

    private Map<TypeInfo, Annotation>[] parameterAnnotations;

    private List<TypeInfo> exceptions;

    private String cachedDescriptor;
    
    DeclaredMethod(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
    }

    DeclaredMethod(ClassFile bc,
                   int flags,
                   String name,
                   TypeInfo rtype,
                   TypeInfo[] ptype,
                   Attribute... attributes) {
        super(bc, flags, name, attributes);
        this.returnType = rtype;
        this.parameterTypes = ptype;
    }

    public boolean hasCode() {
        return !isAbstract() || !isNative();
    }

    public @NotNull Code code() {
        if (!hasCode()) {
            throw new RuntimeException("Abstract method does not have Code attribute! " +
                    "check if it has code using hasCode() before getting it");
        }
        return getOrAddAttribute("Code",
                () -> new Code(this));
    }

    public @NotNull AnnotationDefault defaultValue() {
        if (!hasCode()) {
            throw new RuntimeException("Only abstract method of an annotation can set this");
        }
        return getOrAddAttribute("AnnotationDefault", () -> new AnnotationDefault(this));
    }

    public String toString() {
        return "Method{name=" + getName() + ",descriptor=" + getDescriptor() + '}';
    }

    @Override
    public String getDescriptor() {
        if (cachedDescriptor == null) {
            cachedDescriptor = Types.toMethodSignature(Util.concat(TypeInfo[]::new, returnType, parameterTypes));
        }
        return cachedDescriptor;
    }

    @Override
    void setDescriptor(String s) {
        //TODO
        String[] str = ClassUtil.fromSignature(s);
        Repository repo = classFile().repository();
        this.returnType = repo.getType(str[0]);
        this.parameterTypes = repo.toType(Arrays.copyOfRange(str, 1, str.length));
//        Type[] signatures = classFile().repository().resolveGenericMethodDescriptor(s);
//        this.returnType = classFile().repository().toType(signatures[0]);
//        this.parameterTypes = new TypeInfo[signatures.length];
//        for (int i = 0; i < parameterTypes.length; i++) {
//            parameterTypes[i] = classFile().repository().toType(signatures[i + 1]);
//        }
    }

    @Override
    public TypeInfo getDeclaringClass() {
        return classFile;
    }

    @Override
    public TypeInfo getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeInfo type) {
        this.returnType = type;
    }

    @Override
    public TypeInfo[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(TypeInfo... types) {
        this.parameterTypes = types;

    }

    public void setParameterTypes(Collection<TypeInfo> types) {
        setParameterTypes(types.toArray(new TypeInfo[0]));
    }

    @Override
    public TypeInfo[] getExceptionTypes() {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        return exc.getTable().stream().map(classFile().repository()::toType).toArray(TypeInfo[]::new);
    }

    public void setExceptionTypes(TypeInfo... types) {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        exc.clear();
        exc.addAll(Arrays.asList(types));
    }

    public void addExceptionTypes(TypeInfo... types) {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        exc.addAll(Arrays.asList(types));
    }

    // give a zero-length array to clear all existing exceptions
    public void removeExceptionTypes(TypeInfo... types) {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        if (types.length == 0) {
            exc.clear();
        } else {
            exc.removeAll(Arrays.asList(types));
        }
    }

    @Override
    public Type[] getGenericExceptionTypes() {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        return exc.getTable().toArray(Type[]::new);
    }

    @Override
    public Type[] getGenericParameterTypes() {
        Signature sig = getAttribute("Signature");
        if (sig == null) return getParameterTypes();
        Type[] cached = sig.getCachedGenericType();
        if (cached == null) {
            String unresolved = sig.getGenericType();
            cached = classFile().repository().resolveGenericMethodDescriptor(unresolved);
            sig.setCachedGenericType(cached);
        }
        Type[] types = new Type[cached.length - 1];
        System.arraycopy(cached, 1, types, 0, types.length);
        return types;
    }

    @Override
    public Type getGenericReturnType() {
        Signature sig = getAttribute("Signature");
        if (sig == null) return getReturnType();
        Type[] cached = sig.getCachedGenericType();
        if (cached == null) {
            String unresolved = sig.getGenericType();
            cached = classFile().repository().resolveGenericMethodDescriptor(unresolved);
            sig.setCachedGenericType(cached);
        }
        return cached[0];
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        //TODO
        return new TypeVariable[0];
    }

    @Override
    public Annotation[][] getParameterAnnotations() {
        return Util.map(l -> l.values().toArray(new Annotation[0]), Annotation[][]::new, parameterAnnotations);
    }

    @SuppressWarnings("unchecked")
    public void setParameterAnnotations(Annotation[][] annos) {
        parameterAnnotations = (Map<TypeInfo, Annotation>[]) new Map[annos.length];
        for (int i = 0; i < annos.length; i++) {
            Map<TypeInfo, Annotation> map = new HashMap<>();
            parameterAnnotations[i] = map;
            for (Annotation a : annos[i]) {
                map.put(a.annotationType(), a);
            }
        }
    }

    public void removeParameterAnnotation(int index, TypeInfo type) {
        parameterAnnotations[index].remove(type);
    }

    public void removeParameterAnnotations(int index, String type) {
        removeParameterAnnotation(index, classFile().repository().toType(type));
    }

    public void clearParameterAnnotations(int index) {
        parameterAnnotations[index].clear();
    }

    public void clearParameterAnnotations() {
        parameterAnnotations = null;
    }

    public Annotation[] getParameterAnnotations(int index) {
        return parameterAnnotations[index].values().toArray(new Annotation[0]);
    }

    public Annotation getParameterAnnotation(int index, TypeInfo type) {
        return parameterAnnotations[index].get(type);
    }

    public Annotation getParameterAnnotation(int index, String type) {
        return getParameterAnnotation(index, classFile().repository().toType(type));
    }

    public void addParameterAnnotation(int index, Annotation anno) {
        if (parameterAnnotations.length <= index) {
            parameterAnnotations = Arrays.copyOf(parameterAnnotations, index + 1);
        }
        Map<TypeInfo, Annotation> map = parameterAnnotations[index];
        if (map == null) {
            map = new HashMap<>();
            parameterAnnotations[index] = map;
        }
        map.put(anno.annotationType(), anno);
    }

    public void addParameterAnnotation(int index, TypeInfo type, boolean visible) {
        Annotation anno = new Annotation(this, type, visible);
        addParameterAnnotation(index, anno);
    }

    // TypeAnnotation


    @Override
    public ConstType constantType() {
        return classFile.flaggedInterface() ? ConstType.INTERFACE_METHODREF : ConstType.METHODREF;
    }

    @Override
    void collect(ConstPool pool) {
        super.collect(pool);
        if (parameterAnnotations == null) return;
        for (Map<TypeInfo, Annotation> map : parameterAnnotations) {
            for (Annotation v : map.values()) {
                v.collect(pool);
            }
        }
    }
}
