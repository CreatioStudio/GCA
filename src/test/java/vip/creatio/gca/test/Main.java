package vip.creatio.gca.test;

import org.junit.Test;
import vip.creatio.gca.*;
import vip.creatio.gca.Code;
import vip.creatio.gca.code.Label;
import vip.creatio.gca.util.Util;
import vip.creatio.gca.util.common.ByteVector;

import java.io.*;
import java.util.Random;

import static vip.creatio.gca.ValueType.*;

public class Main {

    public static final String name = "Example.class";
    //public static final String name = "ClassFile.class";

    public static void main(String[] args) throws Exception {

        InputStream stream = new FileInputStream(name);
        byte[] bytes = stream.readAllBytes();
        System.out.println("SourceSize: " + bytes.length);

        ClassFile classFile = ClassFile.parse(bytes);

        //classFile.setSuperClass(classFile.constPool().acquireClass("jdk/internal/reflect/MagicAccessorImpl"));
        //classFile.setThisClass(classFile.constPool().acquireClass("vip.creatio.gca.test.Example"));
//
//        DeclaredMethod mth = classFile.getMethod("main", "void", String[].class.getName());
//        DeclaredMethod target = classFile.getMethod("sayFucker", "void");
//        if (mth != null) {
//            System.out.println(mth);
//            System.out.println(target);
//            mth.code().clear();
//            Code c = mth.code();
//
//            c.emitInvokeStatic(target);
//
//            c.emitGetStatic("java.lang.System", "out", "java.io.PrintStream");
//
//            c.emitNew("java.lang.StringBuilder");
//            c.emitDup();
//            c.emitInvokeSpecial("java.lang.StringBuilder", "<init>", "void");
//            c.emitConst("The value is: ");
//            c.emitInvokeVirtual("java.lang.StringBuilder", "append",
//                    "java.lang.StringBuilder", "java.lang.String");
//            c.emitConst(9969);
//            c.emitInvokeStatic(classFile.getMethod("tableSwitch", "int", "int"));
//            c.emitInvokeStatic("java.lang.Integer", "toString", "java.lang.String", "int");
//            c.emitInvokeVirtual("java.lang.StringBuilder", "append",
//                    "java.lang.StringBuilder", "java.lang.String");
//            c.emitInvokeVirtual("java.lang.StringBuilder", "toString", "java.lang.String");
//
//            c.emitInvokeVirtual("java.io.PrintStream", "println", "void", "java.lang.String");
//            c.emitReturn();
//
//        }
//
//        {
//            DeclaredMethod mth2 = classFile.visitMethod("newFuck", "double", "double");
//
//            Code c = mth2.code();
//            c.emitLoad(ValueType.DOUBLE, 0);
//            c.emitConst(Math.PI);
//            c.emitRem(ValueType.DOUBLE);
//            c.emitDup2();
//            c.emitStore(ValueType.DOUBLE, 0);
//            c.emitStore(ValueType.DOUBLE, 2);
//            c.emitConst(0);
//            c.emitConst(123);
//            c.emitDup();
//            c.emitMul(ValueType.INT);
//            c.emitAdd(ValueType.INT);
//            c.emitInt2Double();
//            c.emitLoad(ValueType.DOUBLE, 0);
//            c.emitMul(ValueType.DOUBLE);
//            c.emitReturn(ValueType.DOUBLE);
//            System.out.println("StackDepth: " + c.maxStack());
//            System.out.println(mth2);
//        }

//        emitSinFunc(classFile);

//        for (DeclaredMethod method : classFile.getMethods()) {
//            Code c = method.code();
//            c.attributes().clear();
//            method.attributes().removeIf(a -> !a.name().equals("Code"));
//
//            //c.removeAttribute("LineNumberTable");
//            //c.removeAttribute("LocalVariableTable");
//            //c.removeAttribute("StackMapTable");
//        }

//        for (DeclaredField f : classFile.getFields()) {
//            f.setName(random());
//            f.addAccessFlags(AccessFlag.SYNTHETIC);
//        }
//
//        for (DeclaredMethod mth : classFile.getMethods()) {
//            if (!mth.getName().string().equals("main") && !mth.getName().string().equals("<clinit>") && !mth.getName().string().equals("<init>"))
//            {
//                mth.setName(random());
//            }
//            mth.addAccessFlags(AccessFlag.SYNTHETIC);
//        }

        new File("test").mkdir();
        //FileChannel channel = new FileOutputStream("obfuscated/Example.class").getChannel();
        FileOutputStream channel = new FileOutputStream("test/" + name);
        ByteVector buffer = new ByteVector();
        classFile.write(buffer);
        channel.write(buffer.array());
        channel.close();

//        byte[] correct = new FileInputStream("ClassReader.class").readAllBytes();
//        byte[] wrong = new FileInputStream("test.class").readAllBytes();
//        System.out.println("Length: " + correct.length + " -> " + wrong.length);
//        for (int i = 0; i < correct.length; i++) {
//            if (correct[i] != wrong[i]) System.out.println("Different at 0x" + BigInteger.valueOf(i).toString(16));
//        }
    }

    public static final Random rd = new Random();
    //public static final char[] demon = {'\r', '\b', '\n', '\f', '^', '*', ',', '\'', '"', '~', '!', '`'};
    public static final char[] demon = {'l', 'i', '_', '$', 'o', 'O', 'u', 'v'};

    private static String random() {
        byte[] b = new byte[Math.max(10, Math.abs(rd.nextInt() % 99))];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) demon[Math.abs(rd.nextInt()) % demon.length];
        }
        return new String(b);
    }


    @Test
    public void test() throws IOException {
        ByteVector vec = new ByteVector();
        vec.putInt(999);
        vec.putInt(666);
        vec.putInt(333);
        vec.putInt(1111);
        vec.flip();
        for (int i = 0; i < 4; i++) {
            System.out.println("NUM: " + vec.getInt());
        }

        for (byte b : vec.array()) {
            System.out.println("VAL: 0x" + Util.toHex(b));
        }
    }

    private static void emitSinFunc(ClassFile f) {
        DeclaredMethod sin = f.visitMethod("sin", "double", "double");
        sin.addAccessFlags(AccessFlag.STATIC);
        Code c = sin.code();
        c.emitLoad(DOUBLE, 0);
        c.emitConst(Math.PI);
        c.emitRem(DOUBLE);
        c.emitDup2();
        c.emitStore(DOUBLE, 2);
        c.emitConst(false);
        c.emitStore(INT, 4);
        c.emitConst(1);
        c.emitDup();
        c.emitStore(INT, 5);
        Label L1 =
        c.emitConst(8).visitLabel();
        Label L6 = new Label("Label6");
        c.emitIfGreaterOrEqual(L6);
        c.emitConst(1D);
        c.emitConst(1);
        c.emitDup2();
        c.emitStore(INT, 6);
        c.emitLoad(INT, 5);
        c.emitConst(2);
        c.emitMul(INT);
        c.emitConst(1);
        c.emitAdd(INT);
        c.emitDup();
        c.emitStore(INT, 7);
        Label L3 = new Label("Label3");
        Label L2 =
        c.emitIfGreaterThan(L3).visitLabel();
        c.emitLoad(INT, 6);
        c.emitMul(INT);
        c.emitDupX2();
        c.emitPop();
        c.emitLoad(DOUBLE, 0);
        c.emitMul(DOUBLE);
        c.emitDup2X1();
        c.emitPop2();
        c.emitIncrement(6, 1);
        c.emitLoad(INT, 6);
        c.emitLoad(INT, 7);
        c.emitGoto(L2);
        L3.setAnchor(
        c.emitInt2Double());
        c.emitDiv(DOUBLE);
        c.emitLoad(DOUBLE, 2);
        c.emitLoad(INT, 4);
        c.emitDup();
        c.emitConst(1);
        c.emitXor(INT);
        c.emitStore(INT, 4);
        Label L4 = new Label("Label4");
        c.emitIfNotEqual(L4);
        c.emitAdd(DOUBLE);
        Label L9 = new Label("Label9");
        c.emitGoto(L9);
        L4.setAnchor(
        c.emitSub(DOUBLE));
        L9.setAnchor(
        c.emitStore(DOUBLE, 2));
        c.emitIncrement(5, 1);
        c.emitLoad(5);
        c.emitGoto(L1);
        L6.setAnchor(
        c.emitLoad(DOUBLE, 2));
        c.emitReturn(DOUBLE);
    }


}
