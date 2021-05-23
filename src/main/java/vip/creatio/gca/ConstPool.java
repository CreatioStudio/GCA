package vip.creatio.gca;

import vip.creatio.gca.constant.*;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ConstPool implements Iterable<Const> {

    protected final ClassFile classFile;

    protected ConstPool(ClassFile classFile) {
        this.classFile = classFile;
    }

    protected abstract void write(ByteVector buffer);

    /**
     * Parsing state: elements cannot be deleted
     */
    public abstract boolean isWriting();

    public abstract void remove(Const c);

    public abstract void remove(String str);

    public abstract boolean contains(Const c);

    public int indexOf(Const c) {
        int index = 1;
        for (Const constant : this) {
            if (constant.equals(c)) return index;
            else index += constant instanceof Const.DualSlot ? 2 : 1;
        }
        return -1;
    }

    public Const get(int index) {
        if (index == 0) return null;

        index &= 0xFFFF;
        int i = 1;
        for (Const constant : this) {
            if (i == index) return constant;
            else index += constant instanceof Const.DualSlot ? 2 : 1;
        }
        return null;
    }


    public abstract <T extends Const> T acquire(T c);

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

    public abstract List<UTFConst> acquireUtf();

    public abstract UTFConst acquireUtf(String data);

    public UTFConst acquireUtf(byte[] data) {
        return acquireUtf(new String(data));
    }



    public abstract IntegerConst acquireInt(int data);

    public abstract FloatConst acquireFloat(float data);

    public abstract LongConst acquireLong(long data);

    public abstract DoubleConst acquireDouble(double data);

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
    public abstract ClassConst acquireClass(String name);

    public ClassConst acquireClass(Class<?> cls) {
        return acquireClass(cls.getName());
    }

    public ClassConst acquireClass(ClassConst c) {
        return acquireClass(c.getName());
    }



    public abstract StringConst acquireString(String s);



    public abstract RefConst acquireFieldRef(ClassConst clazz, String name, String typeSig);

    public RefConst acquireFieldRef(String clazzSig, String name, String typeSig) {
        return acquireFieldRef(acquireClass(clazzSig), name, typeSig);
    }

    public RefConst acquireFieldRef(ClassConst clazz, String name, ClassConst typeClass) {
        return acquireFieldRef(clazz, name, typeClass.getName());
    }

    public RefConst acquireFieldRef(Class<?> clazz, String name, Class<?> typeClass) {
        return acquireFieldRef(acquireClass(clazz), name, typeClass.getName());
    }

    public RefConst acquireFieldRef(Field f) {
        return acquireFieldRef(acquireClass(f.getName()), f.getName(), ClassUtil.getSignature(f));
    }



    public abstract RefConst acquireMethodRef(ClassConst clazz, String name, String signature);

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



    public abstract RefConst acquireInterfaceMethodRef(ClassConst clazz, String name, String signature);

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


    public abstract NameAndTypeConst acquireNameAndType(String name, String descriptor);


    public abstract MethodTypeConst acquireMethodType(String descriptor);

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



    public abstract void add(Const constant);

    public abstract int size();

    public static ConstPool create(ClassFile parent) {
        return new ConstPoolImpl(parent);
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

    protected void collect() {}

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
        }

        @Override
        public void forEachRemaining(Consumer<? super Const> action) {
            itr.forEachRemaining(action);
        }
    }

}
