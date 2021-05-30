package vip.creatio.gca.constant;

public enum ConstType {
    UTF8((byte) 0x01),
    INTEGER((byte) 0x03),
    FLOAT((byte) 0x04),
    LONG((byte) 0x05),
    DOUBLE((byte) 0x06),
    CLASS((byte) 0x07),
    STRING((byte) 0x08),
    FIELDREF((byte) 0x09),
    METHODREF((byte) 0x0A),
    INTERFACE_METHODREF((byte) 0x0B),
    NAME_AND_TYPE((byte) 0x0C),
    METHOD_HANDLE((byte) 0x0F),
    METHOD_TYPE((byte) 0x10),
    INVOKE_DYNAMIC((byte) 0x12),
    MODULE((byte) 0x13),
    PACKAGE((byte) 0x14),
    ;

    final byte tag;

    ConstType(byte tag) {
        this.tag = tag;
    }

    public static ConstType fromTag(byte tag) {
        for (ConstType value : values()) {
            if (value.tag == tag) return value;
        }
        return null;
    }
}
