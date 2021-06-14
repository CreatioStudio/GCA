package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.ConstantValue;

import vip.creatio.gca.attr.Signature;
import vip.creatio.gca.type.*;
import vip.creatio.gca.util.common.ByteVector;
import java.util.EnumSet;

public class DeclaredField extends DeclaredObject implements DeclaredFieldInfo {

    private TypeInfo type;

    DeclaredField(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
    }

    DeclaredField(ClassFile bc,
                  EnumSet<AccessFlag> flags,
                  String name,
                  TypeInfo type,
                  Attribute... attributes) {
        super(bc, flags, name, attributes);
        this.type = type;
    }

    @Override
    EnumSet<AccessFlag> resolveFlags(short flags) {
        return AccessFlag.resolveField(flags);
    }

    public @Nullable Object constantValue() {
        ConstantValue v = getAttribute("ConstantValue");
        return v == null ? null : v.getValue();
    }

    public void setConstantValue(@Nullable Object value) {
        ConstantValue v = getOrAddAttribute("ConstantValue",
                () -> new ConstantValue(this));
        v.setValue(value);
    }

    @Override
    public String toString() {
        return "Field{name=" + getName() + ",descriptor=" + type + '}';
    }

    @Override
    public ClassFile getDeclaringClass() {
        return classFile;
    }

    @Override
    public TypeInfo getType() {
        return type;
    }

    public Type getGenericType() {
        Signature sig = getAttribute("Signature");
        if (sig == null) return getType();
        Type[] types = sig.getCachedGenericType();
        if (types == null) {
            String unresolved = sig.getGenericType();
            return classFile().repository().toGeneric(unresolved);
        }
        return types[0];
    }

    public void setType(Type type) {
        if (type instanceof TypeInfo) {
            this.type = (TypeInfo) type;
            removeAttribute("Signature");
        } else {
            this.type = classFile().repository().toType(type);
            Signature sig = getOrAddAttribute("Signature", () -> new Signature(this));
            sig.setCachedGenericType(new Type[]{type});
        }
    }

    public void setType(String type) {
        setType(classFile().repository().toGeneric(type));
    }

    @Override
    public String getDescriptor() {
        return type.getInternalName();
    }

    @Override
    void setDescriptor(String s) {
        this.type = classFile().repository().toType(s);
    }
}
