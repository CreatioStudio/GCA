package vip.creatio.gca.test;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;

import java.io.FileInputStream;
import java.io.InputStream;

public class Test {

    public static void main(String[] args) throws Exception {
        InputStream stream = new FileInputStream("ClassFile.class");
        //InputStream stream = new FileInputStream("Example.class");
        byte[] bytes = stream.readAllBytes();
        System.out.println("Source Size: " + bytes.length);
        ClassFile classFile = ClassFile.parse(bytes);
        classFile.collect();
        System.out.println("Finish");

        stream = new FileInputStream("test/ClassFile.class");
        //InputStream stream = new FileInputStream("Example.class");
        bytes = stream.readAllBytes();
        System.out.println("Corrupted Size: " + bytes.length);
        ClassFile classFile2 = ClassFile.parse(bytes);
        classFile2.collect();
        System.out.println("Finish");

        System.out.println("Disappeared Strings: " + ClassFileParser.testSet);
    }
}
