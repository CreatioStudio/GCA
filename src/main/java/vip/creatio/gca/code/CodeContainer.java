package vip.creatio.gca.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.DeclaredMethod;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.constant.InvokeDynamicConst;
import vip.creatio.gca.constant.RefConst;
import vip.creatio.gca.type.Types;
import vip.creatio.gca.util.BiHashMap;

import vip.creatio.gca.util.ByteVector;
import java.util.*;
import java.util.function.Consumer;

public class CodeContainer implements Iterable<OpCode> {

    private final DeclaredMethod mth;
    private final ConstPool pool;
    private final ArrayList<OpCode> list = new ArrayList<>();
    private final Map<String, Label> labels = new HashMap<>();

    // caches
    BiHashMap<Integer, OpCode> offsetMap;
    private int modCount = 0, lastModCount = -1;
    private int maxStack, maxLocals;


    public CodeContainer(DeclaredMethod mth) {
        this.mth = mth;
        this.pool = mth.classFile().constPool();
    }

    public void parse(ClassFileParser pool, ByteVector buffer) {
        // Use data in class file if code logic doesn't change
        this.maxStack = buffer.getUShort();
        this.maxLocals = buffer.getUShort();
        this.lastModCount = modCount;

        offsetMap = new BiHashMap<>();
        int len = buffer.getInt();
        int startingOffset = buffer.position();
        int remaining = len;
        while (remaining > 0) {
            OpCode code = OpCode.parse(pool, this, offsetMap, startingOffset, buffer);
            list.add(code);
            remaining = len - (buffer.position() - startingOffset);
        }

        // parse
        for (OpCode op : list) {
            op.parse();
        }
    }

    public void parseFinished() {
        offsetMap = null;
    }

    public int indexOf(OpCode code) {
        return list.indexOf(code);
    }

    public OpCode fromIndex(int index) {
        return list.get(index);
    }

    public void add(int index, OpCode code) {
        list.add(index, code);
        modCount++;
    }

    public void add(OpCode code) {
        list.add(code);
        modCount++;
    }

    public OpCode getFirst() {
        return list.get(0);
    }

    public OpCode getLast() {
        return list.get(list.size() - 1);
    }

    public void clear() {
        list.clear();
        modCount++;
    }

    public boolean hasNext(OpCode code) {
        return next(code) != null;
    }

    public OpCode next(OpCode code) {
        boolean take = false;
        for (OpCode op : list) {
            if (take) {
                return op;
            }
            if (op == code) {
                take = true;
            }
        }
        return null;
    }

    public boolean hasPrev(OpCode code) {
        return prev(code) != null;
    }

    public OpCode prev(OpCode code) {
        OpCode prev = null;
        for (OpCode op : list) {
            if (op == code) return prev;
            prev = op;
        }
        return prev;
    }

    public Label getLabel(String name) {
        return labels.get(name);
    }

    public Label getLabel(OpCode code) {
        for (Label l : labels.values()) {
            if (l.getAnchor() == code) return l;
        }
        return null;
    }

    public Label visitLabel(OpCode code) {
        for (Label l : labels.values()) {
            if (l.getAnchor() == code) return l;
        }
        return addLabel(code);
    }


    public Collection<Label> getLabels() {
        return labels.values();
    }

    public Set<String> getLabelNames() {
        return labels.keySet();
    }

    public @Nullable OpCode fromOffset(int offset) {
        if (offsetMap != null) return offsetMap.get(offset);

        int sum = 0;
        for (OpCode op : list) {
            if (sum == offset) return op;
            else sum += op.byteSize();
        }
        return null;
    }

    public @Nullable OpCode fromOffsetNearest(int offset) {
        if (offsetMap != null) {
            OpCode nearest = null;
            int delta = Integer.MAX_VALUE;
            for (Map.Entry<Integer, OpCode> e : offsetMap.entrySet()) {
                int d = offset - e.getKey();
                if (d < 0) continue;
                if (d < delta) {
                    delta = d;
                    nearest = e.getValue();
                }
            }
            return nearest;
        } else {
            OpCode nearest = null;
            int delta = Integer.MAX_VALUE;
            int sum = 0;
            for (OpCode op : list) {
                int d = offset - sum;
                if (d < 0) return nearest;
                if (d < delta) {
                    delta = d;
                    nearest = op;
                }
                sum += op.byteSize();
            }
            return nearest;
        }
    }

    public int offsetOf(OpCode code) {
        if (offsetMap != null) return offsetMap.reverse().get(code);

        int sum = 0;
        for (OpCode op : list) {
            if (op.equals(code)) return sum;
            else sum += op.byteSize();
        }
        return -1;
    }

    public Label addLabel(OpCode anchor) {
        return addLabel("L" + (labels.size() + 1), anchor);
    }

    public Label addLabel(Label lb) {
        labels.put(lb.getName(), lb);
        return lb;
    }

    public Label addLabel(String name, OpCode anchor) {
        Label label = labels.get(name);
        if (label == null) {
            labels.put(name, label = new Label(name, anchor));
        } else {
            label.setAnchor(anchor);
        }
        return label;
    }

    public boolean renameLabel(String name, String newName) {
        Label label = labels.remove(name);
        if (label == null) return false;
        label.setName(newName);
        labels.put(newName, label);
        return true;
    }

    // get or calculate max stack depth
    public int getMaxStack() {
        if (modCount == lastModCount) {
            return maxStack;
        } else {
            reCacheStackAndLocals();
            return maxStack;
        }
    }

    // get or calculate max locals
    public int getMaxLocals() {
        if (modCount == lastModCount) {
            return maxLocals;
        } else {
            reCacheStackAndLocals();
            return maxLocals;
        }
    }

    private void reCacheStackAndLocals() {
        int maxLocals = 0;
        for (String type : mth.getParameterTypes()) {
            maxLocals += Types.operandSize(type);
        }
        if (!mth.flaggedStatic()) maxLocals++;
        int maxStack = 0;
        int stack = 0;
        for (OpCode op : list) {
            int size = 0;
            if ((op.type().getFlag() & OpCode.FLAG_STORE) != 0) {
                int locSiz = 0;
                switch (op.type()) {
                    case ISTORE:
                    case FSTORE:
                    case ASTORE: {
                        NumberOpCode code = (NumberOpCode) op;
                        locSiz = code.data() + 1;
                        break;
                    }
                    case DSTORE:
                    case LSTORE: {
                        NumberOpCode code = (NumberOpCode) op;
                        locSiz = code.data() + 2;
                        break;
                    }
                    case ISTORE_0:
                    case FSTORE_0:
                    case ASTORE_0:
                        locSiz = 1;
                        break;
                    case ISTORE_1:
                    case FSTORE_1:
                    case ASTORE_1:
                    case DSTORE_0:
                    case LSTORE_0:
                        locSiz = 2;
                        break;
                    case ISTORE_2:
                    case FSTORE_2:
                    case ASTORE_2:
                    case DSTORE_1:
                    case LSTORE_1:
                        locSiz = 3;
                        break;
                    case ISTORE_3:
                    case FSTORE_3:
                    case ASTORE_3:
                    case DSTORE_2:
                    case LSTORE_2:
                        locSiz = 4;
                        break;
                    case DSTORE_3:
                    case LSTORE_3:
                        locSiz = 5;
                        break;
                }
                if (locSiz > maxLocals) maxLocals = locSiz;
                size = op.type().getStackGrow();
            } else {
                switch (op.type()) {
                    case GETSTATIC:
                        size++; // no objectref
                    case GETFIELD: {
                        size--; // objectref
                        ConstOpCode code = (ConstOpCode) op;
                        RefConst field = (RefConst) code.getConstant();
                        size += Types.operandSize(field.getValueType());
                        break;
                    }
                    case PUTSTATIC:
                        size++; // no objectref
                    case PUTFIELD: {
                        size--; // objectref
                        ConstOpCode code = (ConstOpCode) op;
                        RefConst field = (RefConst) code.getConstant();
                        size -= Types.operandSize(field.getValueType());
                        break;
                    }

                    case INVOKESTATIC:
                        size++; // no objectref
                    case INVOKEVIRTUAL:
                    case INVOKESPECIAL:
                    case INVOKEINTERFACE: {
                        size--; // objectref
                        InvocationOpCode code = (InvocationOpCode) op;
                        RefConst mth = code.getRef();
                        for (String type : mth.getParameterTypes()) {
                            size -= Types.operandSize(type);
                        }
                        size += Types.operandSize(mth.getValueType());  // return type
                        break;
                    }
                    case INVOKEDYNAMIC: {
                        InvokeDynamicOpCode code = (InvokeDynamicOpCode) op;
                        InvokeDynamicConst id = code.getRef();
                        for (String type : id.getParameterTypes()) {
                            size -= Types.operandSize(type);
                        }
                        size += Types.operandSize(id.getValueType());  // return type
                        break;
                    }
                    case MULTIANEWARRAY: {
                        NewArrayOpCode code = (NewArrayOpCode) op;
                        size -= code.getDimension();
                        break;
                    }
                    default:
                        size = op.type().getStackGrow();
                }
            }
            stack += size;
            if (stack > maxStack) maxStack = stack;
        }
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.lastModCount = modCount;
    }


    ConstPool constPool() {
        return pool;
    }

    ClassFile classFile() {
        return mth.classFile();
    }

    public void write(ByteVector buffer) {
        // make new cache offset map
        int sum = 0;
        offsetMap = new BiHashMap<>();
        for (OpCode op : list) {
            offsetMap.put(sum, op);
            sum += op.byteSize();
        }

        for (OpCode op : list) {
            try {
                op.serialize(buffer);
            } catch (BytecodeException e) {
                throw e.detailedOpCode().setLocation("Failed to parse opcode " + op.type());
            } catch (Exception e) {
                throw new BytecodeException(this, offsetMap.reverse().get(op), e, "Failed to parse opcode " + op.type());
            }
        }
    }

    public void collect() {
        for (OpCode op : list) {
            op.collect();
        }
    }

    @NotNull
    @Override
    public Iterator<OpCode> iterator() {
        return new Iterator<>() {

            private final Iterator<OpCode> itr = list.iterator();

            @Override
            public boolean hasNext() {
                return itr.hasNext();
            }

            @Override
            public OpCode next() {
                return itr.next();
            }

            @Override
            public void remove() {
                itr.remove();
                modCount++;
            }

            @Override
            public void forEachRemaining(Consumer<? super OpCode> action) {
                itr.forEachRemaining(action);
            }
        };
    }
}
