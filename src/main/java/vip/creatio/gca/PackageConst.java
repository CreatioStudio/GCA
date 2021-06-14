package vip.creatio.gca;

public class PackageConst extends AbstractTypeConst {

    PackageConst(String name) {
        super(name);
    }

    @Override
    public ConstType constantType() {
        return ConstType.PACKAGE;
    }

    public String toString() {
        return "Package{name=" + getName() + '}';
    }
}
