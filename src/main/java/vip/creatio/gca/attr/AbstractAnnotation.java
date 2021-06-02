package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.ByteVector;

abstract class AbstractAnnotation {

    final ConstPool pool;

    AbstractAnnotation(ConstPool pool) {
        this.pool = pool;
    }

    final ConstPool constPool() {
        return pool;
    }

    abstract void write(ByteVector buffer);

    abstract void collect();

    abstract AbstractAnnotation copy();

}
