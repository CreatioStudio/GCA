package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.util.Cacheable;

import java.util.Arrays;

public interface DeclaredSignature extends Cacheable {

    @Nullable String[] getSignatures();

    // for method, it's return type, for field, it's type
    default String getValueType() {
        if (getSignatures() == null) recache();
        return getSignatures()[0];
    }

    default String[] getParameterTypes() {
        if (getSignatures() == null) recache();
        if (getSignatures().length == 1) return new String[0];
        return Arrays.copyOfRange(getSignatures(), 1, getSignatures().length);
    }

    default int getParameterCount() {
        return getParameterTypes().length;
    }
}
