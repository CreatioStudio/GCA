
package vip.creatio.gca.attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.constant.MethodHandleConst;
import vip.creatio.gca.util.ByteVector;

public class BootstrapMethods extends TableAttribute<BootstrapMethods.Method> {

    public BootstrapMethods(ClassFile classFile) {
        super(classFile);
    }

    public static BootstrapMethods parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        BootstrapMethods inst = new BootstrapMethods(container.classFile());
        inst.checkContainerType(container);

        int num = buffer.getUShort();

        for(int i = 0; i < num; ++i) {
            inst.items.add(inst.new Method(pool, buffer));
        }

        return inst;
    }

    public void add(MethodHandleConst ref, Const... args) {
        this.items.removeIf((i) -> i.ref.equals(ref));
        this.items.add(new BootstrapMethods.Method(ref, args));
    }

    protected void writeData(ByteVector buffer) {
        buffer.putShort((short)this.items.size());
        for (Method i : this.items) {
            i.write(buffer);
        }
    }

    protected void collect() {
        super.collect();
        for (Method item : this.items) {
            this.constPool().acquire(item.getRef());
            this.constPool().acquire(item.arguments);
        }
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, ClassFile.class);
    }

    public String name() {
        return "BootstrapMethods";
    }

    public class Method {
        private MethodHandleConst ref;
        private final List<Const> arguments = new ArrayList<>();

        Method(ClassFileParser pool, ByteVector buffer) {
            this.ref = (MethodHandleConst)pool.get(buffer.getShort());
            int count = buffer.getUShort();

            for(int i = 0; i < count; ++i) {
                this.arguments.add(pool.get(buffer.getShort()));
            }

        }

        Method(MethodHandleConst handle, Const... args) {
            this.ref = handle;
            this.arguments.addAll(Arrays.asList(args));
        }

        public MethodHandleConst getRef() {
            return this.ref;
        }

        public void setRef(MethodHandleConst ref) {
            this.ref = ref;
        }

        public void addArgument(Const arg) {
            this.arguments.add(arg);
        }

        public int getIndex() {
            return items.indexOf(this);
        }

        private void write(ByteVector buffer) {
            buffer.putShort(this.ref.index());
            buffer.putShort((short)this.arguments.size());

            for (Const c : this.arguments) {
                buffer.putShort(c.index());
            }
        }

        public int hashCode() {
            return this.ref.hashCode() + this.arguments.hashCode() * 31;
        }

        public String toString() {
            String var10000 = this.ref.toString();
            return "{handle=" + var10000 + ",arguments=" + this.arguments + "}";
        }
    }
}
