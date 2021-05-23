package vip.creatio.gca.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.Serializer;
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

        offsetMap = null;
    }

    public int indexOf(OpCode code) {
        return list.indexOf(code);
    }

    public OpCode fromIndex(int index) {
        return list.get(index);
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

    public void write(ByteVector buffer) {
        // make new cache offset map
        int sum = 0;
        offsetMap = new BiHashMap<>();
        for (OpCode op : list) {
            offsetMap.put(sum, op);
            sum += op.byteSize();
        }

        for (OpCode op : list) {
            op.serialize(buffer);
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
