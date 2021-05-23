package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.attr.Exceptions;
import vip.creatio.gca.constant.ClassConst;

import vip.creatio.gca.util.ByteVector;
import java.util.EnumSet;
import java.util.List;

public class DeclaredMethod extends DeclaredObject {

    DeclaredMethod(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
        recache();
    }

    DeclaredMethod(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   String descriptor,
                   Attribute... attributes) {
        super(bc, flags, name, descriptor, attributes);
        recache();
    }

    DeclaredMethod(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   String descriptor,
                   String[] signatures,
                   Attribute... attributes) {
        super(bc, flags, name, descriptor, attributes);
        this.signatures = signatures;
    }

    @Override
    EnumSet<AccessFlag> resolveFlags(short flags) {
        return AccessFlag.resolveMethod(flags);
    }

    public boolean hasCode() {
        return !flaggedAbstract();
    }

    public @NotNull Code code() {
        if (!hasCode()) {
            throw new RuntimeException("Abstract method does not have Code attribute! " +
                    "check if it has code using hasCode() before getting it");
        }
        return getOrAddAttribute("Code",
                () -> new Code(classFile));
    }

    public @NotNull List<ClassConst> exceptions() {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(classFile));
        return exc.getTable();
    }

    public String toString() {
        return "Method{name=" + getName() + ",descriptor=" + getDescriptor();
    }
}
