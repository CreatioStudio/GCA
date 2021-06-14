package vip.creatio.gca.attr;

import vip.creatio.gca.*;

import vip.creatio.gca.util.common.ByteVector;

/**
 * There can be no more than one SourceFile attribute in the
 * attributes table of a given ClassFile structure.
 *
 * sourceName indicates the source file name of this class file
 */
public class SourceFile extends Attribute {

    private String sourceName;

    private SourceFile(AttributeContainer c) {
        super(c);
    }

    public SourceFile(ClassFile file) {
        this((AttributeContainer) file);
    }

    public SourceFile(ClassFile file, String sourceName) {
        this((AttributeContainer) file);
        this.sourceName = sourceName;
    }

    public static SourceFile parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        SourceFile inst = new SourceFile(container);

        inst.sourceName = pool.getString(buffer.getShort());
        return inst;
    }

    public String getSource() {
        return sourceName;
    }

    public void setSource(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, ClassFile.class);
    }

    @Override
    public String name() {
        return "SourceFile";
    }

    @Override
    protected void collect(ConstPool pool) {
        super.collect(pool);
        pool.acquireUtf(sourceName);
    }

    @Override
    protected void writeData(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(sourceName));
    }

    @Override
    public String toString() {
        return "SourceFile{name=" + sourceName + '}';
    }

    @Override
    public boolean isEmpty() {
        return sourceName == null;
    }
}
