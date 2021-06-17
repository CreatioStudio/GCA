package vip.creatio.gca;

@SuppressWarnings("unused")
public interface Const {

    ConstType constantType();

    // marker interface which indicates this constant takes 2 slots
    interface DualSlot {}
}
