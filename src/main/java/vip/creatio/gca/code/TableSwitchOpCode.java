package vip.creatio.gca.code;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TableSwitchOpCode extends OpCode {

    // 0 - default index, 1... - jump index
    private int[] data;

    private int startValue;

    private Label defaultBranch;
    private final List<Label> branches = new ArrayList<>();

    TableSwitchOpCode(CodeContainer codes, int startingOffset, ByteVector buffer) {
        super(codes);
        int pc = buffer.position() - startingOffset;
        int padding = Util.align(pc + 1) - pc; // 0 - 3 bytes padding
        buffer.position(buffer.position() + padding);
        int defaultIndex = pc + buffer.getInt();

        startValue = buffer.getInt();

        int to = buffer.getInt();
        int count = to - startValue + 1;
        System.out.println("TO: " + to + ", start: " + startValue + ", count: " + count + ", padding: " + padding);

        data = new int[count + 1];
        data[0] = defaultIndex;
        for (int i = 0; i < count; i++) {
            data[i + 1] = pc + buffer.getInt();
        }
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public Label getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(Label defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public int getBranchSize() {
        return branches.size();
    }

    public void setBranch(int value, Label branch) {
        branches.set(value, branch);
    }

    public void addBranch(Label branch) {
        branches.add(branch);
    }

    public void addBranch(int value, Label branch) {
        branches.add(value, branch);
    }

    public List<Label> getBranches() {
        return branches;
    }

    @Override
    public int byteSize() {
        if (data != null) return ((getOffset() + 1) % 4) + (2 + data.length << 2);
        return ((getOffset() + 1) % 4) + ((2 + branches.size()) << 2);
    }

    @Override
    void parse() {
        this.defaultBranch = codes.addLabel(codes.fromOffset(data[0]));
        for (int i = 1; i < data.length; i++) {
            branches.add(codes.addLabel(codes.fromOffset(data[i])));
        }
        // let gc process the useless data
        data = null;
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        int padding = (getOffset() + 1) % 4;
        buffer.position(buffer.position() + padding);
        buffer.putInt(defaultBranch.getOffset());
        buffer.putInt(startValue);
        buffer.putInt(startValue + branches.size() - 1);
        for (Label branch : branches) {
            buffer.putInt(branch.getOffset() - getOffset());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tableswitch from ").append(startValue).append(" to ").append(branches.size() + startValue);
        for (int i = 0; i < branches.size(); i++) {
            sb.append("\n        ").append(i).append(": ").append(branches.get(i).getOffset());
        }
        sb.append("\n        default: ").append(defaultBranch.getOffset());
        return sb.toString();
    }

    @Override
    public OpCodeType type() {
        return OpCodeType.TABLESWITCH;
    }
}
