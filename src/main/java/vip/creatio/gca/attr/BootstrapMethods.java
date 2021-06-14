
package vip.creatio.gca.attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vip.creatio.gca.*;
import vip.creatio.gca.util.common.ByteVector;

public class BootstrapMethods extends TableAttribute<BootstrapMethods.Method> {

    private BootstrapMethods(AttributeContainer c) {
        super(c);
    }

    public BootstrapMethods(ClassFile file) {
        this((AttributeContainer) file);
    }

    public static BootstrapMethods parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer)
    throws ClassFormatError {
        BootstrapMethods inst = new BootstrapMethods(container);

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

    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort((short)this.items.size());
        for (Method i : this.items) {
            i.write(pool, buffer);
        }
    }

    protected void collect(ConstPool pool) {
        super.collect(pool);
        for (Method item : this.items) {
            pool.acquire(item.getRef());
            pool.acquire(item.arguments);
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
            this.ref = (MethodHandleConst) pool.get(buffer.getUShort());
            int count = buffer.getUShort();

            for(int i = 0; i < count; ++i) {
                this.arguments.add(pool.get(buffer.getUShort()));
            }

        }

        Method(MethodHandleConst handle, Const... args) {
            ref = handle;
            arguments.addAll(Arrays.asList(args));
        }

        public MethodHandleConst getRef() {
            return ref;
        }

        public void setRef(MethodHandleConst ref) {
            this.ref = ref;
        }

        public void addArgument(Const arg) {
            arguments.add(arg);
        }

        public int index() {
            return items.indexOf(this);
        }

        private void write(ConstPool pool, ByteVector buffer) {
            buffer.putShort(pool.indexOf(ref));
            buffer.putShort(arguments.size());

            for (Const c : arguments) {
                buffer.putShort(pool.indexOf(c));
            }
        }

        public int hashCode() {
            return ref.hashCode() + arguments.hashCode() * 31;
        }

        public String toString() {
            return "{handle=" + ref.toString() + ",arguments=" + arguments + "}";
        }
    }
}
