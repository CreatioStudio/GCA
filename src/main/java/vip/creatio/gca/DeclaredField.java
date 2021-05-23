package vip.creatio.gca;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.ConstantValue;

import vip.creatio.gca.util.ByteVector;
import java.util.EnumSet;

public class DeclaredField extends DeclaredObject {

    DeclaredField(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
        recache();
    }

    DeclaredField(ClassFile bc,
                  EnumSet<AccessFlag> flags,
                  String name,
                  String descriptor,
                  Attribute... attributes) {
        super(bc, flags, name, descriptor, attributes);
        recache();
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
                () -> new ConstantValue(classFile));
        v.setValue(value);
    }

    @Override
    public String toString() {
        return "Field{name=" + getName() + ",descriptor=" + getDescriptor();
    }
}
