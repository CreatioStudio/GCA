package vip.creatio.gca.code;

import vip.creatio.gca.util.Util;

import java.util.Iterator;

public class BytecodeException extends RuntimeException {

    private final CodeContainer container;
    private final int offset;
    private boolean detailedOp;
    private String msg;

    private String locName;
    private String detail;

    BytecodeException(CodeContainer container, int offset, Exception cause, String msg) {
        super(cause);
        this.container = container;
        this.offset = offset;
        this.msg = msg;
    }

    BytecodeException(CodeContainer container, int offset, String msg) {
        this.container = container;
        this.offset = offset;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(msg);
        sb.append("(offset 0x");
        sb.append(Util.toHex(offset));
        sb.append(")");
        sb.append("\nLocation: ").append(container.classFile().getClassName());
        if (locName != null) {
            sb.append(".").append(locName);
        }
        sb.append("\nBytecodes:");

        Iterator<OpCode> iter = container.iterator();
        // move to the proper position
        while (iter.hasNext() && offset - iter.next().getOffset() > 40);

        for (int i = 0; i < 40; i++) {
            if (!iter.hasNext()) break;
            OpCode op = iter.next();
            String off = Integer.toString(op.getOffset());
            try {
                String name = detailedOp ? op.toString() : op.type().name().toLowerCase();
                sb.append("\n     ").append(off).append(" ".repeat(5 - off.length()))
                        .append(name);
            } catch (Exception e) {
                sb.append("\n  -> ").append(off).append(" ".repeat(5 - off.length()))
                        .append(op.type().name().toLowerCase());
                break;
            }
        }

        sb.append("\n\n");
        if (detail != null) sb.append("Details: ").append(detail).append('\n');
        sb.append("Stacktrace:");
        return sb.toString();
    }

    public BytecodeException detailedOpCode() {
        detailedOp = true;
        return this;
    }

    public BytecodeException setLocation(String name) {
        this.locName = name;
        return this;
    }

    public BytecodeException setDetailInfo(String info) {
        this.detail = info;
        return this;
    }

    public BytecodeException setMessage(String msg) {
        this.msg = msg;
        return this;
    }
}
