package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.DeclaredMethod;
import vip.creatio.gca.ClassConst;

import vip.creatio.gca.util.ByteVector;

/**
 * The Exceptions attribute indicates which checked exceptions a method may throw.
 */
public class Exceptions extends TableAttribute<ClassConst> {

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
            inst.items.add((ClassConst) pool.get(buffer.getShort()));
        }
        return inst;
    }

    @Override
    protected void collect() {
        super.collect();
        constPool().acquire(items);
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
        for (ClassConst i : items) {
            buffer.putShort(i.index());
        }
    }

    // exception: set to null to create a 'finally' block
    public void add(ClassConst exception) {
        // anti duplication
        for (ClassConst table : items) {
            if (exception.equals(table)) {
                return;
            }
        }
        items.add(exception);
    }
}
