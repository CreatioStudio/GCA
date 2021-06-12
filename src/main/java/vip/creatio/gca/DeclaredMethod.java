package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.attr.*;

import vip.creatio.gca.type.*;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import java.lang.ref.SoftReference;
import java.util.*;

public class DeclaredMethod extends DeclaredObject implements DeclaredMethodInfo {

    private TypeInfo returnType;
    private TypeInfo[] parameterTypes;

    private String cachedDescriptor;
    
    DeclaredMethod(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
    }

    DeclaredMethod(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   TypeInfo rtype,
                   TypeInfo[] ptype,
                   Attribute... attributes) {
        super(bc, flags, name, attributes);
        this.returnType = rtype;
        this.parameterTypes = ptype;
    }

    @Override
    EnumSet<AccessFlag> resolveFlags(short flags) {
        return AccessFlag.resolveMethod(flags);
    }

    public boolean hasCode() {
        return !flaggedAbstract();
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
    String getDescriptor() {
        if (cachedDescriptor == null) {
            cachedDescriptor = Types.toMethodSignature(Util.concat(TypeInfo[]::new, returnType, parameterTypes));
        }
        return cachedDescriptor;
    }

    @Override
    void setDescriptor(String s) {
        Type[] signatures = classFile().resolveGeneric(s);
        this.returnType = classFile().toType(signatures[0]);
        this.parameterTypes = new TypeInfo[signatures.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = classFile().toType(signatures[i + 1]);
        }
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
        return exc.getTable().stream().map(classFile()::toType).toArray(TypeInfo[]::new);
    }

    public void setExceptionTypes(Type... types) {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        exc.clear();
        exc.addAll(Arrays.asList(types));
    }

    public void addExceptionTypes(Type... types) {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        exc.addAll(Arrays.asList(types));
    }

    // give a zero-length array to clear all existing exceptions
    public void removeExceptionTypes(Type... types) {
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
            cached = classFile().resolveGeneric(unresolved);
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
            cached = classFile().resolveGeneric(unresolved);
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
    @SuppressWarnings("unchecked")
    public Annotation[][] getParameterAnnotations() {
        ParameterAnnotations annos = getAttribute("RuntimeVisibleParameterAnnotations");
        ParameterAnnotations annosInv = getAttribute("RuntimeInvisibleParameterAnnotations");
        List<List<Annotation>> anno = Util.mergeOnEach(Util::merge,
                annos == null ? new ArrayList<>() : annos.getTable(),
                annosInv == null ? new ArrayList<>() : annosInv.getTable());
        return Util.map(l -> l.toArray(new Annotation[0]), Annotation[][]::new, anno.toArray((List<Annotation>[]) new List[0]));
    }

    public void setParameterAnnotations(Annotation[][] annos)


}
