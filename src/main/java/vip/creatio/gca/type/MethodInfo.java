package vip.creatio.gca.type;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.TypeInfo;

public interface MethodInfo extends MemberInfo {

    @Override
    TypeInfo getDeclaringClass();

    @Override
    String getName();

    TypeInfo getReturnType();

    TypeInfo[] getParameterTypes();

    default int getParameterCount() {
        return getParameterTypes().length;
    }

    default @Nullable DeclaredMethodInfo getImpl() {
        if (this instanceof DeclaredMethodInfo)
            return (DeclaredMethodInfo) this;
        return null;
    }
}
