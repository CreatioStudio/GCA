package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.attr.AnnotationDefault;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.attr.Exceptions;

import vip.creatio.gca.type.MethodInfo;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.util.ByteVector;
import java.util.EnumSet;
import java.util.List;

public class DeclaredMethod extends DeclaredObject implements MethodInfo {

    DeclaredMethod(ClassFile bc, ClassFileParser pool, ByteVector buffer) {
        super(bc, pool, buffer);
    }

    DeclaredMethod(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   TypeInfo descriptor,
                   Attribute... attributes) {
        super(bc, flags, name, descriptor, attributes);
    }

    DeclaredMethod(ClassFile bc,
                   EnumSet<AccessFlag> flags,
                   String name,
                   TypeInfo descriptor,
                   String[] signatures,
                   Attribute... attributes) {
        super(bc, flags, name, descriptor, attributes);
        this.descriptors = signatures;
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
                () -> new Code(this));
    }

    public @NotNull AnnotationDefault defaultValue() {
        if (!hasCode()) {
            throw new RuntimeException("Only abstract method of an annotation can set this");
        }
        return getOrAddAttribute("AnnotationDefault", () -> new AnnotationDefault(this));
    }

    public @NotNull List<ClassConst> exceptions() {
        Exceptions exc = getOrAddAttribute("Exceptions",
                () -> new Exceptions(this));
        return exc.getTable();
    }

    public String toString() {
        return "Method{name=" + getName() + ",descriptor=" + getDescriptor() + '}';
    }
}
