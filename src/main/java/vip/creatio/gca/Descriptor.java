package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.util.Cacheable;
import vip.creatio.gca.util.ClassUtil;

import java.util.Arrays;

public interface Descriptor extends Cacheable {

    @Nullable String[] getDescriptors();

    @Nullable String getDescriptor();  // bytecode signature

    void setDescriptor(String str);  // set bytecode signature

    default void setDescriptors(String... sig) {
        setDescriptor(ClassUtil.getSignature(sig));
    }

    default void setDescriptors(String rtype, String... ptype) {
        setDescriptor(ClassUtil.getSignature(rtype, ptype));
    }

    // for method, it's return type, for field, it's type
    default String getValueType() {
        if (getDescriptors() == null) recache();
        return getDescriptors()[0];
    }

    default String[] getParameterTypes() {
        if (getDescriptors() == null) recache();
        if (getDescriptors().length == 1) return new String[0];
        return Arrays.copyOfRange(getDescriptors(), 1, getDescriptors().length);
    }

    default int getParameterCount() {
        return getParameterTypes().length;
    }
}
