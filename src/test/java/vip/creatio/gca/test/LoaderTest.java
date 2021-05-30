package vip.creatio.gca.test;

import sun.misc.Unsafe;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LoaderTest {

    public static void main(String[] args) throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        Class<?> c = new ClassLoader() {
            public Class<?> test() throws Exception{
                InputStream stream = new FileInputStream("test/Example.class");
                byte[] b = stream.readAllBytes();
                return unsafe.defineAnonymousClass(LoaderTest.class, b, null);
            }
        }.test();
        Method m = c.getMethod("main", String[].class);
        m.invoke(null, (Object) new String[]{});
    }
}
