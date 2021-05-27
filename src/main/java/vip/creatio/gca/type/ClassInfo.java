package vip.creatio.gca.type;

public interface ClassInfo extends Info {

    boolean isLoaded();

    boolean isClassFile();

    ClassFileInfo getClassFile();

    ClassFileInfo loadClass();

    TypeSignature getSignature();

}
