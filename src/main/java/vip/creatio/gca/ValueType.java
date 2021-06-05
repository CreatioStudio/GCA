package vip.creatio.gca;

import vip.creatio.gca.type.Type;

import java.util.List;
import java.util.NoSuchElementException;

public enum ValueType {
    BYTE('B', Byte.class, true),
    CHAR('C', Character.class, true),
    DOUBLE('D', Double.class, true),
    FLOAT('F', Float.class, true),
    INT('I', Integer.class, true),
    LONG('J', Long.class, true),
    SHORT('S', Short.class, true),
    BOOLEAN('Z', Boolean.class, true),
    STRING('s', String.class, true),
    ENUM('e', Type.class),
    CLASS('c', Type.class),
    ANNOTATION('@', Annotation.class),
    ARRAY('[', List.class);

    private final char tag;
    private final Class<?> clazz;
    private final boolean isValue;

    ValueType(char tag, Class<?> clazz, boolean isValue) {
        this.tag = tag;
        this.clazz = clazz;
        this.isValue = isValue;
    }

    ValueType(char tag, Class<?> clazz) {
        this(tag, clazz, false);
    }

    public char getTag() {
        return tag;
    }

    public boolean isValue() {
        return isValue;
    }

    public Class<?> getTypeClass() {
        return clazz;
    }

    public static ValueType fromTag(byte tag) {
        for (ValueType v : values()) {
            if (v.tag == tag) return v;
        }
        throw new NoSuchElementException("No constant found in ValueType with tag " + tag);
    }

    public static ValueType getValueType(Object v) {
        if (v == null) throw new NullPointerException();
        if (v instanceof String)
            return STRING;
        if (v instanceof Integer)
            return INT;
        if (v instanceof Long)
            return LONG;
        if (v instanceof Double)
            return DOUBLE;
        if (v instanceof Float)
            return FLOAT;
        if (v instanceof Short)
            return SHORT;
        if (v instanceof Byte)
            return BYTE;
        if (v instanceof Character)
            return CHAR;
        if (v instanceof Boolean)
            return BOOLEAN;
        throw new IllegalArgumentException(v.getClass().getName());
    }

    public static boolean isValue(Object obj) {
        return obj instanceof String || obj instanceof Integer || obj instanceof Long
                || obj instanceof Double || obj instanceof Float || obj instanceof Short
                || obj instanceof Byte || obj instanceof Character;
    }

}
