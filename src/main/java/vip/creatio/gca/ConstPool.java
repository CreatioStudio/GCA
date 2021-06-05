package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.type.Type;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class ConstPool implements Iterable<Const> {

    protected final ClassFile classFile;

    private final HashSet<Const> constants = new HashSet<>();
    private int size;

    private final HashMap<Object, Const.Value> valueCache = new HashMap<>();
    private final ArrayList<Const> constCache = new ArrayList<>();

    boolean writing = false;

    ConstPool(ClassFile classFile) {
        this.classFile = classFile;
    }

    /**
     * Parsing state: elements cannot be deleted
     */
    boolean isWriting() {
        return writing;
    }

    void setWriting(boolean flag) {
        writing = flag;
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

    public boolean contains(Const c) {
        return constants.contains(c);
    }

    protected final void write(ByteVector buffer) {
        // size: count + 1
        buffer.putShort((short) (this.size() + 1));
        for (Const c : constants) {
            c.write(buffer);
        }
    }

    void recacheMap() {
        //TODO
    }

    public int indexOf(Const c) {
        int i = 1;
        for (Const constant : this) {
            if (constant.equals(c)) return i;
            else i += constant instanceof Const.DualSlot ? 2 : 1;
        }
        return -1;
    }

    public Const get(int index) {
        if (index == 0) return null;

        index &= 0xFFFF;
        int i = 1;
        for (Const constant : this) {
            if (i == index) return constant;
            else i += constant instanceof Const.DualSlot ? 2 : 1;
        }
        return null;
    }


    public <T extends Const> T acquire(T c) {
        if (c == null || c.pool != this) return null;
        for (Const constant : constants) {
            if (constant.equals(c)) return (T) constant;
        }
        add(c);
        return c;
    }

    public ClassConst acquire(Type t) {
        if (t instanceof ClassConst)
            return (ClassConst) acquire((Const) t);

    }

    public final <T extends Const> List<T> acquire(Collection<T> c) {
        return c.stream().map(this::acquire).collect(Collectors.toList());
    }

    @SafeVarargs
    public final <T extends Const> T[] acquire(T... c) {
        T[] copy = Arrays.copyOf(c, c.length);
        for (int i = 0; i < c.length; i++) {
            copy[i] = acquire(c[i]);
        }
        return copy;
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

    public UTFConst acquireUtf(byte[] data) {
        return acquireUtf(new String(data));
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

    public Const.Value acquireValue(Object v) {
        if (v instanceof Number) {
            if (v instanceof Integer) {
                return acquireInt((Integer) v);
            } else if (v instanceof Float) {
                return acquireFloat((Float) v);
            } else if (v instanceof Double) {
                return acquireDouble((Double) v);
            } else if (v instanceof Long) {
                return acquireLong((Long) v);
            }
        } else if (v instanceof String) {
            return acquireString((String) v);
        }
        throw new RuntimeException("unsupported type: " + v.getClass());
    }



    // class name formatted in some like java.lang.xxx$inner
    public ClassConst acquireClass(String name) {
        return acquire(new ClassConst(this, name));
    }

    public ClassConst acquireClass(Class<?> cls) {
        return acquireClass(cls.getName());
    }

    public ClassConst acquireClass(ClassConst c) {
        return acquireClass(c.getTypeName());
    }



    public StringConst acquireString(String s) {
        return acquire(new StringConst(this, s));
    }



    public RefConst acquireFieldRef(ClassConst clazz, String name, String type) {
        return acquire(new RefConst(this, ConstType.FIELDREF, clazz, name, ClassUtil.getSignature(type)));
    }

    public RefConst acquireFieldRef(String clazzSig, String name, String type) {
        return acquireFieldRef(acquireClass(clazzSig), name, type);
    }

    public RefConst acquireFieldRef(ClassConst clazz, String name, ClassConst typeClass) {
        return acquireFieldRef(clazz, name, typeClass.getTypeName());
    }

    public RefConst acquireFieldRef(Class<?> clazz, String name, Class<?> typeClass) {
        return acquireFieldRef(acquireClass(clazz), name, typeClass.getName());
    }

    public RefConst acquireFieldRef(Field f) {
        return acquireFieldRef(acquireClass(f.getName()), f.getName(), ClassUtil.getSignature(f));
    }



    public RefConst acquireMethodRef(ClassConst clazz, String name, String descriptor) {
        return acquire(new RefConst(this, ConstType.METHODREF, clazz, name, descriptor));
    }

    public RefConst acquireMethodRef(String clazz, String name, String signature) {
        return acquireMethodRef(acquireClass(clazz), name, signature);
    }

    public RefConst acquireMethodRef(Class<?> clazz, String name, String signature) {
        return acquireMethodRef(clazz.getName(), name, signature);
    }

    public RefConst acquireMethodRef(Method m) {
        return acquireMethodRef(m.getDeclaringClass(), m.getName(), ClassUtil.getSignature(m));
    }

    public RefConst acquireMethodRef(Class<?> clazz, String name, MethodType type) {
        return acquireMethodRef(clazz, name, type.toMethodDescriptorString());
    }



    public RefConst acquireInterfaceMethodRef(ClassConst clazz, String name, String descriptor) {
        return acquire(new RefConst(this, ConstType.INTERFACE_METHODREF, clazz, name, descriptor));
    }

    public RefConst acquireInterfaceMethodRef(String clazz, String name, String signature) {
        return acquireInterfaceMethodRef(acquireClass(clazz), name, signature);
    }

    public RefConst acquireInterfaceMethodRef(Class<?> clazz, String name, String signature) {
        return acquireInterfaceMethodRef(clazz.getName(), name, signature);
    }

    public RefConst acquireInterfaceMethodRef(Method m) {
        return acquireInterfaceMethodRef(m.getDeclaringClass(), m.getName(), ClassUtil.getSignature(m));
    }

    public RefConst acquireInterfaceMethodRef(Class<?> clazz, String name, MethodType type) {
        return acquireInterfaceMethodRef(clazz, name, type.toMethodDescriptorString());
    }


    public NameAndTypeConst acquireNameAndType(String name, String descriptor) {
        return acquire(new NameAndTypeConst(this, name, descriptor));
    }


    public MethodTypeConst acquireMethodType(String descriptor) {
        return acquire(new MethodTypeConst(this, descriptor));
    }

    public MethodTypeConst acquireMethodType(String... signatures) {
        return acquireMethodType(ClassUtil.getSignature(signatures));
    }

    public MethodTypeConst acquireMethodType(String rtype, String... ptype) {
        return acquireMethodType(ClassUtil.getSignature(rtype, ptype));
    }

    public MethodTypeConst acquireMethodType(MethodType type) {
        return acquireMethodType(type.toMethodDescriptorString());
    }

    public MethodTypeConst acquireMethodType(Method method) {
        return acquireMethodType(ClassUtil.getSignature(method));
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

    static void parse(ClassFileParser pool, ByteVector buffer) throws ClassFormatError {
        int i = 1;
        try {
            for (; i < pool.size(); i++) {
                byte tag = buffer.getByte();
                ConstType type = ConstType.fromTag(tag);
                if (type == null) {
                    throw new ClassFormatError("No constant with tag " + tag
                            + " @" + i
                            + " total " + pool.size()
                            + " position " + BigInteger.valueOf(buffer.position()).toString(16));
                }
                switch (type) {
                    case UTF8:
                        pool.set(i, buffer.position(), new UTFConst(pool.getPool(), buffer));
                        break;
                    case INTEGER:
                        pool.set(i, buffer.position(), new IntegerConst(pool.getPool(), buffer));
                        break;
                    case FLOAT:
                        pool.set(i, buffer.position(), new FloatConst(pool.getPool(), buffer));
                        break;
                    case LONG:
                        pool.set(i, buffer.position(), new LongConst(pool.getPool(), buffer));
                        i++;        // long number continues
                        break;
                    case DOUBLE:
                        pool.set(i, buffer.position(), new DoubleConst(pool.getPool(), buffer));
                        i++;        // long number continues
                        break;
                    case CLASS:
                        pool.set(i, buffer.position(), new ClassConst(pool.getPool()));
                        buffer.position(buffer.position() + 2);
                        break;
                    case STRING:
                        pool.set(i, buffer.position(), new StringConst(pool.getPool()));
                        buffer.position(buffer.position() + 2);
                        break;
                    case FIELDREF:
                        pool.set(i, buffer.position(), new RefConst(pool.getPool(), ConstType.FIELDREF));
                        buffer.position(buffer.position() + 4);
                        break;
                    case METHODREF:
                        pool.set(i, buffer.position(), new RefConst(pool.getPool(), ConstType.METHODREF));
                        buffer.position(buffer.position() + 4);
                        break;
                    case INTERFACE_METHODREF:
                        pool.set(i, buffer.position(), new RefConst(pool.getPool(), ConstType.INTERFACE_METHODREF));
                        buffer.position(buffer.position() + 4);
                        break;
                    case NAME_AND_TYPE:
                        pool.set(i, buffer.position(), new NameAndTypeConst(pool.getPool()));
                        buffer.position(buffer.position() + 4);
                        break;
                    case METHOD_HANDLE:
                        pool.set(i, buffer.position(), new MethodHandleConst(pool.getPool()));
                        buffer.position(buffer.position() + 3);
                        break;
                    case METHOD_TYPE:
                        pool.set(i, buffer.position(), new MethodTypeConst(pool.getPool()));
                        buffer.position(buffer.position() + 2);
                        break;
                    case INVOKE_DYNAMIC:
                        pool.set(i, buffer.position(), new InvokeDynamicConst(pool.getPool()));
                        buffer.position(buffer.position() + 4);
                        break;
                    case MODULE:
                        pool.set(i, buffer.position(), new ModuleConst(pool.getPool()));
                        buffer.position(buffer.position() + 2);
                        break;
                    case PACKAGE:
                        pool.set(i, buffer.position(), new PackageConst(pool.getPool()));
                        buffer.position(buffer.position() + 2);
                        break;
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Exception occurred @" + i
                    + " total " + pool.size()
                    + " position 0x" + BigInteger.valueOf(buffer.position()).toString(16));
            System.err.println("Parsed constants:");
            pool.printConstants();
            throw e;
        }
    }

    public ClassFile classFile() {
        return classFile;
    }

    protected void collect() {
        for (Const c : new HashSet<>(constants)) {
            c.collect();
        }
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
