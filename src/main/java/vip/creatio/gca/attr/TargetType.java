package vip.creatio.gca.attr;

import java.util.NoSuchElementException;

public enum TargetType {



    //##### ClassFile start #####//

    /** type parameter declaration of generic class or interface */
    GENERIC_TYPE_TYPEPAR(0x00, 0, 1),//type_parameter_target

    /**
     * type in extends or implements clause of class declaration (including the direct superclass or direct
     * superinterface of an anonymous class declaration), or in extends clause of interface declaration
     */
    SUPER_CLASS(0x10, 1, 2),//supertype_target

    /** type in bound of type parameter declaration of generic class or interface */
    GENERIC_TYPE_BOUND(0x11, 2, 2),//type_parameter_bound_target

    //##### ClassFile end #####//




    //##### FieldInfo start #####//

    /** type in field or record component declaration */
    COMPONENT(0x13, 3, 0),//empty_target

    //##### FieldInfo end #####//




    //##### MethodInfo start #####//

    /** type parameter declaration of generic method or constructor */
    GENERIC_METHOD_TYPEPAR(0x01, 0, 1),//type_parameter_target

    /** type in bound of type parameter declaration of generic method or constructor */
    GENERIC_METHOD_BOUND(0x12, 2, 2),//type_parameter_bound_target

    /** return type of method, or type of newly constructed object */
    RETURN(0x14, 3, 0),//empty_target

    /** receiver type of method or constructor */
    RECEIVER(0x15, 3, 0),//empty_target

    /** type in formal parameter declaration of method, constructor, or lambda expression */
    FORMAL_PAR(0x16, 4, 1),//formal_parameter_target

    /** type in throws clause of method or constructor */
    METHOD_THROWS(0x17, 5, 2),//throws_target

    //##### MethodInfo end #####//




    //##### Code start #####//

    /** type in local variable declaration */
    LOCAL_VAR(0x40, 6, -1),//localvar_target

    /** type in resource variable declaration */
    RESOURCE_VAR(0x41, 6, -1),//localvar_target

    /** type in exception parameter declaration */
    EXCEPTION_PARAMETER(0x42, 7, 2),//catch_target

    /** type in instanceof expression */
    INSTANCEOF(0x43, 8, 2),//offset_target

    /** type in new expression */
    NEW_EXPRESSION(0x44, 8, 2),//offset_target
    
    /** type in method reference expression using ::new */
    NEW_REF_EXPRESSION(0x45, 8, 2),//offset_target
    
    /** type in method reference expression using ::Identifier */
    METHOD_REF_EXPRESSION(0x46, 8, 2),//offset_target

    /** type in cast expression */
    CAST_EXPRESSION(0x47, 9, 3),//type_argument_target

    /** type argument for generic constructor in new expression or explicit constructor invocation statement */
    GENERIC_NEW_ARGUMENT(0x48, 9, 3),//type_argument_target

    /** type argument for generic method in method invocation expression */
    GENERIC_METHOD_ARGUMENT(0x49, 9, 3),//type_argument_target

    /** type argument for generic constructor in method reference expression using ::new */
    GENERIC_NEW_REF_ARGUMENT(0x4A, 9, 3),//type_argument_target

    /** type argument for generic method in method reference expression using ::Identifier */
    GENERIC_METHOD_REF_ARGUMENT(0x4B, 9, 3);//type_argument_target

    //##### Code end #####//

    //===  Item Types  ===//

    /**
     * The type_parameter_target item indicates that an annotation appears on the declaration of the i'th type
     * parameter of a generic class, generic interface, generic method, or generic constructor.
     *
     * type_parameter_target {
     *     u1 type_parameter_index;
     * }
     *
     * The value of the type_parameter_index item specifies which type parameter declaration is annotated.
     * A type_parameter_index value of 0 specifies the first type parameter declaration.
     */
    public static final int TARGET_TYPE_PARAMETER = 0;

    /**
     * The supertype_target item indicates that an annotation appears on a type in the extends or implements
     * clause of a class or interface declaration.
     *
     * supertype_target {
     *     u2 supertype_index;
     * }
     *
     * A supertype_index value of 65535 specifies that the annotation appears on the superclass in an extends
     * clause of a class declaration.
     *
     * Any other supertype_index value is an index into the interfaces array of the enclosing ClassFile structure,
     * and specifies that the annotation appears on that superinterface in either the implements clause of a class
     * declaration or the extends clause of an interface declaration.
     */
    public static final int TARGET_SUPERTYPE = 1;

    /**
     * The type_parameter_bound_target item indicates that an annotation appears on the i'th bound of the j'th
     * type parameter declaration of a generic class, interface, method, or constructor.
     *
     * type_parameter_bound_target {
     *     u1 type_parameter_index;
     *     u1 bound_index;
     * }
     *
     * The value of the of type_parameter_index item specifies which type parameter declaration has an annotated
     * bound. A type_parameter_index value of 0 specifies the first type parameter declaration.
     *
     * The value of the bound_index item specifies which bound of the type parameter declaration indicated by
     * type_parameter_index is annotated. A bound_index value of 0 specifies the first bound of a type parameter
     * declaration.
     *
     * The type_parameter_bound_target item records that a bound is annotated, but does not record the type which
     * constitutes the bound. The type may be found by inspecting the class signature or method signature stored
     * in the appropriate Signatur
     */
    public static final int TARGET_TYPE_PARAMETER_BOUND = 2;

    /**
     * The empty_target item indicates that an annotation appears on either the type in a field declaration,
     * the type in a record component declaration, the return type of a method, the type of a newly constructed
     * object, or the receiver type of a method or constructor.
     *
     * empty_target {
     * }
     *
     * Only one type appears in each of these locations, so there is no per-type information to represent in
     * the target_info union.
     */
    public static final int TARGET_EMPTY = 3;

    /**
     * The formal_parameter_target item indicates that an annotation appears on the type in a formal parameter
     * declaration of a method, constructor, or lambda expression.
     *
     * formal_parameter_target {
     *     u1 formal_parameter_index;
     * }
     *
     * The value of the formal_parameter_index item specifies which formal parameter declaration has an annotated
     * type. A formal_parameter_index value of i may, but is not required to, correspond to the i'th parameter
     * descriptor in the method descriptor (§4.3.3).
     *
     * The formal_parameter_target item records that a formal parameter's type is annotated, but does not record
     * the type itself. The type may be found by inspecting the method descriptor, although a formal_parameter_index
     * value of 0 does not always indicate the first parameter descriptor in the method descriptor; see the note in
     * §4.7.18 for a similar situation involving the parameter_annotations table.
     */
    public static final int TARGET_FORMAL_PARAMETER = 4;

    /**
     * The throws_target item indicates that an annotation appears on the i'th type in the throws clause of a
     * method or constructor declaration.
     *
     * throws_target {
     *     u2 throws_type_index;
     * }
     *
     * The value of the throws_type_index item is an index into the exception_index_table array of the Exceptions
     * attribute of the method_info structure enclosing the RuntimeVisibleTypeAnnotations attribute.
     */
    public static final int TARGET_THROWS = 5;

    /**
     * The localvar_target item indicates that an annotation appears on the type in a local variable declaration,
     * including a variable declared as a resource in a try-with-resources statement.
     *
     * localvar_target {
     *     u2 table_length;
     *     {   u2 start_pc;
     *         u2 length;
     *         u2 index;
     *     } table[table_length];
     * }
     *
     * The value of the table_length item gives the number of entries in the table array. Each entry indicates a
     * range of code array offsets within which a local variable has a value. It also indicates the index into the
     * local variable array of the current frame at which that local variable can be found. Each entry contains the
     * following three items:
     *
     * start_pc, length
     *      The given local variable has a value at indices into the code array in the interval
     *      [start_pc, start_pc + length), that is, between start_pc inclusive and start_pc + length exclusive.
     *
     * index
     *      The given local variable must be at index in the local variable array of the current frame.
     *
     *      If the local variable at index is of type double or long, it occupies both index and index + 1.
     *
     * A table is needed to fully specify the local variable whose type is annotated, because a single local variable
     * may be represented with different local variable indices over multiple live ranges. The start_pc, length, and
     * index items in each table entry specify the same information as a LocalVariableTable attribute.
     *
     * The localvar_target item records that a local variable's type is annotated, but does not record the type itself.
     * The type may be found by inspecting the appropriate LocalVariableTable attribute.
     */
    public static final int TARGET_LOCALVAR = 6;

    /**
     * The catch_target item indicates that an annotation appears on the i'th type in an exception parameter declaration.
     *
     * catch_target {
     *     u2 exception_table_index;
     * }
     *
     * The value of the exception_table_index item is an index into the exception_table array of the Code attribute
     * enclosing the RuntimeVisibleTypeAnnotations attribute.
     *
     * The possibility of more than one type in an exception parameter declaration arises from the multi-catch
     * clause of the try statement, where the type of the exception parameter is a union of types (JLS §14.20).
     * A compiler usually creates one exception_table entry for each type in the union, which allows the catch_target
     * item to distinguish them. This preserves the correspondence between a type and its annotations.
     */
    public static final int TARGET_CATCH = 7;

    /**
     * The offset_target item indicates that an annotation appears on either the type in an instanceof expression
     * or a new expression, or the type before the :: in a method reference expression.
     *
     * offset_target {
     *     u2 offset;
     * }
     *
     * The value of the offset item specifies the code array offset of either the bytecode instruction
     * corresponding to the instanceof expression, the new bytecode instruction corresponding to the new
     * expression, or the bytecode instruction corresponding to the method reference expression.
     */
    public static final int TARGET_OFFSET = 8;

    /**
     * The type_argument_target item indicates that an annotation appears either on the i'th type in a cast
     * expression, or on the i'th type argument in the explicit type argument list for any of the following:
     * a new expression, an explicit constructor invocation statement, a method invocation expression,
     * or a method reference expression.
     *
     * type_argument_target {
     *     u2 offset;
     *     u1 type_argument_index;
     * }
     *
     * The value of the offset item specifies the code array offset of either the bytecode instruction corresponding
     * to the cast expression, the new bytecode instruction corresponding to the new expression, the bytecode
     * instruction corresponding to the explicit constructor invocation statement, the bytecode instruction corresponding
     * to the method invocation expression, or the bytecode instruction corresponding to the method reference expression.
     *
     * For a cast expression, the value of the type_argument_index item specifies which type in the cast operator
     * is annotated. A type_argument_index value of 0 specifies the first (or only) type in the cast operator.
     *
     * The possibility of more than one type in a cast expression arises from a cast to an intersection type.
     *
     * For an explicit type argument list, the value of the type_argument_index item specifies which type argument
     * is annotated. A type_argument_index value of 0 specifies the first type argument.
     */
    public static final int TARGET_TYPE_ARGUMENT = 9;

    //===  Item Types  ===//


    private final byte tag;
    private final int type;
    private final int size; //-1 - variable length

    TargetType(int tag, int type, int size) {
        this.tag = (byte) tag;
        this.type = type;
        this.size = size;
    }

    public byte getTag() {
        return tag;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public static TargetType fromTag(int tag) {
        byte b = (byte) tag;
        for (TargetType v : values()) {
            if (v.tag == b) return v;
        }
        throw new NoSuchElementException("No element found with tag " + tag);
    }
}
