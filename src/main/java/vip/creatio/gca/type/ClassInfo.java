package vip.creatio.gca.type;

public abstract class ClassInfo implements Type {

    boolean isLoaded();

    boolean isClassFile();

    ClassFileInfo getClassFile();

    ClassFileInfo loadClass();

    TypeSignature getSignature();

}
