package vip.creatio.gca;

import java.util.NoSuchElementException;

public enum ValueType {
    BYTE('B', true),
    CHAR('C', true),
    DOUBLE('D', true),
    FLOAT('F', true),
    INT('I', true),
    LONG('J', true),
    SHORT('S', true),
    BOOLEAN('Z', true),
    STRING('s', true),
    ENUM('e'),
    CLASS('c'),
    ANNOTATION('@'),
    ARRAY('[');

    private final char tag;
    private final boolean isValue;

    ValueType(char tag, boolean isValue) {
        this.tag = tag;
        this.isValue = isValue;
    }

    ValueType(char tag) {
        this(tag, false);
    }

    public char getTag() {
        return tag;
    }

    public boolean isValue() {
        return isValue;
    }

    public static ValueType fromTag(byte tag) {
        for (ValueType v : values()) {
            if (v.tag == tag) return v;
        }
        throw new NoSuchElementException("No constant found in ValueType with tag " + tag);
    }

}
