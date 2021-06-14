package vip.creatio.gca;

public class ModuleConst extends AbstractTypeConst {

    ModuleConst(String name) {
        super(name);
    }

    @Override
    public ConstType constantType() {
        return ConstType.MODULE;
    }

    public String toString() {
        return "Module{name=" + getName() + '}';
    }

}
