package vip.creatio.gca.attr;

import vip.creatio.gca.Attribute;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

// Does not have to care the first 2 variable: attribute_name_index and attribute_length
// since they will be read by Bytecode#resolveAttribute

@FunctionalInterface
public interface AttributeResolver {

    Attribute provide(AttributeContainer container, ClassFileParser pool, ByteVector buffer);

}
