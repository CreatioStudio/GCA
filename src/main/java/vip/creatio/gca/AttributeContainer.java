package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vip.creatio.gca.util.common.ByteVector;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public interface AttributeContainer {

    ClassFile classFile();      // should be packed into a larger interface

    AttributeContainer copy();

    // 2021.6.8 Use Map instead of a List for performance consideration
    Map<String, Attribute> getAttributes();

    default void addAttribute(Attribute att) {
        att.checkContainerType(this);
        getAttributes().put(att.name(), att);
    }

    default void removeAttribute(Attribute att) {
        getAttributes().remove(att.name());
    }

    default void removeAttribute(String name) {
        getAttributes().remove(name);
    }

    default <T extends Attribute> @Nullable T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    default <T extends Attribute> @NotNull T getOrAddAttribute(String name, Supplier<T> add) {
        return (T) getAttributes().computeIfAbsent(name, s -> add.get());
    }

    default boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    default void writeAttributes(ConstPool pool, ByteVector buffer) {
        List<Attribute> usable = getAttributes().values().stream().filter(a -> !a.isEmpty()).collect(Collectors.toList());
        buffer.putShort((short) usable.size());
        for (Attribute attr : usable) {
            attr.write(pool, buffer);
        }
    }

    default void collectConstants(ConstPool pool) {
        List<Attribute> usable = getAttributes().values().stream().filter(a -> !a.isEmpty()).collect(Collectors.toList());
        for (Attribute attr : usable) {
            attr.collect(pool);
        }
    }

}
