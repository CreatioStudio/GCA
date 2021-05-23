package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.constant.ClassConst;

import vip.creatio.gca.util.ByteVector;

/**
 * The Exceptions attribute indicates which checked exceptions a method may throw.
 */
public class Exceptions extends TableAttribute<ClassConst> {

    public Exceptions(ClassFile classFile) {
        super(classFile);
    }

    public static Exceptions parse(ClassFile file, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        Exceptions inst = new Exceptions(file);
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
