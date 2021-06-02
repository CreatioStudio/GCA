package vip.creatio.gca;

import javassist.bytecode.Bytecode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.util.ByteVector;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public interface AttributeContainer {

    ClassFile classFile();      // should be packed into a larger interface

    AttributeContainer copy();

    List<Attribute> attributes();

    default void addAttribute(Attribute att) {
        att.checkContainerType(this);
        attributes().add(att);
    }

    default void removeAttribute(Attribute att) {
        attributes().remove(att);
    }

    default void removeAttribute(String name) {
        attributes().removeIf(c -> c.name().equals(name));
    }

    default void removeAttribute(int index) {
        attributes().remove(index);
    }

    default <T extends Attribute> T getAttribute(int index) {
        return (T) attributes().get(index);
    }

    default <T extends Attribute> @Nullable T getAttribute(String name) {
        for (Attribute att : attributes()) {
            if (att.name().equals(name)) return (T) att;
        }
        return null;
    }

    default <T extends Attribute> @NotNull T getOrAddAttribute(String name, Supplier<T> add) {
        T attr = getAttribute(name);
        if (attr == null) {
            attr = add.get();
            addAttribute(attr);
        }
        return attr;
    }

    default boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    default void writeAttributes(ByteVector buffer) {
        List<Attribute> usable = attributes().stream().filter(a -> !a.isEmpty()).collect(Collectors.toList());
        buffer.putShort((short) usable.size());
        for (Attribute attr : usable) {
            attr.write(buffer);
        }
    }

    default void collectConstants() {
        List<Attribute> usable = attributes().stream().filter(a -> !a.isEmpty()).collect(Collectors.toList());
        for (Attribute attr : usable) {
            attr.collect();
        }
    }

}
