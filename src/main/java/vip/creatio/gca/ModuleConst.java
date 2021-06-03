package vip.creatio.gca;

public class ModuleConst extends AbstractTypeConst {

    ModuleConst(ConstPool pool, String name) {
        super(pool, ConstType.MODULE, name);
    }

    ModuleConst(ConstPool pool) {
        super(pool, ConstType.MODULE);
    }

    public String toString() {
        return "Module{name=" + getName() + '}';
    }

    @Override
    public Const copy() {
        return new ModuleConst(pool, getName());
    }
}
