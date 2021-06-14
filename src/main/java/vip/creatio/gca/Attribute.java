package vip.creatio.gca;

import vip.creatio.gca.attr.*;

import vip.creatio.gca.util.common.ByteVector;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class Attribute {

    private static final Map<String, AttributeResolver> DEFAULT_RESOLVERS = new HashMap<>();
    static {
        DEFAULT_RESOLVERS.put("ConstantValue", ConstantValue::parse);
        DEFAULT_RESOLVERS.put("AnnotationDefault", AnnotationDefault::parse);
        DEFAULT_RESOLVERS.put("Code", Code::parse);
        DEFAULT_RESOLVERS.put("StackMapTable", StackMapTable::parse);
        DEFAULT_RESOLVERS.put("Exceptions", Exceptions::parse);
        DEFAULT_RESOLVERS.put("EnclosingMethod", EnclosingMethod::parse);
        DEFAULT_RESOLVERS.put("InnerClasses", InnerClasses::parse);
        DEFAULT_RESOLVERS.put("LineNumberTable", LineNumberTable::parse);
        //DEFAULT_RESOLVERS.put("Signature", Signature::parse);//TODO
        DEFAULT_RESOLVERS.put("SourceFile", SourceFile::parse);
        DEFAULT_RESOLVERS.put("LocalVariableTable", LocalVariableTable::parse);
        DEFAULT_RESOLVERS.put("LocalVariableTypeTable", LocalVariableTypeTable::parse);
        DEFAULT_RESOLVERS.put("BootstrapMethods", BootstrapMethods::parse);
        DEFAULT_RESOLVERS.put("RuntimeVisibleAnnotations", (a, b, c) -> Annotations.parse(a, b, c, true));
        DEFAULT_RESOLVERS.put("RuntimeInvisibleAnnotations", (a, b, c) -> Annotations.parse(a, b, c, false));
        DEFAULT_RESOLVERS.put("RuntimeVisibleParameterAnnotations", (a, b, c) -> ParameterAnnotations.parse(a, b, c, true));
        DEFAULT_RESOLVERS.put("RuntimeInvisibleParameterAnnotations", (a, b, c) -> ParameterAnnotations.parse(a, b, c, false));
        DEFAULT_RESOLVERS.put("RuntimeVisibleTypeAnnotations", (a, b, c) -> TypeAnnotations.parse(a, b, c, true));
        DEFAULT_RESOLVERS.put("RuntimeInvisibleTypeAnnotations", (a, b, c) -> TypeAnnotations.parse(a, b, c, false));
    }

    public static Map<String, AttributeResolver> getResolvers() {
        return new HashMap<>(DEFAULT_RESOLVERS);
    }

    protected AttributeContainer container;

    protected Attribute(AttributeContainer container) {
        checkContainerType(container);
        this.container = container;
    }

    public abstract String name();

    //public abstract Attribute copy();

    public ClassFile classFile() {
        return container.classFile();
    }

    protected abstract void writeData(ConstPool pool, ByteVector buffer);

    protected final void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(name()));
        int pos = buffer.position();
        buffer.skip(4);
        writeData(pool, buffer);
        int len = buffer.position() - pos - 4;
        buffer.putInt(pos, len);
    }

    // collect constants that required by this attribute
    protected void collect(ConstPool pool) {
        pool.acquireUtf(name());
    }

    protected void checkContainerType(AttributeContainer container) {}

    protected void checkContainerType(AttributeContainer container, Class<? extends AttributeContainer> cls) {
        if (!cls.isAssignableFrom(container.getClass()))
            throw new IllegalArgumentException("Attribute " + name() + " is only expected in " + cls.getSimpleName());
    }

    // whether this attribute is empty, usually used for table attribute
    // this will be called when serialize, if returns false this attribute
    // will not be serialized.
    public boolean isEmpty() {
        return false;
    }

    @Override
    public abstract String toString();

    public static class Undefined extends Attribute {
        private final byte[] data;
        private final String name;

        public Undefined(ClassFile bc, String name, byte[] data) {
            super(bc);
            this.data = data;
            this.name = name;
        }

        public byte[] data() {
            return data;
        }

        @Override
        public String name() {
            return name;
        }

        //@Override
        public Attribute copy() {
            return new Undefined((ClassFile) container, name, data);
        }

        @Override
        public void writeData(ConstPool pool, ByteVector buffer) {
            buffer.putBytes(data);
        }

        @Override
        public String toString() {
            return "Undefined{name=" + name + ",data_length=" + data.length + '}';
        }
    }
}
