package vip.creatio.gca.type;

import vip.creatio.gca.Const;
import vip.creatio.gca.TypeInfo;

public interface MemberInfo extends Const {

    TypeInfo getDeclaringClass();

    String getName();

    String getDescriptor();
}
