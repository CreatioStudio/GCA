package vip.creatio.gca.code;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum OpCodeType {

    /* Place holder */
    /**
     * <b>Operation: </b>
     * Do nothing <br><br>
     *
     * <b>Format: </b><pre><code>
     * nop
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * nop = 0 (0x0)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * No change
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Do nothing.
     */
    NOP             ((byte) 0x0, 1, 0),       //Do nothing

    /* Push const */

    /**
     * <b>Operation: </b>
     * Push null <br><br>
     *
     * <b>Format: </b><pre><code>
     * aconst_null
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * aconst_null = 1 (0x1)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., null
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Push the null object reference onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The Java Virtual Machine does not mandate a concrete value for null.
     */
    ACONST_NULL     ((byte) 0x1, 1, 1),       //Push null

    /**
     * <b>Operation: </b>
     * Push int constant <br><br>
     *
     * <b>Format: </b><pre><code>
     * iconst_&lt;i&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iconst_m1 = 2 (0x2)
     * iconst_0 = 3 (0x3)
     * iconst_1 = 4 (0x4)
     * iconst_2 = 5 (0x5)
     * iconst_3 = 6 (0x6)
     * iconst_4 = 7 (0x7)
     * iconst_5 = 8 (0x8)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., &lt;i&gt;
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Push the int constant &lt;i&gt; (-1, 0, 1, 2, 3, 4 or 5) onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of this family of instructions is equivalent to bipush &lt;i&gt; for the
     * respective value of &lt;i&gt;, except that the operand &lt;i&gt; is implicit.
     */
    ICONST_M1       ((byte) 0x2, 1, 1),       //Push int constant (-1)

    /** @see OpCodeType#ICONST_M1 */
    ICONST_0        ((byte) 0x3, 1, 1),       //Push int constant

    /** @see OpCodeType#ICONST_M1 */
    ICONST_1        ((byte) 0x4, 1, 1),       //Push int constant

    /** @see OpCodeType#ICONST_M1 */
    ICONST_2        ((byte) 0x5, 1, 1),       //Push int constant

    /** @see OpCodeType#ICONST_M1 */
    ICONST_3        ((byte) 0x6, 1, 1),       //Push int constant

    /** @see OpCodeType#ICONST_M1 */
    ICONST_4        ((byte) 0x7, 1, 1),       //Push int constant

    /** @see OpCodeType#ICONST_M1 */
    ICONST_5        ((byte) 0x8, 1, 1),       //Push int constant

    /**
     * <b>Operation: </b>
     * Push long constant <br><br>
     *
     * <b>Format: </b><pre><code>
     * lconst_&lt;l&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lconst_0 = 9 (0x9)
     * lconst_1 = 10 (0xa)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., &lt;l&gt;
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Push the long constant &lt;l&gt; (0 or 1) onto the operand stack.
     */
    LCONST_0        ((byte) 0x9, 1, 2),       //Push long constant

    /** @see OpCodeType#LCONST_0 */
    LCONST_1        ((byte) 0xA, 1, 2),       //Push long constant

    /**
     * <b>Operation: </b>
     * Push float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fconst_&lt;f&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fconst_0 = 11 (0xb)
     * fconst_1 = 12 (0xc)
     * fconst_2 = 13 (0xd)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., &lt;f&gt;
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Push the float constant &lt;f&gt; (0.0, 1.0, or 2.0) onto the operand stack.
     */
    FCONST_0        ((byte) 0xB, 1, 1),       //Push float

    /** @see OpCodeType#FCONST_0 */
    FCONST_1        ((byte) 0xC, 1, 1),       //Push float

    /** @see OpCodeType#FCONST_0 */
    FCONST_2        ((byte) 0xD, 1, 1),       //Push float

    /**
     * <b>Operation: </b>
     * Push double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dconst_&lt;d&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dconst_0 = 14 (0xe)
     * dconst_1 = 15 (0xf)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., &lt;d&gt;
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Push the double constant &lt;d&gt; (0.0 or 1.0) onto the operand stack.
     */
    DCONST_0        ((byte) 0xE, 1, 2),       //Push double

    /** @see OpCodeType#DCONST_0 */
    DCONST_1        ((byte) 0xF, 1, 2),       //Push double


    /**
     * <b>Operation: </b>
     * Push byte <br><br>
     *
     * <b>Format: </b><pre><code>
     * bipush
     * byte
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * bipush = 16 (0x10)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The immediate byte is sign-extended to an int value. That value is pushed onto the
     * operand stack.
     */
    BIPUSH          ((byte) 0x10, 2, 1),      //Push byte

    /**
     * <b>Operation: </b>
     * Push short <br><br>
     *
     * <b>Format: </b><pre><code>
     * sipush
     * byte1
     * byte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * sipush = 17 (0x11)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The immediate unsigned byte1 and byte2 values are assembled into an intermediate
     * short, where the value of the short is (byte1 &lt;&lt; 8) | byte2. The intermediate
     * value is then sign-extended to an int value. That value is pushed onto the operand
     * stack.
     */
    SIPUSH          ((byte) 0x11, 3, 1),      //Push short

    /**
     * <b>Operation: </b>
     * Push item from run-time constant pool <br><br>
     *
     * <b>Format: </b><pre><code>
     * ldc
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ldc = 18 (0x12)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be a valid index into the run-time constant
     * pool of the current class (§2.6). The run-time constant pool entry at index either
     * must be a run-time constant of type int or float, or a reference to a string
     * literal, or a symbolic reference to a class, method type, or method handle (§5.1). <br><br>
     *
     * If the run-time constant pool entry is a run-time constant of type int or float, the
     * numeric value of that run-time constant is pushed onto the operand stack as an int
     * or float, respectively. <br><br>
     *
     * Otherwise, if the run-time constant pool entry is a reference to an instance of
     * class String representing a string literal (§5.1), then a reference to that
     * instance, value, is pushed onto the operand stack. <br><br>
     *
     * Otherwise, if the run-time constant pool entry is a symbolic reference to a class
     * (§5.1), then the named class is resolved (§5.4.3.1) and a reference to the Class
     * object representing that class, value, is pushed onto the operand stack. <br><br>
     *
     * Otherwise, the run-time constant pool entry must be a symbolic reference to a method
     * type or a method handle (§5.1). The method type or method handle is resolved
     * (§5.4.3.5) and a reference to the resulting instance of java.lang.invoke.MethodType
     * or java.lang.invoke.MethodHandle, value, is pushed onto the operand stack. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of a symbolic reference to a class, any of the exceptions
     * pertaining to class resolution (§5.4.3.1) can be thrown. <br><br>
     *
     * During resolution of a symbolic reference to a method type or method handle, any of
     * the exception pertaining to method type or method handle resolution (§5.4.3.5) can
     * be thrown. <br><br>
     *
     * <b>Notes: </b><br>
     * The ldc instruction can only be used to push a value of type float taken from the
     * float value set (§2.3.2) because a constant of type float in the constant pool
     * (§4.4.4) must be taken from the float value set.
     */
    LDC             ((byte) 0x12, 2, 1),      //Push item from run-time constant pool

    /**
     * <b>Operation: </b>
     * Push item from run-time constant pool (wide index) <br><br>
     *
     * <b>Format: </b><pre><code>
     * ldc_w
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ldc_w = 19 (0x13)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index
     * into the run-time constant pool of the current class (§2.6), where the value of the
     * index is calculated as (indexbyte1 &lt;&lt; 8) | indexbyte2. The index must be a
     * valid index into the run-time constant pool of the current class. The run-time
     * constant pool entry at the index either must be a run-time constant of type int or
     * float, or a reference to a string literal, or a symbolic reference to a class,
     * method type, or method handle (§5.1). <br><br>
     *
     * If the run-time constant pool entry is a run-time constant of type int or float, the
     * numeric value of that run-time constant is pushed onto the operand stack as an int
     * or float, respectively. <br><br>
     *
     * Otherwise, if the run-time constant pool entry is a reference to an instance of
     * class String representing a string literal (§5.1), then a reference to that
     * instance, value, is pushed onto the operand stack. <br><br>
     *
     * Otherwise, if the run-time constant pool entry is a symbolic reference to a class
     * (§4.4.1). The named class is resolved (§5.4.3.1) and a reference to the Class object
     * representing that class, value, is pushed onto the operand stack. <br><br>
     *
     * Otherwise, the run-time constant pool entry must be a symbolic reference to a method
     * type or a method handle (§5.1). The method type or method handle is resolved
     * (§5.4.3.5) and a reference to the resulting instance of java.lang.invoke.MethodType
     * or java.lang.invoke.MethodHandle, value, is pushed onto the operand stack. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to a class, any of the exceptions
     * pertaining to class resolution (§5.4.3.1) can be thrown. <br><br>
     *
     * During resolution of a symbolic reference to a method type or method handle, any of
     * the exception pertaining to method type or method handle resolution (§5.4.3.5) can
     * be thrown. <br><br>
     *
     * <b>Notes: </b><br>
     * The ldc_w instruction is identical to the ldc instruction (§ldc) except for its
     * wider run-time constant pool index. <br><br>
     *
     * The ldc_w instruction can only be used to push a value of type float taken from the
     * float value set (§2.3.2) because a constant of type float in the constant pool
     * (§4.4.4) must be taken from the float value set.
     */
    LDC_W           ((byte) 0x13, 3, 1),      //Push item from run-time constant pool (wide index)

    /**
     * <b>Operation: </b>
     * Push long or double from run-time constant pool (wide index) <br><br>
     *
     * <b>Format: </b><pre><code>
     * ldc2_w
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ldc2_w = 20 (0x14)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index
     * into the run-time constant pool of the current class (§2.6), where the value of the
     * index is calculated as (indexbyte1 &lt;&lt; 8) | indexbyte2. The index must be a
     * valid index into the run-time constant pool of the current class. The run-time
     * constant pool entry at the index must be a run-time constant of type long or double
     * (§5.1). The numeric value of that run-time constant is pushed onto the operand stack
     * as a long or double, respectively. <br><br>
     *
     * <b>Notes: </b><br>
     * Only a wide-index version of the ldc2_w instruction exists; there is no ldc2
     * instruction that pushes a long or double with a single-byte index. <br><br>
     *
     * The ldc2_w instruction can only be used to push a value of type double taken from
     * the double value set (§2.3.2) because a constant of type double in the constant pool
     * (§4.4.5) must be taken from the double value set.
     */
    LDC2_W          ((byte) 0x14, 3, 2),      //Push long or double from run-time constant pool (wide index)

    /* Load from local variable */

    /**
     * <b>Operation: </b>
     * Load int from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * iload
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iload = 21 (0x15)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The local variable at index must contain an int. The value
     * of the local variable at index is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The iload opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    ILOAD           ((byte) 0x15, 2, 1, OpCode.FLAG_LOAD),      //Load int from local variable

    /**
     * <b>Operation: </b>
     * Load long from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * lload
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lload = 22 (0x16)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte. Both index and index+1 must be indices into the local
     * variable array of the current frame (§2.6). The local variable at index must contain
     * a long. The value of the local variable at index is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The lload opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    LLOAD           ((byte) 0x16, 2, 2, OpCode.FLAG_LOAD),      //Load long from local variable

    /**
     * <b>Operation: </b>
     * Load float from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * fload
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fload = 23 (0x17)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The local variable at index must contain a float. The
     * value of the local variable at index is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The fload opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    FLOAD           ((byte) 0x17, 2, 1, OpCode.FLAG_LOAD),      //Load float from local variable

    /**
     * <b>Operation: </b>
     * Load double from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * dload
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dload = 24 (0x18)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte. Both index and index+1 must be indices into the local
     * variable array of the current frame (§2.6). The local variable at index must contain
     * a double. The value of the local variable at index is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The dload opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    DLOAD           ((byte) 0x18, 2, 2, OpCode.FLAG_LOAD),      //Load double from local variable

    /**
     * <b>Operation: </b>
     * Load reference from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * aload
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * aload = 25 (0x19)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., objectref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The local variable at index must contain a reference. The
     * objectref in the local variable at index is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The aload instruction cannot be used to load a value of type returnAddress from a
     * local variable onto the operand stack. This asymmetry with the astore instruction
     * (§astore) is intentional. <br><br>
     *
     * The aload opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    ALOAD           ((byte) 0x19, 2, 1, OpCode.FLAG_LOAD),      //Load reference from local variable


    //########## Fast loading ##########//

    /**
     * <b>Operation: </b>
     * Load int from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * iload_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iload_0 = 26 (0x1a)
     * iload_1 = 27 (0x1b)
     * iload_2 = 28 (0x1c)
     * iload_3 = 29 (0x1d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The local variable at &lt;n&gt; must contain an int. The value of the local
     * variable at &lt;n&gt; is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the iload_&lt;n&gt; instructions is the same as iload with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    ILOAD_0         ((byte) 0x1A, 1, 1, OpCode.FLAG_LOAD),      //Load int from local variable

    /** @see OpCodeType#ILOAD_0 */
    ILOAD_1         ((byte) 0x1B, 1, 1, OpCode.FLAG_LOAD),      //Load int from local variable

    /** @see OpCodeType#ILOAD_0 */
    ILOAD_2         ((byte) 0x1C, 1, 1, OpCode.FLAG_LOAD),      //Load int from local variable

    /** @see OpCodeType#ILOAD_0 */
    ILOAD_3         ((byte) 0x1D, 1, 1, OpCode.FLAG_LOAD),      //Load int from local variable

    /**
     * <b>Operation: </b>
     * Load long from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * lload_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lload_0 = 30 (0x1e)
     * lload_1 = 31 (0x1f)
     * lload_2 = 32 (0x20)
     * lload_3 = 33 (0x21)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both &lt;n&gt; and &lt;n&gt;+1 must be indices into the local variable array of the
     * current frame (§2.6). The local variable at &lt;n&gt; must contain a long. The value
     * of the local variable at &lt;n&gt; is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the lload_&lt;n&gt; instructions is the same as lload with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    LLOAD_0         ((byte) 0x1E, 1, 2, OpCode.FLAG_LOAD),      //Load long from local variable

    /** @see OpCodeType#LLOAD_0 */
    LLOAD_1         ((byte) 0x1F, 1, 2, OpCode.FLAG_LOAD),      //Load long from local variable

    /** @see OpCodeType#LLOAD_0 */
    LLOAD_2         ((byte) 0x20, 1, 2, OpCode.FLAG_LOAD),      //Load long from local variable

    /** @see OpCodeType#LLOAD_0 */
    LLOAD_3         ((byte) 0x21, 1, 2, OpCode.FLAG_LOAD),      //Load long from local variable

    /**
     * <b>Operation: </b>
     * Load float from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * fload_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fload_0 = 34 (0x22)
     * fload_1 = 35 (0x23)
     * fload_2 = 36 (0x24)
     * fload_3 = 37 (0x25)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The local variable at &lt;n&gt; must contain a float. The value of the local
     * variable at &lt;n&gt; is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the fload_&lt;n&gt; instructions is the same as fload with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    FLOAD_0         ((byte) 0x22, 1, 1, OpCode.FLAG_LOAD),      //Load float from local variable

    /** @see OpCodeType#FLOAD_0 */
    FLOAD_1         ((byte) 0x23, 1, 1, OpCode.FLAG_LOAD),      //Load float from local variable

    /** @see OpCodeType#FLOAD_0 */
    FLOAD_2         ((byte) 0x24, 1, 1, OpCode.FLAG_LOAD),      //Load float from local variable

    /** @see OpCodeType#FLOAD_0 */
    FLOAD_3         ((byte) 0x25, 1, 1, OpCode.FLAG_LOAD),      //Load float from local variable

    /**
     * <b>Operation: </b>
     * Load double from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * dload_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dload_0 = 38 (0x26)
     * dload_1 = 39 (0x27)
     * dload_2 = 40 (0x28)
     * dload_3 = 41 (0x29)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both &lt;n&gt; and &lt;n&gt;+1 must be indices into the local variable array of the
     * current frame (§2.6). The local variable at &lt;n&gt; must contain a double. The
     * value of the local variable at &lt;n&gt; is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the dload_&lt;n&gt; instructions is the same as dload with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    DLOAD_0         ((byte) 0x26, 1, 2, OpCode.FLAG_LOAD),      //Load double from local variable

    /** @see OpCodeType#DLOAD_0 */
    DLOAD_1         ((byte) 0x27, 1, 2, OpCode.FLAG_LOAD),      //Load double from local variable

    /** @see OpCodeType#DLOAD_0 */
    DLOAD_2         ((byte) 0x28, 1, 2, OpCode.FLAG_LOAD),      //Load double from local variable

    /** @see OpCodeType#DLOAD_0 */
    DLOAD_3         ((byte) 0x29, 1, 2, OpCode.FLAG_LOAD),      //Load double from local variable

    /**
     * <b>Operation: </b>
     * Load reference from local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * aload_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * aload_0 = 42 (0x2a)
     * aload_1 = 43 (0x2b)
     * aload_2 = 44 (0x2c)
     * aload_3 = 45 (0x2d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., objectref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The local variable at &lt;n&gt; must contain a reference. The objectref in
     * the local variable at &lt;n&gt; is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * An aload_&lt;n&gt; instruction cannot be used to load a value of type returnAddress
     * from a local variable onto the operand stack. This asymmetry with the corresponding
     * astore_&lt;n&gt; instruction (§astore_&lt;n&gt;) is intentional. <br><br>
     *
     * Each of the aload_&lt;n&gt; instructions is the same as aload with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    ALOAD_0         ((byte) 0x2A, 1, 1, OpCode.FLAG_LOAD),      //Load reference from local variable

    /** @see OpCodeType#ALOAD_0 */
    ALOAD_1         ((byte) 0x2B, 1, 1, OpCode.FLAG_LOAD),      //Load reference from local variable

    /** @see OpCodeType#ALOAD_0 */
    ALOAD_2         ((byte) 0x2C, 1, 1, OpCode.FLAG_LOAD),      //Load reference from local variable

    /** @see OpCodeType#ALOAD_0 */
    ALOAD_3         ((byte) 0x2D, 1, 1, OpCode.FLAG_LOAD),      //Load reference from local variable

    //########## Array load ##########//

    /**
     * <b>Operation: </b>
     * Load int from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * iaload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iaload = 46 (0x2e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type int. The index must be of type int. Both arrayref and index are popped
     * from the operand stack. The int value in the component of the array at index is
     * retrieved and pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, iaload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the iaload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    IALOAD          ((byte) 0x2E, 1, -1),      //Load int from array

    /**
     * <b>Operation: </b>
     * Load long from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * laload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * laload = 47 (0x2f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type long. The index must be of type int. Both arrayref and index are popped
     * from the operand stack. The long value in the component of the array at index is
     * retrieved and pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, laload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the laload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    LALOAD          ((byte) 0x2F, 1, 0),      //Load long from array

    /**
     * <b>Operation: </b>
     * Load float from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * faload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * faload = 48 (0x30)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type float. The index must be of type int. Both arrayref and index are popped
     * from the operand stack. The float value in the component of the array at index is
     * retrieved and pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, faload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the faload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    FALOAD          ((byte) 0x30, 1, -1),      //Load float from array

    /**
     * <b>Operation: </b>
     * Load double from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * daload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * daload = 49 (0x31)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type double. The index must be of type int. Both arrayref and index are
     * popped from the operand stack. The double value in the component of the array at
     * index is retrieved and pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, daload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the daload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    DALOAD          ((byte) 0x31, 1, 0),      //Load double from array

    /**
     * <b>Operation: </b>
     * Load reference from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * aaload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * aaload = 50 (0x32)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type reference. The index must be of type int. Both arrayref and index are
     * popped from the operand stack. The reference value in the component of the array at
     * index is retrieved and pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, aaload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the aaload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    AALOAD          ((byte) 0x32, 1, -1),      //Load reference from array

    /**
     * <b>Operation: </b>
     * Load byte or boolean from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * baload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * baload = 51 (0x33)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type byte or of type boolean. The index must be of type int. Both arrayref
     * and index are popped from the operand stack. The byte value in the component of the
     * array at index is retrieved, sign-extended to an int value, and pushed onto the top
     * of the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, baload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the baload instruction throws an <code>ArrayIndexOutOfBoundsException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * The baload instruction is used to load values from both byte and boolean arrays. In
     * Oracle's Java Virtual Machine implementation, boolean arrays - that is, arrays of
     * type T_BOOLEAN (§2.2, §newarray) - are implemented as arrays of 8-bit values. Other
     * implementations may implement packed boolean arrays; the baload instruction of such
     * implementations must be used to access those arrays.
     */
    BALOAD          ((byte) 0x33, 1, -1),      //Load byte or boolean from array

    /**
     * <b>Operation: </b>
     * Load char from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * caload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * caload = 52 (0x34)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type char. The index must be of type int. Both arrayref and index are popped
     * from the operand stack. The component of the array at index is retrieved and
     * zero-extended to an int value. That value is pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, caload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the caload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    CALOAD          ((byte) 0x34, 1, -1),      //Load char from array

    /**
     * <b>Operation: </b>
     * Load short from array <br><br>
     *
     * <b>Format: </b><pre><code>
     * saload
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * saload = 53 (0x35)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type short. The index must be of type int. Both arrayref and index are popped
     * from the operand stack. The component of the array at index is retrieved and
     * sign-extended to an int value. That value is pushed onto the operand stack. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, saload throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the saload instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    SALOAD          ((byte) 0x35, 1, -1),      //Load short from array

    //########## Store to local variable ##########//

    /**
     * <b>Operation: </b>
     * Store int into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * istore
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * istore = 54 (0x36)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The value on the top of the operand stack must be of type
     * int. It is popped from the operand stack, and the value of the local variable at
     * index is set to value. <br><br>
     *
     * <b>Notes: </b><br>
     * The istore opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    ISTORE          ((byte) 0x36, 2, -1, OpCode.FLAG_STORE),      //Store int into local variable

    /**
     * <b>Operation: </b>
     * Store long into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * lstore
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lstore = 55 (0x37)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte. Both index and index+1 must be indices into the local
     * variable array of the current frame (§2.6). The value on the top of the operand
     * stack must be of type long. It is popped from the operand stack, and the local
     * variables at index and index+1 are set to value. <br><br>
     *
     * <b>Notes: </b><br>
     * The lstore opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    LSTORE          ((byte) 0x37, 2, -2, OpCode.FLAG_STORE),      //Store long into local variable

    /**
     * <b>Operation: </b>
     * Store float into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * fstore
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fstore = 56 (0x38)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The value on the top of the operand stack must be of type
     * float. It is popped from the operand stack and undergoes value set conversion
     * (§2.8.3), resulting in value'. The value of the local variable at index is set to
     * value'. <br><br>
     *
     * <b>Notes: </b><br>
     * The fstore opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    FSTORE          ((byte) 0x38, 2, -1, OpCode.FLAG_STORE),      //Store float into local variable

    /**
     * <b>Operation: </b>
     * Store double into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * dstore
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dstore = 57 (0x39)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte. Both index and index+1 must be indices into the local
     * variable array of the current frame (§2.6). The value on the top of the operand
     * stack must be of type double. It is popped from the operand stack and undergoes
     * value set conversion (§2.8.3), resulting in value'. The local variables at index and
     * index+1 are set to value'. <br><br>
     *
     * <b>Notes: </b><br>
     * The dstore opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    DSTORE          ((byte) 0x39, 2, -2, OpCode.FLAG_STORE),      //Store double into local variable

    /**
     * <b>Operation: </b>
     * Store reference into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * astore
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * astore = 58 (0x3a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The objectref on the top of the operand stack must be of
     * type returnAddress or of type reference. It is popped from the operand stack, and
     * the value of the local variable at index is set to objectref. <br><br>
     *
     * <b>Notes: </b><br>
     * The astore instruction is used with an objectref of type returnAddress when
     * implementing the finally clause of the Java programming language (§3.13). <br><br>
     *
     * The aload instruction (§aload) cannot be used to load a value of type returnAddress
     * from a local variable onto the operand stack. This asymmetry with the astore
     * instruction is intentional. <br><br>
     *
     * The astore opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    ASTORE          ((byte) 0x3A, 2, -1, OpCode.FLAG_STORE),      //Store reference into local variable

    /**
     * <b>Operation: </b>
     * Store int into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * istore_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * istore_0 = 59 (0x3b)
     * istore_1 = 60 (0x3c)
     * istore_2 = 61 (0x3d)
     * istore_3 = 62 (0x3e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The value on the top of the operand stack must be of type int. It is popped
     * from the operand stack, and the value of the local variable at &lt;n&gt; is set to
     * value. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the istore_&lt;n&gt; instructions is the same as istore with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    ISTORE_0        ((byte) 0x3B, 1, -1, OpCode.FLAG_STORE),      //Store int into local variable

    /** @see OpCodeType#ISTORE_0 */
    ISTORE_1        ((byte) 0x3C, 1, -1, OpCode.FLAG_STORE),      //Store int into local variable

    /** @see OpCodeType#ISTORE_0 */
    ISTORE_2        ((byte) 0x3D, 1, -1, OpCode.FLAG_STORE),      //Store int into local variable

    /** @see OpCodeType#ISTORE_0 */
    ISTORE_3        ((byte) 0x3E, 1, -1, OpCode.FLAG_STORE),      //Store int into local variable

    /**
     * <b>Operation: </b>
     * Store long into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * lstore_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lstore_0 = 63 (0x3f)
     * lstore_1 = 64 (0x40)
     * lstore_2 = 65 (0x41)
     * lstore_3 = 66 (0x42)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both &lt;n&gt; and &lt;n&gt;+1 must be indices into the local variable array of the
     * current frame (§2.6). The value on the top of the operand stack must be of type
     * long. It is popped from the operand stack, and the local variables at &lt;n&gt; and
     * &lt;n&gt;+1 are set to value. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the lstore_&lt;n&gt; instructions is the same as lstore with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    LSTORE_0        ((byte) 0x3F, 1, -2, OpCode.FLAG_STORE),      //Store long into local variable

    /** @see OpCodeType#LSTORE_0 */
    LSTORE_1        ((byte) 0x40, 1, -2, OpCode.FLAG_STORE),      //Store long into local variable

    /** @see OpCodeType#LSTORE_0 */
    LSTORE_2        ((byte) 0x41, 1, -2, OpCode.FLAG_STORE),      //Store long into local variable

    /** @see OpCodeType#LSTORE_0 */
    LSTORE_3        ((byte) 0x42, 1, -2, OpCode.FLAG_STORE),      //Store long into local variable

    /**
     * <b>Operation: </b>
     * Store float into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * fstore_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fstore_0 = 67 (0x43)
     * fstore_1 = 68 (0x44)
     * fstore_2 = 69 (0x45)
     * fstore_3 = 70 (0x46)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The value on the top of the operand stack must be of type float. It is
     * popped from the operand stack and undergoes value set conversion (§2.8.3), resulting
     * in value'. The value of the local variable at &lt;n&gt; is set to value'. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the fstore_&lt;n&gt; instructions is the same as fstore with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    FSTORE_0        ((byte) 0x43, 1, -1, OpCode.FLAG_STORE),      //Store float into local variable

    /** @see OpCodeType#FSTORE_0 */
    FSTORE_1        ((byte) 0x44, 1, -1, OpCode.FLAG_STORE),      //Store float into local variable

    /** @see OpCodeType#FSTORE_0 */
    FSTORE_2        ((byte) 0x45, 1, -1, OpCode.FLAG_STORE),      //Store float into local variable

    /** @see OpCodeType#FSTORE_0 */
    FSTORE_3        ((byte) 0x46, 1, -1, OpCode.FLAG_STORE),      //Store float into local variable

    /**
     * <b>Operation: </b>
     * Store double into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * dstore_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dstore_0 = 71 (0x47)
     * dstore_1 = 72 (0x48)
     * dstore_2 = 73 (0x49)
     * dstore_3 = 74 (0x4a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both &lt;n&gt; and &lt;n&gt;+1 must be indices into the local variable array of the
     * current frame (§2.6). The value on the top of the operand stack must be of type
     * double. It is popped from the operand stack and undergoes value set conversion
     * (§2.8.3), resulting in value'. The local variables at &lt;n&gt; and &lt;n&gt;+1 are
     * set to value'. <br><br>
     *
     * <b>Notes: </b><br>
     * Each of the dstore_&lt;n&gt; instructions is the same as dstore with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    DSTORE_0        ((byte) 0x47, 1, -2, OpCode.FLAG_STORE),      //Store double into local variable

    /** @see OpCodeType#DSTORE_0 */
    DSTORE_1        ((byte) 0x48, 1, -2, OpCode.FLAG_STORE),      //Store double into local variable

    /** @see OpCodeType#DSTORE_0 */
    DSTORE_2        ((byte) 0x49, 1, -2, OpCode.FLAG_STORE),      //Store double into local variable

    /** @see OpCodeType#DSTORE_0 */
    DSTORE_3        ((byte) 0x4A, 1, -2, OpCode.FLAG_STORE),      //Store double into local variable

    /**
     * <b>Operation: </b>
     * Store reference into local variable <br><br>
     *
     * <b>Format: </b><pre><code>
     * astore_&lt;n&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * astore_0 = 75 (0x4b)
     * astore_1 = 76 (0x4c)
     * astore_2 = 77 (0x4d)
     * astore_3 = 78 (0x4e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The &lt;n&gt; must be an index into the local variable array of the current frame
     * (§2.6). The objectref on the top of the operand stack must be of type returnAddress
     * or of type reference. It is popped from the operand stack, and the value of the
     * local variable at &lt;n&gt; is set to objectref. <br><br>
     *
     * <b>Notes: </b><br>
     * An astore_&lt;n&gt; instruction is used with an objectref of type returnAddress when
     * implementing the finally clauses of the Java programming language (§3.13). <br><br>
     *
     * An aload_&lt;n&gt; instruction (§aload_&lt;n&gt;) cannot be used to load a value of
     * type returnAddress from a local variable onto the operand stack. This asymmetry with
     * the corresponding astore_&lt;n&gt; instruction is intentional. <br><br>
     *
     * Each of the astore_&lt;n&gt; instructions is the same as astore with an index of
     * &lt;n&gt;, except that the operand &lt;n&gt; is implicit.
     */
    ASTORE_0        ((byte) 0x4B, 1, -1, OpCode.FLAG_STORE),      //Store reference into local variable

    /** @see OpCodeType#ASTORE_0 */
    ASTORE_1        ((byte) 0x4C, 1, -1, OpCode.FLAG_STORE),      //Store reference into local variable

    /** @see OpCodeType#ASTORE_0 */
    ASTORE_2        ((byte) 0x4D, 1, -1, OpCode.FLAG_STORE),      //Store reference into local variable

    /** @see OpCodeType#ASTORE_0 */
    ASTORE_3        ((byte) 0x4E, 1, -1, OpCode.FLAG_STORE),      //Store reference into local variable

    //########## Array store ##########//

    /**
     * <b>Operation: </b>
     * Store into int array <br><br>
     *
     * <b>Format: </b><pre><code>
     * iastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iastore = 79 (0x4f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type int. Both index and value must be of type int. The arrayref, index, and
     * value are popped from the operand stack. The int value is stored as the component of
     * the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, iastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the iastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    IASTORE         ((byte) 0x4F, 1, -3),      //Store into int array

    /**
     * <b>Operation: </b>
     * Store into long array <br><br>
     *
     * <b>Format: </b><pre><code>
     * lastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lastore = 80 (0x50)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type long. The index must be of type int, and value must be of type long. The
     * arrayref, index, and value are popped from the operand stack. The long value is
     * stored as the component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, lastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the lastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    LASTORE         ((byte) 0x50, 1, -4),      //Store into long array

    /**
     * <b>Operation: </b>
     * Store into float array <br><br>
     *
     * <b>Format: </b><pre><code>
     * fastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fastore = 81 (0x51)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type float. The index must be of type int, and the value must be of type
     * float. The arrayref, index, and value are popped from the operand stack. The float
     * value undergoes value set conversion (§2.8.3), resulting in value', and value' is
     * stored as the component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, fastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the fastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    FASTORE         ((byte) 0x51, 1, -3),      //Store into float array

    /**
     * <b>Operation: </b>
     * Store into double array <br><br>
     *
     * <b>Format: </b><pre><code>
     * dastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dastore = 82 (0x52)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type double. The index must be of type int, and value must be of type double.
     * The arrayref, index, and value are popped from the operand stack. The double value
     * undergoes value set conversion (§2.8.3), resulting in value', which is stored as the
     * component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, dastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the dastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    DASTORE         ((byte) 0x52, 1, -4),      //Store into double array

    /**
     * <b>Operation: </b>
     * Store into reference array <br><br>
     *
     * <b>Format: </b><pre><code>
     * aastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * aastore = 83 (0x53)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type reference. The index must be of type int and value must be of type
     * reference. The arrayref, index, and value are popped from the operand stack. The
     * reference value is stored as the component of the array at index. <br><br>
     *
     * At run time, the type of value must be compatible with the type of the components of
     * the array referenced by arrayref. Specifically, assignment of a value of reference
     * type S (source) to an array component of reference type T (target) is allowed only
     * if: <br><br>
     *
     * If S is a class type, then: <br>
     * &emsp;&emsp;If T is a class type, then S must be the same class as T, or S must be
     * a subclass of T; <br>
     * &emsp;&emsp;If T is an interface type, then S must implement interface T. <br><br>
     *
     * If S is an interface type, then: <br>
     * &emsp;&emsp;If T is a class type, then T must be Object. <br>
     * &emsp;&emsp;If T is an interface type, then T must be the same interface as S or a
     * superinterface of S. <br><br>
     *
     * If S is an array type, namely, the type SC[], that is, an array of components of
     * type SC, then: <br>
     * &emsp;&emsp;If T is a class type, then T must be Object. <br>
     * &emsp;&emsp;If T is an interface type, then T must be one of the interfaces
     * implemented by arrays (JLS §4.10.3). <br>
     * &emsp;&emsp;If T is an array type TC[], that is, an array of components of type TC,
     * then one of the following must be true: <br>
     * &emsp;&emsp;&emsp;&emsp; TC and SC are the same primitive type. <br>
     * &emsp;&emsp;&emsp;&emsp;TC and SC are reference types, and type SC is assignable to
     * TC by these run-time rules. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, aastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the aastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code> <br><br>
     *
     * Otherwise, if arrayref is not null and the actual type of value is not assignment
     * compatible (JLS §5.2) with the actual type of the components of the array, aastore
     * throws an <code>ArrayStoreException.</code>
     */
    AASTORE         ((byte) 0x53, 1, -3),      //Store into reference array

    /**
     * <b>Operation: </b>
     * Store into byte or boolean array <br><br>
     *
     * <b>Format: </b><pre><code>
     * bastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * bastore = 84 (0x54)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type byte or of type boolean. The index and the value must both be of type
     * int. The arrayref, index, and value are popped from the operand stack. The int value
     * is truncated to a byte and stored as the component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, bastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the bastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * The bastore instruction is used to store values into both byte and boolean arrays.
     * In Oracle's Java Virtual Machine implementation, boolean arrays - that is, arrays of
     * type T_BOOLEAN (§2.2, §newarray) - are implemented as arrays of 8-bit values. Other
     * implementations may implement packed boolean arrays; in such implementations the
     * bastore instruction must be able to store boolean values into packed boolean arrays
     * as well as byte values into byte arrays.
     */
    BASTORE         ((byte) 0x54, 1, -3),      //Store into byte or boolean array

    /**
     * <b>Operation: </b>
     * Store into char array <br><br>
     *
     * <b>Format: </b><pre><code>
     * castore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * castore = 85 (0x55)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type char. The index and the value must both be of type int. The arrayref,
     * index, and value are popped from the operand stack. The int value is truncated to a
     * char and stored as the component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, castore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the castore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    CASTORE         ((byte) 0x55, 1, -3),      //Store into char array

    /**
     * <b>Operation: </b>
     * Store into short array <br><br>
     *
     * <b>Format: </b><pre><code>
     * sastore
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * sastore = 86 (0x56)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref, index, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array whose components
     * are of type short. Both index and value must be of type int. The arrayref, index,
     * and value are popped from the operand stack. The int value is truncated to a short
     * and stored as the component of the array indexed by index. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If arrayref is null, sastore throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if index is not within the bounds of the array referenced by arrayref,
     * the sastore instruction throws an <code>ArrayIndexOutOfBoundsException.</code>
     */
    SASTORE         ((byte) 0x56, 1, -3),      //Store into short array

    //########## Number conversion ##########//

    /**
     * <b>Operation: </b>
     * Convert int to long <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2l
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2l = 133 (0x85)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack and sign-extended to a long result. That result is pushed onto the
     * operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2l instruction performs a widening primitive conversion (JLS §5.1.2). Because
     * all values of type int are exactly representable by type long, the conversion is
     * exact.
     */
    I2L             ((byte) 0x85, 1, 1, OpCode.FLAG_MATH),      //Convert int to long

    /**
     * <b>Operation: </b>
     * Convert int to float <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2f
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2f = 134 (0x86)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack and converted to the float result using IEEE 754 round to nearest
     * mode. The result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2f instruction performs a widening primitive conversion (JLS §5.1.2), but may
     * result in a loss of precision because values of type float have only 24 significand
     * bits.
     */
    I2F             ((byte) 0x86, 1, 0, OpCode.FLAG_MATH),      //Convert int to float

    /**
     * <b>Operation: </b>
     * Convert int to double <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2d
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2d = 135 (0x87)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack and converted to a double result. The result is pushed onto the
     * operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2d instruction performs a widening primitive conversion (JLS §5.1.2). Because
     * all values of type int are exactly representable by type double, the conversion is
     * exact.
     */
    I2D             ((byte) 0x87, 1, 1, OpCode.FLAG_MATH),      //Convert int to double

    /**
     * <b>Operation: </b>
     * Convert long to int <br><br>
     *
     * <b>Format: </b><pre><code>
     * l2i
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * l2i = 136 (0x88)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type long. It is popped from
     * the operand stack and converted to an int result by taking the low-order 32 bits of
     * the long value and discarding the high-order 32 bits. The result is pushed onto the
     * operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The l2i instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value. The result may also not have
     * the same sign as value.
     */
    L2I             ((byte) 0x88, 1, -1, OpCode.FLAG_MATH),      //Convert long to int

    /**
     * <b>Operation: </b>
     * Convert long to float <br><br>
     *
     * <b>Format: </b><pre><code>
     * l2f
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * l2f = 137 (0x89)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type long. It is popped from
     * the operand stack and converted to a float result using IEEE 754 round to nearest
     * mode. The result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The l2f instruction performs a widening primitive conversion (JLS §5.1.2) that may
     * lose precision because values of type float have only 24 significand bits.
     */
    L2F             ((byte) 0x89, 1, -1, OpCode.FLAG_MATH),      //Convert long to float

    /**
     * <b>Operation: </b>
     * Convert long to double <br><br>
     *
     * <b>Format: </b><pre><code>
     * l2d
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * l2d = 138 (0x8a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type long. It is popped from
     * the operand stack and converted to a double result using IEEE 754 round to nearest
     * mode. The result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The l2d instruction performs a widening primitive conversion (JLS §5.1.2) that may
     * lose precision because values of type double have only 53 significand bits.
     */
    L2D             ((byte) 0x8A, 1, 0, OpCode.FLAG_MATH),      //Convert long to double

    /**
     * <b>Operation: </b>
     * Convert float to int <br><br>
     *
     * <b>Format: </b><pre><code>
     * f2i
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * f2i = 139 (0x8b)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type float. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3), resulting in value'.
     * Then value' is converted to an int result. This result is pushed onto the operand
     * stack: <br><br>
     *
     * If the value' is NaN, the result of the conversion is an int 0. <br><br>
     *
     * Otherwise, if the value' is not an infinity, it is rounded to an integer value V,
     * rounding towards zero using IEEE 754 round towards zero mode. If this integer value
     * V can be represented as an int, then the result is the int value V. <br><br>
     *
     * Otherwise, either the value' must be too small (a negative value of large magnitude
     * or negative infinity), and the result is the smallest representable value of type
     * int, or the value' must be too large (a positive value of large magnitude or
     * positive infinity), and the result is the largest representable value of type int. <br><br>
     *
     * <b>Notes: </b><br>
     * The f2i instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value' and may also lose precision.
     */
    F2I             ((byte) 0x8B, 1, 0, OpCode.FLAG_MATH),      //Convert float to int

    /**
     * <b>Operation: </b>
     * Convert float to long <br><br>
     *
     * <b>Format: </b><pre><code>
     * f2l
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * f2l = 140 (0x8c)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type float. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3), resulting in value'.
     * Then value' is converted to a long result. This result is pushed onto the operand
     * stack: <br><br>
     *
     * If the value' is NaN, the result of the conversion is a long 0. <br><br>
     *
     * Otherwise, if the value' is not an infinity, it is rounded to an integer value V,
     * rounding towards zero using IEEE 754 round towards zero mode. If this integer value
     * V can be represented as a long, then the result is the long value V. <br><br>
     *
     * Otherwise, either the value' must be too small (a negative value of large magnitude
     * or negative infinity), and the result is the smallest representable value of type
     * long, or the value' must be too large (a positive value of large magnitude or
     * positive infinity), and the result is the largest representable value of type long. <br><br>
     *
     * <b>Notes: </b><br>
     * The f2l instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value' and may also lose precision.
     */
    F2L             ((byte) 0x8C, 1, 1, OpCode.FLAG_MATH),      //Convert float to long

    /**
     * <b>Operation: </b>
     * Convert float to double <br><br>
     *
     * <b>Format: </b><pre><code>
     * f2d
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * f2d = 141 (0x8d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type float. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3), resulting in value'.
     * Then value' is converted to a double result. This result is pushed onto the operand
     * stack. <br><br>
     *
     * <b>Notes: </b><br>
     * Where an f2d instruction is FP-strict (§2.8.2) it performs a widening primitive
     * conversion (JLS §5.1.2). Because all values of the float value set (§2.3.2) are
     * exactly representable by values of the double value set (§2.3.2), such a conversion
     * is exact. <br><br>
     *
     * Where an f2d instruction is not FP-strict, the result of the conversion may be taken
     * from the double-extended-exponent value set; it is not necessarily rounded to the
     * nearest representable value in the double value set. However, if the operand value
     * is taken from the float-extended-exponent value set and the target result is
     * constrained to the double value set, rounding of value may be required.
     */
    F2D             ((byte) 0x8D, 1, 1, OpCode.FLAG_MATH),      //Convert float to double

    /**
     * <b>Operation: </b>
     * Convert double to int <br><br>
     *
     * <b>Format: </b><pre><code>
     * d2i
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * d2i = 142 (0x8e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type double. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3) resulting in value'.
     * Then value' is converted to an int. The result is pushed onto the operand stack: <br><br>
     *
     * If the value' is NaN, the result of the conversion is an int 0. <br><br>
     *
     * Otherwise, if the value' is not an infinity, it is rounded to an integer value V,
     * rounding towards zero using IEEE 754 round towards zero mode. If this integer value
     * V can be represented as an int, then the result is the int value V. <br><br>
     *
     * Otherwise, either the value' must be too small (a negative value of large magnitude
     * or negative infinity), and the result is the smallest representable value of type
     * int, or the value' must be too large (a positive value of large magnitude or
     * positive infinity), and the result is the largest representable value of type int. <br><br>
     *
     * <b>Notes: </b><br>
     * The d2i instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value' and may also lose precision.
     */
    D2I             ((byte) 0x8E, 1, -1, OpCode.FLAG_MATH),      //Convert double to int

    /**
     * <b>Operation: </b>
     * Convert double to long <br><br>
     *
     * <b>Format: </b><pre><code>
     * d2l
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * d2l = 143 (0x8f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type double. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3) resulting in value'.
     * Then value' is converted to a long. The result is pushed onto the operand stack: <br><br>
     *
     * If the value' is NaN, the result of the conversion is a long 0. <br><br>
     *
     * Otherwise, if the value' is not an infinity, it is rounded to an integer value V,
     * rounding towards zero using IEEE 754 round towards zero mode. If this integer value
     * V can be represented as a long, then the result is the long value V. <br><br>
     *
     * Otherwise, either the value' must be too small (a negative value of large magnitude
     * or negative infinity), and the result is the smallest representable value of type
     * long, or the value' must be too large (a positive value of large magnitude or
     * positive infinity), and the result is the largest representable value of type long. <br><br>
     *
     * <b>Notes: </b><br>
     * The d2l instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value' and may also lose precision.
     */
    D2L             ((byte) 0x8F, 1, 0, OpCode.FLAG_MATH),      //Convert double to long

    /**
     * <b>Operation: </b>
     * Convert double to float <br><br>
     *
     * <b>Format: </b><pre><code>
     * d2f
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * d2f = 144 (0x90)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type double. It is popped from
     * the operand stack and undergoes value set conversion (§2.8.3) resulting in value'.
     * Then value' is converted to a float result using IEEE 754 round to nearest mode. The
     * result is pushed onto the operand stack. <br><br>
     *
     * Where an d2f instruction is FP-strict (§2.8.2), the result of the conversion is
     * always rounded to the nearest representable value in the float value set (§2.3.2). <br><br>
     *
     * Where an d2f instruction is not FP-strict, the result of the conversion may be taken
     * from the float-extended-exponent value set (§2.3.2); it is not necessarily rounded
     * to the nearest representable value in the float value set. <br><br>
     *
     * A finite value' too small to be represented as a float is converted to a zero of the
     * same sign; a finite value' too large to be represented as a float is converted to an
     * infinity of the same sign. A double NaN is converted to a float NaN. <br><br>
     *
     * <b>Notes: </b><br>
     * The d2f instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value' and may also lose precision.
     */
    D2F             ((byte) 0x90, 1, -1, OpCode.FLAG_MATH),      //Convert double to float

    /**
     * <b>Operation: </b>
     * Convert int to byte <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2b
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2b = 145 (0x91)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack, truncated to a byte, then sign-extended to an int result. That result
     * is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2b instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value. The result may also not have
     * the same sign as value.
     */
    I2B             ((byte) 0x91, 1, 0, OpCode.FLAG_MATH),      //Convert int to byte

    /**
     * <b>Operation: </b>
     * Convert int to char <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2c
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2c = 146 (0x92)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack, truncated to char, then zero-extended to an int result. That result
     * is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2c instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value. The result (which is always
     * positive) may also not have the same sign as value.
     */
    I2C             ((byte) 0x92, 1, 0, OpCode.FLAG_MATH),      //Convert int to char

    /**
     * <b>Operation: </b>
     * Convert int to short <br><br>
     *
     * <b>Format: </b><pre><code>
     * i2s
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * i2s = 147 (0x93)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value on the top of the operand stack must be of type int. It is popped from the
     * operand stack, truncated to a short, then sign-extended to an int result. That
     * result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The i2s instruction performs a narrowing primitive conversion (JLS §5.1.3). It may
     * lose information about the overall magnitude of value. The result may also not have
     * the same sign as value.
     */
    I2S             ((byte) 0x93, 1, 0, OpCode.FLAG_MATH),      //Convert int to short

    //########## Number operation ##########//

    //int number operation

    /**
     * <b>Operation: </b>
     * Add int <br><br>
     *
     * <b>Format: </b><pre><code>
     * iadd
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iadd = 96 (0x60)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. The int result is value1 + value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * The result is the 32 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type int. If
     * overflow occurs, then the sign of the result may not be the same as the sign of the
     * mathematical sum of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an iadd instruction never
     * throws a run-time exception.
     */
    IADD            ((byte) 0x60, 1, -1, OpCode.FLAG_MATH),      //Add int                       (+)

    /**
     * <b>Operation: </b>
     * Subtract int <br><br>
     *
     * <b>Format: </b><pre><code>
     * isub
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * isub = 100 (0x64)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. The int result is value1 - value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * For int subtraction, a-b produces the same result as a+(-b). For int values,
     * subtraction from zero is the same as negation. <br><br>
     *
     * The result is the 32 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type int. If
     * overflow occurs, then the sign of the result may not be the same as the sign of the
     * mathematical difference of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an isub instruction never
     * throws a run-time exception.
     */
    ISUB            ((byte) 0x64, 1, -1, OpCode.FLAG_MATH),      //Subtract int                  (-)

    /**
     * <b>Operation: </b>
     * Multiply int <br><br>
     *
     * <b>Format: </b><pre><code>
     * imul
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * imul = 104 (0x68)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. The int result is value1 * value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * The result is the 32 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type int. If
     * overflow occurs, then the sign of the result may not be the same as the sign of the
     * mathematical multiplication of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an imul instruction never
     * throws a run-time exception.
     */
    IMUL            ((byte) 0x68, 1, -1, OpCode.FLAG_MATH),      //Multiply int                  (*)

    /**
     * <b>Operation: </b>
     * Divide int <br><br>
     *
     * <b>Format: </b><pre><code>
     * idiv
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * idiv = 108 (0x6c)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. The int result is the value of the Java programming language expression
     * value1 / value2. The result is pushed onto the operand stack. <br><br>
     *
     * An int division rounds towards 0; that is, the quotient produced for int values in
     * n/d is an int value q whose magnitude is as large as possible while satisfying |d ⋅
     * q| ≤ |n|. Moreover, q is positive when |n| ≥ |d| and n and d have the same sign, but
     * q is negative when |n| ≥ |d| and n and d have opposite signs. <br><br>
     *
     * There is one special case that does not satisfy this rule: if the dividend is the
     * negative integer of largest possible magnitude for the int type, and the divisor is
     * -1, then overflow occurs, and the result is equal to the dividend. Despite the
     * overflow, no exception is thrown in this case. <br><br>
     *
     * Run-time Exception
     * If the value of the divisor in an int division is 0, idiv throws an
     * <code>ArithmeticException.</code>
     */
    IDIV            ((byte) 0x6C, 1, -1, OpCode.FLAG_MATH),      //Divide int                    (/)

    /**
     * <b>Operation: </b>
     * Remainder int <br><br>
     *
     * <b>Format: </b><pre><code>
     * irem
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * irem = 112 (0x70)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. The int result is value1 - (value1 / value2) * value2. The result is pushed
     * onto the operand stack. <br><br>
     *
     * The result of the irem instruction is such that (a/b)*b + (a%b) is equal to a. This
     * identity holds even in the special case in which the dividend is the negative int of
     * largest possible magnitude for its type and the divisor is -1 (the remainder is 0).
     * It follows from this rule that the result of the remainder operation can be negative
     * only if the dividend is negative and can be positive only if the dividend is
     * positive. Moreover, the magnitude of the result is always less than the magnitude of
     * the divisor. <br><br>
     *
     * Run-time Exception
     * If the value of the divisor for an int remainder operator is 0, irem throws an
     * <code>ArithmeticException.</code>
     */
    IREM            ((byte) 0x70, 1, -1, OpCode.FLAG_MATH),      //Remainder int                 (%)

    /**
     * <b>Operation: </b>
     * Negate int <br><br>
     *
     * <b>Format: </b><pre><code>
     * ineg
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ineg = 116 (0x74)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type int. It is popped from the operand stack. The int result
     * is the arithmetic negation of value, -value. The result is pushed onto the operand
     * stack. <br><br>
     *
     * For int values, negation is the same as subtraction from zero. Because the Java
     * Virtual Machine uses two's-complement representation for integers and the range of
     * two's-complement values is not symmetric, the negation of the maximum negative int
     * results in that same maximum negative number. Despite the fact that overflow has
     * occurred, no exception is thrown. <br><br>
     *
     * For all int values x, -x equals (~x)+1.
     */
    INEG            ((byte) 0x74, 1, 0, OpCode.FLAG_MATH),      //Negate int                    (-)

    /**
     * <b>Operation: </b>
     * Shift left int <br><br>
     *
     * <b>Format: </b><pre><code>
     * ishl
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ishl = 120 (0x78)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. An int result is calculated by shifting value1 left by s bit positions, where
     * s is the value of the low 5 bits of value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * <b>Notes: </b><br>
     * This is equivalent (even if overflow occurs) to multiplication by 2 to the power s.
     * The shift distance actually used is always in the range 0 to 31, inclusive, as if
     * value2 were subjected to a bitwise logical AND with the mask value 0x1f.
     */
    ISHL            ((byte) 0x78, 1, -1, OpCode.FLAG_MATH),      //Shift left int                (<<)

    /**
     * <b>Operation: </b>
     * Arithmetic shift right int <br><br>
     *
     * <b>Format: </b><pre><code>
     * ishr
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ishr = 122 (0x7a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. An int result is calculated by shifting value1 right by s bit positions, with
     * sign extension, where s is the value of the low 5 bits of value2. The result is
     * pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The resulting value is floor(value1 / 2s), where s is value2 & 0x1f. For
     * non-negative value1, this is equivalent to truncating int division by 2 to the power
     * s. The shift distance actually used is always in the range 0 to 31, inclusive, as if
     * value2 were subjected to a bitwise logical AND with the mask value 0x1f.
     */
    ISHR            ((byte) 0x7A, 1, -1, OpCode.FLAG_MATH),      //Arithmetic shift right int    (>>)

    /**
     * <b>Operation: </b>
     * Logical shift right int <br><br>
     *
     * <b>Format: </b><pre><code>
     * iushr
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iushr = 124 (0x7c)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. The values are popped from the operand
     * stack. An int result is calculated by shifting value1 right by s bit positions, with
     * zero extension, where s is the value of the low 5 bits of value2. The result is
     * pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * If value1 is positive and s is value2 & 0x1f, the result is the same as that of
     * value1 &gt;&gt; s; if value1 is negative, the result is equal to the value of the
     * expression (value1 &gt;&gt; s) + (2 &lt;&lt; ~s). The addition of the (2 &lt;&lt;
     * ~s) term cancels out the propagated sign bit. The shift distance actually used is
     * always in the range 0 to 31, inclusive.
     */
    IUSHR           ((byte) 0x7C, 1, -1, OpCode.FLAG_MATH),      //Logical shift right int       (>>>)

    /**
     * <b>Operation: </b>
     * Boolean AND int <br><br>
     *
     * <b>Format: </b><pre><code>
     * iand
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iand = 126 (0x7e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. They are popped from the operand stack.
     * An int result is calculated by taking the bitwise AND (conjunction) of value1 and
     * value2. The result is pushed onto the operand stack.
     */
    IAND            ((byte) 0x7E, 1, -1, OpCode.FLAG_MATH),      //Boolean bitwise AND int       (&)

    /**
     * <b>Operation: </b>
     * Boolean OR int <br><br>
     *
     * <b>Format: </b><pre><code>
     * ior
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ior = 128 (0x80)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. They are popped from the operand stack.
     * An int result is calculated by taking the bitwise inclusive OR of value1 and value2.
     * The result is pushed onto the operand stack.
     */
    IOR             ((byte) 0x80, 1, -1, OpCode.FLAG_MATH),      //Boolean bitwise OR int        (|)

    /**
     * <b>Operation: </b>
     * Boolean XOR int <br><br>
     *
     * <b>Format: </b><pre><code>
     * ixor
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ixor = 130 (0x82)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. They are popped from the operand stack.
     * An int result is calculated by taking the bitwise exclusive OR of value1 and value2.
     * The result is pushed onto the operand stack.
     */
    IXOR            ((byte) 0x82, 1, -1, OpCode.FLAG_MATH),      //Boolean bitwise XOR int       (^)

    /**
     * <b>Operation: </b>
     * Increment local variable by constant <br><br>
     *
     * <b>Format: </b><pre><code>
     * iinc
     * index
     * const
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * iinc = 132 (0x84)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * No change
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte that must be an index into the local variable array of
     * the current frame (§2.6). The const is an immediate signed byte. The local variable
     * at index must contain an int. The value const is first sign-extended to an int, and
     * then the local variable at index is incremented by that amount. <br><br>
     *
     * <b>Notes: </b><br>
     * The iinc opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index and to increment it by a
     * two-byte immediate signed value.
     */
    IINC            ((byte) 0x84, 3, 0, OpCode.FLAG_MATH),      //Increment local variable by constant


    //long number operation

    /**
     * <b>Operation: </b>
     * Add long <br><br>
     *
     * <b>Format: </b><pre><code>
     * ladd
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ladd = 97 (0x61)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. The values are popped from the operand
     * stack. The long result is value1 + value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * The result is the 64 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type long. If
     * overflow occurs, the sign of the result may not be the same as the sign of the
     * mathematical sum of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an ladd instruction never
     * throws a run-time exception.
     */
    LADD            ((byte) 0x61, 1, -2, OpCode.FLAG_MATH),      //Add long                      (+)

    /**
     * <b>Operation: </b>
     * Subtract long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lsub
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lsub = 101 (0x65)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. The values are popped from the operand
     * stack. The long result is value1 - value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * For long subtraction, a-b produces the same result as a+(-b). For long values,
     * subtraction from zero is the same as negation. <br><br>
     *
     * The result is the 64 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type long. If
     * overflow occurs, then the sign of the result may not be the same as the sign of the
     * mathematical difference of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an lsub instruction never
     * throws a run-time exception.
     */
    LSUB            ((byte) 0x65, 1, -2, OpCode.FLAG_MATH),      //Subtract long                 (-)

    /**
     * <b>Operation: </b>
     * Multiply long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lmul
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lmul = 105 (0x69)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. The values are popped from the operand
     * stack. The long result is value1 * value2. The result is pushed onto the operand
     * stack. <br><br>
     *
     * The result is the 64 low-order bits of the true mathematical result in a
     * sufficiently wide two's-complement format, represented as a value of type long. If
     * overflow occurs, the sign of the result may not be the same as the sign of the
     * mathematical multiplication of the two values. <br><br>
     *
     * Despite the fact that overflow may occur, execution of an lmul instruction never
     * throws a run-time exception.
     */
    LMUL            ((byte) 0x69, 1, -2, OpCode.FLAG_MATH),      //Multiply long                 (*)

    /**
     * <b>Operation: </b>
     * Divide long <br><br>
     *
     * <b>Format: </b><pre><code>
     * ldiv
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ldiv = 109 (0x6d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. The values are popped from the operand
     * stack. The long result is the value of the Java programming language expression
     * value1 / value2. The result is pushed onto the operand stack. <br><br>
     *
     * A long division rounds towards 0; that is, the quotient produced for long values in
     * n / d is a long value q whose magnitude is as large as possible while satisfying |d
     * ⋅ q| ≤ |n|. Moreover, q is positive when |n| ≥ |d| and n and d have the same sign,
     * but q is negative when |n| ≥ |d| and n and d have opposite signs. <br><br>
     *
     * There is one special case that does not satisfy this rule: if the dividend is the
     * negative integer of largest possible magnitude for the long type and the divisor is
     * -1, then overflow occurs and the result is equal to the dividend; despite the
     * overflow, no exception is thrown in this case. <br><br>
     *
     * Run-time Exception
     * If the value of the divisor in a long division is 0, ldiv throws an
     * <code>ArithmeticException.</code>
     */
    LDIV            ((byte) 0x6D, 1, -2, OpCode.FLAG_MATH),      //Divide long                   (/)

    /**
     * <b>Operation: </b>
     * Remainder long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lrem
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lrem = 113 (0x71)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. The values are popped from the operand
     * stack. The long result is value1 - (value1 / value2) * value2. The result is pushed
     * onto the operand stack. <br><br>
     *
     * The result of the lrem instruction is such that (a/b)*b + (a%b) is equal to a. This
     * identity holds even in the special case in which the dividend is the negative long
     * of largest possible magnitude for its type and the divisor is -1 (the remainder is
     * 0). It follows from this rule that the result of the remainder operation can be
     * negative only if the dividend is negative and can be positive only if the dividend
     * is positive; moreover, the magnitude of the result is always less than the magnitude
     * of the divisor. <br><br>
     *
     * Run-time Exception
     * If the value of the divisor for a long remainder operator is 0, lrem throws an
     * <code>ArithmeticException.</code>
     */
    LREM            ((byte) 0x71, 1, -2, OpCode.FLAG_MATH),      //Remainder long                (%)

    /**
     * <b>Operation: </b>
     * Negate long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lneg
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lneg = 117 (0x75)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type long. It is popped from the operand stack. The long result
     * is the arithmetic negation of value, -value. The result is pushed onto the operand
     * stack. <br><br>
     *
     * For long values, negation is the same as subtraction from zero. Because the Java
     * Virtual Machine uses two's-complement representation for integers and the range of
     * two's-complement values is not symmetric, the negation of the maximum negative long
     * results in that same maximum negative number. Despite the fact that overflow has
     * occurred, no exception is thrown. <br><br>
     *
     * For all long values x, -x equals (~x)+1.
     */
    LNEG            ((byte) 0x75, 1, 0, OpCode.FLAG_MATH),      //Negate long                   (-)

    /**
     * <b>Operation: </b>
     * Shift left long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lshl
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lshl = 121 (0x79)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value1 must be of type long, and value2 must be of type int. The values are
     * popped from the operand stack. A long result is calculated by shifting value1 left
     * by s bit positions, where s is the low 6 bits of value2. The result is pushed onto
     * the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * This is equivalent (even if overflow occurs) to multiplication by 2 to the power s.
     * The shift distance actually used is therefore always in the range 0 to 63,
     * inclusive, as if value2 were subjected to a bitwise logical AND with the mask value
     * 0x3f.
     */
    LSHL            ((byte) 0x79, 1, -1, OpCode.FLAG_MATH),      //Shift left long               (<<)

    /**
     * <b>Operation: </b>
     * Arithmetic shift right long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lshr
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lshr = 123 (0x7b)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value1 must be of type long, and value2 must be of type int. The values are
     * popped from the operand stack. A long result is calculated by shifting value1 right
     * by s bit positions, with sign extension, where s is the value of the low 6 bits of
     * value2. The result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * The resulting value is floor(value1 / 2s), where s is value2 & 0x3f. For
     * non-negative value1, this is equivalent to truncating long division by 2 to the
     * power s. The shift distance actually used is therefore always in the range 0 to 63,
     * inclusive, as if value2 were subjected to a bitwise logical AND with the mask value
     * 0x3f.
     */
    LSHR            ((byte) 0x7B, 1, -1, OpCode.FLAG_MATH),      //Arithmetic shift right long   (>>)

    /**
     * <b>Operation: </b>
     * Logical shift right long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lushr
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lushr = 125 (0x7d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value1 must be of type long, and value2 must be of type int. The values are
     * popped from the operand stack. A long result is calculated by shifting value1 right
     * logically by s bit positions, with zero extension, where s is the value of the low 6
     * bits of value2. The result is pushed onto the operand stack. <br><br>
     *
     * <b>Notes: </b><br>
     * If value1 is positive and s is value2 & 0x3f, the result is the same as that of
     * value1 &gt;&gt; s; if value1 is negative, the result is equal to the value of the
     * expression (value1 &gt;&gt; s) + (2L &lt;&lt; ~s). The addition of the (2L &lt;&lt;
     * ~s) term cancels out the propagated sign bit. The shift distance actually used is
     * always in the range 0 to 63, inclusive.
     */
    LUSHR           ((byte) 0x7D, 1, -1, OpCode.FLAG_MATH),      //Logical shift right long      (>>>)

    /**
     * <b>Operation: </b>
     * Boolean AND long <br><br>
     *
     * <b>Format: </b><pre><code>
     * land
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * land = 127 (0x7f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. They are popped from the operand stack.
     * A long result is calculated by taking the bitwise AND of value1 and value2. The
     * result is pushed onto the operand stack.
     */
    LAND            ((byte) 0x7F, 1, -2, OpCode.FLAG_MATH),      //Boolean bitwise AND long      (&)

    /**
     * <b>Operation: </b>
     * Boolean OR long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lor
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lor = 129 (0x81)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. They are popped from the operand stack.
     * A long result is calculated by taking the bitwise inclusive OR of value1 and value2.
     * The result is pushed onto the operand stack.
     */
    LOR             ((byte) 0x81, 1, -2, OpCode.FLAG_MATH),      //Boolean bitwise OR long       (|)

    /**
     * <b>Operation: </b>
     * Boolean XOR long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lxor
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lxor = 131 (0x83)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. They are popped from the operand stack.
     * A long result is calculated by taking the bitwise exclusive OR of value1 and value2.
     * The result is pushed onto the operand stack.
     */
    LXOR            ((byte) 0x83, 1, -2, OpCode.FLAG_MATH),      //Boolean bitwise XOR long      (^)


    //float number operation

    /**
     * <b>Operation: </b>
     * Add float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fadd
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fadd = 98 (0x62)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'.
     * The float result is value1' + value2'. The result is pushed onto the operand stack. <br><br>
     *
     * The result of an fadd instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>The sum of two infinities of the same sign is the infinity of that sign.</li>
     *     <li>The sum of two zeroes of opposite sign is positive zero.</li>
     *     <li>The sum of two zeroes of the same sign is the zero of that sign.</li>
     *     <li>The sum of a zero and a nonzero finite value is equal to the nonzero value.</li>
     *     <li>The sum of two nonzero finite values of the same magnitude and opposite sign is
     *     positive zero.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN and the
     *     values have the same sign or have different magnitudes, the sum is computed and
     *     rounded to the nearest representable value using IEEE 754 round to nearest mode. If
     *     the magnitude is too large to represent as a float, we say the operation overflows;
     *     the result is then an infinity of appropriate sign. If the magnitude is too small to
     *     represent as a float, we say the operation underflows; the result is then a zero of
     *     appropriate sign.</li>
     * </ul>
     *  <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of an fadd instruction never throws a run-time exception.
     */
    FADD            ((byte) 0x62, 1, -1, OpCode.FLAG_MATH),      //Add float                     (+)

    /**
     * <b>Operation: </b>
     * Subtract float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fsub
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fsub = 102 (0x66)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'.
     * The float result is value1' - value2'. The result is pushed onto the operand stack. <br><br>
     *
     * For float subtraction, it is always the case that a-b produces the same result as
     * a+(-b). However, for the fsub instruction, subtraction from zero is not the same as
     * negation, because if x is +0.0, then 0.0-x equals +0.0, but -x equals -0.0. <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of an fsub instruction never throws a run-time exception.
     */
    FSUB            ((byte) 0x66, 1, -1, OpCode.FLAG_MATH),      //Subtract float                (-)

    /**
     * <b>Operation: </b>
     * Multiply float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fmul
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fmul = 106 (0x6a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'.
     * The float result is value1' * value2'. The result is pushed onto the operand stack. <br><br>
     *
     * The result of an fmul instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result is positive if both
     *     values have the same sign, and negative if the values have different signs.</li>
     *     <li>Multiplication of an infinity by a zero results in NaN.</li>
     *     <li>Multiplication of an infinity by a finite value results in a signed infinity, with
     *     the sign-producing rule just given.</li>
     *     <li>In the remaining cases, where neither an infinity nor NaN is involved, the product
     *     is computed and rounded to the nearest representable value using IEEE 754 round to
     *     nearest mode. If the magnitude is too large to represent as a float, we say the
     *     operation overflows; the result is then an infinity of appropriate sign. If the
     *     magnitude is too small to represent as a float, we say the operation underflows; the
     *     result is then a zero of appropriate sign.</li>
     * </ul>
     *  <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of an fmul instruction never throws a run-time exception.
     */
    FMUL            ((byte) 0x6A, 1, -1, OpCode.FLAG_MATH),      //Multiply float                (*)

    /**
     * <b>Operation: </b>
     * Divide float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fdiv
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fdiv = 110 (0x6e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'.
     * The float result is value1' / value2'. The result is pushed onto the operand stack. <br><br>
     *
     * The result of an fdiv instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result is positive if both
     *     values have the same sign, negative if the values have different signs.</li>
     *     <li>Division of an infinity by an infinity results in NaN.</li>
     *     <li>Division of an infinity by a finite value results in a signed infinity, with the
     *     sign-producing rule just given.</li>
     *     <li>Division of a finite value by an infinity results in a signed zero, with the
     *     sign-producing rule just given.</li>
     *     <li>Division of a zero by a zero results in NaN; division of zero by any other finite
     *     value results in a signed zero, with the sign-producing rule just given.</li>
     *     <li>Division of a nonzero finite value by a zero results in a signed infinity, with the
     *     sign-producing rule just given.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN, the
     *     quotient is computed and rounded to the nearest float using IEEE 754 round to
     *     nearest mode. If the magnitude is too large to represent as a float, we say the
     *     operation overflows; the result is then an infinity of appropriate sign. If the
     *     magnitude is too small to represent as a float, we say the operation underflows; the
     *     result is then a zero of appropriate sign.</li>
     * </ul>
     *  <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, division by zero, or loss of
     * precision may occur, execution of an fdiv instruction never throws a run-time
     * exception.
     */
    FDIV            ((byte) 0x6E, 1, -1, OpCode.FLAG_MATH),      //Divide float                  (/)

    /**
     * <b>Operation: </b>
     * Remainder float <br><br>
     *
     * <b>Format: </b><pre><code>
     * frem
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * frem = 114 (0x72)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'.
     * The result is calculated and pushed onto the operand stack as a float. <br><br>
     *
     * The result of an frem instruction is not the same as that of the so-called remainder
     * operation defined by IEEE 754. The IEEE 754 "remainder" operation computes the
     * remainder from a rounding division, not a truncating division, and so its behavior
     * is not analogous to that of the usual integer remainder operator. Instead, the Java
     * Virtual Machine defines frem to behave in a manner analogous to that of the Java
     * Virtual Machine integer remainder instructions (irem and lrem); this may be compared
     * with the C library function fmod. <br><br>
     *
     * The result of an frem instruction is governed by these rules: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result equals the sign of the
     *     dividend.</li>
     *     <li>If the dividend is an infinity or the divisor is a zero or both, the result is NaN.</li>
     *     <li>If the dividend is finite and the divisor is an infinity, the result equals the
     *     dividend.</li>
     *     <li>If the dividend is a zero and the divisor is finite, the result equals the dividend.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN, the
     *     floating-point remainder result from a dividend value1' and a divisor value2' is
     *     defined by the mathematical relation result = value1' - (value2' * q), where q is an
     *     integer that is negative only if value1' / value2' is negative and positive only if
     *     value1' / value2' is positive, and whose magnitude is as large as possible without
     *     exceeding the magnitude of the true mathematical quotient of value1' and value2'.</li>
     * </ul>
     *  <br><br>
     *
     * Despite the fact that division by zero may occur, evaluation of an frem instruction
     * never throws a run-time exception. Overflow, underflow, or loss of precision cannot
     * occur. <br><br>
     *
     * <b>Notes: </b><br>
     * The IEEE 754 remainder operation may be computed by the library routine
     * Math.IEEEremainder.
     */
    FREM            ((byte) 0x72, 1, -1, OpCode.FLAG_MATH),      //Remainder float               (%)

    /**
     * <b>Operation: </b>
     * Negate float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fneg
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fneg = 118 (0x76)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type float. It is popped from the operand stack and undergoes
     * value set conversion (§2.8.3), resulting in value'. The float result is the
     * arithmetic negation of value'. This result is pushed onto the operand stack. <br><br>
     *
     * For float values, negation is not the same as subtraction from zero. If x is +0.0,
     * then 0.0-x equals +0.0, but -x equals -0.0. Unary minus merely inverts the sign of a
     * float. <br><br>
     *
     * Special cases of interest: <br><br>
     * <ul>
     *     <li>If the operand is NaN, the result is NaN (recall that NaN has no sign).</li>
     *     <li>If the operand is an infinity, the result is the infinity of opposite sign.</li>
     *     <li>If the operand is a zero, the result is the zero of opposite sign.</li>
     * </ul>
     */
    FNEG            ((byte) 0x76, 1, 0, OpCode.FLAG_MATH),      //Negate float                  (-)


    //double number operation

    /**
     * <b>Operation: </b>
     * Add double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dadd
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dadd = 99 (0x63)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. The double result is value1' + value2'. The result is pushed onto the
     * operand stack. <br><br>
     *
     * The result of a dadd instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>The sum of two infinities of opposite sign is NaN.</li>
     *     <li>The sum of two infinities of the same sign is the infinity of that sign.</li>
     *     <li>The sum of an infinity and any finite value is equal to the infinity.</li>
     *     <li>The sum of two zeroes of opposite sign is positive zero.</li>
     *     <li>The sum of two zeroes of the same sign is the zero of that sign.</li>
     *     <li>The sum of a zero and a nonzero finite value is equal to the nonzero value.</li>
     *     <li>The sum of two nonzero finite values of the same magnitude and opposite sign is
     *     positive zero.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN and the
     *     values have the same sign or have different magnitudes, the sum is computed and
     *     rounded to the nearest representable value using IEEE 754 round to nearest mode. If
     *     the magnitude is too large to represent as a double, we say the operation overflows;
     *     the result is then an infinity of appropriate sign. If the magnitude is too small to
     *     represent as a double, we say the operation underflows; the result is then a zero of
     *     appropriate sign.</li>
     * </ul>
     * <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of a dadd instruction never throws a run-time exception.
     */
    DADD            ((byte) 0x63, 1, -2, OpCode.FLAG_MATH),      //Add double                    (+)

    /**
     * <b>Operation: </b>
     * Subtract double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dsub
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dsub = 103 (0x67)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. The double result is value1' - value2'. The result is pushed onto the
     * operand stack. <br><br>
     *
     * For double subtraction, it is always the case that a-b produces the same result as
     * a+(-b). However, for the dsub instruction, subtraction from zero is not the same as
     * negation, because if x is +0.0, then 0.0-x equals +0.0, but -x equals -0.0. <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of a dsub instruction never throws a run-time exception.
     */
    DSUB            ((byte) 0x67, 1, -2, OpCode.FLAG_MATH),      //Subtract double               (-)

    /**
     * <b>Operation: </b>
     * Multiply double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dmul
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dmul = 107 (0x6b)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. The double result is value1' * value2'. The result is pushed onto the
     * operand stack. <br><br>
     *
     * The result of a dmul instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result is positive if both
     *     values have the same sign and negative if the values have different signs.</li>
     *     <li>Multiplication of an infinity by a zero results in NaN.</li>
     *     <li>Multiplication of an infinity by a finite value results in a signed infinity, with
     *     the sign-producing rule just given.</li>
     *     <li>In the remaining cases, where neither an infinity nor NaN is involved, the product
     *     is computed and rounded to the nearest representable value using IEEE 754 round to
     *     nearest mode. If the magnitude is too large to represent as a double, we say the
     *     operation overflows; the result is then an infinity of appropriate sign. If the
     *     magnitude is too small to represent as a double, we say the operation underflows;
     *     the result is then a zero of appropriate sign.</li>
     * </ul>
     * <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, or loss of precision may occur,
     * execution of a dmul instruction never throws a run-time exception.
     */
    DMUL            ((byte) 0x6B, 1, -2, OpCode.FLAG_MATH),      //Multiply double               (*)

    /**
     * <b>Operation: </b>
     * Divide double <br><br>
     *
     * <b>Format: </b><pre><code>
     * ddiv
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ddiv = 111 (0x6f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. The double result is value1' / value2'. The result is pushed onto the
     * operand stack. <br><br>
     *
     * The result of a ddiv instruction is governed by the rules of IEEE arithmetic: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result is positive if both
     *     values have the same sign, negative if the values have different signs.</li>
     *     <li>Division of an infinity by an infinity results in NaN.</li>
     *     <li>Division of an infinity by a finite value results in a signed infinity, with the
     *     sign-producing rule just given.</li>
     *     <li>Division of a finite value by an infinity results in a signed zero, with the
     *     sign-producing rule just given.</li>
     *     <li>Division of a zero by a zero results in NaN; division of zero by any other finite
     *     value results in a signed zero, with the sign-producing rule just given.</li>
     *     <li>Division of a nonzero finite value by a zero results in a signed infinity, with the
     *     sign-producing rule just given.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN, the
     *     quotient is computed and rounded to the nearest double using IEEE 754 round to
     *     nearest mode. If the magnitude is too large to represent as a double, we say the
     *     operation overflows; the result is then an infinity of appropriate sign. If the
     *     magnitude is too small to represent as a double, we say the operation underflows;
     *     the result is then a zero of appropriate sign.</li>
     * </ul>
     * <br><br>
     *
     * The Java Virtual Machine requires support of gradual underflow as defined by IEEE
     * 754. Despite the fact that overflow, underflow, division by zero, or loss of
     * precision may occur, execution of a ddiv instruction never throws a run-time
     * exception.
     */
    DDIV            ((byte) 0x6F, 1, -2, OpCode.FLAG_MATH),      //Divide double                 (/)

    /**
     * <b>Operation: </b>
     * Remainder double <br><br>
     *
     * <b>Format: </b><pre><code>
     * drem
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * drem = 115 (0x73)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. The result is calculated and pushed onto the operand stack as a double. <br><br>
     *
     * The result of a drem instruction is not the same as that of the so-called remainder
     * operation defined by IEEE 754. The IEEE 754 "remainder" operation computes the
     * remainder from a rounding division, not a truncating division, and so its behavior
     * is not analogous to that of the usual integer remainder operator. Instead, the Java
     * Virtual Machine defines drem to behave in a manner analogous to that of the Java
     * Virtual Machine integer remainder instructions (irem and lrem); this may be compared
     * with the C library function fmod. <br><br>
     *
     * The result of a drem instruction is governed by these rules: <br>
     * <ul>
     *     <li>If either value1' or value2' is NaN, the result is NaN.</li>
     *     <li>If neither value1' nor value2' is NaN, the sign of the result equals the sign of the
     *     dividend.</li>
     *     <li>If the dividend is an infinity or the divisor is a zero or both, the result is NaN.</li>
     *     <li>If the dividend is finite and the divisor is an infinity, the result equals the
     *     dividend.</li>
     *     <li>If the dividend is a zero and the divisor is finite, the result equals the dividend.</li>
     *     <li>In the remaining cases, where neither operand is an infinity, a zero, or NaN, the
     *     floating-point remainder result from a dividend value1' and a divisor value2' is
     *     defined by the mathematical relation result = value1' - (value2' * q), where q is an
     *     integer that is negative only if value1' / value2' is negative, and positive only if
     *     value1' / value2' is positive, and whose magnitude is as large as possible without
     *     exceeding the magnitude of the true mathematical quotient of value1' and value2'.</li>
     * </ul>
     * <br><br>
     *
     * Despite the fact that division by zero may occur, evaluation of a drem instruction
     * never throws a run-time exception. Overflow, underflow, or loss of precision cannot
     * occur. <br><br>
     *
     * <b>Notes: </b><br>
     * The IEEE 754 remainder operation may be computed by the library routine
     * Math.IEEEremainder.
     */
    DREM            ((byte) 0x73, 1, -2, OpCode.FLAG_MATH),      //Remainder double              (%)

    /**
     * <b>Operation: </b>
     * Negate double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dneg
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dneg = 119 (0x77)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type double. It is popped from the operand stack and undergoes
     * value set conversion (§2.8.3), resulting in value'. The double result is the
     * arithmetic negation of value'. The result is pushed onto the operand stack. <br><br>
     *
     * For double values, negation is not the same as subtraction from zero. If x is +0.0,
     * then 0.0-x equals +0.0, but -x equals -0.0. Unary minus merely inverts the sign of a
     * double. <br><br>
     *
     * Special cases of interest: <br>
     * <ul>
     *     <li>If the operand is NaN, the result is NaN (recall that NaN has no sign).</li>
     *     <li>If the operand is an infinity, the result is the infinity of opposite sign.</li>
     *     <li>If the operand is a zero, the result is the zero of opposite sign.</li>
     * </ul>
     */
    DNEG            ((byte) 0x77, 1, 0, OpCode.FLAG_MATH),      //Negate double                 (-)


    //########## Comparision ##########//
    
    //Compare long

    /**
     * <b>Operation: </b>
     * Compare long <br><br>
     *
     * <b>Format: </b><pre><code>
     * lcmp
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lcmp = 148 (0x94)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type long. They are both popped from the operand
     * stack, and a signed integer comparison is performed. If value1 is greater than
     * value2, the int value 1 is pushed onto the operand stack. If value1 is equal to
     * value2, the int value 0 is pushed onto the operand stack. If value1 is less than
     * value2, the int value -1 is pushed onto the operand stack.
     */
    LCMP            ((byte) 0x94, 1, -3, OpCode.FLAG_MATH),

    //Compare float

    /**
     * <b>Operation: </b>
     * Compare float <br><br>
     *
     * <b>Format: </b><pre><code>
     * fcmp&lt;op&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * fcmpg = 150 (0x96)
     * fcmpl = 149 (0x95)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type float. The values are popped from the operand
     * stack and undergo value set conversion (§2.8.3), resulting in value1' and value2'. A
     * floating-point comparison is performed: <br><br>
     *
     * If value1' is greater than value2', the int value 1 is pushed onto the operand
     * stack. <br><br>
     *
     * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the
     * operand stack. <br><br>
     *
     * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the
     * operand stack. <br><br>
     *
     * Otherwise, at least one of value1' or value2' is NaN. The fcmpg instruction pushes
     * the int value 1 onto the operand stack and the fcmpl instruction pushes the int
     * value -1 onto the operand stack. <br><br>
     *
     * Floating-point comparison is performed in accordance with IEEE 754. All values other
     * than NaN are ordered, with negative infinity less than all finite values and
     * positive infinity greater than all finite values. Positive zero and negative zero
     * are considered equal. <br><br>
     *
     * <b>Notes: </b><br>
     * The fcmpg and fcmpl instructions differ only in their treatment of a comparison
     * involving NaN. NaN is unordered, so any float comparison fails if either or both of
     * its operands are NaN. With both fcmpg and fcmpl available, any float comparison may
     * be compiled to push the same result onto the operand stack whether the comparison
     * fails on non-NaN values or fails because it encountered a NaN. For more information,
     * see §3.5.
     */
    FCMPL           ((byte) 0x95, 1, -1, OpCode.FLAG_MATH),
    
    /** @see OpCodeType#FCMPG */
    FCMPG           ((byte) 0x96, 1, -1, OpCode.FLAG_MATH),

    //Compare double

    /**
     * <b>Operation: </b>
     * Compare double <br><br>
     *
     * <b>Format: </b><pre><code>
     * dcmp&lt;op&gt;
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dcmpg = 152 (0x98)
     * dcmpl = 151 (0x97)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type double. The values are popped from the
     * operand stack and undergo value set conversion (§2.8.3), resulting in value1' and
     * value2'. A floating-point comparison is performed: <br><br>
     *
     * If value1' is greater than value2', the int value 1 is pushed onto the operand
     * stack. <br><br>
     *
     * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the
     * operand stack. <br><br>
     *
     * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the
     * operand stack. <br><br>
     *
     * Otherwise, at least one of value1' or value2' is NaN. The dcmpg instruction pushes
     * the int value 1 onto the operand stack and the dcmpl instruction pushes the int
     * value -1 onto the operand stack. <br><br>
     *
     * Floating-point comparison is performed in accordance with IEEE 754. All values other
     * than NaN are ordered, with negative infinity less than all finite values and
     * positive infinity greater than all finite values. Positive zero and negative zero
     * are considered equal. <br><br>
     *
     * <b>Notes: </b><br>
     * The dcmpg and dcmpl instructions differ only in their treatment of a comparison
     * involving NaN. NaN is unordered, so any double comparison fails if either or both of
     * its operands are NaN. With both dcmpg and dcmpl available, any double comparison may
     * be compiled to push the same result onto the operand stack whether the comparison
     * fails on non-NaN values or fails because it encountered a NaN. For more information,
     * see §3.5.
     */
    DCMPL           ((byte) 0x97, 1, -3, OpCode.FLAG_MATH),
    
    /** @see OpCodeType#DCMPL */
    DCMPG           ((byte) 0x98, 1, -3, OpCode.FLAG_MATH),


    //########## Condition ##########//

    //Branch if int comparison with *zero* succeeds     (if<cond>)

    /**
     * <b>Operation: </b>
     * Branch if int comparison with zero succeeds <br><br>
     *
     * <b>Format: </b><pre><code>
     * if&lt;cond&gt;
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ifeq = 153 (0x99)
     * ifne = 154 (0x9a)
     * iflt = 155 (0x9b)
     * ifge = 156 (0x9c)
     * ifgt = 157 (0x9d)
     * ifle = 158 (0x9e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type int. It is popped from the operand stack and compared
     * against zero. All comparisons are signed. The results of the comparisons are as
     * follows: <br>
     * <ul>
     *     <li>ifeq succeeds if and only if value = 0</li>
     *     <li>ifne succeeds if and only if value ≠ 0</li>
     *     <li>iflt succeeds if and only if value &lt; 0</li>
     *     <li>ifle succeeds if and only if value ≤ 0</li>
     *     <li>ifgt succeeds if and only if value &gt; 0</li>
     *     <li>ifge succeeds if and only if value ≥ 0</li>
     * </ul>
     *  <br><br>
     *
     * If the comparison succeeds, the unsigned branchbyte1 and branchbyte2 are used to
     * construct a signed 16-bit offset, where the offset is calculated to be (branchbyte1
     * &lt;&lt; 8) | branchbyte2. Execution then proceeds at that offset from the address
     * of the opcode of this if&lt;cond&gt; instruction. The target address must be that of
     * an opcode of an instruction within the method that contains this if&lt;cond&gt;
     * instruction. <br><br>
     *
     * Otherwise, execution proceeds at the address of the instruction following this
     * if&lt;cond&gt; instruction.
     */
    IFEQ            ((byte) 0x99, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value = 0

    /** @see OpCodeType#IFEQ */
    IFNE            ((byte) 0x9A, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value ≠ 0

    /** @see OpCodeType#IFEQ */
    IFLT            ((byte) 0x9B, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value < 0

    /** @see OpCodeType#IFEQ */
    IFGE            ((byte) 0x9C, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value ≥ 0

    /** @see OpCodeType#IFEQ */
    IFGT            ((byte) 0x9D, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value > 0

    /** @see OpCodeType#IFEQ */
    IFLE            ((byte) 0x9E, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value ≤ 0

    //Branch if int comparison succeeds                 (if_icmp<cond>)

    /**
     * <b>Operation: </b>
     * Branch if int comparison succeeds <br><br>
     *
     * <b>Format: </b><pre><code>
     * if_icmp&lt;cond&gt;
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * if_icmpeq = 159 (0x9f)
     * if_icmpne = 160 (0xa0)
     * if_icmplt = 161 (0xa1)
     * if_icmpge = 162 (0xa2)
     * if_icmpgt = 163 (0xa3)
     * if_icmple = 164 (0xa4)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type int. They are both popped from the operand
     * stack and compared. All comparisons are signed. The results of the comparison are as
     * follows: <br>
     * <ul>
     *     <li>if_icmpeq succeeds if and only if value1 = value2</li>
     *     <li>if_icmpne succeeds if and only if value1 ≠ value2</li>
     *     <li>if_icmplt succeeds if and only if value1 &lt; value2</li>
     *     <li>if_icmple succeeds if and only if value1 ≤ value2</li>
     *     <li>if_icmpgt succeeds if and only if value1 &gt; value2</li>
     *     <li>if_icmpge succeeds if and only if value1 ≥ value2</li>
     * </ul>
     * <br><br>
     *
     * If the comparison succeeds, the unsigned branchbyte1 and branchbyte2 are used to
     * construct a signed 16-bit offset, where the offset is calculated to be (branchbyte1
     * &lt;&lt; 8) | branchbyte2. Execution then proceeds at that offset from the address
     * of the opcode of this if_icmp&lt;cond&gt; instruction. The target address must be
     * that of an opcode of an instruction within the method that contains this
     * if_icmp&lt;cond&gt; instruction. <br><br>
     *
     * Otherwise, execution proceeds at the address of the instruction following this
     * if_icmp&lt;cond&gt; instruction.
     */
    IF_ICMPEQ       ((byte) 0x9F, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 = value2

    /** @see OpCodeType#IF_ICMPEQ */
    IF_ICMPNE       ((byte) 0xA0, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 ≠ value2

    /** @see OpCodeType#IF_ICMPEQ */
    IF_ICMPLT       ((byte) 0xA1, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 < value2

    /** @see OpCodeType#IF_ICMPEQ */
    IF_ICMPGE       ((byte) 0xA2, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 ≥ value2

    /** @see OpCodeType#IF_ICMPEQ */
    IF_ICMPGT       ((byte) 0xA3, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 > value2

    /** @see OpCodeType#IF_ICMPEQ */
    IF_ICMPLE       ((byte) 0xA4, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 ≤ value2

    //Branch if reference comparison succeeds           (if_acmp<cond>)

    /**
     * <b>Operation: </b>
     * Branch if reference comparison succeeds <br><br>
     *
     * <b>Format: </b><pre><code>
     * if_acmp&lt;cond&gt;
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * if_acmpeq = 165 (0xa5)
     * if_acmpne = 166 (0xa6)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value1, value2 →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Both value1 and value2 must be of type reference. They are both popped from the
     * operand stack and compared. The results of the comparison are as follows: <br>
     * <ul>
     *     <li>if_acmpeq succeeds if and only if value1 = value2</li>
     *     <li>if_acmpne succeeds if and only if value1 ≠ value2</li>
     * </ul>
     * <br><br>
     *
     * If the comparison succeeds, the unsigned branchbyte1 and branchbyte2 are used to
     * construct a signed 16-bit offset, where the offset is calculated to be (branchbyte1
     * &lt;&lt; 8) | branchbyte2. Execution then proceeds at that offset from the address
     * of the opcode of this if_acmp&lt;cond&gt; instruction. The target address must be
     * that of an opcode of an instruction within the method that contains this
     * if_acmp&lt;cond&gt; instruction. <br><br>
     *
     * Otherwise, if the comparison fails, execution proceeds at the address of the
     * instruction following this if_acmp&lt;cond&gt; instruction.
     */
    IF_ACMPEQ       ((byte) 0xA5, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 = value2

    /** @see OpCodeType#IF_ACMPEQ */
    IF_ACMPNE       ((byte) 0xA6, 3, -2, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //if and only if value1 ≠ value2

    //Special

    /**
     * <b>Operation: </b>
     * Check whether object is of given type <br><br>
     *
     * <b>Format: </b><pre><code>
     * checkcast
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * checkcast = 192 (0xc0)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ..., objectref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref must be of type reference. The unsigned indexbyte1 and indexbyte2 are
     * used to construct an index into the run-time constant pool of the current class
     * (§2.6), where the value of the index is (indexbyte1 &lt;&lt; 8) | indexbyte2. The
     * run-time constant pool item at the index must be a symbolic reference to a class,
     * array, or interface type. <br><br>
     *
     * If objectref is null, then the operand stack is unchanged. <br><br>
     *
     * Otherwise, the named class, array, or interface type is resolved (§5.4.3.1). If
     * objectref can be cast to the resolved class, array, or interface type, the operand
     * stack is unchanged; otherwise, the checkcast instruction throws a
     * <code>ClassCastException.</code> <br><br>
     *
     * The following rules are used to determine whether an objectref that is not null can
     * be cast to the resolved type: if S is the class of the object referred to by
     * objectref and T is the resolved class, array, or interface type, checkcast
     * determines whether objectref can be cast to type T as follows: <br>
     * <ul>
     *     <li>
     *         If S is an ordinary (nonarray) class, then:
     *         <ul>
     *             <li>If T is a class type, then S must be the same class as T, or S must be a
     *             subclass of T;</li>
     *             <li>If T is an interface type, then S must implement interface T.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         If S is an interface type, then:
     *         <ul>
     *             <li>If T is a class type, then T must be Object.</li>
     *             <li>If T is an interface type, then T must be the same interface as S or a
     *             superinterface of S.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         If S is a class representing the array type SC[], that is, an array of components of
     *         type SC, then:
     *         <ul>
     *             <li>If T is a class type, then T must be Object.</li>
     *             <li>If T is an interface type, then T must be one of the interfaces implemented by
     *             arrays (JLS §4.10.3).</li>
     *             <li>
     *                 If T is an array type TC[], that is, an array of components of type TC, then
     *                 one of the following must be true:
     *                 <ul>
     *                     <li>TC and SC are the same primitive type.</li>
     *                     <li>TC and SC are reference types, and type SC can be cast to TC by recursive
     *                     application of these rules.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ul>
     * <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class, array, or interface type,
     * any of the exceptions documented in §5.4.3.1 can be thrown. <br><br>
     *
     * <b>Run-time Exception: </b><br>
     * Otherwise, if objectref cannot be cast to the resolved class, array, or interface
     * type, the checkcast instruction throws a <code>ClassCastException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * The checkcast instruction is very similar to the instanceof instruction
     * (§instanceof). It differs in its treatment of null, its behavior when its test fails
     * (checkcast throws an exception, instanceof pushes a result code), and its effect on
     * the operand stack.
     */
    CHECKCAST       ((byte) 0xC0, 3, 0),      //Check whether object is of given type

    /**
     * <b>Operation: </b>
     * Determine if object is of given type <br><br>
     *
     * <b>Format: </b><pre><code>
     * instanceof
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * instanceof = 193 (0xc1)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ..., result
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref, which must be of type reference, is popped from the operand stack.
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at the index
     * must be a symbolic reference to a class, array, or interface type. <br><br>
     *
     * If objectref is null, the instanceof instruction pushes an int result of 0 as an int
     * on the operand stack. <br><br>
     *
     * Otherwise, the named class, array, or interface type is resolved (§5.4.3.1). If
     * objectref is an instance of the resolved class or array or implements the resolved
     * interface, the instanceof instruction pushes an int result of 1 as an int on the
     * operand stack; otherwise, it pushes an int result of 0. <br><br>
     *
     * The following rules are used to determine whether an objectref that is not null is
     * an instance of the resolved type: If S is the class of the object referred to by
     * objectref and T is the resolved class, array, or interface type, instanceof
     * determines whether objectref is an instance of T as follows: <br>
     * <ul>
     *     <li>
     *         If S is an ordinary (nonarray) class, then:
     *         <ul>
     *             <li>If T is a class type, then S must be the same class as T, or S must be a
     *             subclass of T;</li>
     *             <li>If T is an interface type, then S must implement interface T.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         If S is an interface type, then:
     *         <ul>
     *             <li>If T is a class type, then T must be Object.</li>
     *             <li>If T is an interface type, then T must be the same interface as S or a
     *             superinterface of S.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         If S is a class representing the array type SC[], that is, an array of components of
     *         type SC, then:
     *         <ul>
     *             <li>If T is a class type, then T must be Object.</li>
     *             <li>If T is an interface type, then T must be one of the interfaces implemented by
     *             arrays (JLS §4.10.3).</li>
     *             <li>
     *                 If T is an array type TC[], that is, an array of components of type TC, then one of
     *                 the following must be true:
     *                 <ul>
     *                     <li>TC and SC are the same primitive type.</li>
     *                     <li>TC and SC are reference types, and type SC can be cast to TC by these run-time
     *                     rules.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ul>
     * <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class, array, or interface type,
     * any of the exceptions documented in §5.4.3.1 can be thrown. <br><br>
     *
     * <b>Notes: </b><br>
     * The instanceof instruction is very similar to the checkcast instruction
     * (§checkcast). It differs in its treatment of null, its behavior when its test fails
     * (checkcast throws an exception, instanceof pushes a result code), and its effect on
     * the operand stack.
     */
    INSTANCEOF      ((byte) 0xC1, 3, 0),      //Determine if object is of given type

    /**
     * <b>Operation: </b>
     * Branch if reference is null <br><br>
     *
     * <b>Format: </b><pre><code>
     * ifnull
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ifnull = 198 (0xc6)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must of type reference. It is popped from the operand stack. If value is
     * null, the unsigned branchbyte1 and branchbyte2 are used to construct a signed 16-bit
     * offset, where the offset is calculated to be (branchbyte1 &lt;&lt; 8) | branchbyte2.
     * Execution then proceeds at that offset from the address of the opcode of this ifnull
     * instruction. The target address must be that of an opcode of an instruction within
     * the method that contains this ifnull instruction. <br><br>
     *
     * Otherwise, execution proceeds at the address of the instruction following this
     * ifnull instruction.
     */
    IFNULL          ((byte) 0xC6, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //Branch if reference is null

    /**
     * <b>Operation: </b>
     * Branch if reference not null <br><br>
     *
     * <b>Format: </b><pre><code>
     * ifnonnull
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ifnonnull = 199 (0xc7)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The value must be of type reference. It is popped from the operand stack. If value
     * is not null, the unsigned branchbyte1 and branchbyte2 are used to construct a signed
     * 16-bit offset, where the offset is calculated to be (branchbyte1 &lt;&lt; 8) |
     * branchbyte2. Execution then proceeds at that offset from the address of the opcode
     * of this ifnonnull instruction. The target address must be that of an opcode of an
     * instruction within the method that contains this ifnonnull instruction. <br><br>
     *
     * Otherwise, execution proceeds at the address of the instruction following this
     * ifnonnull instruction.
     */
    IFNONNULL       ((byte) 0xC7, 3, -1, OpCode.FLAG_JUMP | OpCode.FLAG_IF),      //Branch if reference not null

    //########## Stack operation ##########//
    //pop

    /**
     * <b>Operation: </b>
     * Pop the top operand stack value <br><br>
     *
     * <b>Format: </b><pre><code>
     * pop
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * pop = 87 (0x57)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Pop the top value from the operand stack. <br><br>
     *
     * The pop instruction must not be used unless value is a value of a category 1
     * computational type (§2.11.1).
     */
    POP             ((byte) 0x57, 1, -1),      //Pop the top operand stack value

    /**
     * <b>Operation: </b>
     * Pop the top one or two operand stack values <br><br>
     *
     * <b>Format: </b><pre><code>
     * pop2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * pop2 = 88 (0x58)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Form 1:
     * ..., value2, value1 →
     * ...
     * where each of value1 and value2 is a value of a category 1 computational type
     * (§2.11.1).
     * Form 2:
     * ..., value →
     * ...
     * where value is a value of a category 2 computational type (§2.11.1).
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Pop the top one or two values from the operand stack.
     */
    POP2            ((byte) 0x58, 1, -2),      //Pop the top one or two operand stack values


    //duplicate

    /**
     * <b>Operation: </b>
     * Duplicate the top operand stack value <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup = 89 (0x59)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ..., value, value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top value on the operand stack and push the duplicated value onto the
     * operand stack. <br><br>
     *
     * The dup instruction must not be used unless value is a value of a category 1
     * computational type (§2.11.1).
     */
    DUP             ((byte) 0x59, 1, 1),      //Duplicate the top operand stack value

    /**
     * <b>Operation: </b>
     * Duplicate the top operand stack value and insert two values down <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup_x1
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup_x1 = 90 (0x5a)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value2, value1 →
     * ..., value1, value2, value1
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top value on the operand stack and insert the duplicated value two
     * values down in the operand stack. <br><br>
     *
     * The dup_x1 instruction must not be used unless both value1 and value2 are values of
     * a category 1 computational type (§2.11.1).
     */
    DUP_X1          ((byte) 0x5A, 1, 1),      //Duplicate the top operand stack value and insert two values down

    /**
     * <b>Operation: </b>
     * Duplicate the top operand stack value and insert two or three values down <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup_x2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup_x2 = 91 (0x5b)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Form 1:
     * ..., value3, value2, value1 →
     * ..., value1, value3, value2, value1
     * where value1, value2, and value3 are all values of a category 1 computational type
     * (§2.11.1).
     * Form 2:
     * ..., value2, value1 →
     * ..., value1, value2, value1
     * where value1 is a value of a category 1 computational type and value2 is a value of
     * a category 2 computational type (§2.11.1).
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top value on the operand stack and insert the duplicated value two or
     * three values down in the operand stack.
     */
    DUP_X2          ((byte) 0x5B, 1, 1),      //Duplicate the top operand stack value and insert two or three values down

    /**
     * <b>Operation: </b>
     * Duplicate the top one or two operand stack values <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup2 = 92 (0x5c)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Form 1:
     * ..., value2, value1 →
     * ..., value2, value1, value2, value1
     * where both value1 and value2 are values of a category 1 computational type
     * (§2.11.1).
     * Form 2:
     * ..., value →
     * ..., value, value
     * where value is a value of a category 2 computational type (§2.11.1).
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top one or two values on the operand stack and push the duplicated
     * value or values back onto the operand stack in the original order.
     */
    DUP2            ((byte) 0x5C, 1, 2),      //Duplicate the top one or two operand stack values

    /**
     * <b>Operation: </b>
     * Duplicate the top one or two operand stack values and insert two or three values
     * down <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup2_x1
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup2_x1 = 93 (0x5d)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Form 1:
     * ..., value3, value2, value1 →
     * ..., value2, value1, value3, value2, value1
     * where value1, value2, and value3 are all values of a category 1 computational type
     * (§2.11.1).
     * Form 2:
     * ..., value2, value1 →
     * ..., value1, value2, value1
     * where value1 is a value of a category 2 computational type and value2 is a value of
     * a category 1 computational type (§2.11.1).
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top one or two values on the operand stack and insert the duplicated
     * values, in the original order, one value beneath the original value or values in the
     * operand stack.
     */
    DUP2_X1         ((byte) 0x5D, 1, 2),      //Duplicate the top one or two operand stack values and insert two or three values down

    /**
     * <b>Operation: </b>
     * Duplicate the top one or two operand stack values and insert two, three, or four
     * values down <br><br>
     *
     * <b>Format: </b><pre><code>
     * dup2_x2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dup2_x2 = 94 (0x5e)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Form 1:
     * ..., value4, value3, value2, value1 →
     * ..., value2, value1, value4, value3, value2, value1
     * where value1, value2, value3, and value4 are all values of a category 1
     * computational type (§2.11.1).
     * Form 2:
     * ..., value3, value2, value1 →
     * ..., value1, value3, value2, value1
     * where value1 is a value of a category 2 computational type and value2 and value3 are
     * both values of a category 1 computational type (§2.11.1).
     * Form 3:
     * ..., value3, value2, value1 →
     * ..., value2, value1, value3, value2, value1
     * where value1 and value2 are both values of a category 1 computational type and
     * value3 is a value of a category 2 computational type (§2.11.1).
     * Form 4:
     * ..., value2, value1 →
     * ..., value1, value2, value1
     * where value1 and value2 are both values of a category 2 computational type
     * (§2.11.1).
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Duplicate the top one or two values on the operand stack and insert the duplicated
     * values, in the original order, into the operand stack.
     */
    DUP2_X2         ((byte) 0x5E, 1, 2),      //Duplicate the top one or two operand stack values and insert two, three, or four values down

    //swap

    /**
     * <b>Operation: </b>
     * Swap the top two operand stack values <br><br>
     *
     * <b>Format: </b><pre><code>
     * swap
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * swap = 95 (0x5f)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value2, value1 →
     * ..., value1, value2
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Swap the top two values on the operand stack. <br><br>
     *
     * The swap instruction must not be used unless value1 and value2 are both values of a
     * category 1 computational type (§2.11.1). <br><br>
     *
     * <b>Notes: </b><br>
     * The Java Virtual Machine does not provide an instruction implementing a swap on
     * operands of category 2 computational types.
     */
    SWAP            ((byte) 0x5F, 1, 0),      //Swap the top two operand stack values


    //########## Route control ##########//

    /**
     * <b>Operation: </b>
     * Branch always <br><br>
     *
     * <b>Format: </b><pre><code>
     * goto
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * goto = 167 (0xa7)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * No change
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned bytes branchbyte1 and branchbyte2 are used to construct a signed 16-bit
     * branchoffset, where branchoffset is (branchbyte1 &lt;&lt; 8) | branchbyte2.
     * Execution proceeds at that offset from the address of the opcode of this goto
     * instruction. The target address must be that of an opcode of an instruction within
     * the method that contains this goto instruction.
     */
    GOTO            ((byte) 0xA7, 3, 0, OpCode.FLAG_JUMP),      //Branch always

    /**
     * <b>Operation: </b>
     * Branch always (wide index) <br><br>
     *
     * <b>Format: </b><pre><code>
     * goto_w
     * branchbyte1
     * branchbyte2
     * branchbyte3
     * branchbyte4
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * goto_w = 200 (0xc8)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * No change
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned bytes branchbyte1, branchbyte2, branchbyte3, and branchbyte4 are used
     * to construct a signed 32-bit branchoffset, where branchoffset is (branchbyte1
     * &lt;&lt; 24) | (branchbyte2 &lt;&lt; 16) | (branchbyte3 &lt;&lt; 8) | branchbyte4.
     * Execution proceeds at that offset from the address of the opcode of this goto_w
     * instruction. The target address must be that of an opcode of an instruction within
     * the method that contains this goto_w instruction. <br><br>
     *
     * <b>Notes: </b><br>
     * Although the goto_w instruction takes a 4-byte branch offset, other factors limit
     * the size of a method to 65535 bytes (§4.11). This limit may be raised in a future
     * release of the Java Virtual Machine.
     */
    GOTO_W          ((byte) 0xC8, 5, 0, OpCode.FLAG_JUMP),      //Branch always (wide index)

    /**
     * <b>Operation: </b>
     * Jump subroutine <br><br>
     *
     * <b>Format: </b><pre><code>
     * jsr
     * branchbyte1
     * branchbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * jsr = 168 (0xa8)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., address
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The address of the opcode of the instruction immediately following this jsr
     * instruction is pushed onto the operand stack as a value of type returnAddress. The
     * unsigned branchbyte1 and branchbyte2 are used to construct a signed 16-bit offset,
     * where the offset is (branchbyte1 &lt;&lt; 8) | branchbyte2. Execution proceeds at
     * that offset from the address of this jsr instruction. The target address must be
     * that of an opcode of an instruction within the method that contains this jsr
     * instruction. <br><br>
     *
     * <b>Notes: </b><br>
     * Note that jsr pushes the address onto the operand stack and ret (§ret) gets it out
     * of a local variable. This asymmetry is intentional. <br><br>
     *
     * In Oracle's implementation of a compiler for the Java programming language prior to
     * Java SE 6, the jsr instruction was used with the ret instruction in the
     * implementation of the finally clause (§3.13, §4.10.2.5).
     */
    JSR             ((byte) 0xA8, 3, 1, OpCode.FLAG_JUMP),      //Jump subroutine

    /**
     * <b>Operation: </b>
     * Jump subroutine (wide index) <br><br>
     *
     * <b>Format: </b><pre><code>
     * jsr_w
     * branchbyte1
     * branchbyte2
     * branchbyte3
     * branchbyte4
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * jsr_w = 201 (0xc9)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., address
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The address of the opcode of the instruction immediately following this jsr_w
     * instruction is pushed onto the operand stack as a value of type returnAddress. The
     * unsigned branchbyte1, branchbyte2, branchbyte3, and branchbyte4 are used to
     * construct a signed 32-bit offset, where the offset is (branchbyte1 &lt;&lt; 24) |
     * (branchbyte2 &lt;&lt; 16) | (branchbyte3 &lt;&lt; 8) | branchbyte4. Execution
     * proceeds at that offset from the address of this jsr_w instruction. The target
     * address must be that of an opcode of an instruction within the method that contains
     * this jsr_w instruction. <br><br>
     *
     * <b>Notes: </b><br>
     * Note that jsr_w pushes the address onto the operand stack and ret (§ret) gets it out
     * of a local variable. This asymmetry is intentional. <br><br>
     *
     * In Oracle's implementation of a compiler for the Java programming language prior to
     * Java SE 6, the jsr_w instruction was used with the ret instruction in the
     * implementation of the finally clause (§3.13, §4.10.2.5). <br><br>
     *
     * Although the jsr_w instruction takes a 4-byte branch offset, other factors limit the
     * size of a method to 65535 bytes (§4.11). This limit may be raised in a future
     * release of the Java Virtual Machine.
     */
    JSR_W           ((byte) 0xC9, 5, 1, OpCode.FLAG_JUMP),      //Jump subroutine (wide index)


    //########## Thread and synchronization ##########//

    /**
     * <b>Operation: </b>
     * Enter monitor for object <br><br>
     *
     * <b>Format: </b><pre><code>
     * monitorenter
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * monitorenter = 194 (0xc2)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref must be of type reference. <br><br>
     *
     * Each object is associated with a monitor. A monitor is locked if and only if it has
     * an owner. The thread that executes monitorenter attempts to gain ownership of the
     * monitor associated with objectref, as follows: <br><br>
     *
     * If the entry count of the monitor associated with objectref is zero, the thread
     * enters the monitor and sets its entry count to one. The thread is then the owner of
     * the monitor. <br><br>
     *
     * If the thread already owns the monitor associated with objectref, it reenters the
     * monitor, incrementing its entry count. <br><br>
     *
     * If another thread already owns the monitor associated with objectref, the thread
     * blocks until the monitor's entry count is zero, then tries again to gain ownership. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If objectref is null, monitorenter throws a <code>NullPointerException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * A monitorenter instruction may be used with one or more monitorexit instructions
     * (§monitorexit) to implement a synchronized statement in the Java programming
     * language (§3.14). The monitorenter and monitorexit instructions are not used in the
     * implementation of synchronized methods, although they can be used to provide
     * equivalent locking semantics. Monitor entry on invocation of a synchronized method,
     * and monitor exit on its return, are handled implicitly by the Java Virtual Machine's
     * method invocation and return instructions, as if monitorenter and monitorexit were
     * used. <br><br>
     *
     * The association of a monitor with an object may be managed in various ways that are
     * beyond the scope of this specification. For instance, the monitor may be allocated
     * and deallocated at the same time as the object. Alternatively, it may be dynamically
     * allocated at the time when a thread attempts to gain exclusive access to the object
     * and freed at some later time when no thread remains in the monitor for the object. <br><br>
     *
     * The synchronization constructs of the Java programming language require support for
     * operations on monitors besides entry and exit. These include waiting on a monitor
     * (Object.wait) and notifying other threads waiting on a monitor (Object.notifyAll and
     * Object.notify). These operations are supported in the standard package java.lang
     * supplied with the Java Virtual Machine. No explicit support for these operations
     * appears in the instruction set of the Java Virtual Machine.
     */
    MONITORENTER    ((byte) 0xC2, 1, -1),      //Enter monitor for object  (Object.wait()/Object.notify())

    /**
     * <b>Operation: </b>
     * Exit monitor for object <br><br>
     *
     * <b>Format: </b><pre><code>
     * monitorexit
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * monitorexit = 195 (0xc3)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref must be of type reference. <br><br>
     *
     * The thread that executes monitorexit must be the owner of the monitor associated
     * with the instance referenced by objectref. <br><br>
     *
     * The thread decrements the entry count of the monitor associated with objectref. If
     * as a result the value of the entry count is zero, the thread exits the monitor and
     * is no longer its owner. Other threads that are blocking to enter the monitor are
     * allowed to attempt to do so. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If objectref is null, monitorexit throws a <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if the thread that executes monitorexit is not the owner of the monitor
     * associated with the instance referenced by objectref, monitorexit throws an
     * <code>IllegalMonitorStateException.</code> <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the second of those rules is
     * violated by the execution of this monitorexit instruction, then monitorexit throws
     * an <code>IllegalMonitorStateException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * One or more monitorexit instructions may be used with a monitorenter instruction
     * (§monitorenter) to implement a synchronized statement in the Java programming
     * language (§3.14). The monitorenter and monitorexit instructions are not used in the
     * implementation of synchronized methods, although they can be used to provide
     * equivalent locking semantics. <br><br>
     *
     * The Java Virtual Machine supports exceptions thrown within synchronized methods and
     * synchronized statements differently: <br>
     * <ul>
     *     <li>Monitor exit on normal synchronized method completion is handled by the Java Virtual
     *     Machine's return instructions. Monitor exit on abrupt synchronized method completion
     *     is handled implicitly by the Java Virtual Machine's athrow instruction.</li>
     *     <li>When an exception is thrown from within a synchronized statement, exit from the
     *     monitor entered prior to the execution of the synchronized statement is achieved
     *     using the Java Virtual Machine's exception handling mechanism (§3.14).</li>
     * </ul>
     */
    MONITOREXIT     ((byte) 0xC3, 1, -1),      //Exit monitor for object   (synchronized)


    //########## Switch and return ##########//

    //return with or without value

    /**
     * <b>Operation: </b>
     * Return from subroutine <br><br>
     *
     * <b>Format: </b><pre><code>
     * ret
     * index
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ret = 169 (0xa9)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * No change
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The index is an unsigned byte between 0 and 255, inclusive. The local variable at
     * index in the current frame (§2.6) must contain a value of type returnAddress. The
     * contents of the local variable are written into the Java Virtual Machine's pc
     * register, and execution continues there. <br><br>
     *
     * <b>Notes: </b><br>
     * Note that jsr (§jsr) pushes the address onto the operand stack and ret gets it out
     * of a local variable. This asymmetry is intentional. <br><br>
     *
     * In Oracle's implementation of a compiler for the Java programming language prior to
     * Java SE 6, the ret instruction was used with the jsr and jsr_w instructions (§jsr,
     * §jsr_w) in the implementation of the finally clause (§3.13, §4.10.2.5). <br><br>
     *
     * The ret instruction should not be confused with the return instruction (§return). A
     * return instruction returns control from a method to its invoker, without passing any
     * value back to the invoker. <br><br>
     *
     * The ret opcode can be used in conjunction with the wide instruction (§wide) to
     * access a local variable using a two-byte unsigned index.
     */
    RET             ((byte) 0xA9, 2, 0),      //Return from subroutine

    /**
     * <b>Operation: </b>
     * Return int from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * ireturn
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * ireturn = 172 (0xac)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The current method must have return type boolean, byte, short, char, or int. The
     * value must be of type int. If the current method is a synchronized method, the
     * monitor entered or reentered on invocation of the method is updated and possibly
     * exited as if by execution of a monitorexit instruction (§monitorexit) in the current
     * thread. If no exception is thrown, value is popped from the operand stack of the
     * current frame (§2.6) and pushed onto the operand stack of the frame of the invoker.
     * Any other values on the operand stack of the current method are discarded. <br><br>
     *
     * The interpreter then returns control to the invoker of the method, reinstating the
     * frame of the invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, ireturn throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then ireturn throws an
     * <code>IllegalMonitorStateException.</code>
     */
    IRETURN         ((byte) 0xAC, 1, -1),      //Return int from method

    /**
     * <b>Operation: </b>
     * Return long from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * lreturn
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lreturn = 173 (0xad)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The current method must have return type long. The value must be of type long. If
     * the current method is a synchronized method, the monitor entered or reentered on
     * invocation of the method is updated and possibly exited as if by execution of a
     * monitorexit instruction (§monitorexit) in the current thread. If no exception is
     * thrown, value is popped from the operand stack of the current frame (§2.6) and
     * pushed onto the operand stack of the frame of the invoker. Any other values on the
     * operand stack of the current method are discarded. <br><br>
     *
     * The interpreter then returns control to the invoker of the method, reinstating the
     * frame of the invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, lreturn throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then lreturn throws an
     * <code>IllegalMonitorStateException.</code>
     */
    LRETURN         ((byte) 0xAD, 1, -2),      //Return long from method

    /**
     * <b>Operation: </b>
     * Return float from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * freturn
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * freturn = 174 (0xae)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The current method must have return type float. The value must be of type float. If
     * the current method is a synchronized method, the monitor entered or reentered on
     * invocation of the method is updated and possibly exited as if by execution of a
     * monitorexit instruction (§monitorexit) in the current thread. If no exception is
     * thrown, value is popped from the operand stack of the current frame (§2.6) and
     * undergoes value set conversion (§2.8.3), resulting in value'. The value' is pushed
     * onto the operand stack of the frame of the invoker. Any other values on the operand
     * stack of the current method are discarded. <br><br>
     *
     * The interpreter then returns control to the invoker of the method, reinstating the
     * frame of the invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, freturn throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then freturn throws an
     * <code>IllegalMonitorStateException.</code>
     */
    FRETURN         ((byte) 0xAE, 1, -1),      //Return float from method

    /**
     * <b>Operation: </b>
     * Return double from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * dreturn
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * dreturn = 175 (0xaf)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The current method must have return type double. The value must be of type double.
     * If the current method is a synchronized method, the monitor entered or reentered on
     * invocation of the method is updated and possibly exited as if by execution of a
     * monitorexit instruction (§monitorexit) in the current thread. If no exception is
     * thrown, value is popped from the operand stack of the current frame (§2.6) and
     * undergoes value set conversion (§2.8.3), resulting in value'. The value' is pushed
     * onto the operand stack of the frame of the invoker. Any other values on the operand
     * stack of the current method are discarded. <br><br>
     *
     * The interpreter then returns control to the invoker of the method, reinstating the
     * frame of the invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, dreturn throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then dreturn throws an
     * <code>IllegalMonitorStateException.</code> <br><br>
     *
     * dstore
     */
    DRETURN         ((byte) 0xAF, 1, -2),      //Return double from method

    /**
     * <b>Operation: </b>
     * Return reference from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * areturn
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * areturn = 176 (0xb0)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref must be of type reference and must refer to an object of a type that
     * is assignment compatible (JLS §5.2) with the type represented by the return
     * descriptor (§4.3.3) of the current method. If the current method is a synchronized
     * method, the monitor entered or reentered on invocation of the method is updated and
     * possibly exited as if by execution of a monitorexit instruction (§monitorexit) in
     * the current thread. If no exception is thrown, objectref is popped from the operand
     * stack of the current frame (§2.6) and pushed onto the operand stack of the frame of
     * the invoker. Any other values on the operand stack of the current method are
     * discarded. <br><br>
     *
     * The interpreter then reinstates the frame of the invoker and returns control to the
     * invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, areturn throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then areturn throws an
     * <code>IllegalMonitorStateException.</code>
     */
    ARETURN         ((byte) 0xB0, 1, -1),      //Return reference from method

    /**
     * <b>Operation: </b>
     * Return void from method <br><br>
     *
     * <b>Format: </b><pre><code>
     * return
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * return = 177 (0xb1)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * [empty]
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The current method must have return type void. If the current method is a
     * synchronized method, the monitor entered or reentered on invocation of the method is
     * updated and possibly exited as if by execution of a monitorexit instruction
     * (§monitorexit) in the current thread. If no exception is thrown, any values on the
     * operand stack of the current frame (§2.6) are discarded. <br><br>
     *
     * The interpreter then returns control to the invoker of the method, reinstating the
     * frame of the invoker. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the Java Virtual Machine implementation does not enforce the rules on structured
     * locking described in §2.11.10, then if the current method is a synchronized method
     * and the current thread is not the owner of the monitor entered or reentered on
     * invocation of the method, return throws an
     * <code>IllegalMonitorStateException.</code> This can happen, for example, if a
     * synchronized method contains a monitorexit instruction, but no monitorenter
     * instruction, on the object on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then return throws an
     * <code>IllegalMonitorStateException.</code>
     */
    RETURN          ((byte) 0xB1, 1, 0),      //Return void from method

    //switch

    /**
     * <b>Operation: </b>
     * Access jump table by index and jump <br><br>
     *
     * <b>Format: </b><pre><code>
     * tableswitch
     * &lt;0-3 byte pad&gt;
     * defaultbyte1
     * defaultbyte2
     * defaultbyte3
     * defaultbyte4
     * lowbyte1
     * lowbyte2
     * lowbyte3
     * lowbyte4
     * highbyte1
     * highbyte2
     * highbyte3
     * highbyte4
     * jump offsets...
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * tableswitch = 170 (0xaa)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., index →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * A tableswitch is a variable-length instruction. Immediately after the tableswitch
     * opcode, between zero and three bytes must act as padding, such that defaultbyte1
     * begins at an address that is a multiple of four bytes from the start of the current
     * method (the opcode of its first instruction). Immediately after the padding are
     * bytes constituting three signed 32-bit values: default, low, and high. Immediately
     * following are bytes constituting a series of high - low + 1 signed 32-bit offsets.
     * The value low must be less than or equal to high. The high - low + 1 signed 32-bit
     * offsets are treated as a 0-based jump table. Each of these signed 32-bit values is
     * constructed as (byte1 &lt;&lt; 24) | (byte2 &lt;&lt; 16) | (byte3 &lt;&lt; 8) |
     * byte4. <br><br>
     *
     * The index must be of type int and is popped from the operand stack. If index is less
     * than low or index is greater than high, then a target address is calculated by
     * adding default to the address of the opcode of this tableswitch instruction.
     * Otherwise, the offset at position index - low of the jump table is extracted. The
     * target address is calculated by adding that offset to the address of the opcode of
     * this tableswitch instruction. Execution then continues at the target address. <br><br>
     *
     * The target address that can be calculated from each jump table offset, as well as
     * the one that can be calculated from default, must be the address of an opcode of an
     * instruction within the method that contains this tableswitch instruction. <br><br>
     *
     * <b>Notes: </b><br>
     * The alignment required of the 4-byte operands of the tableswitch instruction
     * guarantees 4-byte alignment of those operands if and only if the method that
     * contains the tableswitch starts on a 4-byte boundary.
     */
    TABLESWITCH     ((byte) 0xAA, -1, -1),      //Access jump table by index and jump (switch)

    /**
     * <b>Operation: </b>
     * Access jump table by key match and jump <br><br>
     *
     * <b>Format: </b><pre><code>
     * lookupswitch
     * &lt;0-3 byte pad&gt;
     * defaultbyte1
     * defaultbyte2
     * defaultbyte3
     * defaultbyte4
     * npairs1
     * npairs2
     * npairs3
     * npairs4
     * match-offset pairs...
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * lookupswitch = 171 (0xab)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., key →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * A lookupswitch is a variable-length instruction. Immediately after the lookupswitch
     * opcode, between zero and three bytes must act as padding, such that defaultbyte1
     * begins at an address that is a multiple of four bytes from the start of the current
     * method (the opcode of its first instruction). Immediately after the padding follow a
     * series of signed 32-bit values: default, npairs, and then npairs pairs of signed
     * 32-bit values. The npairs must be greater than or equal to 0. Each of the npairs
     * pairs consists of an int match and a signed 32-bit offset. Each of these signed
     * 32-bit values is constructed from four unsigned bytes as (byte1 &lt;&lt; 24) |
     * (byte2 &lt;&lt; 16) | (byte3 &lt;&lt; 8) | byte4. <br><br>
     *
     * The table match-offset pairs of the lookupswitch instruction must be sorted in
     * increasing numerical order by match. <br><br>
     *
     * The key must be of type int and is popped from the operand stack. The key is
     * compared against the match values. If it is equal to one of them, then a target
     * address is calculated by adding the corresponding offset to the address of the
     * opcode of this lookupswitch instruction. If the key does not match any of the match
     * values, the target address is calculated by adding default to the address of the
     * opcode of this lookupswitch instruction. Execution then continues at the target
     * address. <br><br>
     *
     * The target address that can be calculated from the offset of each match-offset pair,
     * as well as the one calculated from default, must be the address of an opcode of an
     * instruction within the method that contains this lookupswitch instruction. <br><br>
     *
     * <b>Notes: </b><br>
     * The alignment required of the 4-byte operands of the lookupswitch instruction
     * guarantees 4-byte alignment of those operands if and only if the method that
     * contains the lookupswitch is positioned on a 4-byte boundary. <br><br>
     *
     * The match-offset pairs are sorted to support lookup routines that are quicker than
     * linear search.
     */
    LOOKUPSWITCH    ((byte) 0xAB, -1, -1),      //Access jump table by key match and jump (switch)


    //########## Class and Object field operation ##########//

    /**
     * <b>Operation: </b>
     * Get static field from class <br><br>
     *
     * <b>Format: </b><pre><code>
     * getstatic
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * getstatic = 178 (0xb2)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a field (§5.1), which gives the name and descriptor
     * of the field as well as a symbolic reference to the class or interface in which the
     * field is to be found. The referenced field is resolved (§5.4.3.2). <br><br>
     *
     * On successful resolution of the field, the class or interface that declared the
     * resolved field is initialized (§5.5) if that class or interface has not already been
     * initialized. <br><br>
     *
     * The value of the class or interface field is fetched and pushed onto the operand
     * stack. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class or interface field, any of
     * the exceptions pertaining to field resolution (§5.4.3.2) can be thrown. <br><br>
     *
     * Otherwise, if the resolved field is not a static (class) field or an interface
     * field, getstatic throws an IncompatibleClassChangeError. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if execution of this getstatic instruction causes initialization of the
     * referenced class or interface, getstatic may throw an Error as detailed in §5.5.
     */
    GETSTATIC       ((byte) 0xB2, 3, 0 /* variable */ ),      //Get static field from class

    /**
     * <b>Operation: </b>
     * Set static field in class <br><br>
     *
     * <b>Format: </b><pre><code>
     * putstatic
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * putstatic = 179 (0xb3)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a field (§5.1), which gives the name and descriptor
     * of the field as well as a symbolic reference to the class or interface in which the
     * field is to be found. The referenced field is resolved (§5.4.3.2). <br><br>
     *
     * On successful resolution of the field, the class or interface that declared the
     * resolved field is initialized (§5.5) if that class or interface has not already been
     * initialized. <br><br>
     *
     * The type of a value stored by a putstatic instruction must be compatible with the
     * descriptor of the referenced field (§4.3.2). If the field descriptor type is
     * boolean, byte, char, short, or int, then the value must be an int. If the field
     * descriptor type is float, long, or double, then the value must be a float, long, or
     * double, respectively. If the field descriptor type is a reference type, then the
     * value must be of a type that is assignment compatible (JLS §5.2) with the field
     * descriptor type. If the field is final, it must be declared in the current class,
     * and the instruction must occur in the &lt;clinit&gt; method of the current class
     * (§2.9). <br><br>
     *
     * The value is popped from the operand stack and undergoes value set conversion
     * (§2.8.3), resulting in value'. The class field is set to value'. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class or interface field, any of
     * the exceptions pertaining to field resolution (§5.4.3.2) can be thrown. <br><br>
     *
     * Otherwise, if the resolved field is not a static (class) field or an interface
     * field, putstatic throws an IncompatibleClassChangeError. <br><br>
     *
     * Otherwise, if the field is final, it must be declared in the current class, and the
     * instruction must occur in the &lt;clinit&gt; method of the current class. Otherwise,
     * an IllegalAccessError is thrown. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if execution of this putstatic instruction causes initialization of the
     * referenced class or interface, putstatic may throw an Error as detailed in §5.5. <br><br>
     *
     * <b>Notes: </b><br>
     * A putstatic instruction may be used only to set the value of an interface field on
     * the initialization of that field. Interface fields may be assigned to only once, on
     * execution of an interface variable initialization expression when the interface is
     * initialized (§5.5, JLS §9.3.1).
     */
    PUTSTATIC       ((byte) 0xB3, 3, 0 /* variable */ ),      //Set static field in class

    /**
     * <b>Operation: </b>
     * Fetch field from object <br><br>
     *
     * <b>Format: </b><pre><code>
     * getfield
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * getfield = 180 (0xb4)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * ..., value
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref, which must be of type reference, is popped from the operand stack.
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a field (§5.1), which gives the name and descriptor
     * of the field as well as a symbolic reference to the class in which the field is to
     * be found. The referenced field is resolved (§5.4.3.2). The value of the referenced
     * field in objectref is fetched and pushed onto the operand stack. <br><br>
     *
     * The type of objectref must not be an array type. If the field is protected, and it
     * is a member of a superclass of the current class, and the field is not declared in
     * the same run-time package (§5.3) as the current class, then the class of objectref
     * must be either the current class or a subclass of the current class. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the field, any of the errors
     * pertaining to field resolution (§5.4.3.2) can be thrown. <br><br>
     *
     * Otherwise, if the resolved field is a static field, getfield throws an
     * IncompatibleClassChangeError. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if objectref is null, the getfield instruction throws a
     * <code>NullPointerException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * The getfield instruction cannot be used to access the length field of an array. The
     * arraylength instruction (§arraylength) is used instead.
     */
    GETFIELD        ((byte) 0xB4, 3, 0 /* variable */ ),      //Fetch field from object

    /**
     * <b>Operation: </b>
     * Set field in object <br><br>
     *
     * <b>Format: </b><pre><code>
     * putfield
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * putfield = 181 (0xb5)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref, value →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a field (§5.1), which gives the name and descriptor
     * of the field as well as a symbolic reference to the class in which the field is to
     * be found. The class of objectref must not be an array. If the field is protected,
     * and it is a member of a superclass of the current class, and the field is not
     * declared in the same run-time package (§5.3) as the current class, then the class of
     * objectref must be either the current class or a subclass of the current class. <br><br>
     *
     * The referenced field is resolved (§5.4.3.2). The type of a value stored by a
     * putfield instruction must be compatible with the descriptor of the referenced field
     * (§4.3.2). If the field descriptor type is boolean, byte, char, short, or int, then
     * the value must be an int. If the field descriptor type is float, long, or double,
     * then the value must be a float, long, or double, respectively. If the field
     * descriptor type is a reference type, then the value must be of a type that is
     * assignment compatible (JLS §5.2) with the field descriptor type. If the field is
     * final, it must be declared in the current class, and the instruction must occur in
     * an instance initialization method (&lt;init&gt;) of the current class (§2.9). <br><br>
     *
     * The value and objectref are popped from the operand stack. The objectref must be of
     * type reference. The value undergoes value set conversion (§2.8.3), resulting in
     * value', and the referenced field in objectref is set to value'. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the field, any of the exceptions
     * pertaining to field resolution (§5.4.3.2) can be thrown. <br><br>
     *
     * Otherwise, if the resolved field is a static field, putfield throws an
     * IncompatibleClassChangeError. <br><br>
     *
     * Otherwise, if the field is final, it must be declared in the current class, and the
     * instruction must occur in an instance initialization method (&lt;init&gt;) of the
     * current class. Otherwise, an IllegalAccessError is thrown. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if objectref is null, the putfield instruction throws a
     * <code>NullPointerException.</code>
     */
    PUTFIELD        ((byte) 0xB5, 3, 0 /* variable */ ),      //Set field in object


    //########## Method invocation ##########//

    /**
     * <b>Operation: </b>
     * Invoke instance method; dispatch based on class <br><br>
     *
     * <b>Format: </b><pre><code>
     * invokevirtual
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * invokevirtual = 182 (0xb6)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref, [arg1, [arg2 ...]] →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a method (§5.1), which gives the name and descriptor
     * (§4.3.3) of the method as well as a symbolic reference to the class in which the
     * method is to be found. The named method is resolved (§5.4.3.3). <br><br>
     *
     * The resolved method must not be an instance initialization method, or the class or
     * interface initialization method (§2.9). <br><br>
     *
     * If the resolved method is protected, and it is a member of a superclass of the
     * current class, and the method is not declared in the same run-time package (§5.3) as
     * the current class, then the class of objectref must be either the current class or a
     * subclass of the current class. <br><br>
     *
     * If the resolved method is not signature polymorphic (§2.9), then the invokevirtual
     * instruction proceeds as follows. <br><br>
     *
     * Let C be the class of objectref. The actual method to be invoked is selected by the
     * following lookup procedure: <br>
     * <ol>
     *     <li>If C contains a declaration for an instance method m that overrides (§5.4.5) the
     *     resolved method, then m is the method to be invoked.</li>
     *     <li>Otherwise, if C has a superclass, a search for a declaration of an instance method
     *     that overrides the resolved method is performed, starting with the direct superclass
     *     of C and continuing with the direct superclass of that class, and so forth, until an
     *     overriding method is found or no further superclasses exist. If an overriding method
     *     is found, it is the method to be invoked.</li>
     *     <li>Otherwise, if there is exactly one maximally-specific method (§5.4.3.3) in the
     *     superinterfaces of C that matches the resolved method's name and descriptor and is
     *     not abstract, then it is the method to be invoked.</li>
     * </ol>
     * <br><br>
     *
     * The objectref must be followed on the operand stack by nargs argument values, where
     * the number, type, and order of the values must be consistent with the descriptor of
     * the selected instance method. <br><br>
     *
     * If the method is synchronized, the monitor associated with objectref is entered or
     * reentered as if by execution of a monitorenter instruction (§monitorenter) in the
     * current thread. <br><br>
     *
     * If the method is not native, the nargs argument values and objectref are popped from
     * the operand stack. A new frame is created on the Java Virtual Machine stack for the
     * method being invoked. The objectref and the argument values are consecutively made
     * the values of local variables of the new frame, with objectref in local variable 0,
     * arg1 in local variable 1 (or, if arg1 is of type long or double, in local variables
     * 1 and 2), and so on. Any argument value that is of a floating-point type undergoes
     * value set conversion (§2.8.3) prior to being stored in a local variable. The new
     * frame is then made current, and the Java Virtual Machine pc is set to the opcode of
     * the first instruction of the method to be invoked. Execution continues with the
     * first instruction of the method. <br><br>
     *
     * If the method is native and the platform-dependent code that implements it has not
     * yet been bound (§5.6) into the Java Virtual Machine, that is done. The nargs
     * argument values and objectref are popped from the operand stack and are passed as
     * parameters to the code that implements the method. Any argument value that is of a
     * floating-point type undergoes value set conversion (§2.8.3) prior to being passed as
     * a parameter. The parameters are passed and the code is invoked in an
     * implementation-dependent manner. When the platform-dependent code returns, the
     * following take place: <br>
     * <ul>
     *     <li>If the native method is synchronized, the monitor associated with objectref is
     *     updated and possibly exited as if by execution of a monitorexit instruction
     *     (§monitorexit) in the current thread.</li>
     *     <li>If the native method returns a value, the return value of the platform-dependent
     *     code is converted in an implementation-dependent way to the return type of the
     *     native method and pushed onto the operand stack.</li>
     * </ul>
     * <br><br>
     *
     * <i>If the resolved method is signature polymorphic (§2.9)</i>, then the invokevirtual
     * instruction proceeds as follows. <br><br>
     *
     * First, a reference to an instance of java.lang.invoke.MethodType is obtained as if
     * by resolution of a symbolic reference to a method type (§5.4.3.5) with the same
     * parameter and return types as the descriptor of the method referenced by the
     * invokevirtual instruction. <br><br>
     * <ul>
     *     <li>If the named method is invokeExact, the instance of <code>java.lang.invoke.MethodType</code> must
     *     be semantically equal to the type descriptor of the receiving method handle
     *     objectref. The method handle to be invoked is objectref.</li>
     *     <li>If the named method is invoke, and the instance of <code>java.lang.invoke.MethodType</code> is
     *     semantically equal to the type descriptor of the receiving method handle objectref,
     *     then the method handle to be invoked is objectref.</li>
     *     <li>If the named method is invoke, and the instance of <code>java.lang.invoke.MethodType</code> is
     *     not semantically equal to the type descriptor of the receiving method handle
     *     objectref, then the Java Virtual Machine attempts to adjust the type descriptor of
     *     the receiving method handle, as if by a call to
     *     <code>java.lang.invoke.MethodHandle.asType</code>, to obtain an exactly invokable method handle
     *     m. The method handle to be invoked is m.</li>
     * </ul>
     * <br><br>
     *
     * The objectref must be followed on the operand stack by nargs argument values, where
     * the number, type, and order of the values must be consistent with the type
     * descriptor of the method handle to be invoked. (This type descriptor will correspond
     * to the method descriptor appropriate for the kind of the method handle to be
     * invoked, as specified in §5.4.3.5.) <br><br>
     *
     * Then, if the method handle to be invoked has bytecode behavior, the Java Virtual
     * Machine invokes the method handle as if by execution of the bytecode behavior
     * associated with the method handle's kind. If the kind is 5 (REF_invokeVirtual), 6
     * (REF_invokeStatic), 7 (REF_invokeSpecial), 8 (REF_newInvokeSpecial), or 9
     * (REF_invokeInterface), then a frame will be created and made current in the course
     * of executing the bytecode behavior; when the method invoked by the bytecode behavior
     * completes (normally or abruptly), the frame of its invoker is considered to be the
     * frame for the method containing this invokevirtual instruction. <br><br>
     *
     * <i>The frame in which the bytecode behavior itself executes is not visible. </i><br><br>
     *
     * Otherwise, if the method handle to be invoked has no bytecode behavior, the Java
     * Virtual Machine invokes it in an implementation-dependent manner. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the method, any of the exceptions
     * pertaining to method resolution (§5.4.3.3) can be thrown. <br><br>
     *
     * Otherwise, if the resolved method is a class (static) method, the invokevirtual
     * instruction throws an IncompatibleClassChangeError. <br><br>
     *
     * Otherwise, if the resolved method is signature polymorphic, then during resolution
     * of the method type derived from the descriptor in the symbolic reference to the
     * method, any of the exceptions pertaining to method type resolution (§5.4.3.5) can be
     * thrown. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if objectref is null, the invokevirtual instruction throws a
     * <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if the resolved method is a protected method of a superclass of the
     * current class, declared in a different run-time package, and the class of objectref
     * is not the current class or a subclass of the current class, then invokevirtual
     * throws an <code>IllegalAccessError.</code> <br><br>
     *
     * Otherwise, if the resolved method is not signature polymorphic: <br>
     * <ul>
     *     <li>If step 1 or step 2 of the lookup procedure selects an abstract method,
     *     invokevirtual throws an <code>AbstractMethodError.</code></li>
     *     <li>Otherwise, if step 1 or step 2 of the lookup procedure selects a native method and
     *     the code that implements the method cannot be bound, invokevirtual throws an
     *     <code>UnsatisfiedLinkError.</code></li>
     *     <li>Otherwise, if step 3 of the lookup procedure determines there are multiple
     *     maximally-specific methods in the superinterfaces of C that match the resolved
     *     method's name and descriptor and are not abstract, invokevirtual throws an
     *     <code>IncompatibleClassChangeError.</code></li>
     *     <li>Otherwise, if step 3 of the lookup procedure determines there are zero
     *     maximally-specific methods in the superinterfaces of C that match the resolved
     *     method's name and descriptor and are not abstract, invokevirtual throws an
     *     <code>AbstractMethodError.</code></li>
     * </ul>
     * <br><br>
     *
     * Otherwise, if the resolved method is signature polymorphic, then: <br>
     * <ul>
     *     <li>If the method name is invokeExact, and the obtained instance of
     *      * java.lang.invoke.MethodType is not semantically equal to the type descriptor of the
     *      * receiving method handle, the invokevirtual instruction throws a
     *      * <code>java.lang.invoke.WrongMethodTypeException.</code></li>
     *     <li>If the method name is invoke, and the obtained instance of
     *      * java.lang.invoke.MethodType is not a valid argument to the
     *      * java.lang.invoke.MethodHandle.asType method invoked on the receiving method handle,
     *      * the invokevirtual instruction throws a
     *      * <code>java.lang.invoke.WrongMethodTypeException.</code></li>
     * </ul>
     * <br><br>
     *
     * <b>Notes: </b><br>
     * The nargs argument values and objectref are not one-to-one with the first nargs+1
     * local variables. Argument values of types long and double must be stored in two
     * consecutive local variables, thus more than nargs local variables may be required to
     * pass nargs argument values to the invoked method. <br><br>
     *
     * It is possible that the symbolic reference of an invokevirtual instruction resolves
     * to an interface method. In this case, it is possible that there is no overriding
     * method in the class hierarchy, but that a non-abstract interface method matches the
     * resolved method's descriptor. The selection logic matches such a method, using the
     * same rules as for invokeinterface.
     */
    INVOKEVIRTUAL   ((byte) 0xB6, 3, 0 /* variable */ , OpCode.FLAG_INVOKE),      //Invoke instance method; dispatch based on class

    /**
     * <b>Operation: </b>
     * Invoke instance method; special handling for superclass, private, and instance
     * initialization method invocations <br><br>
     *
     * <b>Format: </b><pre><code>
     * invokespecial
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * invokespecial = 183 (0xb7)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref, [arg1, [arg2 ...]] →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a method or an interface method (§5.1), which gives
     * the name and descriptor (§4.3.3) of the method as well as a symbolic reference to
     * the class or interface in which the method is to be found. The named method is
     * resolved (§5.4.3.3, §5.4.3.4). <br><br>
     *
     * If the resolved method is protected, and it is a member of a superclass of the
     * current class, and the method is not declared in the same run-time package (§5.3) as
     * the current class, then the class of objectref must be either the current class or a
     * subclass of the current class. <br><br>
     *
     * If all of the following are true, let C be the direct superclass of the current
     * class: <br>
     * <ul>
     *     <li>The resolved method is not an instance initialization method (§2.9).</li>
     *     <li>If the symbolic reference names a class (not an interface), then that class is a
     *     superclass of the current class.</li>
     *     <li>The ACC_SUPER flag is set for the class file (§4.1).</li>
     * </ul>
     * <br><br>
     *
     * Otherwise, let C be the class or interface named by the symbolic reference. <br><br>
     *
     * The actual method to be invoked is selected by the following lookup procedure: <br>
     * <ol>
     *     <li>If C contains a declaration for an instance method with the same name and descriptor
     *     as the resolved method, then it is the method to be invoked.</li>
     *     <li>Otherwise, if C is a class and has a superclass, a search for a declaration of an
     *     instance method with the same name and descriptor as the resolved method is
     *     performed, starting with the direct superclass of C and continuing with the direct
     *     superclass of that class, and so forth, until a match is found or no further
     *     superclasses exist. If a match is found, then it is the method to be invoked.</li>
     *     <li>Otherwise, if C is an interface and the class Object contains a declaration of a
     *     public instance method with the same name and descriptor as the resolved method,
     *     then it is the method to be invoked.</li>
     *     <li>Otherwise, if there is exactly one maximally-specific method (§5.4.3.3) in the
     *     superinterfaces of C that matches the resolved method's name and descriptor and is
     *     not abstract, then it is the method to be invoked.</li>
     * </ol>
     * <br><br>
     *
     * The objectref must be of type reference and must be followed on the operand stack by
     * nargs argument values, where the number, type, and order of the values must be
     * consistent with the descriptor of the selected instance method. <br><br>
     *
     * If the method is synchronized, the monitor associated with objectref is entered or
     * reentered as if by execution of a monitorenter instruction (§monitorenter) in the
     * current thread. <br><br>
     *
     * If the method is not native, the nargs argument values and objectref are popped from
     * the operand stack. A new frame is created on the Java Virtual Machine stack for the
     * method being invoked. The objectref and the argument values are consecutively made
     * the values of local variables of the new frame, with objectref in local variable 0,
     * arg1 in local variable 1 (or, if arg1 is of type long or double, in local variables
     * 1 and 2), and so on. Any argument value that is of a floating-point type undergoes
     * value set conversion (§2.8.3) prior to being stored in a local variable. The new
     * frame is then made current, and the Java Virtual Machine pc is set to the opcode of
     * the first instruction of the method to be invoked. Execution continues with the
     * first instruction of the method. <br><br>
     *
     * If the method is native and the platform-dependent code that implements it has not
     * yet been bound (§5.6) into the Java Virtual Machine, that is done. The nargs
     * argument values and objectref are popped from the operand stack and are passed as
     * parameters to the code that implements the method. Any argument value that is of a
     * floating-point type undergoes value set conversion (§2.8.3) prior to being passed as
     * a parameter. The parameters are passed and the code is invoked in an
     * implementation-dependent manner. When the platform-dependent code returns, the
     * following take place: <br>
     * <ul>
     *     <li>If the native method is synchronized, the monitor associated with objectref is
     *     updated and possibly exited as if by execution of a monitorexit instruction
     *     (§monitorexit) in the current thread.</li>
     *     <li>If the native method returns a value, the return value of the platform-dependent
     *     code is converted in an implementation-dependent way to the return type of the
     *     native method and pushed onto the operand stack.</li>
     * </ul>
     * <br><br>
     *
     * <b>Linking Exceptions: <br>
     * During resolution of the symbolic reference to the method, any of the exceptions
     * pertaining to method resolution (§5.4.3.3) can be thrown. <br><br>
     *
     * Otherwise, if the resolved method is an instance initialization method, and the
     * class in which it is declared is not the class symbolically referenced by the
     * instruction, a NoSuchMethodError is thrown. <br><br>
     *
     * Otherwise, if the resolved method is a class (static) method, the invokespecial
     * instruction throws an IncompatibleClassChangeError. <br><br></b>
     *
     * <b>Run-time Exceptions: <br>
     * Otherwise, if objectref is null, the invokespecial instruction throws a
     * <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if the resolved method is a protected method of a superclass of the
     * current class, declared in a different run-time package, and the class of objectref
     * is not the current class or a subclass of the current class, then invokespecial
     * throws an <code>IllegalAccessError.</code> <br><br>
     *
     * Otherwise, if step 1, step 2, or step 3 of the lookup procedure selects an abstract
     * method, invokespecial throws an <code>AbstractMethodError.</code> <br><br>
     *
     * Otherwise, if step 1, step 2, or step 3 of the lookup procedure selects a native
     * method and the code that implements the method cannot be bound, invokespecial throws
     * an <code>UnsatisfiedLinkError.</code> <br><br>
     *
     * Otherwise, if step 4 of the lookup procedure determines there are multiple
     * maximally-specific methods in the superinterfaces of C that match the resolved
     * method's name and descriptor and are not abstract, invokespecial throws an
     * <code>IncompatibleClassChangeError.</code> <br><br>
     *
     * Otherwise, if step 4 of the lookup procedure determines there are zero
     * maximally-specific methods in the superinterfaces of C that match the resolved
     * method's name and descriptor and are not abstract, invokespecial throws an
     * <code>AbstractMethodError.</code> <br><br></b>
     *
     * <b>Notes: </b><br>
     * The difference between the invokespecial instruction and the invokevirtual
     * instruction (§invokevirtual) is that invokevirtual invokes a method based on the
     * class of the object. The invokespecial instruction is used to invoke instance
     * initialization methods (§2.9) as well as private methods and methods of a superclass
     * of the current class. <br><br>
     *
     * <i>The invokespecial instruction was named invokenonvirtual prior to JDK release 1.0.2.</i> <br><br>
     *
     * The nargs argument values and objectref are not one-to-one with the first nargs+1
     * local variables. Argument values of types long and double must be stored in two
     * consecutive local variables, thus more than nargs local variables may be required to
     * pass nargs argument values to the invoked method. <br><br>
     *
     * The invokespecial instruction handles invocation of a private interface method, a
     * non-abstract interface method referenced via a direct superinterface, and a
     * non-abstract interface method referenced via a superclass. In these cases, the rules
     * for selection are essentially the same as those for invokeinterface (except that the
     * search starts from a different class).
     */
    INVOKESPECIAL   ((byte) 0xB7, 3, 0 /* variable */ , OpCode.FLAG_INVOKE),      //Invoke instance method; special handling for superclass, private, and instance initialization method invocations

    /**
     * <b>Operation: </b>
     * Invoke a class (static) method <br><br>
     *
     * <b>Format: </b><pre><code>
     * invokestatic
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * invokestatic = 184 (0xb8)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., [arg1, [arg2 ...]] →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to a method or an interface method (§5.1), which gives
     * the name and descriptor (§4.3.3) of the method as well as a symbolic reference to
     * the class or interface in which the method is to be found. The named method is
     * resolved (§5.4.3.3). <br><br>
     *
     * The resolved method must not be an instance initialization method, or the class or
     * interface initialization method (§2.9). <br><br>
     *
     * The resolved method must be static, and therefore cannot be abstract. <br><br>
     *
     * On successful resolution of the method, the class or interface that declared the
     * resolved method is initialized (§5.5) if that class or interface has not already
     * been initialized. <br><br>
     *
     * The operand stack must contain nargs argument values, where the number, type, and
     * order of the values must be consistent with the descriptor of the resolved method. <br><br>
     *
     * If the method is synchronized, the monitor associated with the resolved Class object
     * is entered or reentered as if by execution of a monitorenter instruction
     * (§monitorenter) in the current thread. <br><br>
     *
     * If the method is not native, the nargs argument values are popped from the operand
     * stack. A new frame is created on the Java Virtual Machine stack for the method being
     * invoked. The nargs argument values are consecutively made the values of local
     * variables of the new frame, with arg1 in local variable 0 (or, if arg1 is of type
     * long or double, in local variables 0 and 1) and so on. Any argument value that is of
     * a floating-point type undergoes value set conversion (§2.8.3) prior to being stored
     * in a local variable. The new frame is then made current, and the Java Virtual
     * Machine pc is set to the opcode of the first instruction of the method to be
     * invoked. Execution continues with the first instruction of the method. <br><br>
     *
     * If the method is native and the platform-dependent code that implements it has not
     * yet been bound (§5.6) into the Java Virtual Machine, that is done. The nargs
     * argument values are popped from the operand stack and are passed as parameters to
     * the code that implements the method. Any argument value that is of a floating-point
     * type undergoes value set conversion (§2.8.3) prior to being passed as a parameter.
     * The parameters are passed and the code is invoked in an implementation-dependent
     * manner. When the platform-dependent code returns, the following take place: <br>
     *
     * <ul>
     *     <li>If the native method is synchronized, the monitor associated with the resolved Class
     *     object is updated and possibly exited as if by execution of a monitorexit
     *     instruction (§monitorexit) in the current thread.</li>
     *     <li>If the native method returns a value, the return value of the platform-dependent
     *     code is converted in an implementation-dependent way to the return type of the
     *     native method and pushed onto the operand stack.</li>
     * </ul>
     * <br><br>
     *
     * <b>Linking Exceptions: <br>
     * During resolution of the symbolic reference to the method, any of the exceptions
     * pertaining to method resolution (§5.4.3.3) can be thrown. <br><br>
     *
     * Otherwise, if the resolved method is an instance method, the invokestatic
     * instruction throws an <code>IncompatibleClassChangeError.</code> <br><br></b>
     *
     * <b>Run-time Exceptions: <br>
     * Otherwise, if execution of this invokestatic instruction causes initialization of
     * the referenced class or interface, invokestatic may throw an Error as detailed in
     * §5.5. <br><br>
     *
     * Otherwise, if the resolved method is native and the code that implements the method
     * cannot be bound, invokestatic throws an <code>UnsatisfiedLinkError.</code> <br><br></b>
     *
     * <b>Notes: </b><br>
     * The nargs argument values are not one-to-one with the first nargs local variables.
     * Argument values of types long and double must be stored in two consecutive local
     * variables, thus more than nargs local variables may be required to pass nargs
     * argument values to the invoked method.
     */
    INVOKESTATIC    ((byte) 0xB8, 3, 0 /* variable */ , OpCode.FLAG_INVOKE),      //Invoke a class (static) method

    /**
     * <b>Operation: </b>
     * Invoke interface method <br><br>
     *
     * <b>Format: </b><pre><code>
     * invokeinterface
     * indexbyte1
     * indexbyte2
     * count
     * 0
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * invokeinterface = 185 (0xb9)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref, [arg1, [arg2 ...]] →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index
     * must be a symbolic reference to an interface method (§5.1), which gives the name and
     * descriptor (§4.3.3) of the interface method as well as a symbolic reference to the
     * interface in which the interface method is to be found. The named interface method
     * is resolved (§5.4.3.4). <br><br>
     *
     * The resolved interface method must not be an instance initialization method, or the
     * class or interface initialization method (§2.9). <br><br>
     *
     * The count operand is an unsigned byte that must not be zero. The objectref must be
     * of type reference and must be followed on the operand stack by nargs argument
     * values, where the number, type, and order of the values must be consistent with the
     * descriptor of the resolved interface method. The value of the fourth operand byte
     * must always be zero. <br><br>
     *
     * Let C be the class of objectref. The actual method to be invoked is selected by the
     * following lookup procedure: <br>
     * <ol>
     *     <li>If C contains a declaration for an instance method with the same name and descriptor
     *     as the resolved method, then it is the method to be invoked.</li>
     *     <li>Otherwise, if C has a superclass, a search for a declaration of an instance method
     *     with the same name and descriptor as the resolved method is performed, starting with
     *     the direct superclass of C and continuing with the direct superclass of that class,
     *     and so forth, until a match is found or no further superclasses exist. If a match is
     *     found, then it is the method to be invoked.</li>
     *     <li>Otherwise, if there is exactly one maximally-specific method (§5.4.3.3) in the
     *     superinterfaces of C that matches the resolved method's name and descriptor and is
     *     not abstract, then it is the method to be invoked.</li>
     * </ol>
     * <br><br>
     *
     * If the method is synchronized, the monitor associated with objectref is entered or
     * reentered as if by execution of a monitorenter instruction (§monitorenter) in the
     * current thread. <br><br>
     *
     * If the method is not native, the nargs argument values and objectref are popped from
     * the operand stack. A new frame is created on the Java Virtual Machine stack for the
     * method being invoked. The objectref and the argument values are consecutively made
     * the values of local variables of the new frame, with objectref in local variable 0,
     * arg1 in local variable 1 (or, if arg1 is of type long or double, in local variables
     * 1 and 2), and so on. Any argument value that is of a floating-point type undergoes
     * value set conversion (§2.8.3) prior to being stored in a local variable. The new
     * frame is then made current, and the Java Virtual Machine pc is set to the opcode of
     * the first instruction of the method to be invoked. Execution continues with the
     * first instruction of the method. <br><br>
     *
     * If the method is native and the platform-dependent code that implements it has not
     * yet been bound (§5.6) into the Java Virtual Machine, that is done. The nargs
     * argument values and objectref are popped from the operand stack and are passed as
     * parameters to the code that implements the method. Any argument value that is of a
     * floating-point type undergoes value set conversion (§2.8.3) prior to being passed as
     * a parameter. The parameters are passed and the code is invoked in an
     * implementation-dependent manner. When the platform-dependent code returns: <br>
     *
     * <ul>
     *     <li>If the native method is synchronized, the monitor associated with objectref is
     *     updated and possibly exited as if by execution of a monitorexit instruction
     *     (§monitorexit) in the current thread.</li>
     *     <li>If the native method returns a value, the return value of the platform-dependent
     *     code is converted in an implementation-dependent way to the return type of the
     *     native method and pushed onto the operand stack.</li>
     * </ul>
     * <br><br>
     *
     * <b>Linking Exceptions: <br>
     * During resolution of the symbolic reference to the interface method, any of the
     * exceptions pertaining to interface method resolution (§5.4.3.4) can be thrown. <br><br>
     *
     * Otherwise, if the resolved method is static or private, the invokeinterface
     * instruction throws an <code>IncompatibleClassChangeError.</code> <br><br></b>
     *
     * <b>Run-time Exceptions: <br>
     * Otherwise, if objectref is null, the invokeinterface instruction throws a
     * <code>NullPointerException.</code> <br><br>
     *
     * Otherwise, if the class of objectref does not implement the resolved interface,
     * invokeinterface throws an <code>IncompatibleClassChangeError.</code> <br><br>
     *
     * Otherwise, if step 1 or step 2 of the lookup procedure selects a method that is not
     * public, invokeinterface throws an <code>IllegalAccessError.</code> <br><br>
     *
     * Otherwise, if step 1 or step 2 of the lookup procedure selects an abstract method,
     * invokeinterface throws an <code>AbstractMethodError.</code> <br><br>
     *
     * Otherwise, if step 1 or step 2 of the lookup procedure selects a native method and
     * the code that implements the method cannot be bound, invokeinterface throws an
     * <code>UnsatisfiedLinkError.</code> <br><br>
     *
     * Otherwise, if step 3 of the lookup procedure determines there are multiple
     * maximally-specific methods in the superinterfaces of C that match the resolved
     * method's name and descriptor and are not abstract, invokeinterface throws an
     * <code>IncompatibleClassChangeError.</code> <br><br>
     *
     * Otherwise, if step 3 of the lookup procedure determines there are zero
     * maximally-specific methods in the superinterfaces of C that match the resolved
     * method's name and descriptor and are not abstract, invokeinterface throws an
     * <code>AbstractMethodError.</code> <br><br></b>
     *
     * <b>Notes: </b><br>
     * The count operand of the invokeinterface instruction records a measure of the number
     * of argument values, where an argument value of type long or type double contributes
     * two units to the count value and an argument of any other type contributes one unit.
     * This information can also be derived from the descriptor of the selected method. The
     * redundancy is historical. <br><br>
     *
     * The fourth operand byte exists to reserve space for an additional operand used in
     * certain of Oracle's Java Virtual Machine implementations, which replace the
     * invokeinterface instruction by a specialized pseudo-instruction at run time. It must
     * be retained for backwards compatibility. <br><br>
     *
     * The nargs argument values and objectref are not one-to-one with the first nargs+1
     * local variables. Argument values of types long and double must be stored in two
     * consecutive local variables, thus more than nargs local variables may be required to
     * pass nargs argument values to the invoked method. <br><br>
     *
     * The selection logic allows a non-abstract method declared in a superinterface to be
     * selected. Methods in interfaces are only considered if there is no matching method
     * in the class hierarchy. In the event that there are two non-abstract methods in the
     * superinterface hierarchy, with neither more specific than the other, an error
     * occurs; there is no attempt to disambiguate (for example, one may be the referenced
     * method and one may be unrelated, but we do not prefer the referenced method). On the
     * other hand, if there are many abstract methods but only one non-abstract method, the
     * non-abstract method is selected (unless an abstract method is more specific).
     */
    INVOKEINTERFACE ((byte) 0xB9, 5, 0 /* variable */ , OpCode.FLAG_INVOKE),      //Invoke interface method

    /**
     * <b>Operation: </b>
     * Invoke dynamic method <br><br>
     *
     * <b>Format: </b><pre><code>
     * invokedynamic
     * indexbyte1
     * indexbyte2
     * 0
     * 0
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * invokedynamic = 186 (0xba)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., [arg1, [arg2 ...]] →
     * ...
     * </code></pre>
     *
     * <b>Description: </b><br>
     * Each specific lexical occurrence of an invokedynamic instruction is called a dynamic
     * call site. <br><br>
     *
     * First, the unsigned indexbyte1 and indexbyte2 are used to construct an index into
     * the run-time constant pool of the current class (§2.6), where the value of the index
     * is (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that
     * index must be a symbolic reference to a call site specifier (§5.1). The values of
     * the third and fourth operand bytes must always be zero. <br><br>
     *
     * The call site specifier is resolved (§5.4.3.6) for this specific dynamic call site
     * to obtain a reference to a java.lang.invoke.MethodHandle instance that will serve as
     * the bootstrap method, a reference to a java.lang.invoke.MethodType instance, and
     * references to static arguments. <br><br>
     *
     * Next, as part of the continuing resolution of the call site specifier, the bootstrap
     * method is invoked as if by execution of an invokevirtual instruction
     * (§invokevirtual) that contains a run-time constant pool index to a symbolic
     * reference R where: <br>
     * <ul>
     *     <li>R is a symbolic reference to a method of a class (§5.1);</li>
     *     <li>for the symbolic reference to the class in which the method is to be found, R
     *     specifies java.lang.invoke.MethodHandle;</li>
     *     <li>for the name of the method, R specifies invoke;</li>
     *     <li>for the descriptor of the method, R specifies a return type of
     *         java.lang.invoke.CallSite and parameter types derived from the items pushed on the
     *         operand stack.<br><br>
     *
     *         The first three parameter types are java.lang.invoke.MethodHandles.Lookup, String,
     *         and java.lang.invoke.MethodType, in that order. If the call site specifier has any
     *         static arguments, then a parameter type for each argument is appended to the
     *         parameter types of the method descriptor in the order that the arguments were pushed
     *         on to the operand stack. These parameter types may be Class,
     *         java.lang.invoke.MethodHandle, java.lang.invoke.MethodType, String, int, long,
     *         float, or double.
     *     </li>
     * </ul>
     * <br><br>
     *
     * and where it is as if the following items were pushed, in order, on the operand
     * stack: <br>
     *
     * <ul>
     *     <li>the reference to the java.lang.invoke.MethodHandle object for the bootstrap method;</li>
     *     <li>a reference to a java.lang.invoke.MethodHandles.Lookup object for the class in which
     *     this dynamic call site occurs;</li>
     *     <li>a reference to the String for the method name in the call site specifier;</li>
     *     <li>the reference to the java.lang.invoke.MethodType object obtained for the method
     *     descriptor in the call site specifier;</li>
     *     <li>references to classes, method types, method handles, and string literals denoted as
     *     static arguments in the call site specifier, and numeric values (§2.3.1, §2.3.2)
     *     denoted as static arguments in the call site specifier, in the order in which they
     *     appear in the call site specifier. (That is, no boxing occurs for primitive values.)</li>
     * </ul>
     * <br><br>
     *
     * The symbolic reference R describes a method which is signature polymorphic (§2.9).
     * Due to the operation of invokevirtual on a signature polymorphic method called
     * invoke, the type descriptor of the receiving method handle (representing the
     * bootstrap method) need not be semantically equal to the method descriptor specified
     * by R. For example, the first parameter type specified by R could be Object instead
     * of java.lang.invoke.MethodHandles.Lookup, and the return type specified by R could
     * be Object instead of java.lang.invoke.CallSite. As long as the bootstrap method can
     * be invoked by the invoke method without a
     * <code>java.lang.invoke.WrongMethodTypeException</code> being thrown, the type
     * descriptor of the method handle which represents the bootstrap method is arbitrary. <br><br>
     *
     * If the bootstrap method is a variable arity method, then some or all of the
     * arguments on the operand stack specified above may be collected into a trailing
     * array parameter. <br><br>
     *
     * The invocation of a bootstrap method occurs within a thread that is attempting
     * resolution of the symbolic reference to the call site specifier of this dynamic call
     * site. If there are several such threads, the bootstrap method may be invoked in
     * several threads concurrently. Therefore, bootstrap methods which access global
     * application data must take the usual precautions against race conditions. <br><br>
     *
     * The result returned by the bootstrap method must be a reference to an object whose
     * class is java.lang.invoke.CallSite or a subclass of java.lang.invoke.CallSite. This
     * object is known as the call site object. The reference is popped from the operand
     * stack used as if in the execution of an invokevirtual instruction. <br><br>
     *
     * If several threads simultaneously execute the bootstrap method for the same dynamic
     * call site, the Java Virtual Machine must choose one returned call site object and
     * install it visibly to all threads. Any other bootstrap methods executing for the
     * dynamic call site are allowed to complete, but their results are ignored, and the
     * threads' execution of the dynamic call site proceeds with the chosen call site
     * object. <br><br>
     *
     * The call site object has a type descriptor (an instance of
     * java.lang.invoke.MethodType) which must be semantically equal to the
     * java.lang.invoke.MethodType object obtained for the method descriptor in the call
     * site specifier. <br><br>
     *
     * The result of successful call site specifier resolution is a call site object which
     * is permanently bound to the dynamic call site. <br><br>
     *
     * The method handle represented by the target of the bound call site object is
     * invoked. The invocation occurs as if by execution of an invokevirtual instruction
     * (§invokevirtual) that indicates a run-time constant pool index to a symbolic
     * reference to a method (§5.1) with the following properties: <br>
     * <ul>
     *     <li>The method's name is invokeExact;</li>
     *     <li>The method's descriptor is the method descriptor in the call site specifier; and</li>
     *     <li>The method's symbolic reference to the class in which the method is to be found
     *     indicates the class java.lang.invoke.MethodHandle.</li>
     * </ul>
     * <br><br>
     *
     * The operand stack will be interpreted as containing a reference to the target of the
     * call site object, followed by nargs argument values, where the number, type, and
     * order of the values must be consistent with the method descriptor in the call site
     * specifier. <br><br>
     *
     * <b>Linking Exceptions: <br>
     * If resolution of the symbolic reference to the call site specifier throws an
     * exception E, the invokedynamic instruction throws a
     * <code>BootstrapMethodError</code> that wraps E. <br><br>
     *
     * Otherwise, during the continuing resolution of the call site specifier, if
     * invocation of the bootstrap method completes abruptly (§2.6.5) because of a throw of
     * exception E, the invokedynamic instruction throws a
     * <code>BootstrapMethodError</code> that wraps E. (This can occur if the bootstrap
     * method has the wrong arity, parameter type, or return type, causing
     * java.lang.invoke.MethodHandle . invoke to throw
     * java.lang.invoke.WrongMethodTypeException.) <br><br>
     *
     * Otherwise, during the continuing resolution of the call site specifier, if the
     * result from the bootstrap method invocation is not a reference to an instance of
     * java.lang.invoke.CallSite, the invokedynamic instruction throws a
     * <code>BootstrapMethodError.</code> <br><br>
     *
     * Otherwise, during the continuing resolution of the call site specifier, if the type
     * descriptor of the target of the call site object is not semantically equal to the
     * method descriptor in the call site specifier, the invokedynamic instruction throws a
     * <code>BootstrapMethodError.</code> <br><br></b>
     *
     * <b>Run-time Exceptions: </b><br>
     * If this specific dynamic call site completed resolution of its call site specifier,
     * it implies that a non-null reference to an instance of java.lang.invoke.CallSite is
     * bound to this dynamic call site. Therefore, the operand stack item which represents
     * a reference to the target of the call site object is never null. Similarly, it
     * implies that the method descriptor in the call site specifier is semantically equal
     * to the type descriptor of the method handle to be invoked as if by execution of an
     * invokevirtual instruction. Together, these invariants mean that an invokedynamic
     * instruction which is bound to a call site object never throws a
     * <code>NullPointerException</code> or a
     * <code>java.lang.invoke.WrongMethodTypeException.</code>
     */
    INVOKEDYNAMIC   ((byte) 0xBA, 5, 0 /* variable */ , OpCode.FLAG_INVOKE),      //Invoke dynamic method


    //########## Array and instance creation ##########//

    //new instance

    /**
     * <b>Operation: </b>
     * Create new object <br><br>
     *
     * <b>Format: </b><pre><code>
     * new
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * new = 187 (0xbb)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ... →
     * ..., objectref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The unsigned indexbyte1 and indexbyte2 are used to construct an index into the
     * run-time constant pool of the current class (§2.6), where the value of the index is
     * (indexbyte1 &lt;&lt; 8) | indexbyte2. The run-time constant pool item at the index
     * must be a symbolic reference to a class or interface type. The named class or
     * interface type is resolved (§5.4.3.1) and should result in a class type. Memory for
     * a new instance of that class is allocated from the garbage-collected heap, and the
     * instance variables of the new object are initialized to their default initial values
     * (§2.3, §2.4). The objectref, a reference to the instance, is pushed onto the operand
     * stack. <br><br>
     *
     * On successful resolution of the class, it is initialized (§5.5) if it has not
     * already been initialized. <br><br>
     *
     * <b>Linking Exceptions: <br>
     * During resolution of the symbolic reference to the class, array, or interface type,
     * any of the exceptions documented in §5.4.3.1 can be thrown. <br><br>
     *
     * Otherwise, if the symbolic reference to the class, array, or interface type resolves
     * to an interface or is an abstract class, new throws an
     * <code>InstantiationError.</code> <br><br></b>
     *
     * <b>Run-time Exceptions: <br>
     * Otherwise, if execution of this new instruction causes initialization of the
     * referenced class, new may throw an Error as detailed in JLS §15.9.4. <br><br></b>
     *
     * <b>Notes: </b><br>
     * The new instruction does not completely create a new instance; instance creation is
     * not completed until an instance initialization method (§2.9) has been invoked on the
     * uninitialized instance.
     */
    NEW             ((byte) 0xBB, 3, 1),      //Create new object


    //new array

    /**
     * <b>Operation: </b>
     * Create new array <br><br>
     *
     * <b>Format: </b><pre><code>
     * newarray
     * atype
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * newarray = 188 (0xbc)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., count →
     * ..., arrayref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The count must be of type int. It is popped off the operand stack. The count
     * represents the number of elements in the array to be created. <br><br>
     *
     * The atype is a code that indicates the type of array to create. It must take one of
     * the following values: <br><br>
     *
     * Table 6.5.newarray-A. Array type codes<br>
     *
     * Array Type&#9;atype<br>
     * T_BOOLEAN&#9;4<br>
     * T_CHAR&#9;&#9;5<br>
     * T_FLOAT&#9;&#9;6<br>
     * T_DOUBLE&#9;7<br>
     * T_BYTE&#9;&#9;8<br>
     * T_SHORT&#9;9<br>
     * T_INT&#9;&#9;10<br>
     * T_LONG&#9;&#9;11<br>
     * <br>
     * A new array whose components are of type atype and of length count is allocated from
     * the garbage-collected heap. A reference arrayref to this new array object is pushed
     * into the operand stack. Each of the elements of the new array is initialized to the
     * default initial value (§2.3, §2.4) for the element type of the array type. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If count is less than zero, newarray throws a
     * <code>NegativeArraySizeException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * In Oracle's Java Virtual Machine implementation, arrays of type boolean (atype is
     * T_BOOLEAN) are stored as arrays of 8-bit values and are manipulated using the baload
     * and bastore instructions (§baload, §bastore) which also access arrays of type byte.
     * Other implementations may implement packed boolean arrays; the baload and bastore
     * instructions must still be used to access those arrays.
     */
    NEWARRAY        ((byte) 0xBC, 2, 0),      //Create new array

    /**
     * <b>Operation: </b>
     * Create new array of reference <br><br>
     *
     * <b>Format: </b><pre><code>
     * anewarray
     * indexbyte1
     * indexbyte2
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * anewarray = 189 (0xbd)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., count →
     * ..., arrayref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The count must be of type int. It is popped off the operand stack. The count
     * represents the number of components of the array to be created. The unsigned
     * indexbyte1 and indexbyte2 are used to construct an index into the run-time constant
     * pool of the current class (§2.6), where the value of the index is (indexbyte1
     * &lt;&lt; 8) | indexbyte2. The run-time constant pool item at that index must be a
     * symbolic reference to a class, array, or interface type. The named class, array, or
     * interface type is resolved (§5.4.3.1). A new array with components of that type, of
     * length count, is allocated from the garbage-collected heap, and a reference arrayref
     * to this new array object is pushed onto the operand stack. All components of the new
     * array are initialized to null, the default value for reference types (§2.4). <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class, array, or interface type,
     * any of the exceptions documented in §5.4.3.1 can be thrown. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if count is less than zero, the anewarray instruction throws a
     * <code>NegativeArraySizeException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * The anewarray instruction is used to create a single dimension of an array of object
     * references or part of a multidimensional array.
     */
    ANEWARRAY       ((byte) 0xBD, 3, 0),      //Create new array of reference

    /**
     * <b>Operation: </b>
     * Create new multidimensional array <br><br>
     *
     * <b>Format: </b><pre><code>
     * multianewarray
     * indexbyte1
     * indexbyte2
     * dimensions
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * multianewarray = 197 (0xc5)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., count1, [count2, ...] →
     * ..., arrayref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The dimensions operand is an unsigned byte that must be greater than or equal to 1.
     * It represents the number of dimensions of the array to be created. The operand stack
     * must contain dimensions values. Each such value represents the number of components
     * in a dimension of the array to be created, must be of type int, and must be
     * non-negative. The count1 is the desired length in the first dimension, count2 in the
     * second, etc. <br><br>
     *
     * All of the count values are popped off the operand stack. The unsigned indexbyte1
     * and indexbyte2 are used to construct an index into the run-time constant pool of the
     * current class (§2.6), where the value of the index is (indexbyte1 &lt;&lt; 8) |
     * indexbyte2. The run-time constant pool item at the index must be a symbolic
     * reference to a class, array, or interface type. The named class, array, or interface
     * type is resolved (§5.4.3.1). The resulting entry must be an array class type of
     * dimensionality greater than or equal to dimensions. <br><br>
     *
     * A new multidimensional array of the array type is allocated from the
     * garbage-collected heap. If any count value is zero, no subsequent dimensions are
     * allocated. The components of the array in the first dimension are initialized to
     * subarrays of the type of the second dimension, and so on. The components of the last
     * allocated dimension of the array are initialized to the default initial value (§2.3,
     * §2.4) for the element type of the array type. A reference arrayref to the new array
     * is pushed onto the operand stack. <br><br>
     *
     * <b>Linking Exceptions: </b><br>
     * During resolution of the symbolic reference to the class, array, or interface type,
     * any of the exceptions documented in §5.4.3.1 can be thrown. <br><br>
     *
     * Otherwise, if the current class does not have permission to access the element type
     * of the resolved array class, multianewarray throws an
     * <code>IllegalAccessError.</code> <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * Otherwise, if any of the dimensions values on the operand stack are less than zero,
     * the multianewarray instruction throws a <code>NegativeArraySizeException.</code> <br><br>
     *
     * <b>Notes: </b><br>
     * It may be more efficient to use newarray or anewarray (§newarray, §anewarray) when
     * creating an array of a single dimension. <br><br>
     *
     * The array class referenced via the run-time constant pool may have more dimensions
     * than the dimensions operand of the multianewarray instruction. In that case, only
     * the first dimensions of the dimensions of the array are created.
     */
    MULTIANEWARRAY  ((byte) 0xC5, 4, 0 /* variable */ ),      //Create new multidimensional array


    //array length

    /**
     * <b>Operation: </b>
     * Get length of array <br><br>
     *
     * <b>Format: </b><pre><code>
     * arraylength
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * arraylength = 190 (0xbe)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., arrayref →
     * ..., length
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The arrayref must be of type reference and must refer to an array. It is popped from
     * the operand stack. The length of the array it references is determined. That length
     * is pushed onto the operand stack as an int. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If the arrayref is null, the arraylength instruction throws a
     * <code>NullPointerException.</code>
     */
    ARRAYLENGTH     ((byte) 0xBE, 1, 0),      //Get length of array


    //new Exception

    /**
     * <b>Operation: </b>
     * Throw exception or error <br><br>
     *
     * <b>Format: </b><pre><code>
     * athrow
     * </code></pre>
     *
     * <b>Forms: </b><pre><code>
     * athrow = 191 (0xbf)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * ..., objectref →
     * objectref
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The objectref must be of type reference and must refer to an object that is an
     * instance of class Throwable or of a subclass of Throwable. It is popped from the
     * operand stack. The objectref is then thrown by searching the current method (§2.6)
     * for the first exception handler that matches the class of objectref, as given by the
     * algorithm in §2.10. <br><br>
     *
     * If an exception handler that matches objectref is found, it contains the location of
     * the code intended to handle this exception. The pc register is reset to that
     * location, the operand stack of the current frame is cleared, objectref is pushed
     * back onto the operand stack, and execution continues. <br><br>
     *
     * If no matching exception handler is found in the current frame, that frame is
     * popped. If the current frame represents an invocation of a synchronized method, the
     * monitor entered or reentered on invocation of the method is exited as if by
     * execution of a monitorexit instruction (§monitorexit). Finally, the frame of its
     * invoker is reinstated, if such a frame exists, and the objectref is rethrown. If no
     * such frame exists, the current thread exits. <br><br>
     *
     * <b>Run-time Exceptions: </b><br>
     * If objectref is null, athrow throws a <code>NullPointerException</code> instead of
     * objectref. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation does not enforce the rules on
     * structured locking described in §2.11.10, then if the method of the current frame is
     * a synchronized method and the current thread is not the owner of the monitor entered
     * or reentered on invocation of the method, athrow throws an
     * <code>IllegalMonitorStateException</code> instead of the object previously being
     * thrown. This can happen, for example, if an abruptly completing synchronized method
     * contains a monitorexit instruction, but no monitorenter instruction, on the object
     * on which the method is synchronized. <br><br>
     *
     * Otherwise, if the Java Virtual Machine implementation enforces the rules on
     * structured locking described in §2.11.10 and if the first of those rules is violated
     * during invocation of the current method, then athrow throws an
     * <code>IllegalMonitorStateException</code> instead of the object previously being
     * thrown. <br><br>
     *
     * <b>Notes: </b><br>
     * The operand stack diagram for the athrow instruction may be misleading: If a handler
     * for this exception is matched in the current method, the athrow instruction discards
     * all the values on the operand stack, then pushes the thrown object onto the operand
     * stack. However, if no handler is matched in the current method and the exception is
     * thrown farther up the method invocation chain, then the operand stack of the method
     * (if any) that handles the exception is cleared and objectref is pushed onto that
     * empty operand stack. All intervening frames from the method that threw the exception
     * up to, but not including, the method that handles the exception are discarded.
     */
    ATHROW          ((byte) 0xBF, 1, -1 /* stack is cleared */ ),      //Throw exception or error


    //wide

    /**
     * <b>Operation: </b>
     * Extend local variable index by additional bytes<br><br>
     *
     * <b>Format 1 </b><pre><code>
     * wide
     * &lt;opcode&gt;
     * indexbyte1
     * indexbyte2</code></pre>
     * where &lt;opcode&gt; is one of iload, fload, aload, lload, dload, istore, fstore,
     * astore, lstore, dstore, or ret<br><br>
     *
     * <b>Format 2 </b><pre><code>
     * wide
     * iinc
     * indexbyte1
     * indexbyte2
     * constbyte1
     * constbyte2 </code></pre><br><br>
     *
     * <b>Forms: </b><pre><code>
     * wide = 196 (0xc4)
     * </code></pre>
     *
     * <b>Operand Stack: </b><pre><code>
     * Same as modified instruction
     * </code></pre>
     *
     * <b>Description: </b><br>
     * The wide instruction modifies the behavior of another instruction. It takes one of
     * two formats, depending on the instruction being modified. The first form of the wide
     * instruction modifies one of the instructions iload, fload, aload, lload, dload,
     * istore, fstore, astore, lstore, dstore, or ret (§iload, §fload, §aload, §lload,
     * §dload, §istore, §fstore, §astore, §lstore, §dstore, §ret). The second form applies
     * only to the iinc instruction (§iinc). <br><br>
     *
     * In either case, the wide opcode itself is followed in the compiled code by the
     * opcode of the instruction wide modifies. In either form, two unsigned bytes
     * indexbyte1 and indexbyte2 follow the modified opcode and are assembled into a 16-bit
     * unsigned index to a local variable in the current frame (§2.6), where the value of
     * the index is (indexbyte1 &lt;&lt; 8) | indexbyte2. The calculated index must be an
     * index into the local variable array of the current frame. Where the wide instruction
     * modifies an lload, dload, lstore, or dstore instruction, the index following the
     * calculated index (index + 1) must also be an index into the local variable array. In
     * the second form, two immediate unsigned bytes constbyte1 and constbyte2 follow
     * indexbyte1 and indexbyte2 in the code stream. Those bytes are also assembled into a
     * signed 16-bit constant, where the constant is (constbyte1 &lt;&lt; 8) | constbyte2. <br><br>
     *
     * The widened bytecode operates as normal, except for the use of the wider index and,
     * in the case of the second form, the larger increment range. <br><br>
     *
     * <b>Notes: </b><br>
     * Although we say that wide "modifies the behavior of another instruction," the wide
     * instruction effectively treats the bytes constituting the modified instruction as
     * operands, denaturing the embedded instruction in the process. In the case of a
     * modified iinc instruction, one of the logical operands of the iinc is not even at
     * the normal offset from the opcode. The embedded instruction must never be executed
     * directly; its opcode must never be the target of any control transfer instruction.
     */
    WIDE            ((byte) 0xC4, -1, 0 /* depends the following */ );      //Extend local variable index by additional bytes

    private static final Map<Byte, OpCodeType> TYPE_MAP = new HashMap<>();

    static {
        for (OpCodeType v : values()) {
            TYPE_MAP.put(v.opCode, v);
        }
    }

    private final byte opCode;
    private final int byteSize;
    private final int stackGrow;
    private final int flag;

    OpCodeType(byte opCode, int byteSize, int stackGrow) {
        this.opCode = opCode;
        this.byteSize = byteSize;
        this.stackGrow = stackGrow;
        this.flag = 0;
    }

    OpCodeType(byte opCode, int byteSize, int stackGrow, int flag) {
        this.opCode = opCode;
        this.byteSize = byteSize;
        this.stackGrow = stackGrow;
        this.flag = flag;
    }

    public byte getTag() {
        return opCode;
    }

    // returns -1 to indicates that this is a variable-length opcode
    public int byteSize() {
        return byteSize;
    }

    public int getFlag() {
        return flag;
    }

    public int getStackGrow() {
        return stackGrow;
    }

    public static OpCodeType fromCode(int code) {
        return TYPE_MAP.get((byte) code);
    }

}
