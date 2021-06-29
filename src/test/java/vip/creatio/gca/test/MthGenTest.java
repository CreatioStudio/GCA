package vip.creatio.gca.test;

import vip.creatio.gca.Repository;
import vip.creatio.gca.type.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MthGenTest {

    public static void main(String[] args) {
        Repository repo = new Repository();
        GenericDeclaration gd = new GenericDeclaration() {
            final TypeVariable[] b = {TypeVariable.make(this, "Generic", Collections.emptyList()),
                    TypeVariable.make(this, "T", Collections.emptyList())};
            @Override
            public TypeVariable[] getTypeParameters() {
                return b;
            }

            @Override
            public AnnotationInfo[] getAnnotations() {
                return new AnnotationInfo[0];
            }
        };
        String sigs = "Ljava/lang/String<IITT;[[TGeneric;[[[Ljava/lang/Objects;>;";
        byte[] b = sigs.getBytes();
        int[] ptr = new int[1];
        List<Type> t = new ArrayList<>();
        while (ptr[0] < b.length) {
            t.add(Types.nextInternalSignature(repo, gd, null, b, ptr));
        }
        for (Type type : t) {
            System.out.println("TYPE: " + type.getClass());
            System.out.println(type.getCanonicalName());
            if (type instanceof ArrayType) System.out.println("Array: " + arrayString(type));
        }
        System.out.println("TYPES: " + t);
    }

    private static String arrayString(Type t) {
        if (t instanceof ArrayType) {
            return "Array: " + ((ArrayType) t).getComponentType();
        }
        return t.toString();
    }

}
 