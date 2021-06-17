package vip.creatio.gca;

import vip.creatio.gca.type.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A pool that stores class files, methods, fields and type management
 */
public class Repository implements TypeFactory {

    private final Map<String, TypeInfo> classMap = new ConcurrentHashMap<>();

    private final Map<TypeInfo, Set<MethodInfo>> methods = new ConcurrentHashMap<>();
    private final Map<TypeInfo, Set<FieldInfo>> fields = new ConcurrentHashMap<>();

    public Repository() {}

    @Override
    public MethodInfo toMethod(TypeInfo declClass, String name, TypeInfo... signatures) {
        MethodInfo info = getMethod(declClass, name, signatures);
        if (info != null) return info;
        info = new VirtualMethodInfo(false, declClass, name, signatures);
        Set<MethodInfo> set = methods.computeIfAbsent(declClass, k -> new HashSet<>());
        set.add(info);
        return info;
    }

    @Override
    public MethodInfo toInterfaceMethod(TypeInfo declClass, String name, TypeInfo... signatures) {
        MethodInfo info = getMethod(declClass, name, signatures);
        if (info != null) return info;
        info = new VirtualMethodInfo(true, declClass, name, signatures);
        Set<MethodInfo> set = methods.computeIfAbsent(declClass, k -> new HashSet<>());
        set.add(info);
        return info;
    }

    @Override
    public FieldInfo toField(TypeInfo declClass, String name, TypeInfo type) {
        FieldInfo info = getField(declClass, name, type);
        if (info != null) return info;
        info = new VirtualFieldInfo(declClass, name, type);
        Set<FieldInfo> set = fields.computeIfAbsent(declClass, k -> new HashSet<>());
        set.add(info);
        return info;
    }

    @Override
    public TypeInfo[] getTypes() {
        return classMap.values().toArray(new TypeInfo[0]);
    }

    @Override
    public TypeInfo getType(String signature) {
        // query java class first
        TypeInfo type = Types.get(signature);
        if (type != null) return type;

        // query local cached class
        return classMap.get(signature);
    }

    public MethodInfo getMethod(TypeInfo declClass, String name, TypeInfo... signatures) {
        Set<MethodInfo> list = methods.get(declClass);
        if (list == null) return null;
        for (MethodInfo method : list) {
            if (method.getName().equals(name) && Types.withSignature(method, signatures[0],
                    Arrays.copyOfRange(signatures, 1, signatures.length))) {
                return method;
            }
        }
        return null;
    }

    public FieldInfo getField(TypeInfo declClass, String name, TypeInfo type) {
        Set<FieldInfo> list = fields.get(declClass);
        if (list == null) return null;
        for (FieldInfo field : list) {
            if (field.getName().equals(name) && Types.withSignature(field, type)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public void addType(TypeInfo type) {
        classMap.put(type.getName(), type);
    }
}
