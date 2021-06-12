package vip.creatio.gca.type;

import java.util.*;

// util class
public final class Types {
    
    private static class Primitive extends ClassObjectInfo {
        
        private final String sigName;
        
        private Primitive(Class<?> clazz, String sigName) {
            super(clazz);
            this.sigName = sigName;
        }

        @Override
        public String getInternalName() {
            return sigName;
        }
    }

    private static final Map<String, TypeInfo> BASIC_CLASS_MAP = new HashMap<>();
    
    public static final TypeInfo INT            = add(new Primitive(int.class, "I"));
    public static final TypeInfo SHORT          = add(new Primitive(short.class, "S"));
    public static final TypeInfo CHAR           = add(new Primitive(char.class, "C"));
    public static final TypeInfo BYTE           = add(new Primitive(byte.class, "B"));
    public static final TypeInfo LONG           = add(new Primitive(long.class, "J"));
    public static final TypeInfo FLOAT          = add(new Primitive(float.class, "F"));
    public static final TypeInfo DOUBLE         = add(new Primitive(double.class, "D"));
    public static final TypeInfo BOOL           = add(new Primitive(boolean.class, "Z"));
    public static final TypeInfo VOID           = add(new Primitive(void.class, "V"));
    
    public static final TypeInfo INT_WRAPPER    = add(ClassObjectInfo.make(Integer.class));
    public static final TypeInfo SHORT_WRAPPER  = add(ClassObjectInfo.make(Short.class));
    public static final TypeInfo CHAR_WRAPPER   = add(ClassObjectInfo.make(Character.class));
    public static final TypeInfo BYTE_WRAPPER   = add(ClassObjectInfo.make(Byte.class));
    public static final TypeInfo LONG_WRAPPER   = add(ClassObjectInfo.make(Long.class));
    public static final TypeInfo FLOAT_WRAPPER  = add(ClassObjectInfo.make(Float.class));
    public static final TypeInfo DOUBLE_WRAPPER = add(ClassObjectInfo.make(Double.class));
    public static final TypeInfo BOOL_WRAPPER   = add(ClassObjectInfo.make(Boolean.class));
    public static final TypeInfo VOID_WRAPPER   = add(ClassObjectInfo.make(Void.class));

    public static final TypeInfo STRING         = add(ClassObjectInfo.make(String.class));
    public static final TypeInfo STRING_BUILDER = add(ClassObjectInfo.make(StringBuilder.class));
    public static final TypeInfo OBJECT         = add(ClassObjectInfo.make(Object.class));
    public static final TypeInfo THREAD         = add(ClassObjectInfo.make(Thread.class));
    public static final TypeInfo SYSTEM         = add(ClassObjectInfo.make(System.class));
    public static final TypeInfo THROWABLE      = add(ClassObjectInfo.make(Throwable.class));
    public static final TypeInfo MATH           = add(ClassObjectInfo.make(Math.class));
    public static final TypeInfo RUNNABLE       = add(ClassObjectInfo.make(Runnable.class));
    public static final TypeInfo PROCESS        = add(ClassObjectInfo.make(Process.class));
    public static final TypeInfo NUMBER         = add(ClassObjectInfo.make(Number.class));
    public static final TypeInfo ITERABLE       = add(ClassObjectInfo.make(Iterable.class));
    public static final TypeInfo ITERATOR       = add(ClassObjectInfo.make(Iterator.class));
    public static final TypeInfo ENUM           = add(ClassObjectInfo.make(Enum.class));
    public static final TypeInfo COMPARABLE     = add(ClassObjectInfo.make(Comparable.class));
    public static final TypeInfo CLONEABLE      = add(ClassObjectInfo.make(Cloneable.class));
    public static final TypeInfo CLASS_LOADER   = add(ClassObjectInfo.make(ClassLoader.class));
    public static final TypeInfo CLASS          = add(ClassObjectInfo.make(Class.class));
    public static final TypeInfo CHAR_SEQUENCE  = add(ClassObjectInfo.make(CharSequence.class));
    public static final TypeInfo EXCEPTION      = add(ClassObjectInfo.make(Exception.class));
    public static final TypeInfo ERROR          = add(ClassObjectInfo.make(Error.class));

    public static final TypeInfo INT_ARRAY      = add(ArrayType.make(INT));
    public static final TypeInfo SHORT_ARRAY    = add(ArrayType.make(SHORT));
    public static final TypeInfo CHAR_ARRAY     = add(ArrayType.make(CHAR));
    public static final TypeInfo BYTE_ARRAY     = add(ArrayType.make(BYTE));
    public static final TypeInfo LONG_ARRAY     = add(ArrayType.make(LONG));
    public static final TypeInfo FLOAT_ARRAY    = add(ArrayType.make(FLOAT));
    public static final TypeInfo DOUBLE_ARRAY   = add(ArrayType.make(DOUBLE));
    public static final TypeInfo BOOL_ARRAY     = add(ArrayType.make(BOOL));
    public static final TypeInfo OBJECT_ARRAY   = add(ArrayType.make(OBJECT));

    public static final TypeInfo ARRAYS         = add(ClassObjectInfo.make(Arrays.class));
    public static final TypeInfo ARRAY_LIST     = add(ClassObjectInfo.make(ArrayList.class));
    public static final TypeInfo COLLECTION     = add(ClassObjectInfo.make(Collection.class));
    public static final TypeInfo COLLECTIONS    = add(ClassObjectInfo.make(Collections.class));
    public static final TypeInfo COMPARATOR     = add(ClassObjectInfo.make(Comparator.class));
    public static final TypeInfo ENUM_MAP       = add(ClassObjectInfo.make(EnumMap.class));
    public static final TypeInfo ENUM_SET       = add(ClassObjectInfo.make(EnumSet.class));
    public static final TypeInfo HASH_MAP       = add(ClassObjectInfo.make(HashMap.class));
    public static final TypeInfo HASH_SET       = add(ClassObjectInfo.make(HashSet.class));
    public static final TypeInfo LINKED_LIST    = add(ClassObjectInfo.make(LinkedList.class));
    public static final TypeInfo LIST           = add(ClassObjectInfo.make(List.class));
    public static final TypeInfo MAP            = add(ClassObjectInfo.make(Map.class));
    public static final TypeInfo OBJECTS        = add(ClassObjectInfo.make(Objects.class));
    public static final TypeInfo OPTIONAL       = add(ClassObjectInfo.make(Optional.class));
    public static final TypeInfo QUEUE          = add(ClassObjectInfo.make(Queue.class));
    public static final TypeInfo RANDOM         = add(ClassObjectInfo.make(Random.class));
    public static final TypeInfo RANDOM_ACCESS  = add(ClassObjectInfo.make(RandomAccess.class));
    public static final TypeInfo SCANNER        = add(ClassObjectInfo.make(Scanner.class));
    public static final TypeInfo SET            = add(ClassObjectInfo.make(Set.class));
    public static final TypeInfo STACK          = add(ClassObjectInfo.make(Stack.class));
    public static final TypeInfo STRING_JOINER  = add(ClassObjectInfo.make(StringJoiner.class));
    public static final TypeInfo TREE_MAP       = add(ClassObjectInfo.make(TreeMap.class));
    public static final TypeInfo TREE_SET       = add(ClassObjectInfo.make(TreeSet.class));
    public static final TypeInfo UUID           = add(ClassObjectInfo.make(UUID.class));
    
    public static final TypeInfo ASSERTION_ERROR                    = add(ClassObjectInfo.make(AssertionError.class));
    public static final TypeInfo ABSTRACT_METHOD_ERROR              = add(ClassObjectInfo.make(AbstractMethodError.class));
    public static final TypeInfo ARITHMETIC_EXCEPTION               = add(ClassObjectInfo.make(ArithmeticException.class));
    public static final TypeInfo CLASS_CAST_EXCEPTION               = add(ClassObjectInfo.make(ClassCastException.class));
    public static final TypeInfo CLASS_FORMAT_ERROR                 = add(ClassObjectInfo.make(ClassFormatError.class));
    public static final TypeInfo INTERNAL_ERROR                     = add(ClassObjectInfo.make(InternalError.class));
    public static final TypeInfo INTERRUPTED_EXCEPTION              = add(ClassObjectInfo.make(InterruptedException.class));
    public static final TypeInfo NEGATIVE_ARRAY_SIZE_EXCEPTION      = add(ClassObjectInfo.make(NegativeArraySizeException.class));
    public static final TypeInfo NO_CLASS_DEF_FOUND_ERROR           = add(ClassObjectInfo.make(NoClassDefFoundError.class));
    public static final TypeInfo NULL_POINTER_EXCEPTION             = add(ClassObjectInfo.make(NullPointerException.class));
    public static final TypeInfo RUNTIME_EXCEPTION                  = add(ClassObjectInfo.make(RuntimeException.class));
    public static final TypeInfo SECURITY_EXCEPTION                 = add(ClassObjectInfo.make(SecurityException.class));

    static TypeInfo add(TypeInfo type) {
        BASIC_CLASS_MAP.put(type.getTypeName(), type);
        return type;
    }

    static TypeInfo get(String typeName) {
        if (typeName.startsWith("java") || typeName.startsWith("jdk")) {
            TypeInfo type = BASIC_CLASS_MAP.get(typeName);
            if (type == null) {
                try {
                    Class<?> cls = Class.forName(typeName, false, ClassLoader.getSystemClassLoader());
                    type = ClassObjectInfo.make(cls);
                    return add(type);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            return type;
        }
        return null;
    }


    //TODO
    public static int operandSize(String type) {
        if (type.equals("double") || type.equals("long")) return 2;
        else if (type.equals("void")) return 0;
        return 1;
    }

    public static String toMethodSignature(Type[] signatures) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 1; i < signatures.length; i++) {
            sb.append(signatures[i].getInternalSignature());
        }
        sb.append(")");
        sb.append(signatures[0].getInternalSignature());
        return sb.toString();
    }
}
