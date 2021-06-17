package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.AttributeResolver;

import vip.creatio.gca.attr.BootstrapMethods;
import vip.creatio.gca.type.MethodInfo;
import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.util.Pair;
import vip.creatio.gca.util.Util;

import java.util.*;

public class ClassFileParser implements Iterable<Const> {

    private final Map<String, AttributeResolver> resolvers = Attribute.getResolvers();
    private final ParsedConst[] constants;
    private final ClassFile file;
    private List<Pair<InvokeDynamicConst, Integer>> dynamics;

    ClassFileParser(ClassFile file, int size, @Nullable Map<String, AttributeResolver> resolvers) {
        this.constants = new ParsedConst[size];
        this.file = file;
        if (resolvers != null) this.resolvers.putAll(resolvers);
    }

    public Const get(int index) {
        if (index == 0) return null;
        return getParsed(index).constant;
    }

    private ParsedConst getParsed(int index) {
        return constants[index - 1];
    }

    private Const getAndEnsureParsed(int index, ByteVector buf) {
        ParsedConst p = constants[index - 1];
        if (!p.parsed) try {
            p.parse(buf);
        } catch (RuntimeException e) {
            System.err.println("Failed to parse constant " + p.constant + " @ " + index);
            System.err.println("Parsed constants:");
            printConstants();
            throw e;
        }
        return p.constant;
    }

    public String getString(int index) {
        return ((UTFConst) get(index)).string();
    }

    void set(int index, int pos, @Nullable Const c) {
        constants[index - 1] = new ParsedConst(pos, c);
    }

    ClassFile classFile() {
        return file;
    }

    public int size() {
        return constants.length;
    }

    private AttributeResolver getResolver(String name) {
        for (Map.Entry<String, AttributeResolver> entry : resolvers.entrySet()) {
            if (entry.getKey().equals(name)) return entry.getValue();
        }
        return null;
    }

    public Attribute resolveAttribute(ByteVector buffer) {
        return resolveAttribute(file, buffer);
    }

    // @Nullable
    public Attribute resolveAttribute(AttributeContainer contaner, ByteVector buffer) {
        String name = getString(buffer.getShort());
        AttributeResolver resolver = getResolver(name);
        int length = buffer.getInt();
        if (resolver == null) {
            System.err.println("Debug: No attribute resolver found with name " + name);
            byte[] data = new byte[length];
            buffer.getBytes(data, 0, length);
            return new Attribute.Undefined(file, name, data);
        }

        return resolver.provide(contaner, this, buffer);
    }

    void printConstants() {
        int j = 1;
        try {
            for (ParsedConst cc : constants) {
                if (cc != null) {
                    System.err.println("   " + j++ + ". " + (cc.constant == null ? "/" : cc.constant));
                    if (cc.constant instanceof Const.DualSlot) j++;
                }
            }
        } catch (Exception ignored) {}
    }

    @NotNull
    @Override
    public Iterator<Const> iterator() {
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < constants.length - 1;
            }

            @Override
            public Const next() {
                ParsedConst c = constants[index++];
                return c == null ? null : c.constant;
            }
        };
    }

    void parseDynamics() {
        if (dynamics == null) return;
        BootstrapMethods attr = classFile().bootstrapMethods();
        if (!attr.isEmpty()) {
            for (Pair<InvokeDynamicConst, Integer> pair : dynamics) {
                pair.getKey().setMethod(attr.get(pair.getValue()));
            }
        }
    }

    void parse(ByteVector buffer) throws ClassFormatError {
        int i = 1;
        try {
            // first route parsing, parse basic value(int, float, double, long & UTF) and create offset vector
            for (; i < size(); i++) {
                byte tag = buffer.getByte();
                ConstType type = ConstType.fromTag(tag);
                int pos = buffer.position() - 1;
                if (type == null) {
                    throw new ClassFormatError("No constant with tag " + tag
                            + " @" + i
                            + " total " + size()
                            + " position " + Util.toHex(pos));
                }
                switch (type) {
                    case UTF8:
                        set(i, pos, new UTFConst(buffer));
                        break;
                    case INTEGER:
                    case FLOAT:
                        set(i, pos, new ValueConst(this, type, buffer));
                        break;
                    case LONG:
                    case DOUBLE:
                        set(i, pos, new ValueConst(this, type, buffer));
                        i++;        // long number continues
                        break;
                    default:
                        set(i, pos, null);
                        buffer.skip(type.size - 1);
                }
            }

            for (i = 0; i < constants.length - 1; i++) {
                ParsedConst c = constants[i];
                if (!c.parsed && c.constant == null) {
                    try {
                        c.parse(buffer);
                    } catch (RuntimeException | ClassFormatError e) {
                        System.err.println("Failed to parse constant " + c.constant + " @ " + i);
                        System.err.println("Parsed constants:");
                        printConstants();
                        throw e;
                    }
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Exception occurred @" + i
                    + " total " + size()
                    + " position 0x" + Util.toHex(buffer.position()));
            System.err.println("Parsed constants:");
            printConstants();
            throw e;
        }
    }

    private class ParsedConst {

        private final int pos;
        private Const constant;
        private boolean parsed;

        private ParsedConst(int pos, Const constant) {
            this.pos = pos;
            this.constant = constant;
            if (constant instanceof ValueConst) parsed = true;
        }

        private void parse(ByteVector buf) {
            byte tag = buf.getByte(pos);
            ConstType type = ConstType.fromTag(tag);

            if (type == null) {
                throw new ClassFormatError("No constant with tag " + tag
                        + " total " + size()
                        + " position " + Util.toHex(buf.position()));
            }

            Repository repo = classFile().repository();

            switch (type) {
                case CLASS: {
                    String name = getString(buf.getUShort(pos + 1));
                    constant = repo.toType(name);
                    break;
                }
                case STRING: {
                    String content = getString(buf.getUShort(pos + 1));
                    constant = new ValueConst(content);
                    break;
                }
                case FIELDREF:
                case METHODREF:
                case INTERFACE_METHODREF:
                {
                    int index = buf.getUShort(pos + 1);
                    TypeInfo t = (TypeInfo) getAndEnsureParsed(index, buf);
                    NameAndTypeConst pair = (NameAndTypeConst) getAndEnsureParsed(buf.getUShort(pos + 3), buf);

                    if (type == ConstType.FIELDREF) {
                        constant = repo.toField(t, pair.getName(), repo.toType(pair.getDescriptor()));
                    } else if (type == ConstType.METHODREF) {
                        constant = repo.toMethod(t, pair.getName(), repo.resolveMethodDescriptor(pair.getDescriptor()));
                    } else {
                        constant = repo.toInterfaceMethod(t, pair.getName(), repo.resolveMethodDescriptor(pair.getDescriptor()));
                    }
                    break;
                }
                case NAME_AND_TYPE: {
                    String name = getString(buf.getUShort(pos + 1));
                    String desc = getString(buf.getUShort(pos + 3));
                    constant = new NameAndTypeConst(name, desc);
                    break;
                }
                case METHOD_HANDLE: {
                    ReferenceKind kind = ReferenceKind.fromId(buf.getByte(pos + 1));
                    MethodInfo info = (MethodInfo) getAndEnsureParsed(buf.getUShort(pos + 2), buf);
                    constant = new MethodHandleConst(kind, info);
                    break;
                }
                case METHOD_TYPE: {
                    String desc = getString(buf.getUShort(pos + 1));
                    constant = new MethodTypeConst(repo.resolveMethodDescriptor(desc));
                    break;
                }
                case DYNAMIC: {
                    throw new UnsupportedOperationException("Dynamic hasn't been implemented yet!");
                }
                case INVOKE_DYNAMIC: {
                    int index = buf.getUShort(pos + 1);
                    NameAndTypeConst pair = (NameAndTypeConst) getAndEnsureParsed(buf.getUShort(pos + 3), buf);
                    constant = new InvokeDynamicConst(pair);
                    if (dynamics == null) dynamics = new ArrayList<>();
                    dynamics.add(new Pair<>((InvokeDynamicConst) constant, index));
                    break;
                }
                case MODULE: {
                    String name = getString(buf.getUShort(pos + 1));
                    constant = new ModuleConst(name);
                    break;
                }
                case PACKAGE: {
                    String name = getString(buf.getUShort(pos + 1));
                    constant = new PackageConst(name);
                    break;
                }
            }
            parsed = true;
        }
    }
}
