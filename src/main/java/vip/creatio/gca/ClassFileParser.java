package vip.creatio.gca;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.attr.AttributeResolver;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.constant.ConstPoolImpl;
import vip.creatio.gca.constant.ConstType;
import vip.creatio.gca.constant.UTFConst;

import vip.creatio.gca.util.ByteVector;

import java.util.*;

public class ClassFileParser implements Iterable<Const> {

    private final Map<String, AttributeResolver> resolvers = Attribute.getResolvers();
    private final ParsedConst[] constants;
    ClassFile file;
    ConstPool pool;

    ClassFileParser(ClassFile file, int size, @Nullable Map<String, AttributeResolver> resolvers) {
        this.constants = new ParsedConst[size];
        this.file = file;
        this.pool = file.constPool();
        if (resolvers != null) this.resolvers.putAll(resolvers);
    }

    public Const get(int index) {
        if (index == 0) return null;
        return constants[(index & 0xFFFF) - 1].constant;
    }

    public String getString(int index) {
        return ((UTFConst) get(index)).string();
    }

    void set(int index, int pos, Const c) {
        constants[(index & 0xFFFF) - 1] = new ParsedConst(pos, c);
    }

    public ConstPool getPool() {
        return pool;
    }

    public int size() {
        return constants.length;
    }

    public AttributeResolver getResolver(String name) {
        for (Map.Entry<String, AttributeResolver> entry : resolvers.entrySet()) {
            if (entry.getKey().equals(name)) return entry.getValue();
        }
        return null;
    }

    public Attribute resolveAttribute(ByteVector buffer) {
        String name = getString(buffer.getShort());
        AttributeResolver resolver = getResolver(name);
        int length = buffer.getInt();
        if (resolver == null) {
            System.err.println("Debug: No attribute resolver found with name " + name);
            byte[] data = new byte[length];
            buffer.getBytes(data, 0, length);
            return new Attribute.Undefined(file, name, data);
        }

        return resolver.provide(file, this, buffer);
    }

    void parse(ByteVector buffer) {
        Set<String> local = new HashSet<>();
        for (int i = 0; i < constants.length; i++) {
            ParsedConst c = constants[i];
            if (c != null) {
                if (c.constant instanceof UTFConst) {
                    local.add(((UTFConst) c.constant).string());
                }
                try {
                    c.parse(buffer);
                } catch (RuntimeException e) {
                    System.err.println("Failed to parse constant " + c.constant + " @ " + i);
                    System.err.println("Parsed constants:");
                    printConstants();
                    throw e;
                }
            }
        }
        for (ParsedConst c : constants) {
            if (c != null && !c.constant.isImmutable()) {
                pool.add(c.constant);
            }
        }

        if (testSet.isEmpty()) {
            testSet.addAll(local);
        } else {
            testSet.removeAll(local);
        }
    }

    public static Set<String> testSet = new HashSet<>();

    void printConstants() {
        int j = 1;
        try {
            for (ParsedConst cc : constants) {
                if (cc != null) {
                    System.err.println("   " + j++ + ". " + cc.constant);
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
                return index == constants.length - 1;
            }

            @Override
            public Const next() {
                ParsedConst c = constants[index++];
                return c == null ? null : c.constant;
            }
        };
    }

    private class ParsedConst {

        private final int pos;
        private final Const constant;

        private ParsedConst(int pos, Const constant) {
            this.pos = pos;
            this.constant = constant;
        }

        private void parse(ByteVector buf) {
            buf.position(pos);
            ConstPoolImpl.parseConstant(ClassFileParser.this, constant, buf);
        }
    }
}
