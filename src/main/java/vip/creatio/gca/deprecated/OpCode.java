package vip.creatio.gca;

import java.io.BufferedInputStream;
import java.io.IOException;

@SuppressWarnings("unused")
abstract class OpCode {
    final Attribute.Code linked_att;
    final byte opCode;

    OpCode(Attribute.Code att, byte code) {
        linked_att = att;
        this.opCode = code;
    }

    OpCode(Attribute.Code att, OpCodeType code) {
        linked_att = att;
        this.opCode = code.getCode();
    }

    abstract OpCodeType getType();

    abstract int skip();

    abstract String getItem();

    interface Returnable {
        Object getReturn();
    }

    interface ReturnNumber extends Returnable {
        @Override
        default Object getReturn() {
            return getNumber();
        }

        Number getNumber();
    }

    //TODO: reference
    interface ReturnReference extends Returnable {

    }

    interface ReturnLocals extends Returnable {
        @Override
        default Object getReturn() {
            return getLocals();
        }

        Locals getLocals();
    }

    interface ReturnInt extends ReturnNumber {
        @Override
        default Number getNumber() {
            return getInt();
        }

        int getInt();
    }

    interface ReturnLong extends ReturnNumber {
        @Override
        default Number getNumber() {
            return getLong();
        }

        long getLong();
    }

    interface ReturnFloat extends ReturnNumber {
        @Override
        default Number getNumber() {
            return getFloat();
        }

        float getFloat();
    }

    interface ReturnDouble extends ReturnNumber {
        @Override
        default Number getNumber() {
            return getDouble();
        }

        double getDouble();
    }

    /** Push null to stack */
    static class aconst_null extends OpCode {

        aconst_null(Attribute.Code att) {
            super(att, (byte) 0x1);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aconst_null;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "null";
        }

        public String toString() {
            return "aconst_null";
        }
    }

    /** Push int value -1 to stack */
    static class iconst_m1 extends OpCode implements ReturnInt {
        iconst_m1(Attribute.Code att) {
            super(att, (byte) 0x2);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_m1;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "-1";
        }

        @Override
        public int getInt() {
            return -1;
        }

        public String toString() {
            return "iconst_m1";
        }
    }

    /** Push int value 0 to stack */
    static class iconst_0 extends OpCode implements ReturnInt {
        iconst_0(Attribute.Code att) {
            super(att, (byte) 0x3);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_0;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "0";
        }

        @Override
        public int getInt() {
            return 0;
        }

        public String toString() {
            return "iconst_0";
        }
    }

    /** Push int value 1 to stack */
    static class iconst_1 extends OpCode implements ReturnInt {
        iconst_1(Attribute.Code att) {
            super(att, (byte) 0x4);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_1;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "1";
        }

        @Override
        public int getInt() {
            return 1;
        }

        public String toString() {
            return "iconst_1";
        }
    }

    /** Push int value 2 to stack */
    static class iconst_2 extends OpCode implements ReturnInt {
        iconst_2(Attribute.Code att) {
            super(att, (byte) 0x5);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_2;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "2";
        }

        @Override
        public int getInt() {
            return 2;
        }

        public String toString() {
            return "iconst_2";
        }
    }

    /** Push int value 3 to stack */
    static class iconst_3 extends OpCode implements ReturnInt {
        iconst_3(Attribute.Code att) {
            super(att, (byte) 0x6);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_3;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "3";
        }

        @Override
        public int getInt() {
            return 3;
        }

        public String toString() {
            return "iconst_3";
        }
    }

    /** Push int value 4 to stack */
    static class iconst_4 extends OpCode implements ReturnInt {
        iconst_4(Attribute.Code att) {
            super(att, (byte) 0x7);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_4;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "4";
        }

        @Override
        public int getInt() {
            return 4;
        }

        public String toString() {
            return "iconst_4";
        }
    }

    /** Push int value 5 to stack */
    static class iconst_5 extends OpCode implements ReturnInt {
        iconst_5(Attribute.Code att) {
            super(att, (byte) 0x8);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iconst_5;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "5";
        }

        @Override
        public int getInt() {
            return 5;
        }

        public String toString() {
            return "iconst_5";
        }
    }

    /** Push long value 0L to stack */
    static class lconst_0 extends OpCode implements ReturnLong {
        lconst_0(Attribute.Code att) {
            super(att, (byte) 0x9);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lconst_0;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "0L";
        }

        @Override
        public long getLong() {
            return 0L;
        }

        public String toString() {
            return "lconst_0";
        }
    }

    /** Push long value 1L to stack */
    static class lconst_1 extends OpCode implements ReturnLong {
        lconst_1(Attribute.Code att) {
            super(att, (byte) 0xA);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lconst_1;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "1L";
        }

        @Override
        public long getLong() {
            return 1L;
        }

        public String toString() {
            return "lconst_1";
        }
    }

    /** Push float value 0.0f to stack */
    static class fconst_0 extends OpCode implements ReturnFloat {
        fconst_0(Attribute.Code att) {
            super(att, (byte) 0xB);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fconst_0;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "0.0f";
        }

        @Override
        public float getFloat() {
            return 0f;
        }

        public String toString() {
            return "fconst_0";
        }
    }

    /** Push float value 1.0f to stack */
    static class fconst_1 extends OpCode implements ReturnFloat{
        fconst_1(Attribute.Code att) {
            super(att, (byte) 0xC);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fconst_1;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "1.0f";
        }

        @Override
        public float getFloat() {
            return 1f;
        }

        public String toString() {
            return "fconst_1";
        }
    }

    /** Push float value 2.0f to stack */
    static class fconst_2 extends OpCode implements ReturnFloat {
        fconst_2(Attribute.Code att) {
            super(att, (byte) 0xD);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fconst_2;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "2.0f";
        }

        @Override
        public float getFloat() {
            return 2f;
        }

        public String toString() {
            return "fconst_2";
        }
    }

    /** Push double value 0.0d to stack */
    static class dconst_0 extends OpCode implements ReturnDouble {
        dconst_0(Attribute.Code att) {
            super(att, (byte) 0xE);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dconst_0;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        public double getDouble() {
            return 0d;
        }

        @Override
        String getItem() {
            return "0.0d";
        }

        public String toString() {
            return "dconst_0";
        }
    }

    /** Push double value 1.0d to stack */
    static class dconst_1 extends OpCode implements ReturnDouble {
        dconst_1(Attribute.Code att) {
            super(att, (byte) 0xF);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dconst_1;
        }

        @Override
        int skip() {
            return 1;
        }

        @Override
        String getItem() {
            return "1.0d";
        }

        @Override
        public double getDouble() {
            return 1d;
        }

        public String toString() {
            return "dconst_1";
        }
    }

    /** Push a byte value to stack */
    static class bipush extends OpCode implements ReturnInt {

        final byte b;

        bipush(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x10);
            b = (byte) buffer.read();
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.bipush;
        }

        @Override
        int skip() {
            return 2;
        }

        @Override
        String getItem() {
            return String.valueOf(getInt());
        }

        @Override
        public int getInt() {
            return b & 0xFF;
        }

        public String toString() {
            return "bipush";
        }
    }

    /** Push a short value to stack */
    static class sipush extends OpCode implements ReturnInt {

        final short s;

        sipush(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x11);
            byte[] b2 = new byte[2];
            buffer.read(b2);
            s = (short) (((b2[0] << 8) | b2[1]) & 0xFFFF);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.bipush;
        }

        @Override
        int skip() {
            return 3;
        }

        @Override
        String getItem() {
            return String.valueOf(getInt());
        }

        @Override
        public int getInt() {
            return s & 0xFFFF;
        }

        public String toString() {
            return "sipush";
        }
    }

    /** Push a item from run-time constant, can only get index that from 0 to 255 */
    static class ldc extends OpCode implements ReturnReference {

        final int index;

        ldc(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x12);
            index = buffer.read() & 0xFF;
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.ldc;
        }

        @Override
        int skip() {
            return 2;
        }

        @Override
        String getItem() {
            String str = "#" + index;
            Constant cst = linked_att.linked_reader.constantPool[index];
            return str + " ".repeat(24 - str.length()) + "// " + cst.getType() + ' ' + cst.getItem0();
        }

        @Override
        public Object getReturn() {
            return linked_att.linked_reader.constantPool[index];
        }

        public String toString() {
            return "ldc";
        }
    }

    /** Push a item from run-time constant, can get index that from 0 to 65535 */
    static class ldc_w extends OpCode implements ReturnReference {

        final int index;

        ldc_w(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x13);
            byte[] b2 = new byte[2];
            buffer.read(b2);
            index = (short) (((b2[0] << 8) | b2[1]) & 0xFFFF);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.ldc_w;
        }

        @Override
        int skip() {
            return 3;
        }

        @Override
        String getItem() {
            String str = "#" + index;
            Constant cst = linked_att.linked_reader.constantPool[index];
            return str + " ".repeat(24 - str.length()) + "// " + cst.getType() + ' ' + cst.getItem0();
        }

        @Override
        public Object getReturn() {
            return linked_att.linked_reader.constantPool[index];
        }

        public String toString() {
            return "ldc_w";
        }
    }

    /** Push long or double from run-time constant pool (wide index, 0~65535) */
    static class ldc2_w extends OpCode implements ReturnNumber {

        final int index;

        ldc2_w(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x14);
            byte[] b2 = new byte[2];
            buffer.read(b2);
            index = (short) (((b2[0] << 8) | b2[1]) & 0xFFFF);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.ldc2_w;
        }

        @Override
        int skip() {
            return 3;
        }

        @Override
        String getItem() {
            String str = "#" + index;
            Constant cst = linked_att.linked_reader.constantPool[index];
            return str + " ".repeat(24 - str.length()) + "// " + cst.getType() + ' ' + cst.getItem0();
        }

        @Override
        public Number getNumber() {
            Constant cst = linked_att.linked_reader.constantPool[index];
            if (cst instanceof Constant.DOUBLE) {
                return ((Constant.DOUBLE) cst).getDouble();
            }
            if (cst instanceof Constant.LONG) {
                return ((Constant.LONG) cst).getLong();
            }
            throw new RuntimeException("Not a valid constant:" + cst);
        }

        public String toString() {
            return "ldc2_w";
        }
    }

    static abstract class varload extends OpCode implements ReturnLocals {

        varload(Attribute.Code att, byte code) {
            super(att, code);
        }

        abstract int getIndex();

        @Override
        String getItem() {
            return " -> " + getLocals();
        }

        @Override
        public Locals getLocals() {
            return linked_att.locals[getIndex()];
        }
    }

    static abstract class indexload extends varload {

        final int index;

        indexload(Attribute.Code att, byte code, BufferedInputStream buffer) throws IOException {
            super(att, code);
            index = buffer.read() & 0xFF;
        }

        @Override
        int skip() {
            return 2;
        }

        @Override
        int getIndex() {
            return index;
        }
    }

    static abstract class presetload extends varload {

        public presetload(Attribute.Code att, byte code) {
            super(att, code);
        }

        @Override
        int skip() {
            return 1;
        }
    }

    static abstract class arrayload extends indexload {

        public arrayload(Attribute.Code att, byte code, BufferedInputStream buffer) throws IOException {
            super(att, code, buffer);
        }

        @Override
        String getItem() {
            return " -> " + getLocals() + '[' + getIndex() + ']';
        }
    }

    /** Load int from local variable */
    static class iload extends indexload {

        iload(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x15, buffer);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iload;
        }

        public String toString() {
            return "iload";
        }
    }

    /** Load int from local variable */
    static class iload_0 extends presetload {

        iload_0(Attribute.Code att) {
            super(att, (byte) 0x1A);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iload_0;
        }

        @Override
        int getIndex() {
            return 0;
        }

        public String toString() {
            return "iload_0";
        }
    }

    /** Load int from local variable */
    static class iload_1 extends presetload {

        iload_1(Attribute.Code att) {
            super(att, (byte) 0x1B);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iload_1;
        }

        @Override
        int getIndex() {
            return 1;
        }

        public String toString() {
            return "iload_1";
        }
    }

    /** Load int from local variable */
    static class iload_2 extends presetload {

        iload_2(Attribute.Code att) {
            super(att, (byte) 0x1C);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iload_2;
        }

        @Override
        int getIndex() {
            return 2;
        }

        public String toString() {
            return "iload_2";
        }
    }

    /** Load int from local variable */
    static class iload_3 extends presetload {

        iload_3(Attribute.Code att) {
            super(att, (byte) 0x1D);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.iload_3;
        }

        @Override
        int getIndex() {
            return 3;
        }

        public String toString() {
            return "iload_3";
        }
    }

    /** Load long from local variable */
    static class lload extends indexload {

        lload(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x16, buffer);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lload;
        }

        public String toString() {
            return "lload";
        }
    }

    /** Load long from local variable */
    static class lload_0 extends presetload {

        lload_0(Attribute.Code att) {
            super(att, (byte) 0x1E);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lload_0;
        }

        @Override
        int getIndex() {
            return 0;
        }

        public String toString() {
            return "lload_0";
        }
    }

    /** Load long from local variable */
    static class lload_1 extends presetload {

        lload_1(Attribute.Code att) {
            super(att, (byte) 0x1F);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lload_1;
        }

        @Override
        int getIndex() {
            return 1;
        }

        public String toString() {
            return "lload_1";
        }
    }

    /** Load long from local variable */
    static class lload_2 extends presetload {

        lload_2(Attribute.Code att) {
            super(att, (byte) 0x20);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lload_2;
        }

        @Override
        int getIndex() {
            return 2;
        }

        public String toString() {
            return "lload_2";
        }
    }

    /** Load long from local variable */
    static class lload_3 extends presetload {

        lload_3(Attribute.Code att) {
            super(att, (byte) 0x21);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.lload_3;
        }

        @Override
        int getIndex() {
            return 3;
        }

        public String toString() {
            return "lload_3";
        }
    }

    /** Load float from local variable */
    static class fload extends indexload {

        fload(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x17, buffer);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fload;
        }

        public String toString() {
            return "fload";
        }
    }

    /** Load float from local variable */
    static class fload_0 extends presetload {

        fload_0(Attribute.Code att) {
            super(att, (byte) 0x22);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fload_0;
        }

        @Override
        int getIndex() {
            return 0;
        }

        public String toString() {
            return "fload_0";
        }
    }

    /** Load float from local variable */
    static class fload_1 extends presetload {

        fload_1(Attribute.Code att) {
            super(att, (byte) 0x23);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fload_1;
        }

        @Override
        int getIndex() {
            return 1;
        }

        public String toString() {
            return "fload_1";
        }
    }

    /** Load float from local variable */
    static class fload_2 extends presetload {

        fload_2(Attribute.Code att) {
            super(att, (byte) 0x24);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fload_2;
        }

        @Override
        int getIndex() {
            return 2;
        }

        public String toString() {
            return "fload_2";
        }
    }

    /** Load float from local variable */
    static class fload_3 extends presetload {

        fload_3(Attribute.Code att) {
            super(att, (byte) 0x25);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.fload_3;
        }

        @Override
        int getIndex() {
            return 3;
        }

        public String toString() {
            return "fload_3";
        }
    }

    /** Load double from local variable */
    static class dload extends indexload {

        dload(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x18, buffer);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dload;
        }

        public String toString() {
            return "dload";
        }
    }

    /** Load double from local variable */
    static class dload_0 extends presetload {

        dload_0(Attribute.Code att) {
            super(att, (byte) 0x26);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dload_0;
        }

        @Override
        int getIndex() {
            return 0;
        }

        public String toString() {
            return "dload_0";
        }
    }

    /** Load double from local variable */
    static class dload_1 extends presetload {

        dload_1(Attribute.Code att) {
            super(att, (byte) 0x27);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dload_1;
        }

        @Override
        int getIndex() {
            return 1;
        }

        public String toString() {
            return "dload_1";
        }
    }

    /** Load double from local variable */
    static class dload_2 extends presetload {

        dload_2(Attribute.Code att) {
            super(att, (byte) 0x28);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dload_2;
        }

        @Override
        int getIndex() {
            return 2;
        }

        public String toString() {
            return "dload_2";
        }
    }

    /** Load double from local variable */
    static class dload_3 extends presetload {

        dload_3(Attribute.Code att) {
            super(att, (byte) 0x29);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.dload_3;
        }

        @Override
        int getIndex() {
            return 3;
        }

        public String toString() {
            return "dload_3";
        }
    }

    /** Load reference from local variable */
    static class aload extends indexload {

        aload(Attribute.Code att, BufferedInputStream buffer) throws IOException {
            super(att, (byte) 0x19, buffer);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aload;
        }

        public String toString() {
            return "aload";
        }
    }

    /** Load reference from local variable */
    static class aload_0 extends presetload {

        aload_0(Attribute.Code att) {
            super(att, (byte) 0x2A);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aload_0;
        }

        @Override
        int getIndex() {
            return 0;
        }

        public String toString() {
            return "aload_0";
        }
    }

    /** Load reference from local variable */
    static class aload_1 extends presetload {

        aload_1(Attribute.Code att) {
            super(att, (byte) 0x2B);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aload_1;
        }

        @Override
        int getIndex() {
            return 1;
        }

        public String toString() {
            return "aload_1";
        }
    }

    /** Load reference from local variable */
    static class aload_2 extends presetload {

        aload_2(Attribute.Code att) {
            super(att, (byte) 0x2C);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aload_2;
        }

        @Override
        int getIndex() {
            return 2;
        }

        public String toString() {
            return "aload_2";
        }
    }

    /** Load reference from local variable */
    static class aload_3 extends presetload {

        aload_3(Attribute.Code att) {
            super(att, (byte) 0x2D);
        }

        @Override
        OpCodeType getType() {
            return OpCodeType.aload_3;
        }

        @Override
        int getIndex() {
            return 3;
        }

        public String toString() {
            return "aload_3";
        }
    }
}
