package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;

public class ModuleConst extends AbstractTypeConst {

    public ModuleConst(ConstPool pool, String name) {
        super(pool, ConstType.MODULE, name);
    }

    public ModuleConst(ConstPool pool) {
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
