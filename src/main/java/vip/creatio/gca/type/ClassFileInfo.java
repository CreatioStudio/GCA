package vip.creatio.gca.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.AccessFlag;
import vip.creatio.gca.util.ClassUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public interface ClassFileInfo extends ClassInfo {

    URL getLocation();

    int getMinorVer();

    int getMajorVer();

    EnumSet<AccessFlag> getAccessFlags();

    ClassInfo getSuperClass();

    List<MethodInfo> getMethods();

    List<FieldInfo> getFields();

    default @Nullable FieldInfo getField(String name) {
        for (FieldInfo f : getFields()) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    default @Nullable MethodInfo getMethod(String name, String... signatures) {
        String sig = ClassUtil.getSignature(signatures);
        for (MethodInfo method : getMethods()) {
            if (method.getName().equals(name) && method.getSignatures().equals(sig)) return method;
        }
        return null;
    }

    default List<MethodInfo> getMethods(String name) {
        List<MethodInfo> mths = new ArrayList<>();
        for (MethodInfo mth : getMethods()) {
            if (mth.getName().equals(name)) mths.add(mth);
        }
        return mths;
    }

    List<ClassInfo> getInterfaces();

    default @Nullable ClassInfo getInterface(String name) {
        for (ClassInfo info : getInterfaces()) {
            if (info.getName().equals(name)) return info;
        }
        return null;
    }



}
