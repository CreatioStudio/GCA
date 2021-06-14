package vip.creatio.gca.type;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.TypeInfo;
import vip.creatio.gca.util.ClassUtil;
import vip.creatio.gca.util.Util;

public interface TypeFactory {

    TypeInfo[] getTypes();

    void addType(TypeInfo type);

    MethodInfo toMethod(TypeInfo declClass, String name, TypeInfo... signatures);

    MethodInfo toInterfaceMethod(TypeInfo declClass, String name, TypeInfo... signatures);

    FieldInfo toField(TypeInfo declClass, String name, TypeInfo type);

    default TypeInfo getType(String signature) {
        // query java class first
        TypeInfo type = Types.get(signature);
        if (type != null) return type;

        // query local cached class
        for (TypeInfo t : getTypes()) {
            if (t.getTypeName().equals(signature))
                return t;
        }

        return null;
    }

    default TypeInfo toType(String signature) {
        TypeInfo info = getType(signature);
        if (info == null) {
            info = new VirtualClassInfo(signature);
            addType(info);
        }
        return info;
    }

    default TypeInfo[] toType(String... signatures) {
        return Util.map(this::toType, TypeInfo[]::new, signatures);
    }

    default TypeInfo toType(@NotNull Type type) {
        if (type instanceof TypeInfo) {
            return (TypeInfo) type;
        } else if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getRawType();
        } else {
            return toType(type.getTypeName());
        }
    }

    default TypeInfo toType(Class<?> cls) {
        TypeInfo info = getType(cls.getName());
        if (info == null) {
            info = new ClassObjectInfo(cls);
            addType(info);
        }
        return info;
    }

    default TypeInfo[] toType(Class<?>[] classes) {
        TypeInfo[] infos = new TypeInfo[classes.length];
        for (int i = 0; i < classes.length; i++) {
            infos[i] = toType(classes[i]);
        }
        return infos;
    }

    default Type toGeneric(java.lang.reflect.Type type) {
        //TODO
        return null;
    }

    default Type toGeneric(String generic) {
        //TODO
        return null;
    }

    default Type[] toGeneric(java.lang.reflect.Type[] types) {
        Type[] infos = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            infos[i] = toGeneric(types[i]);
        }
        return infos;
    }

    default Type[] resolveGenericMethodDescriptor(String generic) {
        String[] types = ClassUtil.fromSignature(generic);
        Type[] infos = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            infos[i] = toGeneric(types[i]);
        }
        return infos;
    }

    default TypeInfo[] resolveMethodDescriptor(String descriptor) {
        String[] types = ClassUtil.fromSignature(descriptor);
        TypeInfo[] infos = new TypeInfo[types.length];
        for (int i = 0; i < types.length; i++) {
            infos[i] = toType(types[i]);
        }
        return infos;
    }





}
