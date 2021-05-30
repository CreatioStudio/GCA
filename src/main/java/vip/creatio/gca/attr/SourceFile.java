package vip.creatio.gca.attr;

import vip.creatio.gca.Attribute;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.ClassFileParser;

import vip.creatio.gca.util.ByteVector;

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
    protected void collect() {
        super.collect();
        constPool().acquireUtf(sourceName);
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort(constPool().acquireUtf(sourceName).index());
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
