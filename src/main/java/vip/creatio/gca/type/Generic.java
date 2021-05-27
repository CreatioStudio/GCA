package vip.creatio.gca.type;

public interface Generic extends Type {

    @Override
    default boolean isGeneric() {
        return true;
    }
}
