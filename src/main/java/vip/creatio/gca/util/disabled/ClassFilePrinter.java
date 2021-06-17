package vip.creatio.gca.util;

import vip.creatio.gca.*;

import java.io.PrintStream;
import java.util.EnumSet;
import java.util.Iterator;

public final class ClassFilePrinter {

    private static PrintStream out = System.out;

    public static void setPrintStream(PrintStream stream) {
        out = stream;
    }

    public static void print(ClassFile f) {
        f.collect();
        out.println("Classfile " + f.getClass().getName() + "@" + Integer.toHexString(f.hashCode()));
        {
            String sf = f.getSourceFile();
            if (sf != null) out.println("  Compiled from \"" + sf + "\"");
        }
        out.println("  " + getClassDeclaration(f));
        out.println("  Minor version: " + f.getMinorVer());
        out.println("  Major version: " + f.getMajorVer());
        out.println("  Access  flags: " + getAccessFlags(f.getAccessFlags()));
        {
            String s = "#" + f.getThisClass().index();
            out.println("  This    class: " + s + " ".repeat(40 - 15 - s.length())
                    + "// " + f.getThisClass().getBytecodeName());
            s = "#" + f.getSuperclass().index();
            out.println("  Super   class: " + s + " ".repeat(40 - 15 - s.length())
                    + "// " + f.getSuperclass().getBytecodeName());
        }
        out.println("  Interfaces: " + f.getInterfaces().size() +
                ", Fields: " + f.getFields().size() +
                ", Methods: " + f.getMethods().size() +
                ", Attributes: " + f.getAttributes().size());
        out.println("\nConstant pool:");
        for (Const constant : f.constPool()) {
            out.println(getConstant(f.constPool(), constant));
        }
        out.println("\nFields:");
        out.println("\nMethods:");
    }

    public static String getClassDeclaration(ClassFile f) {
        StringBuilder sb = new StringBuilder();
        sb.append(AccessFlag.toString(f.getAccessFlags()));
        if (f.flaggedAnnotation()) {
            sb.append(" @interface ");
            sb.append(f.getTypeName());
        } else if (f.flaggedInterface()) {
            sb.append(" interface ");
            sb.append(f.getTypeName());
            if (f.getInterfaces().size() != 0) {
                sb.append(" extends ");
                Iterator<ClassConst> iter = f.getInterfaces().iterator();
                while (iter.hasNext()) {
                    sb.append(iter.next().getTypeName());
                    if (iter.hasNext()) sb.append(", ");
                }
            }
        } else {
            if (f.flaggedEnum()) {
                sb.append(" enum ");
            } else {
                sb.append(" class ");
            }
            sb.append(f.getTypeName());
            if (!f.getSuperclass().getTypeName().equals("java.lang.Object")) {
                sb.append(" extends ");
                sb.append(f.getSuperclass().getTypeName());
            }
            if (f.getInterfaces().size() != 0) {
                sb.append(" implements ");
                Iterator<ClassConst> iter = f.getInterfaces().iterator();
                while (iter.hasNext()) {
                    sb.append(iter.next().getTypeName());
                    if (iter.hasNext()) sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    private static String getAccessFlags(EnumSet<AccessFlag> flags) {
        StringBuilder sb = new StringBuilder();
        sb.append("(0x").append(Util.toHex(AccessFlag.serialize(flags))).append(") ");
        Iterator<AccessFlag> iter = flags.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next().name());
            if (iter.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    private static String getConstant(ConstPool pool, Const c) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(c.index());
        sb.append(" ".repeat(6 - sb.length())).append(" = ").append(c.constantType());
        sb.append(" ".repeat(28 - sb.length()));
        switch (c.constantType()) {
            case UTF8:
                sb.append("'").append(((UTFConst) c).string()).append("'");
                break;
            case INTEGER:
                sb.append(((IntegerConst) c).getInt());
                break;
            case FLOAT:
                sb.append(((FloatConst) c).getFloat()).append("F");
                break;
            case LONG:
                sb.append(((LongConst) c).getLong()).append("L");
                break;
            case DOUBLE:
                sb.append(((DoubleConst) c).getDouble()).append("D");
                break;
            case CLASS:
                sb.append("#").append(pool.acquireUtf(((ClassConst) c).getBytecodeName()).index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(((ClassConst) c).getBytecodeName());
                break;
            case STRING:
                sb.append("#").append(pool.acquireUtf(((StringConst) c).getString()).index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(((StringConst) c).getString());
                break;
            case FIELDREF:
            case INTERFACE_METHODREF:
            case METHODREF: {
                NameAndTypeConst pair = pool.acquireNameAndType(((RefConst) c).getName(), ((RefConst) c).getDescriptor());
                sb.append("#").append(((RefConst) c).getDeclaringClass().index())
                        .append(":#")
                        .append(pair.index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(((RefConst) c).getDeclaringClass().getBytecodeName())
                        .append(".").append(pair.getName()).append(":").append(pair.getDescriptor());
                break;
            }

            case NAME_AND_TYPE: {
                NameAndTypeConst pair = (NameAndTypeConst) c;
                sb.append("#").append(pool.acquireUtf(pair.getName()).index())
                        .append(":#")
                        .append(pool.acquireUtf(pair.getDescriptor()).index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(pair.getName())
                        .append(":").append(pair.getDescriptor());
                break;
            }
            case METHOD_HANDLE: {
                MethodHandleConst handle = (MethodHandleConst) c;
                sb.append(handle.getKind().getId()).append(":#")
                        .append(handle.getRef().index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(handle.getRef())
                        .append(" ").append(handle.getRef().getDeclaringClass().getBytecodeName())
                        .append(".").append(handle.getRef().getName()).append(":")
                        .append(handle.getRef().getDescriptor());
                break;
            }
            case METHOD_TYPE: {
                MethodTypeConst type = (MethodTypeConst) c;
                sb.append("#").append(pool.acquireUtf(type.getDescriptor()).index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// ").append(type.getDescriptor());
                break;
            }
            case INVOKE_DYNAMIC: {
                InvokeDynamicConst inv = (InvokeDynamicConst) c;
                NameAndTypeConst pair = pool.acquireNameAndType(inv.getMethodName(), inv.getDescriptor());
                sb.append("#").append(inv.getMethod().index())
                        .append(":#")
                        .append(pair.index())
                        .append(" ".repeat(42 - sb.length()))
                        .append("// #").append(inv.getMethod().index())
                        .append(" : ").append(inv.getMethod().getRef().getRef().getDeclaringClass().getBytecodeName())
                        .append(".").append(pair.getName()).append(":").append(pair.getDescriptor());
                break;
            }
            case MODULE:
                break;
            case PACKAGE:
                break;
        }
        return sb.toString();
    }
}
