package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.ByteVector;

public class StackMapTable extends TableAttribute<StackMapTable.Frame> {

    public StackMapTable(ClassFile classFile) {
        super(classFile);
    }

    public static StackMapTable parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        StackMapTable inst = new StackMapTable(container.classFile());
        inst.checkContainerType(container);

        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(inst.new Frame(pool, buffer));
        }
        return inst;
    }

    public void add(int startPc, int length, String name, String descriptor, int index) {
        items.add(new Frame(startPc, length, name, descriptor, index));
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, Code.class);
    }

    @Override
    public String name() {
        return "LocalVariableTable";
    }

    @Override
    protected void collect() {
        super.collect();
        ConstPool pool = constPool();
        for (Frame item : items) {
            pool.acquireUtf(item.getName());
            pool.acquireUtf(item.getDescriptor());
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Frame i : items) {
            i.write(buffer);
        }
    }

    public final class Frame {

        private int startPc;
        private int length;
        private String name;
        private String descriptor;
        private int index;

        Frame(int startPc, int length, String name, String descriptor, int index) {
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        Frame(ClassFileParser pool, ByteVector buffer) {
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
