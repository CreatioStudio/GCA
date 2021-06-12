package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.DeclaredMethod;
import vip.creatio.gca.ClassConst;

import vip.creatio.gca.type.Type;
import vip.creatio.gca.type.TypeInfo;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The Exceptions attribute indicates which checked exceptions a method may throw.
 */
public class Exceptions extends TableAttribute<Type> {

    private Exceptions(AttributeContainer container) {
        super(container);
    }

    public Exceptions(DeclaredMethod mth) {
        this((AttributeContainer) mth);
    }

    public static Exceptions parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        Exceptions inst = new Exceptions(container);

        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(((ClassConst) pool.get(buffer.getShort())).getTypeInfo());
        }
        return inst;
    }

    @Override
    protected void collect() {
        super.collect();
        for (Type item : items) {
            constPool().acquireClass(item);
        }
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, DeclaredMethod.class);
    }

    @Override
    public String name() {
        return "Exceptions";
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Type i : items) {
            buffer.putShort(constPool().acquireClass(i).index());
        }
    }

    // exception: set to null to create a 'finally' block
    public void add(TypeInfo exception) {
        // anti duplication
        for (Type table : items) {
            if (exception.equals(table)) {
                return;
            }
        }
        items.add(exception);
    }

    public void addAll(Collection<Type> types) {
        items.addAll(types);
    }

    public void removeAll(Collection<Type> types) {
        Iterator<Type> iter = items.iterator();
        while (iter.hasNext()) {
            for (Type type : types) {
                if (type.getTypeName().equals(iter.next().getTypeName()))
                    iter.remove();
            }
        }
    }
}
