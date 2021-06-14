package vip.creatio.gca;

@SuppressWarnings("unused")
public interface Const {

    ConstType constantType();

    // marker class which indicates this constant can be accepted in ConstantValue
    abstract class Value implements Const {

        public abstract Object value();

        public abstract ValueType valueType();

        @Override
        public abstract ConstType constantType();

        public static Value of(Object v) {
            ValueType type = ValueType.getValueType(v);
            switch (type) {
                case BYTE:
                    return new IntegerConst((Byte) v);
                case CHAR:
                    return new IntegerConst((Character) v);
                case DOUBLE:
                    return new DoubleConst((Double) v);
                case FLOAT:
                    return new FloatConst((Float) v);
                case INT:
                    return new IntegerConst((Integer) v);
                case LONG:
                    return new LongConst((Long) v);
                case SHORT:
                    return new IntegerConst((Short) v);
                case BOOLEAN:
                    return new IntegerConst(((boolean) v) ? 1 : 0);
                case STRING:
                    return new StringConst((String) v);
                default:
                    throw new UnsupportedOperationException(v.getClass().getName());
            }
        }
    }

    // marker interface which indicates this constant takes 2 slots
    interface DualSlot {}
}
