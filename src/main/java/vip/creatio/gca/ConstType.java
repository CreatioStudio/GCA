package vip.creatio.gca;

public enum ConstType {
    UTF8                    (0x01, -1),
    INTEGER                 (0x03, 5),
    FLOAT                   (0x04, 5),
    LONG                    (0x05, 9),
    DOUBLE                  (0x06, 9),
    CLASS                   (0x07, 3),
    STRING                  (0x08, 3),
    FIELDREF                (0x09, 5),
    METHODREF               (0x0A, 5),
    INTERFACE_METHODREF     (0x0B, 5),
    NAME_AND_TYPE           (0x0C, 5),
    METHOD_HANDLE           (0x0F, 4),
    METHOD_TYPE             (0x10, 3),
    DYNAMIC                 (0x11, 5),
    INVOKE_DYNAMIC          (0x12, 5),
    MODULE                  (0x13, 3),
    PACKAGE                 (0x14, 3),
    ;

    final byte tag;
    final int size;

    ConstType(int tag, int size) {
        this.tag = (byte) tag;
        this.size = size;
    }

    public static ConstType fromTag(byte tag) {
        for (ConstType value : values()) {
            if (value.tag == tag) return value;
        }
        return null;
    }
}
