package vip.creatio.gca.attr;

import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ConstPool;
import vip.creatio.gca.util.ByteVector;

abstract class AbstractAnnotation {

    final ClassFile file;

    final ConstPool pool;

    AbstractAnnotation(ClassFile file) {
        this.file = file;
        this.pool = file.constPool();
    }

    final ConstPool constPool() {
        return pool;
    }

    abstract void write(ByteVector buffer);

    abstract void collect();

}
