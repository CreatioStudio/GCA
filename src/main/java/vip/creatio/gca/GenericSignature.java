package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.util.Cacheable;
import vip.creatio.gca.util.ClassUtil;

import java.util.Arrays;

public interface GenericSignature extends Cacheable {

    @Nullable String[] getGenericSignatures();

    @Nullable String getGenericSignature();  // bytecode signature

    void setGenericSignature(String str);  // set bytecode signature

    default void setGenericSignatures(String... sig) {
        setGenericSignature(ClassUtil.getSignature(sig));
    }

    default void setGenericSignatures(String rtype, String... ptype) {
        setGenericSignature(ClassUtil.getSignature(rtype, ptype));
    }

    // for method, it's return type, for field, it's type
    default String getGenericValueType() {
        if (getGenericSignatures() == null) recache();
        return getGenericSignatures()[0];
    }

    default String[] getGenericParameterTypes() {
        if (getGenericSignatures() == null) recache();
        if (getGenericSignatures().length == 1) return new String[0];
        return Arrays.copyOfRange(getGenericSignatures(), 1, getGenericSignatures().length);
    }

    default int getParameterCount() {
        return getGenericSignatures().length;
    }
}
