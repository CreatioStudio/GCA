package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Pair;

/**
 * If LineNumberTable attributes are present in the attributes table of a
 * given Code attribute, then they may appear in any order. Furthermore,
 * multiple LineNumberTable attributes may together represent a given line
 * of a source file; that is, LineNumberTable attributes need not be
 * one-to-one with source lines.
 */
public class LineNumberTable extends TableAttribute<Pair<Integer, Integer>> {

    public LineNumberTable(ClassFile classFile) {
        super(classFile);
    }

    public static LineNumberTable parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        LineNumberTable inst = new LineNumberTable(container.classFile());
        inst.checkContainerType(container);

        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(new Pair<>(buffer.getUShort(), buffer.getUShort()));
        }
        return inst;
    }

    public void add(int start, int lineNumber) {
        for (Pair<Integer, Integer> item : items) {
            if (item.getKey() == start) {
                item.setValue(lineNumber);
                return;
            }
        }
        items.add(new Pair<>(start, lineNumber));
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, Code.class);
    }

    @Override
    public String name() {
        return "LineNumberTable";
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Pair<Integer, Integer> i : items) {
            buffer.putShort(i.getKey());
            buffer.putShort(i.getValue());
        }
    }

    public static class IndexPair {

        private int startPc;
        private int lineNum;

        IndexPair(int startPc, int lineNum) {
            this.startPc = startPc;
            this.lineNum = lineNum;
        }

        IndexPair(ByteVector buffer) {
            startPc = buffer.getUShort();
            lineNum = buffer.getUShort();
        }

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public int getLineNum() {
            return lineNum;
        }

        public void setLineNum(int lineNum) {
            this.lineNum = lineNum;
        }

        private void write(ByteVector buffer) {
            buffer.putShort(startPc);
            buffer.putShort(lineNum);
        }

        @Override
        public String toString() {
            return "{start_pc=" + startPc + ",line_num=" + lineNum + '}';
        }
    }
}
