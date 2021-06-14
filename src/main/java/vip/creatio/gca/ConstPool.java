package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import vip.creatio.gca.type.*;
import vip.creatio.gca.util.common.BiMap;
import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.common.HashBiMap;

import java.util.*;

public final class ConstPool implements Iterable<Const> {

    protected final ClassFile classFile;

    /*
     * Object:
     *  int, float, double, long - value
     *  UTF - byteArray
     *  String - String
     *  Class - TypeInfo
     *  Ref - MemberInfo
     *  MethodHandle - MethodHandle
     *  MethodType - MethodType
     *  InvokeDynamic - InvokeDynamic
     *
     */
    private final HashBiMap<Const, Integer> constants = HashBiMap.create();      // index
    private final HashMap<Object, Const.Value> values = new HashMap<>();
    private final HashMap<String, UTFConst> strings = new HashMap<>();

    private int size = 1;

    ConstPool(ClassFile classFile) {
        this.classFile = classFile;
    }

    public boolean contains(Const c) {
        return constants.containsKey(c);
    }

    final void write(ByteVector buffer) {
        // size: count + 1
        buffer.putShort(size);

        BiMap<Integer, Const> rev = constants.inverse();
        for (int i = 1; i < size; i++) {
            Const c = rev.get(i);
            if (c != null) {
                write(c, buffer);
            }
        }
    }

    // index of a constant
    public int indexOf(Const c) {
        return constants.getOrDefault(c, -1);
    }

    // index of an Integer const
    public int indexOf(int v) {
        return indexOf(values.get(v));
    }

    // index of a Double const
    public int indexOf(double v) {
        return indexOf(values.get(v));
    }

    // index of a Float const
    public int indexOf(float v) {
        return indexOf(values.get(v));
    }

    // index of a Long const
    public int indexOf(long v) {
        return indexOf(values.get(v));
    }

    // index of an UTF const
    public int indexOf(String str) {
        return indexOf(strings.get(str));
    }

    public int indexOfValue(Object v) {
        return indexOf(values.get(v));
    }

    // index of a NameAndType const
    public int indexOf(String name, String type) {
        return indexOf(new NameAndTypeConst(name, type));
    }

    public Const get(int index) {
        if (index == 0) return null;

        index &= 0xFFFF;
        for (Map.Entry<Const, Integer> entry : constants.entrySet()) {
            if (entry.getValue().equals(index)) return entry.getKey();
        }
        return null;
    }


    public void acquire(Const c) {
        if (c == null || constants.containsKey(c)) return;
        add(c);
    }

    public final void acquire(Collection<? extends Const> c) {
        c.forEach(this::acquire);
    }

    public final void acquire(Const... c) {
        for (Const constant : c) {
            acquire(constant);
        }
    }

    public void acquireUtf(String data) {
        if (!strings.containsKey(data)) {
            add(new UTFConst(data));
        }
    }



    public void acquireInt(int data) {
        if (!values.containsKey(data)) {
            add(new IntegerConst(data));
        }
    }

    public void acquireFloat(float data) {
        if (!values.containsKey(data)) {
            add(new FloatConst(data));
        }
    }

    public void acquireLong(long data) {
        if (!values.containsKey(data)) {
            add(new LongConst(data));
        }
    }

    public void acquireDouble(double data) {
        if (!values.containsKey(data)) {
            add(new DoubleConst(data));
        }
    }

    public void acquireValue(Object v) {
        if (v instanceof Number) {
            if (v instanceof Integer) {
                acquireInt((Integer) v);
            } else if (v instanceof Float) {
                acquireFloat((Float) v);
            } else if (v instanceof Double) {
                acquireDouble((Double) v);
            } else if (v instanceof Long) {
                acquireLong((Long) v);
            }
        } else if (v instanceof String) {
            acquireString((String) v);
        }
        throw new RuntimeException("unsupported type: " + v.getClass());
    }



    public void acquireClass(TypeInfo c) {
        acquire(c);
    }



    public void acquireString(String s) {
        if (!values.containsKey(s)) {
            add(new StringConst(s));
        }
    }


    public void acquireNameAndType(String name, String descriptor) {
        acquire(new NameAndTypeConst(name, descriptor));
    }


    public void acquireMethodType(TypeInfo rtype, TypeInfo... ptype) {
        acquire(new MethodTypeConst(rtype, ptype));
    }



    // invokeDynamic



    public void add(Const constant) {
        constants.put(constant, size);
        size += constant instanceof Const.DualSlot ? 2 : 1;
        if (constant instanceof UTFConst) {
            strings.put(((UTFConst) constant).string(), (UTFConst) constant);
        } else if (constant instanceof Const.Value) {
            values.put(((Const.Value) constant).value(), (Const.Value) constant);
        } else {
            collect(constant);
        }

    }

    void collect() {
        for (Const c : constants.keySet().toArray(new Const[0])) {
            collect(c);
        }
    }

    private void collect(Const c) {
        switch (c.constantType()) {
            case CLASS:
                ((TypeInfo) c).collect(this);
                break;
            case STRING:
                ((StringConst) c).collect(this);
                break;
            case FIELDREF:
            case METHODREF:
            case INTERFACE_METHODREF:
            {
                MemberInfo info = (MemberInfo) c;
                acquireClass(info.getDeclaringClass());
                acquireNameAndType(info.getName(), info.getDescriptor());
                break;
            }
            case NAME_AND_TYPE:
                ((NameAndTypeConst) c).collect(this);
                break;
            case METHOD_HANDLE:
                ((MethodHandleConst) c).collect(this);
                break;
            case METHOD_TYPE:
                ((MethodTypeConst) c).collect(this);
                break;
            case DYNAMIC:
                throw new UnsupportedOperationException("not implement yet");
            case INVOKE_DYNAMIC:
                ((InvokeDynamicConst) c).collect(this);
                break;
            case MODULE:
            case PACKAGE:
                ((AbstractTypeConst) c).collect(this);
                break;
        }
    }

    private void write(Const c, ByteVector buf) {
        ConstType t = c.constantType();
        buf.putByte(t.tag);
        switch (t) {
            case UTF8:
                ((UTFConst) c).write(buf);
                break;
            case INTEGER:
                ((IntegerConst) c).write(buf);
                break;
            case FLOAT:
                ((FloatConst) c).write(buf);
                break;
            case LONG:
                ((LongConst) c).write(buf);
                break;
            case DOUBLE:
                ((DoubleConst) c).write(buf);
                break;
            case CLASS:
                ((TypeInfo) c).write(this, buf);
                break;
            case STRING:
                ((StringConst) c).write(this, buf);
                break;
            case FIELDREF:
            case METHODREF:
            case INTERFACE_METHODREF:
            {
                MemberInfo mth = (MemberInfo) c;
                buf.putShort(indexOf(mth.getDeclaringClass()));
                buf.putShort(indexOf(mth.getName(), mth.getDescriptor()));
                break;
            }
            case NAME_AND_TYPE:
                ((NameAndTypeConst) c).write(this, buf);
                break;
            case METHOD_HANDLE:
                ((MethodHandleConst) c).write(this, buf);
                break;
            case METHOD_TYPE:
                ((MethodTypeConst) c).write(this, buf);
                break;
            case DYNAMIC:
                throw new UnsupportedOperationException("not implement yet");
            case INVOKE_DYNAMIC:
                ((InvokeDynamicConst) c).write(this, buf);
                break;
            case MODULE:
            case PACKAGE:
                ((AbstractTypeConst) c).write(this, buf);
                break;
        }
    }

    @NotNull
    @Override
    public Iterator<Const> iterator() {
        return constants.keySet().iterator();
    }
}
