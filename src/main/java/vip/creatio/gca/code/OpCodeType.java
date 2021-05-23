package vip.creatio.gca.code;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum OpCodeType {

    /* Place holder */
    /**
     * Operation: Do nothing
     * Format:
     *      nop
     * Forms:
     *      nop = 0 (0x0)
     * Operand Stack: No change
     * Description: Do nothing
     */
    NOP             ((byte) 0x0, 1),       //Do nothing

    /* Push const */
    /**
     * Operation: Push null
     * Format:
     *      aconst_null
     * Forms:
     *      aconst_null = 1 (0x1)
     * Operand Stack:
     *      ...->
     *      ..., null
     * Description: Push the null object reference onto the operand stack.
     * Notes: The Java Virtual Machine does not mandate a concrete value for null.
     */
    ACONST_NULL     ((byte) 0x1, 1),       //Push null

    /**
     * Operation: Push int constant
     * Format:
     *      iconst_<i>
     * Forms:
     *      iconst_m1 = 2 (0x2)
     *      iconst_0 = 3 (0x3)
     *      iconst_1 = 4 (0x4)
     *      iconst_2 = 5 (0x5)
     *      iconst_3 = 6 (0x6)
     *      iconst_4 = 7 (0x7)
     *      iconst_5 = 8 (0x8)
     * Operand Stack:
     *      ...->
     *      ..., <i>
     * Description:
     *      Push the int constant <i> (-1, 0, 1, 2, 3, 4 or 5) onto the operand stack.
     * Notes:
     *      Each of this family of instructions is equivalent to bipush <i> for the respective value of <i>, except
     *      that the operand <i> is implicit.
     */
    ICONST_M1       ((byte) 0x2, 1),       //Push int constant (-1)
    ICONST_0        ((byte) 0x3, 1),       //Push int constant
    ICONST_1        ((byte) 0x4, 1),       //Push int constant
    ICONST_2        ((byte) 0x5, 1),       //Push int constant
    ICONST_3        ((byte) 0x6, 1),       //Push int constant
    ICONST_4        ((byte) 0x7, 1),       //Push int constant
    ICONST_5        ((byte) 0x8, 1),       //Push int constant

    /**
     * Operation: Push long constant
     * Format:
     *      lconst_<l>
     * Forms:
     *      lconst_0 = 9 (0x9)
     *      lconst_1 = 10 (0xa)
     * Operand Stack:
     *      ...->
     *      ..., <l>
     * Description:
     *      Push the long constant <l> (0 or 1) onto the operand stack.
     */
    LCONST_0        ((byte) 0x9, 1),       //Push long constant
    LCONST_1        ((byte) 0xA, 1),       //Push long constant

    /**
     * Operation: Push float
     * Format:
     *      fconst_<f>
     * Forms:
     *      fconst_0 = 11 (0xb)
     *      fconst_1 = 12 (0xc)
     *      fconst_2 = 13 (0xd)
     * Operand Stack:
     *      ...->
     *      ..., <f>
     * Description:
     *      Push the float constant <f> (0.0, 1.0, or 2.0) onto the operand stack.
     */
    FCONST_0        ((byte) 0xB, 1),       //Push float
    FCONST_1        ((byte) 0xC, 1),       //Push float
    FCONST_2        ((byte) 0xD, 1),       //Push float

    /**
     * Operation: Push double
     * Format:
     *      dconst_<d>
     * Forms:
     *      dconst_0 = 14 (0xE)
     *      dconst_1 = 15 (0xF)
     * Operand Stack:
     *      ...->
     *      ..., <d>
     * Description:
     *      Push the double constant <d> (0.0 or 1.0) onto the operand stack.
     */
    DCONST_0        ((byte) 0xE, 1),       //Push double
    DCONST_1        ((byte) 0xF, 1),       //Push double


    /**
     * Operation: Push byte
     * Format:
     *      bipush
     *      byte
     * Forms:
     *      bipush = 16 (0x10)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The immediate byte is sign-extended to an int value. That value is pushed onto the operand stack.
     */
    BIPUSH          ((byte) 0x10, 2),      //Push byte

    /**
     * Operation: Push short
     * Format:
     *      sipush
     *      byte1
     *      byte2
     * Forms:
     *      sipush = 17 (0x11)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The immediate unsigned byte1 and byte2 values are assembled into an intermediate short where the value of the
     *      short is (byte1 << 8) | byte2. The intermediate value is then sign-extended to an int value. That value is
     *      pushed onto the operand stack.
     */
    SIPUSH          ((byte) 0x11, 3),      //Push short


    /**
     * Operation: Push item from run-time constant pool
     * Format:
     *      ldc
     *      index
     * Forms:
     *      ldc = 18 (0x12)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The index is an unsigned byte that must be a valid index into the run-time constant pool of the current class
     *      (§2.6). The run-time constant pool entry at index either must be a run-time constant of type int or float, or
     *      a reference to a string literal, or a symbolic reference to a class, method type, or method handle (§5.1).
     *
     *      If the run-time constant pool entry is a run-time constant of type int or float, the numeric value of that
     *      run-time constant is pushed onto the operand stack as an int or float, respectively.
     *
     *      Otherwise, if the run-time constant pool entry is a reference to an instance of class String representin
     *      tring literal (§5.1), then a reference to that instance, value, is pushed onto the operand stack.
     *
     *      Otherwise, if the run-time constant pool entry is a symbolic reference to a class (§5.1), then the named class
     *      is resolved (§5.4.3.1) and a reference to the Class object representing that class, value, is pushed onto the
     *      operand stack.
     *
     *      Otherwise, the run-time constant pool entry must be a symbolic reference to a method type or a method handle
     *      (§5.1). The method type or method handle is resolved (§5.4.3.5) and a reference to the resulting instance of
     *      java.lang.invoke.MethodType or java.lang.invoke.MethodHandle, value, is pushed onto the operand stack.
     *
     * Linking Exceptions:
     *      During resolution of a symbolic reference to a class, any of the exceptions pertaining to class resolution
     *      (§5.4.3.1) can be thrown.
     *
     *      During resolution of a symbolic reference to a method type or method handle, any of the exception pertaining
     *      to method type or method handle resolution (§5.4.3.5) can be thrown.
     *
     * Notes:
     *      The ldc instruction can only be used to push a value of type float taken from the float value set (§2.3.2)
     *      because a constant of type float in the constant pool (§4.4.4) must be taken from the float value set.
     */
    LDC             ((byte) 0x12, 2),      //Push item from run-time constant pool

    /**
     * Operation: Push item from run-time constant pool (wide index)
     * Format:
     *      ldc_w
     *      indexbyte1
     *      indexbyte2
     * Forms:
     *      ldc_w = 19 (0x13)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index into the run-time constant
     *      pool of the current class (§2.6), where the value of the index is calculated as (indexbyte1 << 8) | indexbyte2.
     *      The index must be a valid index into the run-time constant pool of the current class. The run-time constant
     *      pool entry at the index either must be a run-time constant of type int or float, or a reference to a string
     *      literal, or a symbolic reference to a class, method type, or method handle (§5.1).
     *
     *      If the run-time constant pool entry is a run-time constant of type int or float, the numeric value of that
     *      run-time constant is pushed onto the operand stack as an int or float, respectively.
     *
     *      Otherwise, if the run-time constant pool entry is a reference to an instance of class String representing a
     *      string literal (§5.1), then a reference to that instance, value, is pushed onto the operand stack.
     *
     *      Otherwise, if the run-time constant pool entry is a symbolic reference to a class (§4.4.1). The named class
     *      is resolved (§5.4.3.1) and a reference to the Class object representing that class, value, is pushed onto the
     *      operand stack.
     *
     *      Otherwise, the run-time constant pool entry must be a symbolic reference to a method type or a method handle
     *      (§5.1). The method type or method handle is resolved (§5.4.3.5) and a reference to the resulting instance of
     *      java.lang.invoke.MethodType or java.lang.invoke.MethodHandle, value, is pushed onto the operand stack.
     *
     * Linking Exceptions:
     *      During resolution of the symbolic reference to a class, any of the exceptions pertaining to class resolution
     *      (§5.4.3.1) can be thrown.
     *
     *      During resolution of a symbolic reference to a method type or method handle, any of the exception pertaining
     *      to method type or method handle resolution (§5.4.3.5) can be thrown.
     *
     * Notes:
     *      The ldc_w instruction is identical to the ldc instruction (§ldc) except for its wider run-time constant pool
     *      index.
     *
     *      The ldc_w instruction can only be used to push a value of type float taken from the float value set (§2.3.2)
     *      because a constant of type float in the constant pool (§4.4.4) must be taken from the float value set.
     */
    LDC_W           ((byte) 0x13, 3),      //Push item from run-time constant pool (wide index)

    /**
     * Operation: Push long or double from run-time constant pool (wide index)
     * Format:
     *      ldc2_w
     *      indexbyte1
     *      indexbyte2
     * Forms:
     *      ldc2_w = 20 (0x14)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index into the run-time constant
     *      pool of the current class (§2.6), where the value of the index is calculated as (indexbyte1 << 8) | indexbyte2.
     *      The index must be a valid index into the run-time constant pool of the current class. The run-time constant
     *      pool entry at the index must be a run-time constant of type long or double (§5.1). The numeric value of that
     *      run-time constant is pushed onto the operand stack as a long or double, respectively.
     *
     * Notes:
     *      Only a wide-index version of the ldc2_w instruction exists; there is no ldc2 instruction that pushes a long
     *      or double with a single-byte index.
     *
     *      The ldc2_w instruction can only be used to push a value of type double taken from the double value set (§2.3.2)
     *      because a constant of type double in the constant pool (§4.4.5) must be taken from the double value set.
     */
    LDC2_W          ((byte) 0x14, 3),      //Push long or double from run-time constant pool (wide index)

    /* Load from local variable */
    /**
     * Operation: Load primitive from local variable
     * Format:
     *      xload
     *      index
     * Forms:
     *      xload = 21 (0x15)
     * Operand Stack:
     *      ...->
     *      ..., value
     * Description:
     *      The index is an unsigned byte that must be an index into the local variable array of the current frame (§2.6).
     *      The local variable at index must contain an int. The value of the local variable at index is pushed onto the
     *      operand stack.
     *
     * Notes:
     *      The xload opcode can be used in conjunction with the wide instruction (§wide) to access a local variable
     *      using a two-byte unsigned index.
     */
    ILOAD           ((byte) 0x15, 2),      //Load int from local variable
    LLOAD           ((byte) 0x16, 2),      //Load long from local variable
    FLOAD           ((byte) 0x17, 2),      //Load float from local variable
    DLOAD           ((byte) 0x18, 2),      //Load double from local variable

    /**
     * Operation: Load reference from local variable
     * Format:
     *      dload
     *      index
     * Forms:
     *      dload = 24 (0x18)
     * Operand Stack:
     *      ...->
     *      ..., objectref
     * Description:
     *      The index is an unsigned byte that must be an index into the local variable array of the current frame (§2.6).
     *      The local variable at index must contain a reference. The objectref in the local variable at index is pushed
     *      onto the operand stack.
     *
     * Notes:
     *      The aload instruction cannot be used to load a value of type returnAddress from a local variable onto the
     *      operand stack. This asymmetry with the astore instruction (§astore) is intentional.
     *
     *      The aload opcode can be used in conjunction with the wide instruction (§wide) to access a local variable using
     *      a two-byte unsigned index.
     */
    ALOAD           ((byte) 0x19, 2),      //Load reference from local variable

    /** Fast loading */
    ILOAD_0         ((byte) 0x1A, 1),      //Load int from local variable
    ILOAD_1         ((byte) 0x1B, 1),      //Load int from local variable
    ILOAD_2         ((byte) 0x1C, 1),      //Load int from local variable
    ILOAD_3         ((byte) 0x1D, 1),      //Load int from local variable
    LLOAD_0         ((byte) 0x1E, 1),      //Load long from local variable
    LLOAD_1         ((byte) 0x1F, 1),      //Load long from local variable
    LLOAD_2         ((byte) 0x20, 1),      //Load long from local variable
    LLOAD_3         ((byte) 0x21, 1),      //Load long from local variable
    FLOAD_0         ((byte) 0x22, 1),      //Load float from local variable
    FLOAD_1         ((byte) 0x23, 1),      //Load float from local variable
    FLOAD_2         ((byte) 0x24, 1),      //Load float from local variable
    FLOAD_3         ((byte) 0x25, 1),      //Load float from local variable
    DLOAD_0         ((byte) 0x26, 1),      //Load double from local variable
    DLOAD_1         ((byte) 0x27, 1),      //Load double from local variable
    DLOAD_2         ((byte) 0x28, 1),      //Load double from local variable
    DLOAD_3         ((byte) 0x29, 1),      //Load double from local variable
    ALOAD_0         ((byte) 0x2A, 1),      //Load reference from local variable
    ALOAD_1         ((byte) 0x2B, 1),      //Load reference from local variable
    ALOAD_2         ((byte) 0x2C, 1),      //Load reference from local variable
    ALOAD_3         ((byte) 0x2D, 1),      //Load reference from local variable

    /** Array load */
    IALOAD          ((byte) 0x2E, 1),      //Load int from array
    LALOAD          ((byte) 0x2F, 1),      //Load long from array
    FALOAD          ((byte) 0x30, 1),      //Load float from array
    DALOAD          ((byte) 0x31, 1),      //Load double from array
    AALOAD          ((byte) 0x32, 1),      //Load reference from array
    BALOAD          ((byte) 0x33, 1),      //Load byte or boolean from array
    CALOAD          ((byte) 0x34, 1),      //Load char from array
    SALOAD          ((byte) 0x35, 1),      //Load short from array

    /** Store to local variable */
    ISTORE          ((byte) 0x36, 2),      //Store int into local variable
    LSTORE          ((byte) 0x37, 2),      //Store long into local variable
    FSTORE          ((byte) 0x38, 2),      //Store float into local variable
    DSTORE          ((byte) 0x39, 2),      //Store double into local variable
    ISTORE_0        ((byte) 0x3B, 1),      //Store int into local variable
    ISTORE_1        ((byte) 0x3C, 1),      //Store int into local variable
    ISTORE_2        ((byte) 0x3D, 1),      //Store int into local variable
    ISTORE_3        ((byte) 0x3E, 1),      //Store int into local variable
    LSTORE_0        ((byte) 0x3F, 1),      //Store long into local variable
    LSTORE_1        ((byte) 0x40, 1),      //Store long into local variable
    LSTORE_2        ((byte) 0x41, 1),      //Store long into local variable
    LSTORE_3        ((byte) 0x42, 1),      //Store long into local variable
    FSTORE_0        ((byte) 0x43, 1),      //Store float into local variable
    FSTORE_1        ((byte) 0x44, 1),      //Store float into local variable
    FSTORE_2        ((byte) 0x45, 1),      //Store float into local variable
    FSTORE_3        ((byte) 0x46, 1),      //Store float into local variable
    DSTORE_0        ((byte) 0x47, 1),      //Store double into local variable
    DSTORE_1        ((byte) 0x48, 1),      //Store double into local variable
    DSTORE_2        ((byte) 0x49, 1),      //Store double into local variable
    DSTORE_3        ((byte) 0x4A, 1),      //Store double into local variable

    /** Array store */
    ASTORE          ((byte) 0x3A, 2),      //Store reference into local variable
    ASTORE_0        ((byte) 0x4B, 1),      //Store reference into local variable
    ASTORE_1        ((byte) 0x4C, 1),      //Store reference into local variable
    ASTORE_2        ((byte) 0x4D, 1),      //Store reference into local variable
    ASTORE_3        ((byte) 0x4E, 1),      //Store reference into local variable
    IASTORE         ((byte) 0x4F, 1),      //Store into int array
    LASTORE         ((byte) 0x50, 1),      //Store into long array
    FASTORE         ((byte) 0x51, 1),      //Store into float array
    DASTORE         ((byte) 0x52, 1),      //Store into double array
    AASTORE         ((byte) 0x53, 1),      //Store into reference array
    BASTORE         ((byte) 0x54, 1),      //Store into byte or boolean array
    CASTORE         ((byte) 0x55, 1),      //Store into char array
    SASTORE         ((byte) 0x56, 1),      //Store into short array

    /** Number conversion */
    I2L             ((byte) 0x85, 1),      //Convert int to long
    I2F             ((byte) 0x86, 1),      //Convert int to float
    I2D             ((byte) 0x87, 1),      //Convert int to double
    L2I             ((byte) 0x88, 1),      //Convert long to int
    L2F             ((byte) 0x89, 1),      //Convert long to float
    L2D             ((byte) 0x8A, 1),      //Convert long to double
    F2I             ((byte) 0x8B, 1),      //Convert float to int
    F2L             ((byte) 0x8C, 1),      //Convert float to long
    F2D             ((byte) 0x8D, 1),      //Convert float to double
    D2I             ((byte) 0x8E, 1),      //Convert double to int
    D2L             ((byte) 0x8F, 1),      //Convert double to long
    D2F             ((byte) 0x90, 1),      //Convert double to float
    I2B             ((byte) 0x91, 1),      //Convert int to byte
    I2C             ((byte) 0x92, 1),      //Convert int to char
    I2S             ((byte) 0x93, 1),      //Convert int to short

    /** Number operation */
    //int number operation
    IADD            ((byte) 0x60, 1),      //Add int                       (+)
    ISUB            ((byte) 0x64, 1),      //Subtract int                  (-)
    IMUL            ((byte) 0x68, 1),      //Multiply int                  (*)
    IDIV            ((byte) 0x6C, 1),      //Divide int                    (/)
    IREM            ((byte) 0x70, 1),      //Remainder int                 (%)
    INEG            ((byte) 0x74, 1),      //Negate int                    (-)
    ISHL            ((byte) 0x78, 1),      //Shift left int                (<<)
    ISHR            ((byte) 0x7A, 1),      //Arithmetic shift right int    (>>)
    IUSHR           ((byte) 0x7C, 1),      //Logical shift right int       (>>>)
    IAND            ((byte) 0x7E, 1),      //Boolean bitwise AND int       (&)
    IOR             ((byte) 0x80, 1),      //Boolean bitwise OR int        (|)
    IXOR            ((byte) 0x82, 1),      //Boolean bitwise XOR int       (^)
    IINC            ((byte) 0x84, 3),      //Increment local variable by constant

    //long number operation
    LADD            ((byte) 0x61, 1),      //Add long                      (+)
    LSUB            ((byte) 0x65, 1),      //Subtract long                 (-)
    LMUL            ((byte) 0x69, 1),      //Multiply long                 (*)
    LDIV            ((byte) 0x6D, 1),      //Divide long                   (/)
    LREM            ((byte) 0x71, 1),      //Remainder long                (%)
    LNEG            ((byte) 0x75, 1),      //Negate long                   (-)
    LSHL            ((byte) 0x79, 1),      //Shift left long               (<<)
    LSHR            ((byte) 0x7B, 1),      //Arithmetic shift right long   (>>)
    LUSHR           ((byte) 0x7D, 1),      //Logical shift right long      (>>>)
    LAND            ((byte) 0x7F, 1),      //Boolean bitwise AND long      (&)
    LOR             ((byte) 0x81, 1),      //Boolean bitwise OR long       (|)
    LXOR            ((byte) 0x83, 1),      //Boolean bitwise XOR long      (^)

    //float number operation
    FADD            ((byte) 0x62, 1),      //Add float                     (+)
    FSUB            ((byte) 0x66, 1),      //Subtract float                (-)
    FMUL            ((byte) 0x6A, 1),      //Multiply float                (*)
    FDIV            ((byte) 0x6E, 1),      //Divide float                  (/)
    FREM            ((byte) 0x72, 1),      //Remainder float               (%)
    FNEG            ((byte) 0x76, 1),      //Negate float                  (-)

    //double number operation
    DADD            ((byte) 0x63, 1),      //Add double                    (+)
    DSUB            ((byte) 0x67, 1),      //Subtract double               (-)
    DMUL            ((byte) 0x6B, 1),      //Multiply double               (*)
    DDIV            ((byte) 0x6F, 1),      //Divide double                 (/)
    DREM            ((byte) 0x73, 1),      //Remainder double              (%)
    DNEG            ((byte) 0x77, 1),      //Negate double                 (-)

    /** Comparision */
    //Compare long                      //Compare long,  If value1 is equal to value2, the int value 0 is pushed onto
    LCMP            ((byte) 0x94, 1),      // the operand stack. If value1 is less than value2, the int value -1 is pushed
                                        // onto the operand stack.

    //Compare float                     //The fcmpg and fcmpl instructions differ only in their treatment of a comparison
    FCMPL           ((byte) 0x95, 1),      // involving NaN. NaN is unordered, so any float comparison fails if either or both
    FCMPG           ((byte) 0x96, 1),      // of its operands are NaN. With both fcmpg and fcmpl available, any float comparison
                                        // may be compiled to push the same result onto the operand stack whether the
                                        // comparison fails on non-NaN values or fails because it encountered a NaN.

    //Compare double                    /*The dcmpg and dcmpl instructions differ only in their treatment of a comparison
    DCMPL           ((byte) 0x97, 1),      // involving NaN. NaN is unordered, so any double comparison fails if either or
    DCMPG           ((byte) 0x98, 1),      // both of its operands are NaN. With both dcmpg and dcmpl available, any double
                                        // comparison may be compiled to push the same result onto the operand stack
                                        // whether the comparison fails on non-NaN values or fails because it encountered
                                        // a NaN.

    /** Condition */
    //Branch if int comparison with *zero* succeeds     (if<cond>)
    IFEQ            ((byte) 0x99, 3),      //if and only if value = 0
    IFNE            ((byte) 0x9A, 3),      //if and only if value ≠ 0
    IFLT            ((byte) 0x9B, 3),      //if and only if value < 0
    IFGE            ((byte) 0x9C, 3),      //if and only if value ≥ 0
    IFGT            ((byte) 0x9D, 3),      //if and only if value > 0
    IFLE            ((byte) 0x9E, 3),      //if and only if value ≤ 0

    //Branch if int comparison succeeds                 (if_icmp<cond>)
    IF_ICMPEQ       ((byte) 0x9F, 3),      //if and only if value1 = value2
    IF_ICMPNE       ((byte) 0xA0, 3),      //if and only if value1 ≠ value2
    IF_ICMPLT       ((byte) 0xA1, 3),      //if and only if value1 < value2
    IF_ICMPGE       ((byte) 0xA2, 3),      //if and only if value1 ≥ value2
    IF_ICMPGT       ((byte) 0xA3, 3),      //if and only if value1 > value2
    IF_ICMPLE       ((byte) 0xA4, 3),      //if and only if value1 ≤ value2

    //Branch if reference comparison succeeds           (if_acmp<cond>)
    IF_ACMPEQ       ((byte) 0xA5, 3),      //if and only if value1 = value2
    IF_ACMPNE       ((byte) 0xA6, 3),      //if and only if value1 ≠ value2

    //Special
    CHECKCAST       ((byte) 0xC0, 3),      //Check whether object is of given type
    INSTANCEOF      ((byte) 0xC1, 3),      //Determine if object is of given type
    IFNULL          ((byte) 0xC6, 3),      //Branch if reference is null
    IFNONNULL       ((byte) 0xC7, 3),      //Branch if reference not null

    /** Stack operation */
    //pop
    POP             ((byte) 0x57, 1),      //Pop the top operand stack value
    POP2            ((byte) 0x58, 1),      //Pop the top one or two operand stack values

    //duplicate
    DUP             ((byte) 0x59, 1),      //Duplicate the top operand stack value
    DUP_X1          ((byte) 0x5A, 1),      //Duplicate the top operand stack value and insert two values down
    DUP_X2          ((byte) 0x5B, 1),      //Duplicate the top operand stack value and insert two or three values down
    DUP2            ((byte) 0x5C, 1),      //Duplicate the top one or two operand stack values
    DUP2_X1         ((byte) 0x5D, 1),      //Duplicate the top one or two operand stack values and insert two or three values down
    DUP2_X2         ((byte) 0x5E, 1),      //Duplicate the top one or two operand stack values and insert two, three, or four values down

    //swap
    SWAP            ((byte) 0x5F, 1),      //Swap the top two operand stack values

    /** Route control */
    GOTO            ((byte) 0xA7, 3),      //Branch always
    GOTO_W          ((byte) 0xC8, 5),      //Branch always (wide index)
    JSR             ((byte) 0xA8, 3),      //Jump subroutine
    JSR_W           ((byte) 0xC9, 5),      //Jump subroutine (wide index)

    /** Thread and synchronization */
    MONITORENTER    ((byte) 0xC2, 1),      //Enter monitor for object  (Object.wait()/Object.notify())
    MONITOREXIT     ((byte) 0xC3, 1),      //Exit monitor for object   (synchronized)

    /** Switch and return */
    //return with or without value
    RET             ((byte) 0xA9, 2),      //Return from subroutine
    IRETURN         ((byte) 0xAC, 1),      //Return int from method
    LRETURN         ((byte) 0xAD, 1),      //Return long from method
    FRETURN         ((byte) 0xAE, 1),      //Return float from method
    DRETURN         ((byte) 0xAF, 1),      //Return double from method
    ARETURN         ((byte) 0xB0, 1),      //Return reference from method
    RETURN          ((byte) 0xB1, 1),      //Return void from method

    //switch
    /**
     * Operation
     *      Access jump table by index and jump
     *
     * Format
     *
     *      tableswitch
     *      <0-3 byte pad>
     *      defaultbyte1
     *      defaultbyte2
     *      defaultbyte3
     *      defaultbyte4
     *      lowbyte1
     *      lowbyte2
     *      lowbyte3
     *      lowbyte4
     *      highbyte1
     *      highbyte2
     *      highbyte3
     *      highbyte4
     *      jump offsets...
     * Forms
     *      tableswitch = 170 (0xaa)
     *
     * Operand Stack
     *      ..., index →
     *
     *      ...
     *
     * Description
     *      A tableswitch is a variable-length instruction. Immediately after the tableswitch opcode, between zero and three
     *      bytes must act as padding, such that defaultbyte1 begins at an address that is a multiple of four bytes from the
     *      start of the current method (the opcode of its first instruction). Immediately after the padding are bytes
     *      constituting three signed 32-bit values: default, low, and high. Immediately following are bytes constituting a
     *      series of high - low + 1 signed 32-bit offsets. The value low must be less than or equal to high. The high - low + 1
     *      signed 32-bit offsets are treated as a 0-based jump table. Each of these signed 32-bit values is constructed as
     *      (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4.
     *
     *      The index must be of type int and is popped from the operand stack. If index is less than low or index is greater
     *      than high, then a target address is calculated by adding default to the address of the opcode of this tableswitch
     *      instruction. Otherwise, the offset at position index - low of the jump table is extracted. The target address is
     *      calculated by adding that offset to the address of the opcode of this tableswitch instruction. Execution then
     *      continues at the target address.
     *
     *      The target address that can be calculated from each jump table offset, as well as the one that can be calculated
     *      from default, must be the address of an opcode of an instruction within the method that contains this tableswitch
     *      instruction.
     *
     * Notes
     *      The alignment required of the 4-byte operands of the tableswitch instruction guarantees 4-byte alignment of those
     *      operands if and only if the method that contains the tableswitch starts on a 4-byte boundary.
     */
    TABLESWITCH     ((byte) 0xAA, -1),      //Access jump table by index and jump (switch)

    /**
     * Operation
     *      Access jump table by key match and jump
     *
     * Format
     *
     *      lookupswitch
     *      <0-3 byte pad>
     *      defaultbyte1
     *      defaultbyte2
     *      defaultbyte3
     *      defaultbyte4
     *      npairs1
     *      npairs2
     *      npairs3
     *      npairs4
     *      match-offset pairs...
     * Forms
     *      lookupswitch = 171 (0xab)
     *
     * Operand Stack
     *      ..., key →
     *
     *      ...
     *
     * Description
     *      A lookupswitch is a variable-length instruction. Immediately after the lookupswitch opcode, between zero and
     *      three bytes must act as padding, such that defaultbyte1 begins at an address that is a multiple of four bytes
     *      from the start of the current method (the opcode of its first instruction). Immediately after the padding
     *      follow a series of signed 32-bit values: default, npairs, and then npairs pairs of signed 32-bit values. The
     *      npairs must be greater than or equal to 0. Each of the npairs pairs consists of an int match and a signed
     *      32-bit offset. Each of these signed 32-bit values is constructed from four unsigned bytes as
     *      (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4.
     *
     *      The table match-offset pairs of the lookupswitch instruction must be sorted in increasing numerical order by
     *      match.
     *
     *      The key must be of type int and is popped from the operand stack. The key is compared against the match values.
     *      If it is equal to one of them, then a target address is calculated by adding the corresponding offset to the
     *      address of the opcode of this lookupswitch instruction. If the key does not match any of the match values, the
     *      target address is calculated by adding default to the address of the opcode of this lookupswitch instruction.
     *      Execution then continues at the target address.
     *
     *      The target address that can be calculated from the offset of each match-offset pair, as well as the one
     *      calculated from default, must be the address of an opcode of an instruction within the method that contains
     *      this lookupswitch instruction.
     *
     * Notes
     *      The alignment required of the 4-byte operands of the lookupswitch instruction guarantees 4-byte alignment of
     *      those operands if and only if the method that contains the lookupswitch is positioned on a 4-byte boundary.
     *
     *      The match-offset pairs are sorted to support lookup routines that are quicker than linear search.
     */
    LOOKUPSWITCH    ((byte) 0xAB, -1),      //Access jump table by key match and jump (switch)


    /** Class and Object field operation */
    GETSTATIC       ((byte) 0xB2, 3),      //Get static field from class
    PUTSTATIC       ((byte) 0xB3, 3),      //Set static field in class
    GETFIELD        ((byte) 0xB4, 3),      //Fetch field from object
    PUTFIELD        ((byte) 0xB5, 3),      //Set field in object

    /** Method invocation */
    INVOKEVIRTUAL   ((byte) 0xB6, 3),      //Invoke instance method; dispatch based on class
    INVOKESPECIAL   ((byte) 0xB7, 3),      //Invoke instance method; special handling for superclass, private, and instance initialization method invocations
    INVOKESTATIC    ((byte) 0xB8, 3),      //Invoke a class (static) method
    INVOKEINTERFACE ((byte) 0xB9, 5),      //Invoke interface method
    INVOKEDYNAMIC   ((byte) 0xBA, 5),      //Invoke dynamic method

    /** Array and instance creation */
    //new instance
    NEW             ((byte) 0xBB, 3),      //Create new object
    //new array
    NEWARRAY        ((byte) 0xBC, 2),      //Create new array
    ANEWARRAY       ((byte) 0xBD, 3),      //Create new array of reference
    MULTIANEWARRAY  ((byte) 0xC5, 4),      //Create new multidimensional array
    //array length
    ARRAYLENGTH     ((byte) 0xBE, 1),      //Get length of array
    //new Exception
    ATHROW          ((byte) 0xBF, 1),      //Throw exception or error
    //wide
    WIDE            ((byte) 0xC4, -1);      //Extend local variable index by additional bytes

    private static final Map<Byte, OpCodeType> TYPE_MAP = new HashMap<>();

    static {
        for (OpCodeType v : values()) {
            TYPE_MAP.put(v.opCode, v);
        }
    }

    private final byte opCode;
    private final int byteSize;

    OpCodeType(byte opCode, int byteSize) {
        this.opCode = opCode;
        this.byteSize = byteSize;
    }

    public byte getTag() {
        return opCode;
    }

    // returns -1 to indicates that this is a variable-length opcode
    public int byteSize() {
        return byteSize;
    }

    public static OpCodeType fromCode(int code) {
        return TYPE_MAP.get((byte) code);
    }

}
