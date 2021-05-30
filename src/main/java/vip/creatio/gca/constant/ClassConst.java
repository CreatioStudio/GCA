package vip.creatio.gca.constant;

import vip.creatio.gca.ConstPool;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

public class ClassConst extends AbstractTypeConst {

    public ClassConst(ConstPool pool, String name) {
        super(pool, ConstType.CLASS, name);
    }

    public ClassConst(ConstPool pool) {
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
