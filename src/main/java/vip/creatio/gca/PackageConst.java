package vip.creatio.gca;

public class PackageConst extends AbstractTypeConst {

    PackageConst(ConstPool pool, String name) {
        super(pool, ConstType.PACKAGE, name);
    }

    PackageConst(ConstPool pool) {
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
