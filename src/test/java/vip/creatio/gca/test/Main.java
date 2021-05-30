package vip.creatio.gca.test;

import org.junit.Test;
import vip.creatio.gca.Attribute;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.DeclaredMethod;
import vip.creatio.gca.attr.Code;
import vip.creatio.gca.code.OpCode;
import vip.creatio.gca.constant.ClassConst;
import vip.creatio.gca.util.ByteVector;

import java.io.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Random;

public class Main {

    public static final String name = "Example.class";
//    public static final String name = "ClassFile.class";

    public static void main(String[] args) throws Exception {

        InputStream stream = new FileInputStream(name);
        byte[] bytes = stream.readAllBytes();
        System.out.println("SourceSize: " + bytes.length);

        ClassFile classFile = ClassFile.parse(bytes);
        System.out.println("ConstSize: " + classFile.constPool().size());
        classFile.setMajorVer(55);

        //classFile.setSuperClass(classFile.constPool().acquireClass("jdk/internal/reflect/MagicAccessorImpl"));
        classFile.setThisClass(classFile.constPool().acquireClass("vip/creatio/gca/test/Example"));

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
        classFile.toByteArray();
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
            System.out.println("VAL: 0x" + hex(b));
        }
    }

    private String hex(byte v) {
        return BigInteger.valueOf(v & 0xFF).toString(16);
    }
}
