package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;

public class PackageConst extends AbstractTypeConst {

    public PackageConst(ConstPool pool, String name) {
        super(pool, ConstType.PACKAGE, name);
    }

    public PackageConst(ConstPool pool) {
        super(pool, ConstType.PACKAGE);
    }

    public String toString() {
        return "Package{name=" + getName() + '}';
    }

    @Override
    public Const copy() {
        return new PackageConst(pool, getName());
    }
}
