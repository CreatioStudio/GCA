package vip.creatio.gca.attr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.*;
import vip.creatio.gca.code.BytecodeException;
import vip.creatio.gca.code.CodeContainer;
import vip.creatio.gca.code.Label;
import vip.creatio.gca.code.OpCode;
import vip.creatio.gca.constant.ClassConst;

import vip.creatio.gca.util.ByteVector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Code attribute contains the Java Virtual Machine instructions
 * and auxiliary information for a single method, instance
 * initialization method, or class or interface initialization method.
 * Every Java Virtual Machine implementation must recognize Code
 * attributes. If the method is either native or abstract, its method_info
 * structure must not have a Code attribute. Otherwise, its method_info
 * structure must have exactly one Code attribute.
 */
public class Code extends Attribute implements AttributeContainer, Iterable<OpCode> {

    private int maxStack;
    private int maxLocals;
    private final CodeContainer codes = new CodeContainer(this);
    private final List<ExceptionTable> tables = new ArrayList<>();
    private final List<Attribute> attributes = new ArrayList<>();

    private Code(AttributeContainer container) {
        super(container);
    }

    public Code(DeclaredMethod mth) {
        this((AttributeContainer) mth);
    }

    public static Code parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        Code c = new Code(container);

        c.maxStack = buffer.getShort() & 0xFFFF;
        c.maxLocals = buffer.getShort() & 0xFFFF;

        c.codes.parse(pool, buffer);

        {
            int tableSize = buffer.getShort();
            for (int i = 0; i < tableSize; i++) {
                c.tables.add(c.new ExceptionTable(pool, buffer));
            }
        }

        {
            int attrSize = buffer.getShort();
            for (int i = 0; i < attrSize; i++) {
                c.attributes.add(pool.resolveAttribute(c, buffer));
            }
        }

        c.codes.parseFinished();
        return c;
    }

    public int maxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int maxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public List<ExceptionTable> exceptionTables() {
        return tables;
    }

    public List<Attribute> attributes() {
        return attributes;
    }

    @NotNull
    @Override
    public Iterator<OpCode> iterator() {
        return codes.iterator();
    }

    public int offsetOf(OpCode code) {
        int sum = 0;
        for (OpCode op : codes) {
            if (op == code) return sum;
            else sum += op.byteSize();
        }
        return sum;
    }

    public @Nullable OpCode fromOffset(int offset) {
        return codes.fromOffset(offset);
    }

    public @Nullable OpCode fromOffsetNearest(int offset) {
        return codes.fromOffsetNearest(offset);
    }

    public int indexOf(OpCode code) {
        return codes.indexOf(code);
    }

    public OpCode get(int index) {
        return codes.fromIndex(index);
    }

    public Label getLabel(OpCode code) {
        return codes.getLabel(code);
    }

    public Label visitLabel(OpCode code) {
        Label l = getLabel(code);
        if (l == null) l = codes.addLabel(code);
        return l;
    }

    public Label getLabel(String name) {
        return codes.getLabel(name);
    }

    @Override
    protected void collect() {
        super.collect();
        codes.collect();
        collectConstants();
    }

    @Override
    public void writeData(ByteVector buffer) {
        buffer.putShort((short) maxStack);
        buffer.putShort((short) maxLocals);
        int pos = buffer.position();
        buffer.skip(4);
        codes.write(buffer);
        int len = buffer.position() - pos - 4;
        buffer.putInt(pos, len);
        buffer.putShort((short) tables.size());
        for (ExceptionTable tab : tables) {
            tab.write(buffer);
        }
        AttributeContainer.super.writeAttributes(buffer);
    }

    public LocalVariableTable variableTable() {
        return getOrAddAttribute("LocalVariableTable", () -> new LocalVariableTable(this));
    }

    public LineNumberTable lineNumberTable() {
        return getOrAddAttribute("LineNumberTable", () -> new LineNumberTable(this));
    }

    public StackMapTable stackMapTable() {
        return getOrAddAttribute("StackMapTable", () -> new StackMapTable(this));
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, DeclaredMethod.class);
    }

    @Override
    public String name() {
        return "Code";
    }

    @Override
    public String toString() {
        return "Code{max_stack=" + maxStack + ",max_locals=" + maxLocals + ",codes=" + codes +
                ",exception_table=" + tables + ",attributes=" + attributes + '}';
    }

    public final class ExceptionTable {

        private int startPc;
        private int endPc;
        private int handlerPc;
        // null means catch anything(keyword finally)
        private ClassConst catchType = null;

        ExceptionTable(int start, int end, int handler, ClassConst type) {
            this.startPc = start;
            this.endPc = end;
            this.handlerPc = handler;
            this.catchType = type;
        }

        ExceptionTable(ClassFileParser pool, ByteVector buffer) {
            this.startPc = buffer.getShort() & 0xFFFF;
            this.endPc = buffer.getShort() & 0xFFFF;
            this.handlerPc = buffer.getShort() & 0xFFFF;
            this.catchType = (ClassConst) pool.get(buffer.getShort() & 0xFFFF);
        }

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public int getEndPc() {
            return endPc;
        }

        public void setEndPc(int endPc) {
            this.endPc = endPc;
        }

        public int getHandlerPc() {
            return handlerPc;
        }

        public void setHandlerPc(int handlerPc) {
            this.handlerPc = handlerPc;
        }

        public ClassConst getCatchType() {
            return catchType;
        }

        public void setCatchType(ClassConst catchType) {
            this.catchType = catchType;
        }

        public int getIndex() {
            return tables.indexOf(this);
        }

        private void write(ByteVector buffer) {
            buffer.putShort((short) startPc);
            buffer.putShort((short) endPc);
            buffer.putShort((short) handlerPc);
            buffer.putShort(catchType == null ? 0 : catchType.index());
        }

        @Override
        public String toString() {
            return "ExceptionTable{start_pc=" + startPc +
                    ",end_pc=" + endPc +
                    ",handler_pc=" + handlerPc +
                    ", catch_type=" + (catchType == null ? "any" : catchType) + '}';
        }
    }
}
