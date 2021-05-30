package vip.creatio.gca.attr;

import vip.creatio.gca.*;
import vip.creatio.gca.code.OpCode;
import vip.creatio.gca.constant.ClassConst;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypeAnnotation extends AbstractAnnotation {

    private TargetType target;

    /*
     *
     * union {
     *     int                  index;              // type_parameter, formal_parameter
     *     struct {                                 // type_parameter_bound
     *         int                  index;
     *         int                  boundIndex;
     *     };
     *     ClassConst           superType;          // supertype(ClassFile::interfaces.index)
     *     void                 empty_target;
     *     Exceptions           exceptions;         // throws
     *     List<VarTable>       localVar;           // localvar
     *     Code.ExceptionTable  catchTarget;        // catch
     *     OpCode               offset;             // offset
     *     struct {                                 // type_argument
     *         OpCode               offset;
     *         int                  typeArgumentIndex;
     *     };
     * } target_info;
     *
     */

    // type_parameter(index), type_parameter_bound(index), formal_parameter(index)
    private int index;

    // type_parameter_bound(index?)
    private int boundIndex;

    // supertype(ClassFile::interfaces.index)
    private ClassConst superType;

    // throws(Exceptions)
    private Exceptions exceptions;
    private ClassConst throwsType;

    // catch(Code::ExceptionTable)
    private Code.ExceptionTable catchTarget;

    // offset, type_argument
    private OpCode offset;

    // type_argument
    private int typeArgumentIndex;

    // localvar
    private List<VarTable> localVar;

    private final List<Pair<PathKind, Integer>> path = new ArrayList<>();

    private String type;

    private final List<Pair<String, ElementValue>> nameValuePairs = new ArrayList<>();

    TypeAnnotation(ClassFile file) {
        super(file);
    }

    public TypeAnnotation(ClassFile file, String type) {
        this(file);
        this.type = type;
    }

    static TypeAnnotation parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        //assert container instanceof Code;
        TypeAnnotation anno = new TypeAnnotation(container.classFile());
        anno.target = TargetType.fromTag(buffer.getByte());

        // read union "target_info"
        switch (anno.target.getType()) {
            case TargetType.TARGET_TYPE_PARAMETER:
                anno.index = buffer.getUByte();
                break;
            case TargetType.TARGET_SUPERTYPE:
                assert container instanceof ClassFile;
                anno.superType = ((ClassFile) container).getInterfaces().get(buffer.getUShort());
                break;
            case TargetType.TARGET_TYPE_PARAMETER_BOUND:
                anno.index = buffer.getUByte();
                anno.boundIndex = buffer.getUByte();
                break;
            case TargetType.TARGET_EMPTY:
                // no contents
                break;
            case TargetType.TARGET_FORMAL_PARAMETER:
                anno.index = buffer.getUByte();
                break;
            case TargetType.TARGET_THROWS:
                assert container instanceof DeclaredMethod;
                anno.throwsType = ((DeclaredMethod) container).exceptions().get(buffer.getUShort());
                break;
            case TargetType.TARGET_LOCALVAR:
                assert container instanceof Code;
                int len = buffer.getUShort();
                anno.localVar = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    anno.localVar.add(new VarTable((Code) container, buffer));
                }
                break;
            case TargetType.TARGET_CATCH:
                assert container instanceof Code;
                anno.catchTarget = ((Code) container).exceptionTables().get(buffer.getUShort());
                break;
            case TargetType.TARGET_OFFSET:
                assert container instanceof Code;
                anno.offset = ((Code) container).fromOffset(buffer.getUShort());
                break;
            case TargetType.TARGET_TYPE_ARGUMENT:
                assert container instanceof Code;
                anno.offset = ((Code) container).fromOffset(buffer.getUShort());
                anno.typeArgumentIndex = buffer.getUByte();
                break;
            default:
                throw new ClassFormatError("Unknown target type: " + anno.target.getType());
        }

        {       // parse target_path
            int len = buffer.getUByte();
            for (int i = 0; i < len; i++) {
                anno.path.add(new Pair<>(PathKind.fromTag(buffer.getUByte()), buffer.getUByte()));
            }
        }

        anno.type = pool.getString(buffer.getUShort());

        {
            int num = buffer.getUShort();
            for (int i = 0; i < num; i++) {
                String name = pool.getString(buffer.getUShort());
                anno.nameValuePairs.add(new Pair<>(name, new ElementValue(container.classFile(), pool, buffer)));
            }
        }
        return anno;
    }

    public String getClassName() {
        return type;
    }

    public ElementValue addValue(String name, ValueType type) {
        ElementValue value = new ElementValue(constPool(), type);
        nameValuePairs.add(new Pair<>(name, value));
        return value;
    }

    public ElementValue getValue(String name) {
        for (Pair<String, ElementValue> p : nameValuePairs) {
            if (p.getKey().equals(name)) return p.getValue();
        }
        return null;
    }

    public boolean removeValue(String name) {
        return nameValuePairs.removeIf(p -> p.getKey().equals(name));
    }

    public ElementValue addConstValue(String name, Const.Value value) {
        ElementValue v = new ElementValue(constPool(), value.valueType());
        v.union = value;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addClassValue(String name, String clsName) {
        ElementValue v = new ElementValue(constPool(), ValueType.CLASS);
        v.union = clsName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addEnumValue(String name, String enumClass, String enumName) {
        ElementValue v = new ElementValue(constPool(), ValueType.ENUM);
        v.union = enumClass;
        v.enumName = enumName;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addAnnotationValue(String name, TypeAnnotation anno) {
        ElementValue v = new ElementValue(constPool(), ValueType.ANNOTATION);
        v.union = anno;
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addArrayValue(String name, Collection<ElementValue> values) {
        ElementValue v = new ElementValue(constPool(), ValueType.ARRAY);
        v.union = new ArrayList<>(values);
        nameValuePairs.add(new Pair<>(name, v));
        return v;
    }

    public ElementValue addArrayValue(String name, ElementValue... values) {
        return addArrayValue(name, Arrays.asList(values));
    }


    public int getTypeParameterIndex() {
        checkType(TargetType.TARGET_TYPE_PARAMETER, TargetType.TARGET_TYPE_PARAMETER_BOUND);
        return index;
    }

    public void setTypeParameterIndex(int index) {
        checkType(TargetType.TARGET_TYPE_PARAMETER, TargetType.TARGET_TYPE_PARAMETER_BOUND);
        this.index = index;
    }

    public ClassConst getSuperType() {
        checkType(TargetType.TARGET_SUPERTYPE);
        return superType;
    }

    public void setSuperType(ClassConst type) {
        checkType(TargetType.TARGET_SUPERTYPE);
        this.superType = type;
    }

    public int getBoundIndex() {
        checkType(TargetType.TARGET_TYPE_PARAMETER_BOUND);
        return boundIndex;
    }

    public void setBoundIndex(int index) {
        checkType(TargetType.TARGET_TYPE_PARAMETER_BOUND);
        this.boundIndex = index;
    }

    public int getFormatParameterIndex() {
        checkType(TargetType.TARGET_FORMAL_PARAMETER);
        return index;
    }

    public void setFormatParameterIndex(int index) {
        checkType(TargetType.TARGET_FORMAL_PARAMETER);
        this.index = index;
    }

    public ClassConst getThrowsType() {
        checkType(TargetType.TARGET_THROWS);
        return throwsType;
    }

    public void setThrowsType(ClassConst type) {
        checkType(TargetType.TARGET_THROWS);
        this.throwsType = type;
    }

    //TODO: requires enhancements
    public List<VarTable> getLocalVar() {
        checkType(TargetType.TARGET_LOCALVAR);
        return localVar;
    }

    public Code.ExceptionTable getExceptionTable() {
        checkType(TargetType.TARGET_CATCH);
        return catchTarget;
    }

    public void setExceptionTable(Code.ExceptionTable table) {
        checkType(TargetType.TARGET_CATCH);
        this.catchTarget = table;
    }

    public OpCode getOffsetCode() {
        checkType(TargetType.TARGET_OFFSET, TargetType.TARGET_TYPE_ARGUMENT);
        return offset;
    }

    public void setOffsetCode(OpCode code) {
        checkType(TargetType.TARGET_OFFSET, TargetType.TARGET_TYPE_ARGUMENT);
        this.offset = code;
    }

    public int getTypeArgumentIndex() {
        checkType(TargetType.TARGET_TYPE_ARGUMENT);
        return typeArgumentIndex;
    }

    public void setTypeArgumentIndex(int index) {
        checkType(TargetType.TARGET_TYPE_ARGUMENT);
        this.typeArgumentIndex = index;
    }

    private void checkType(int type) {
        if (this.target.getType() != type) {
            throw new IllegalArgumentException("Only type annotation with target type " + type + " can do this action");
        }
    }

    private void checkType(int type, int type2) {
        if (this.target.getType() != type && this.target.getType() != type2) {
            throw new IllegalArgumentException("Only type annotation with target type " + type + " can do this action");
        }
    }

    void write(ByteVector buffer) {
        buffer.putByte(target.getTag());
        switch (target.getType()) {
            case TargetType.TARGET_TYPE_PARAMETER:
                buffer.putByte(index);
                break;
            case TargetType.TARGET_SUPERTYPE:
                buffer.putShort(superType.index());
                break;
            case TargetType.TARGET_TYPE_PARAMETER_BOUND:
                buffer.putByte(index);
                buffer.putByte(boundIndex);
                break;
            case TargetType.TARGET_EMPTY:
                // no contents
                break;
            case TargetType.TARGET_FORMAL_PARAMETER:
                buffer.putByte(index);
                break;
            case TargetType.TARGET_THROWS:
                buffer.putShort(throwsType.index());
                break;
            case TargetType.TARGET_LOCALVAR:
                buffer.putShort(localVar.size());
                for (VarTable v : localVar) {
                    v.write(buffer);
                }
                break;
            case TargetType.TARGET_CATCH:
                buffer.putShort(catchTarget.getIndex());
                break;
            case TargetType.TARGET_OFFSET:
                buffer.putShort(offset.offset());
                break;
            case TargetType.TARGET_TYPE_ARGUMENT:
                buffer.putShort(offset.offset());
                buffer.putByte(typeArgumentIndex);
                break;
            default:
                throw new ClassFormatError("Unknown target type: " + target.getType());
        }

        buffer.putByte(path.size());

        for (Pair<PathKind, Integer> pair : path) {
            buffer.putByte(pair.getKey().getTag());
            buffer.putByte(pair.getValue());
        }

        buffer.putShort(constPool().acquireUtf(type).index());
        buffer.putShort(nameValuePairs.size());
        for (Pair<String, ElementValue> p : nameValuePairs) {
            buffer.putShort(constPool().acquireUtf(p.getKey()).index());
            p.getValue().write(buffer);
        }
    }

    void collect() {
        switch (target.getType()) {
            case TargetType.TARGET_SUPERTYPE:
            case TargetType.TARGET_TYPE_PARAMETER_BOUND:
                constPool().acquire(superType);
                break;
            case TargetType.TARGET_THROWS:
                constPool().acquire(throwsType);
                break;
        }
        constPool().acquireUtf(type);
        for (Pair<String, ElementValue> p : nameValuePairs) {
            constPool().acquireUtf(p.getKey());
            p.getValue().collect();
        }
    }

    public static class VarTable {
        private OpCode startPc;
        private int length;
        private int index;      // Local var table

        VarTable(OpCode startPc, int length, int index) {
            this.startPc = startPc;
            this.length = length;
            this.index = index;
        }

        VarTable(Code container, ByteVector vec) {
            this.startPc = container.fromOffset(vec.getUShort());
            this.length = vec.getUShort();
            this.index = vec.getUShort();
        }

        public OpCode getStartPc() {
            return startPc;
        }

        public void setStartPc(OpCode startPc) {
            this.startPc = startPc;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private void write(ByteVector buffer) {
            buffer.putShort(startPc.offset());
            buffer.putShort(length);
            buffer.putShort(index);
        }
    }

    public enum PathKind {
        ARRAY(0),          // Annotation is deeper in an array type
        NESTED(1),         // Annotation is deeper in a nested type
        WILDCARD(2),       // Annotation is on the bound of a wildcard type argument of a parameterized type
        TYPE_ARG(3);       // Annotation is on a type argument of a parameterized type

        private final int tag;

        PathKind(int tag) {
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }

        public static PathKind fromTag(int tag) {
            for (PathKind v : values()) {
                if (v.tag == tag) return v;
            }
            throw new RuntimeException("No such kind with tag " + tag);
        }
    }
}
