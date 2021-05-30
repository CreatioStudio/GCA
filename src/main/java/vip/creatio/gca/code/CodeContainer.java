package vip.creatio.gca.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.util.BiHashMap;

import vip.creatio.gca.util.ByteVector;
import java.util.*;

public class CodeContainer implements Iterable<OpCode> {

    private final Code code;
    private final ConstPool pool;
    private final List<OpCode> list = new ArrayList<>();
    private final Map<String, Label> labels = new HashMap<>();

    BiHashMap<Integer, OpCode> offsetMap;

    public CodeContainer(Code code) {
        this.code = code;
        this.pool = code.classFile().constPool();
    }

    public void parse(ClassFileParser pool, ByteVector buffer) {
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

    ConstPool constPool() {
        return pool;
    }

    ClassFile classFile() {
        return code.classFile();
    }

    Code codeAttr() {
        return code;
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
        return list.iterator();
    }
}
