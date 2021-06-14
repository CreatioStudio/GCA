package vip.creatio.gca.type;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ConstType;
import vip.creatio.gca.TypeInfo;

public interface FieldInfo extends MemberInfo {

    @Override
    TypeInfo getDeclaringClass();

    @Override
    String getName();

    TypeInfo getType();

    default @Nullable DeclaredFieldInfo getImpl() {
        if (this instanceof DeclaredFieldInfo) {
            return (DeclaredFieldInfo) this;
        }
        return null;
    }

    @Override
    default ConstType constantType() {
        return ConstType.FIELDREF;
    }

    @Override
    default String getDescriptor() {
        return getType().getInternalName();
    }
}
