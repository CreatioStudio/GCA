package vip.creatio.gca.attr;

import vip.creatio.gca.*;

import vip.creatio.gca.util.common.ByteVector;

import java.util.Collection;
import java.util.Iterator;

/**
 * The Exceptions attribute indicates which checked exceptions a method may throw.
 */
public class Exceptions extends TableAttribute<TypeInfo> {

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
            inst.items.add((TypeInfo) pool.get(buffer.getShort()));
        }
        return inst;
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        for (TypeInfo item : items) {
            pool.acquireClass(item);
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
    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (TypeInfo i : items) {
            buffer.putShort(pool.indexOf(i));
        }
    }

    // exception: set to null to create a 'finally' block
    public void add(TypeInfo exception) {
        // anti duplication
        for (TypeInfo table : items) {
            if (exception.equals(table)) {
                return;
            }
        }
        items.add(exception);
    }

    public void addAll(Collection<TypeInfo> types) {
        items.addAll(types);
    }

    public void removeAll(Collection<TypeInfo> types) {
        Iterator<TypeInfo> iter = items.iterator();
        while (iter.hasNext()) {
            for (TypeInfo type : types) {
                if (type.getTypeName().equals(iter.next().getTypeName()))
                    iter.remove();
            }
        }
    }
}
