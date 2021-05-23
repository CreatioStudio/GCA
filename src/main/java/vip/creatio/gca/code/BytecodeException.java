package vip.creatio.gca.code;

import vip.creatio.gca.util.Util;

public class BytecodeException extends RuntimeException {

    private final CodeContainer container;
    private final int offset;
    private final String msg;

    private String mthName;
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
        if (mthName != null) {
            sb.append("\nMethod: ").append(mthName);
        }
        sb.append("\nBytecodes:");
        for (OpCode op : container) {
            String off = Integer.toString(op.getOffset());
            sb.append("\n     ").append(off).append('.').append(" ".repeat(4 - off.length())).append(op.type().name().toLowerCase());
        }
        if (detail != null) sb.append("\n\nDetails: ").append(detail).append('\n');
        return sb.toString();
    }

    public BytecodeException setMethodName(String name) {
        this.mthName = name;
        return this;
    }

    public BytecodeException setDetailInfo(String info) {
        this.detail = info;
        return this;
    }
}
