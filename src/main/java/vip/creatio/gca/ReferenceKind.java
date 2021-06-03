package vip.creatio.gca;

public enum ReferenceKind {
    REF_getField((byte) 1, ConstType.FIELDREF),
    REF_getStatic((byte) 2, ConstType.FIELDREF),
    REF_putField((byte) 3, ConstType.FIELDREF),
    REF_putStatic((byte) 4, ConstType.FIELDREF),
    REF_invokeVirtual((byte) 5, ConstType.METHODREF),
    REF_invokeStatic((byte) 6, ConstType.METHODREF),
    REF_invokeSpecial((byte) 7, ConstType.METHODREF),
    REF_newInvokeSpecial((byte) 8, ConstType.METHODREF),
    REF_invokeInterface((byte) 9, ConstType.INTERFACE_METHODREF);

    private final byte id;
    private final ConstType type;

    ReferenceKind(byte id, ConstType type) {
        this.id = id;
        this.type = type;
    }

    public static ReferenceKind fromId(byte id) {
        for (ReferenceKind kind : values()) {
            if (kind.id == id) return kind;
        }
        return null;
    }

    public byte getId() {
        return id;
    }

    public ConstType getType() {
        return type;
    }
}
