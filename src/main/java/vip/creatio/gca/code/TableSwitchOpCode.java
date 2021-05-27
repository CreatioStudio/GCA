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
        int padding = Util.align(pc) - pc; // 0 - 3 bytes padding
        buffer.skip(padding);
        int defaultIndex = pc + buffer.getInt() - 1 /* original size */;

        startValue = buffer.getInt();

        int to = buffer.getInt();
        int count = to - startValue + 1;

        data = new int[count + 1];
        data[0] = defaultIndex;
        for (int i = 0; i < count; i++) {
            data[i + 1] = pc + buffer.getInt() - 1 /* original size */;
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
        int offset = getOffset() + 1;
        if (data != null) return Util.align(offset) - offset + (2 + data.length << 2) + 1 /* original size */;
        return Util.align(offset) - offset + ((3 + branches.size()) << 2) + 1 /* original size */;
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
        int offset = getOffset();
        int padding = Util.align(offset + 1) - offset - 1;
        buffer.skip(padding);
        try {
            buffer.putInt(defaultBranch.getOffset() - offset);
            buffer.putInt(startValue);
            buffer.putInt(startValue + branches.size() - 1);
            for (Label branch : branches) {
                buffer.putInt(branch.getOffset() - offset);
            }
        } catch (Exception e) {
            throw new BytecodeException(codes, offset, e, "")
                    .setDetailInfo("default_branch=" + defaultBranch.getAnchor()
                    + ", start_value=" + startValue + ", branches=" + branches);
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
