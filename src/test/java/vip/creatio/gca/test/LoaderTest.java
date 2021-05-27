package vip.creatio.gca.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoaderTest {

    public static void main(String[] args) throws Exception {
        Class<?> c = new ClassLoader() {
            public Class<?> test() throws Exception{
                InputStream stream = new FileInputStream("test/Example.class");
                byte[] b = stream.readAllBytes();
                return defineClass("Example", b, 0, b.length);
            }
        }.test();
        Method m = c.getMethod("main", String[].class);
        m.invoke(null, (Object) new String[]{});
    }
}
