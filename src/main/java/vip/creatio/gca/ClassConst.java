package vip.creatio.gca;

public class ClassConst extends AbstractTypeConst {

    ClassConst(ConstPool pool, String name) {
        super(pool, ConstType.CLASS, name);
    }

    ClassConst(ConstPool pool) {
        super(pool, ConstType.CLASS);
    }

    public String toString() {
        return "Class{name=" + getName() + '}';
    }

    @Override
    public Const copy() {
        return new ClassConst(pool, getName());
    }
}
