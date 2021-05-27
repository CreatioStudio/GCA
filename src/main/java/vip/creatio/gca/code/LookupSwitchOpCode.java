package vip.creatio.gca.code;

import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Util;

import java.util.HashMap;
import java.util.Map;

public class LookupSwitchOpCode extends OpCode {

    // 0 - default index, n, n+1... - match result + jump index
    private int[] data;

    private Label defaultBranch;
    private final Map<Integer, Label> branches = new HashMap<>();

    LookupSwitchOpCode(CodeContainer codes, int startingOffset, ByteVector buffer) {
        super(codes);
        int pc = buffer.position() - startingOffset;
        int padding = Util.align(pc) - pc; // 0 - 3 bytes padding
        buffer.skip(padding);
        int defaultIndex = pc + buffer.getInt() - 1 /* original size */;

        int numPairs = buffer.getInt() * 2;

        data = new int[numPairs + 1];
        data[0] = defaultIndex;
        for (int i = 0; i < numPairs; i += 2) {
            data[i + 1] = pc + buffer.getInt();
            data[i + 2] = pc + buffer.getInt() - 1 /* original size */;
        }
    }

    public int getPairCount() {
        return branches.size();
    }

    public Label getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(Label defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public void addBranch(int value, Label branch) {
        branches.put(value, branch);
    }

    public Map<Integer, Label> getBranches() {
        return branches;
    }

    @Override
    public int byteSize() {
        int offset = getOffset() + 1;

        if (data != null)
            return Util.align(offset) - offset + ((1 + data.length) << 2) + 1 /* original size */;

        return Util.align(offset) - offset + 8 + (branches.size() << 3) + 1 /* original size */;
    }

    @Override
    void parse() {
        this.defaultBranch = codes.addLabel(codes.fromOffset(data[0]));
        for (int i = 1; i < data.length; i += 2) {
            branches.put(data[i], codes.addLabel(codes.fromOffset(data[i + 1])));
        }
        // let gc process the useless data
        data = null;
    }

    @Override
    public void serialize(ByteVector buffer) {
        super.serialize(buffer);
        int offset = getOffset();
        int padding = Util.align(offset + 1) - offset - 1;
        System.out.println("Padding: " + padding);
        buffer.skip(padding);
        try {
            buffer.putInt(defaultBranch.getOffset());
            buffer.putInt(branches.size());
            for (Map.Entry<Integer, Label> entry : branches.entrySet()) {
                buffer.putInt(entry.getKey());
                buffer.putInt(entry.getValue().getOffset() - offset);
            }
        } catch (Exception e) {
            throw new BytecodeException(codes, offset, e, "")
                    .setDetailInfo("default_branch=" + defaultBranch.getAnchor()
                            + ", branches=" + branches);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("lookupswitch ").append(branches.size());
        for (Map.Entry<Integer, Label> entry : branches.entrySet()) {
            sb.append("\n        ").append(entry.getKey()).append(": ").append(entry.getValue().getOffset());
        }
        sb.append("\n        default: ").append(defaultBranch.getOffset());
        return sb.toString();
    }

    @Override
    public OpCodeType type() {
        return OpCodeType.LOOKUPSWITCH;
    }
}
