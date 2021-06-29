package vip.creatio.gca.type;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.DeclaredMethod;
import vip.creatio.gca.TypeInfo;

import java.util.*;

// util class
public final class Types {

    public static String toBytecodeName(String binaryName) {
        return binaryName.replace('.', '/');
    }

    public static class Primitive extends ClassObjectInfo {
        
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

    public static TypeInfo get(String typeName) {
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
        //TODO: Array type
        switch (typeName) {
            case "I":
                return INT;
            case "C":
                return CHAR;
            case "S":
                return SHORT;
            case "Z":
                return BOOL;
            case "F":
                return FLOAT;
            case "D":
                return DOUBLE;
            case "V":
                return VOID;
            case "J":
                return LONG;
            case "B":
                return BYTE;
            default:
                return null;
        }
    }


    //TODO
    public static int operandSize(String type) {
        if (type.equals("double") || type.equals("long")) return 2;
        else if (type.equals("void")) return 0;
        return 1;
    }

    public static int operandSize(TypeInfo t) {
        if (t.equals(DOUBLE) || t.equals(LONG)) return 2;
        else if (t.equals(VOID)) return 0;
        return 1;
    }

    public static String toMethodSignature(TypeInfo[] signatures) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 1; i < signatures.length; i++) {
            sb.append(signatures[i].getInternalName());
        }
        sb.append(")");
        sb.append(signatures[0].getInternalName());
        return sb.toString();
    }

    public static String toMethodSignature(TypeInfo rtype, TypeInfo... ptype) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (TypeInfo type : ptype) {
            sb.append(type.getInternalName());
        }
        sb.append(")");
        sb.append(rtype.getInternalName());
        return sb.toString();
    }

    public static String toMethodGenericSignature(TypeFactory factory, DeclaredMethod mth) {
        StringBuilder sb = new StringBuilder();
        // add type variables decl if needed
        {
            TypeVariable[] vars = mth.getTypeParameters();
            if (vars.length != 0) {
                sb.append('<');
                for (TypeVariable var : vars) {
                    sb.append(var.getName());
                    for (Type bound : var.getBounds()) {
                        ClassInfo raw = factory.toType(bound).getImpl();
                        if (raw != null && raw.isInterface()) {
                            sb.append("::");
                        } else
                            sb.append(':');
                        sb.append(bound.getInternalName());
                    }
                }
                sb.append('>');
            }
        }
        // add parameter types
        sb.append('(');
        for (Type type : mth.getGenericParameterTypes()) {
            sb.append(type.getInternalName());
        }
        sb.append(')');
        // add return type
        sb.append(mth.getGenericReturnType().getInternalName());
        // add generic exception if needed
        {
            Type[] etype = mth.getGenericExceptionTypes();
            if (!Arrays.equals(etype, mth.getExceptionTypes())) {
                for (Type type : etype) {
                    sb.append('^');
                    sb.append(type.getInternalName());
                }
            }
        }
        return sb.toString();
    }

    public static boolean equals(TypeInfo t1, TypeInfo t2) {
        return t1.equals(t2);
    }

    public static boolean equals(MethodInfo m1, MethodInfo m2) {
        return m1.getName().equals(m2.getName())
                && m1.getParameterCount() == m2.getParameterCount()
                && m1.getDescriptor().equals(m2.getDescriptor());
    }

    public static boolean withSignature(MethodInfo mth, TypeInfo rtype, TypeInfo... ptype) {
        return mth.getReturnType().equals(rtype) && Arrays.equals(mth.getParameterTypes(), ptype);
    }

    public static boolean equals(FieldInfo f1, FieldInfo f2) {
        return f1.getName().equals(f2.getName()) && f1.getDescriptor().equals(f2.getDescriptor());
    }

    public static boolean withSignature(FieldInfo field, TypeInfo type) {
        return field.getType().equals(type);
    }

    public static void resolveMethodSignature(TypeFactory factory, DeclaredMethod mth, String str) {
        byte[] raw = str.getBytes();
        int[] ptr = new int[1];
        // method type parameter decl
        ClassFile decl = mth.getDeclaringClass();
        {
            List<TypeVariable> varList = resolveTypeVarDecl(factory, decl, raw, ptr);
            if (!varList.isEmpty()) mth.setTypeParameters(varList);
        }

        List<Type> paraType = new ArrayList<>();
        // parameter declaration
        if (raw[ptr[0]] == '(') {
            ptr[0]++;
            while (raw[ptr[0]] != ')') {
                paraType.add(nextInternalSignature(factory, decl, null,raw, ptr));
            }
            ++ptr[0];
        } else {
            throw new RuntimeException("Illegal method signature: " + str);
        }
        mth.setParameterTypes(paraType);

        ++ptr[0];// skip ')'

        Type returnType = nextInternalSignature(factory, decl, null, raw, ptr);

        // parse exceptions
        if (ptr[0] < raw.length) {
            List<Type> exce = new ArrayList<>();
            do {
                if (raw[ptr[0]] == '^') {
                    ptr[0]++;
                    exce.add(nextInternalSignature(factory, decl, null, raw, ptr));
                } else {
                    throw new RuntimeException("Illegal exception signature: '" + (char) raw[ptr[0]] + "'");
                }
            } while (ptr[0] < raw.length);
            mth.setExceptionTypes(exce.toArray(new Type[0]));
        }
    }

    private static List<TypeVariable>
    resolveTypeVarDecl(TypeFactory factory, GenericDeclaration decl, byte[] raw, int[] ptr) {
        if (raw[0] == '<') {
            List<TypeVariable> list = new ArrayList<>();
            ptr[0] = 1;

            while (raw[ptr[0]] != '>') {
                int index = ptr[0];
                while (raw[index++] != ':');
                String name = new String(raw, ptr[0], index - ptr[0]);
                ptr[0] = index + 1;
                if (raw[ptr[0]] == ':') ++ptr[0];   // interface double mark
                Type t = nextInternalSignature(factory, decl, null, raw, ptr);

                TypeVariable var;
                if (raw[ptr[0]] == ':') {
                    if (raw[ptr[0] + 1] == ':') ++ptr[0];   // interface double mark
                    List<Type> range = new ArrayList<>();
                    range.add(t);
                    do {
                        ++ptr[0];
                        range.add(nextInternalSignature(factory, decl, null, raw, ptr));
                    } while (raw[ptr[0]] == ':');
                    var = TypeVariable.make(decl, name, range);
                } else {
                    var = TypeVariable.make(decl, name, t);
                }
                list.add(var);
            };

            ++ptr[0];
            return list;
        }
        return Collections.emptyList();
    }

    private static Type
    nextInternalSignature(TypeFactory factory, GenericDeclaration decl, Type upper, byte[] raw, int[] indexPtr) {
        switch (raw[indexPtr[0]]) {
            case 'I':
                indexPtr[0]++;
                return INT;
            case 'B':
                indexPtr[0]++;
                return BYTE;
            case 'C':
                indexPtr[0]++;
                return CHAR;
            case 'Z':
                indexPtr[0]++;
                return BOOL;
            case 'J':
                indexPtr[0]++;
                return LONG;
            case 'F':
                indexPtr[0]++;
                return FLOAT;
            case 'D':
                indexPtr[0]++;
                return DOUBLE;
            case 'V':
                indexPtr[0]++;
                return VOID;
            case 'S':
                indexPtr[0]++;
                return SHORT;
            case '*':       // wildcard
                indexPtr[0]++;
                return WildcardType.ANY;
            case '-':       // super
                indexPtr[0]++;
                return WildcardType.makeLower(nextInternalSignature(factory, decl, upper, raw, indexPtr));
            case '+':       // extends
                indexPtr[0]++;
                return WildcardType.makeUpper(nextInternalSignature(factory, decl, upper, raw, indexPtr));
            case 'L':       // class type
            {
                int start = indexPtr[0] + 1;
                int index = start;
                for (; index < raw.length; index++) {
                    byte b = raw[index];
                    // non-generic type
                    if (b == ';') {
                        String str = new String(raw, start, index - start).replace('/', '.');

                        indexPtr[0] = index + 1;
                        return factory.toType(str);
                    }
                    // generic type
                    else if (b == '<') {
                        indexPtr[0] = index + 1;

                        String str = new String(raw, start, index - start).replace('/', '.');
                        TypeInfo rawType = factory.toType(str);

                        ParameterizedType.Mutable mutable = ParameterizedType.makeMutable(rawType, (Collection<Type>) null, upper);

                        List<Type> generics = new ArrayList<>();
                        do {
                            generics.add(nextInternalSignature(factory, decl, mutable, raw, indexPtr));
                        } while (raw[indexPtr[0]] != '>');

                        mutable.setTypeArguments(generics);
                        indexPtr[0] += 2; // (HERE)>; -> >;(HERE)
                        return mutable;
                    }
                }
                throw new RuntimeException("malformed reference signature: '" + new String(raw, start - 1, index - start - 1) + "'");
            }
            case 'T':       // type variable
            {
                //Type variable
                int start = indexPtr[0] + 1;
                int index = start;
                while (index < raw.length) {
                    if (raw[index] == ';') {
                        String str = new String(raw, start, index - start);

                        indexPtr[0] = index + 1;

                        TypeVariable var = decl.getTypeParameter(str);
                        if (var == null) throw new IllegalArgumentException("undeclared type parameter: " + str);
                        return var;
                    }
                    index++;
                }
            }
            case '[':       // array type
            {
                indexPtr[0]++;
                Type t = nextInternalSignature(factory, decl, upper, raw, indexPtr);
                return ArrayType.make(t);
            }
            default:
                throw new IllegalArgumentException("Unknown token: '" + (char) raw[indexPtr[0]] + "'");
        }
    }
}
