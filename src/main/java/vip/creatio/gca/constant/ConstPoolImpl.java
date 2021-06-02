package vip.creatio.gca.constant;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

import java.util.*;
import java.util.function.Consumer;

public class ConstPoolImpl extends ConstPool {

    Set<Const> constants = new HashSet<>();
    int size;
    //List<Const> constants = new ArrayList<>();
    //HashMap<Integer, Const> constMap = new HashMap<>();

    boolean writing = false;

    public ConstPoolImpl(ClassFile classFile) {
        super(classFile);
    }

    @Override
    public boolean isWriting() {
        return writing;
    }

    public void setWriting(boolean flag) {
        writing = flag;
    }

    @Override
    public boolean contains(Const c) {
        return constants.contains(c);
    }

    public void recacheMap() {
//        constMap.clear();
//        int index = 1;
//        for (Const c : constants) {
//            if (c != null) {
//                constMap.put(index++, c);
//                if (c instanceof Const.DualSlot) ++index;
//            }
//        }
    }

    protected final void write(ByteVector buffer) {
        // size: count + 1
        buffer.putShort((short) (this.size() + 1));
        for (Const c : constants) {
            c.write(buffer);
        }
    }

    @Override
    protected void collect() {
        for (Const c : new HashSet<>(constants)) {
            c.collect();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Const> T acquire(T c) {
        if (c == null || c.pool != this) return null;
        for (Const constant : constants) {
            if (constant.equals(c)) return (T) constant;
        }
        add(c);
        return c;
    }



    public void remove(Const c) {
        if (isWriting()) throw new IllegalStateException("constant cannot be removed in parsing period");
        constants.remove(c);
        size--;
    }

    public void remove(String str) {
        if (isWriting()) throw new IllegalStateException("constant cannot be removed in parsing period");
        constants.removeIf(u -> u instanceof UTFConst && ((UTFConst) u).string().equals(str));
        recalcSize();
    }

    private void recalcSize() {
        size = 0;
        for (Const c : this) {
            size += c instanceof Const.DualSlot ? 2 : 1;
        }
    }

    public List<UTFConst> acquireUtf() {
        List<UTFConst> l = new ArrayList<>();
        for (Const constant : constants) {
            if (constant instanceof UTFConst) l.add((UTFConst) constant);
        }
        return l;
    }

    public UTFConst acquireUtf(String data) {
        return acquire(new UTFConst(this, data.getBytes()));
    }

    public IntegerConst acquireInt(int data) {
        return acquire(new IntegerConst(this, data));
    }

    public FloatConst acquireFloat(float data) {
        return acquire(new FloatConst(this, data));
    }

    public LongConst acquireLong(long data) {
        return acquire(new LongConst(this, data));
    }

    public DoubleConst acquireDouble(double data) {
        return acquire(new DoubleConst(this, data));
    }

    public ClassConst acquireClass(String name) {
        return acquire(new ClassConst(this, name));
    }

    public StringConst acquireString(String s) {
        return acquire(new StringConst(this, s));
    }

    public RefConst acquireFieldRef(ClassConst clazz, String name, String type) {
        return acquire(new RefConst(this, ConstType.FIELDREF, clazz, name, ClassUtil.getSignature(type)));
    }

    public RefConst acquireMethodRef(ClassConst clazz, String name, String descriptor) {
        return acquire(new RefConst(this, ConstType.METHODREF, clazz, name, descriptor));
    }

    public RefConst acquireInterfaceMethodRef(ClassConst clazz, String name, String descriptor) {
        return acquire(new RefConst(this, ConstType.INTERFACE_METHODREF, clazz, name, descriptor));
    }

    public NameAndTypeConst acquireNameAndType(String name, String descriptor) {
        return acquire(new NameAndTypeConst(this, name, descriptor));
    }

    public MethodTypeConst acquireMethodType(String descriptor) {
        return acquire(new MethodTypeConst(this, descriptor));
    }



    // invokeDynamic



    public void add(Const constant) {
        constant.pool = this;
        constants.add(constant);
        if (isWriting()) {
            //constMap.put(constants.size() - 1, constant);
            constant.collect();
        }
        size += constant instanceof Const.DualSlot ? 2 : 1;
    }

    public int size() {
        return size;
    }

    public static void parseConstant(ClassFileParser pool, Const c, ByteVector buf) {
        c.parse(pool, buf);
    }

    @NotNull
    @Override
    public Iterator<Const> iterator() {
        return new AccessItr(constants.iterator());
    }

    protected class AccessItr implements Iterator<Const> {
        private final Iterator<Const> itr;

        public AccessItr(Iterator<Const> itr) {
            this.itr = itr;
        }

        @Override
        public boolean hasNext() {
            return itr.hasNext();
        }

        @Override
        public Const next() {
            return itr.next();
        }

        @Override
        public void remove() {
            if (isWriting()) throw new IllegalStateException("constant cannot be removed in parsing period");
            itr.remove();
            size--;
        }

        @Override
        public void forEachRemaining(Consumer<? super Const> action) {
            itr.forEachRemaining(action);
        }
    }
}
