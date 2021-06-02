package vip.creatio.gca.type;

public interface Type {

    //java.lang.String / [Ljava/lang/String; // java binary name that can be accepted by Class::forName
    String getTypeName();

    //java/lang/String / [Ljava/lang/String;
    String getBytecodeTypeName();

}
