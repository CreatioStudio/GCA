package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;

import vip.creatio.gca.util.ByteVector;

/**
 * If LocalVariableTable attributes are present in the attributes table of
 * a given Code attribute, then they may appear in any order. There may be
 * no more than one LocalVariableTable attribute per local variable in the
 * Code attribute.
 */
public class LocalVariableTable extends TableAttribute<LocalVariableTable.Variable> {

    public LocalVariableTable(ClassFile classFile) {
        super(classFile);
    }

    public static LocalVariableTable parse(ClassFile file, ClassFileParser pool, ByteVector buffer) {
        LocalVariableTable inst = new LocalVariableTable(file);
        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(inst.new Variable(pool, buffer));
        }
        return inst;
    }

    public void add(int startPc, int length, String name, String descriptor, int index) {
        items.add(new Variable(startPc, length, name, descriptor, index));
    }

    @Override
    public String name() {
        return "LocalVariableTable";
    }

    @Override
    protected void collect() {
        super.collect();
        ConstPool pool = constPool();
        for (Variable item : items) {
            pool.acquireUtf(item.getName());
            pool.acquireUtf(item.getDescriptor());
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Variable i : items) {
            i.write(buffer);
        }
    }

    public final class Variable {

        private int startPc;
        private int length;
        private String name;
        private String descriptor;
        private int index;

        Variable(int startPc, int length, String name, String descriptor, int index) {
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        Variable(ClassFileParser pool, ByteVector buffer) {
            startPc = buffer.getUShort();
            length = buffer.getUShort();
            name = pool.getString(buffer.getUShort());
            descriptor = pool.getString(buffer.getUShort());
            index = buffer.getUShort();
        }

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void write(ByteVector buffer) {
            buffer.putShort(startPc);
            buffer.putShort(length);
            buffer.putShort(constPool().acquireUtf(name).index());
            buffer.putShort(constPool().acquireUtf(descriptor).index());
            buffer.putShort(index);
        }

        @Override
        public String toString() {
            return "{start_pc=" + startPc + ",length=" + length + ",name=" + name + ",descriptor="
                    + descriptor + ",index=" + index + '}';
        }
    }
}
