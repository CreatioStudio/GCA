package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.code.Label;
import vip.creatio.gca.code.OpCode;
import vip.creatio.gca.code.OpCodeType;
import vip.creatio.gca.constant.Const;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A StackMapTable attribute is used during the process of verification by type checking (§4.10.1).
 *
 * There may be at most one StackMapTable attribute in the attributes table of a Code attribute.
 *
 * In a class file whose version number is 50.0 or above, if a method's Code attribute does not have a StackMapTable
 * attribute, it has an implicit stack map attribute (§4.10.1). This implicit stack map attribute is equivalent to a
 * StackMapTable attribute with number_of_entries equal to zero.
 *
 * The StackMapTable attribute has the following format:
 *
 * StackMapTable_attribute {
 *     u2              attribute_name_index;
 *     u4              attribute_length;
 *     u2              number_of_entries;
 *     stack_map_frame entries[number_of_entries];
 * }
 *
 * The items of the StackMapTable_attribute structure are as follows:
 *
 * attribute_name_index
 *      The value of the attribute_name_index item must be a valid index into the constant_pool table. The constant_pool
 *      entry at that index must be a CONSTANT_Utf8_info structure (§4.4.7) representing the string "StackMapTable".
 *
 * attribute_length
 *      The value of the attribute_length item indicates the length of the attribute, excluding the initial six bytes.
 *
 * number_of_entries
 *      The value of the number_of_entries item gives the number of stack_map_frame entries in the entries table.
 *
 * entries[]
 *      Each entry in the entries table describes one stack map frame of the method. The order of the stack map frames
 *      in the entries table is significant.
 *
 * A stack map frame specifies (either explicitly or implicitly) the bytecode offset at which it applies, and the
 * verification types of local variables and operand stack entries for that offset.
 *
 * Each stack map frame described in the entries table relies on the previous frame for some of its semantics. The
 * first stack map frame of a method is implicit, and computed from the method descriptor by the type checker (§4.10.1.6).
 * The stack_map_frame structure at entries[0] therefore describes the second stack map frame of the method.
 *
 * The bytecode offset at which a stack map frame applies is calculated by taking the value offset_delta specified in
 * the frame (either explicitly or implicitly), and adding offset_delta + 1 to the bytecode offset of the previous frame,
 * unless the previous frame is the initial frame of the method. In that case, the bytecode offset at which the stack
 * map frame applies is the value offset_delta specified in the frame.
 *
 * By using an offset delta rather than storing the actual bytecode offset, we ensure, by definition, that stack map
 * frames are in the correctly sorted order. Furthermore, by consistently using the formula offset_delta + 1 for all
 * explicit frames (as opposed to the implicit first frame), we guarantee the absence of duplicates.
 *
 * We say that an instruction in the bytecode has a corresponding stack map frame if the instruction starts at offset i
 * in the code array of a Code attribute, and the Code attribute has a StackMapTable attribute whose entries array
 * contains a stack map frame that applies at bytecode offset i.
 */
public class StackMapTable extends TableAttribute<StackMapTable.Frame> {

    public StackMapTable(Code c) {
        super(c);
    }

    private StackMapTable(AttributeContainer c) {
        super(c);
    }

    public static StackMapTable parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        StackMapTable inst = new StackMapTable(container);

        int len = buffer.getUShort();
        int[] index = new int[1];
        for (int i = 0; i < len; i++) {
            inst.items.add(inst.new Frame((Code) container, pool, buffer, index));
        }
        return inst;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, Code.class);
    }

    @Override
    public String name() {
        return "StackMapTable";
    }

    @Override
    protected void collect() {
        super.collect();
        for (Frame item : items) {
            item.collect();
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        int[] lastIndex = new int[1];
        for (Frame i : items) {
            i.write(buffer, lastIndex);
        }
    }

    private Code code() {
        return (Code) container;
    }

    public final class Frame {

        private final FrameType type;
        private int k;  // local variable index
        private Label label;  // non-null
        private List<VerificationInfo> stack;
        private List<VerificationInfo> locals;

        Frame(Code c, ClassFileParser pool, ByteVector buffer, int[] lastIndex /* int pointer */ ) {
            int v = buffer.getUByte();
            this.type = FrameType.fromTag(v);
            int offDelta;
            switch (this.type) {
                case SAME:
                    offDelta = v;
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    break;
                case SAME_LOCAL_1_STACK_ITEM:
                    offDelta = v - type.getTagLow();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    this.stack = new ArrayList<>();
                    this.stack.add(new VerificationInfo(c, pool, buffer));
                    break;
                case SAME_LOCAL_1_STACK_ITEM_EX:
                    offDelta = buffer.getUShort();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    this.stack = new ArrayList<>();
                    this.stack.add(new VerificationInfo(c, pool, buffer));
                    break;
                case CHOP:
                    this.k = 251 - v;
                    offDelta = buffer.getUShort();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    break;
                case SAME_EX:
                    offDelta = buffer.getUShort();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    break;
                case APPEND:
                    this.k = v - 251;
                    offDelta = buffer.getUShort();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;
                    this.locals = new ArrayList<>();
                    for (int i = 0; i < k; i++) {
                        locals.add(new VerificationInfo(c, pool, buffer));
                    }
                    break;
                case FULL:
                    offDelta = buffer.getUShort();
                    this.label = c.visitLabel(c.fromOffset(lastIndex[0] + offDelta));
                    lastIndex[0] += offDelta + 1;

                    int num = buffer.getUShort();
                    this.locals = new ArrayList<>();
                    for (int i = 0; i < num; i++) {
                        locals.add(new VerificationInfo(c, pool, buffer));
                    }

                    num = buffer.getUShort();
                    this.stack = new ArrayList<>();
                    for (int i = 0; i < num; i++) {
                        stack.add(new VerificationInfo(c, pool, buffer));
                    }
                    break;
            }
        }

        public FrameType getType() {
            return type;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public Label getLabel() {
            return label;
        }

        // get k
        public int getChopIndex() {
            checkType(FrameType.CHOP);
            return k;
        }

        public void setChopIndex(int k) {
            checkType(FrameType.CHOP);
            this.k = k;
        }

        public List<VerificationInfo> getLocals() {
            checkType(FrameType.APPEND, FrameType.FULL);
            return locals;
        }

        public List<VerificationInfo> getStacks() {
            checkType(FrameType.FULL);
            return stack;
        }

        public VerificationInfo getStack() {
            checkType(FrameType.SAME_LOCAL_1_STACK_ITEM, FrameType.SAME_LOCAL_1_STACK_ITEM_EX);
            return stack.get(0);
        }

        public void setStack(VerificationInfo v) {
            checkType(FrameType.SAME_LOCAL_1_STACK_ITEM, FrameType.SAME_LOCAL_1_STACK_ITEM_EX);
            stack.set(0, v);
        }

        private void write(ByteVector buffer, int[] lastIndex /* int ptr */ ) {
            int offset = label.getOffset();
            int offsetDelta = label.getOffset() - lastIndex[0];
            lastIndex[0] = offset + 1;

            switch (type) {
                case SAME:
                    if (offsetDelta <  64) {
                        buffer.putByte(offsetDelta);
                        break;
                    }
                case SAME_EX:
                    buffer.putByte(type.getTagLow());
                    buffer.putShort(offsetDelta);
                    break;
                case SAME_LOCAL_1_STACK_ITEM:
                    if (offsetDelta < 64) {
                        buffer.putByte(type.getTagLow() + offsetDelta);
                        stack.get(0).write(buffer);
                        break;
                    }
                case SAME_LOCAL_1_STACK_ITEM_EX:
                    buffer.putByte(type.getTagLow());
                    buffer.putShort(offsetDelta);
                    stack.get(0).write(buffer);
                    break;
                case CHOP:
                    buffer.putByte(251 - k);
                    buffer.putShort(offsetDelta);
                    break;
                case APPEND:
                    buffer.putByte(251 + k);
                    buffer.putShort(offsetDelta);
                    for (int i = 0; i < k; i++) {
                        locals.get(i).write(buffer);
                    }
                    break;
                case FULL:
                    buffer.putByte(type.getTagLow());
                    buffer.putShort(offsetDelta);
                    buffer.putShort(locals.size());
                    for (VerificationInfo l : locals) {
                        l.write(buffer);
                    }
                    buffer.putShort(stack.size());
                    for (VerificationInfo s : stack) {
                        s.write(buffer);
                    }
                    break;
            }
        }

        private void collect() {
            if (locals != null) {
                for (VerificationInfo l : locals) {
                    l.collect();
                }
            }
            if (stack != null) {
                for (VerificationInfo s : stack) {
                    s.collect();
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(type.toString());
            sb.append("{offset=").append(label.getOffset());
            switch (type) {
                case SAME_LOCAL_1_STACK_ITEM:
                case SAME_LOCAL_1_STACK_ITEM_EX:
                    sb.append(",stack=").append(getStack());
                    break;
                case CHOP:
                    sb.append(",chop_index=").append(k);
                    break;
                case APPEND:
                    sb.append(",append=").append(getLocals());
                    break;
                case FULL:
                    sb.append(",locals=").append(getLocals());
                    sb.append(",stacks=").append(getStacks());
                    break;
            }
            sb.append('}');

            return sb.toString();
        }

        private void checkType(FrameType type) {
            if (this.type != type) {
                throw new UnsupportedOperationException("Only frame with type " + type + " can do this operation!");
            }
        }

        private void checkType(FrameType type1, FrameType type2) {
            if (this.type != type1 && this.type != type2) {
                throw new UnsupportedOperationException("Only frame with type " + type1 + " and " + type2 + " can do this operation!");
            }
        }
    }

    public final class VerificationInfo {
        private final VarInfo type;
        private Object data;

        VerificationInfo(Code c, ClassFileParser pool, ByteVector buffer) {
            this.type = VarInfo.fromTag(buffer.getUByte());
            if (this.type == VarInfo.OBJECT) {
                this.data = pool.get(buffer.getUShort());
            } else if (this.type == VarInfo.UNINIT_VAR) {
                this.data = c.fromOffset(buffer.getUShort()); // new operator
                assert data != null;
                if (((OpCode) data).type() != OpCodeType.NEW) throw new ClassFormatError("should be a NEW opcode here");
            }
        }

        public VarInfo getType() {
            return type;
        }

        public Const getConstant() {
            if (type != VarInfo.OBJECT)
                throw new UnsupportedOperationException("This operation can only be performed on Object_var_info");
            return (Const) data;
        }

        public void setConstant(Const c) {
            if (type != VarInfo.OBJECT)
                throw new UnsupportedOperationException("This operation can only be performed on Object_var_info");
            data = c;
        }

        public OpCode getOpCode() {
            if (type != VarInfo.OBJECT)
                throw new UnsupportedOperationException("This operation can only be performed on Uninitialized_var_info");
            return (OpCode) data;
        }

        public void setConstant(OpCode c) {
            if (type != VarInfo.UNINIT_VAR)
                throw new UnsupportedOperationException("This operation can only be performed on Uninitialized_var_info");
            data = c;
        }

        private void write(ByteVector buffer) {
            buffer.putByte(type.getTag());
            if (type == VarInfo.OBJECT) {
                buffer.putShort(((Const) data).index());
            } else if (type == VarInfo.UNINIT_VAR) {
                buffer.putShort(((OpCode) data).offset());
            }
        }

        private void collect() {
            if (type == VarInfo.OBJECT) {
                constPool().acquire((Const) data);
            }
        }
    }

    public enum VarInfo {

        /**
         * The Top_variable_info item indicates that the local variable has the verification type top.
         */
        TOP(0),

        /**
         * The Integer_variable_info item indicates that the location has the verification type int.
         */
        INT(1),

        /**
         * The Float_variable_info item indicates that the location has the verification type float.
         */
        FLOAT(2),

        /**
         * The Null_variable_info type indicates that the location has the verification type null.
         */
        NULL(5),

        /**
         * The UninitializedThis_variable_info item indicates that the location has the verification type
         * uninitializedThis.
         */
        UNINIT_THIS(6),

        /**
         * The Object_variable_info item indicates that the location has the verification type which is the class
         * represented by the CONSTANT_Class_info structure (§4.4.1) found in the constant_pool table at the index given
         * by cpool_index.
         *
         * Object_variable_info {
         *     u1 tag = ITEM_Object; // 7
         *     u2 cpool_index;
         * }
         */
        OBJECT(7),

        /**
         * The Uninitialized_variable_info item indicates that the location has the verification type
         * uninitialized(Offset). The Offset item indicates the offset, in the code array of the Code attribute that
         * contains this StackMapTable attribute, of the new instruction (§new) that created the object being stored
         * in the location.
         *
         * Uninitialized_variable_info {
         *     u1 tag = ITEM_Uninitialized; // 8
         *     u2 offset;
         * }
         */
        UNINIT_VAR(8),

        /*
        A verification type that specifies two locations in the local variable array or in the operand stack is
        represented by the following items of the verification_type_info union

        The Long_variable_info and Double_variable_info items indicate the verification type of the second of two
        locations as follows:

            If the first of the two locations is a local variable, then:
                It must not be the local variable with the highest index.
                The next higher numbered local variable has the verification type top.

            If the first of the two locations is an operand stack entry, then:
                It must not be the topmost location of the operand stack.
                The next location closer to the top of the operand stack has the verification type top.
         */

        /**
         *The Double_variable_info item indicates that the first of two locations has the verification type double.
         */
        DOUBLE(3),

        /**
         * The Long_variable_info item indicates that the first of two locations has the verification type long.
         */
        LONG(4);

        private final int tag;

        VarInfo(int tag) {
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }

        public static VarInfo fromTag(int tag) {
            for (VarInfo v : values()) {
                if (v.tag == tag) return v;
            }
            throw new NoSuchElementException("No constant found with tag " + tag);
        }
    }

    public enum FrameType {

        /**
         * The frame type same_frame is represented by tags in the range [0-63]. This frame type indicates that the
         * frame has exactly the same local variables as the previous frame and that the operand stack is empty.
         * The offset_delta value for the frame is the value of the tag item, frame_type.
         *
         * same_frame {
         *     u1 frame_type = SAME; // 0-63
         * }
         */
        SAME(0, 63),

        /**
         * The frame type same_locals_1_stack_item_frame is represented by tags in the range [64, 127]. This frame
         * type indicates that the frame has exactly the same local variables as the previous frame and that the
         * operand stack has one entry. The offset_delta value for the frame is given by the formula frame_type - 64.
         * The verification type of the one stack entry appears after the frame type.
         *
         * same_locals_1_stack_item_frame {
         *     u1 frame_type = SAME_LOCALS_1_STACK_ITEM; // 64-127
         *     verification_type_info stack[1];
         * }
         */
        SAME_LOCAL_1_STACK_ITEM(64, 127),

        //Tags in the range [128-246] are reserved for future use.

        /**
         * The frame type same_locals_1_stack_item_frame_extended is represented by the tag 247. This frame type
         * indicates that the frame has exactly the same local variables as the previous frame and that the operand
         * stack has one entry. The offset_delta value for the frame is given explicitly, unlike in the frame type
         * same_locals_1_stack_item_frame. The verification type of the one stack entry appears after offset_delta.
         *
         * same_locals_1_stack_item_frame_extended {
         *     u1 frame_type = SAME_LOCALS_1_STACK_ITEM_EXTENDED; // 247
         *     u2 offset_delta;
         *     verification_type_info stack[1];
         * }
         */
        SAME_LOCAL_1_STACK_ITEM_EX(247),

        /**
         * The frame type chop_frame is represented by tags in the range [248-250]. This frame type indicates that the
         * frame has the same local variables as the previous frame except that the last k local variables are absent,
         * and that the operand stack is empty. The value of k is given by the formula 251 - frame_type. The
         * offset_delta value for the frame is given explicitly.
         *
         * chop_frame {
         *     u1 frame_type = CHOP; // 248-250
         *     u2 offset_delta;
         * }
         *
         * Assume the verification types of local variables in the previous frame are given by locals, an array
         * structured as in the full_frame frame type. If locals[M-1] in the previous frame represented local
         * variable X and locals[M] represented local variable Y, then the effect of removing one local variable is
         * that locals[M-1] in the new frame represents local variable X and locals[M] is undefined.
         *
         * It is an error if k is larger than the number of local variables in locals for the previous frame, that is,
         * if the number of local variables in the new frame would be less than zero.
         */
        CHOP(248, 250),

        /**
         * The frame type same_frame_extended is represented by the tag 251. This frame type indicates that the frame
         * has exactly the same local variables as the previous frame and that the operand stack is empty. The
         * offset_delta value for the frame is given explicitly, unlike in the frame type same_frame.
         *
         * same_frame_extended {
         *     u1 frame_type = SAME_FRAME_EXTENDED; // 251
         *     u2 offset_delta;
         * }
         */
        SAME_EX(251),

        /**
         * The frame type append_frame is represented by tags in the range [252-254]. This frame type indicates that
         * the frame has the same locals as the previous frame except that k additional locals are defined, and that
         * the operand stack is empty. The value of k is given by the formula frame_type - 251. The offset_delta value
         * for the frame is given explicitly.
         *
         * append_frame {
         *     u1 frame_type = APPEND; // 252-254
         *     u2 offset_delta;
         *     verification_type_info locals[frame_type -251];
         * }
         *
         * The 0th entry in locals represents the verification type of the first additional local variable. If locals[M]
         * represents local variable N, then:
         *
         * locals[M+1] represents local variable N+1 if locals[M] is one of Top_variable_info, Integer_variable_info,
         * Float_variable_info, Null_variable_info, UninitializedThis_variable_info, Object_variable_info, or
         * Uninitialized_variable_info; and
         *
         * locals[M+1] represents local variable N+2 if locals[M] is either Long_variable_info or Double_variable_info.
         *
         * It is an error if, for any index i, locals[i] represents a local variable whose index is greater than the
         * maximum number of local variables for the method.
         */
        APPEND(252, 254),

        /**
         * The frame type full_frame is represented by the tag 255. The offset_delta value for the frame is given
         * explicitly.
         *
         * full_frame {
         *     u1 frame_type = FULL_FRAME; // 255
         *     u2 offset_delta;
         *     u2 number_of_locals;
         *     verification_type_info locals[
         *     number_of_locals];
         *     u2 number_of_stack_items;
         *     verification_type_info stack[
         *     number_of_stack_items];
         * }
         *
         * The 0th entry in locals represents the verification type of local variable 0. If locals[M] represents local
         * variable N, then:
         *
         * locals[M+1] represents local variable N+1 if locals[M] is one of Top_variable_info, Integer_variable_info,
         * Float_variable_info, Null_variable_info, UninitializedThis_variable_info, Object_variable_info, or
         * Uninitialized_variable_info; and
         *
         * locals[M+1] represents local variable N+2 if locals[M] is either Long_variable_info or Double_variable_info.
         *
         * It is an error if, for any index i, locals[i] represents a local variable whose index is greater than the
         * maximum number of local variables for the method.
         *
         * The 0th entry in stack represents the verification type of the bottom of the operand stack, and subsequent
         * entries in stack represent the verification types of stack entries closer to the top of the operand stack.
         * We refer to the bottom of the operand stack as stack entry 0, and to subsequent entries of the operand stack
         * as stack entry 1, 2, etc. If stack[M] represents stack entry N, then:
         *
         * stack[M+1] represents stack entry N+1 if stack[M] is one of Top_variable_info, Integer_variable_info,
         * Float_variable_info, Null_variable_info, UninitializedThis_variable_info, Object_variable_info, or
         * Uninitialized_variable_info; and
         *
         * stack[M+1] represents stack entry N+2 if stack[M] is either Long_variable_info or Double_variable_info.
         *
         * It is an error if, for any index i, stack[i] represents a stack entry whose index is greater than the
         * maximum operand stack size for the method.
         */
        FULL(255);

        private final int tagLow;
        private final int tagHigh;

        FrameType(int low, int high) {
            this.tagLow = low;
            this.tagHigh = high;
        }

        FrameType(int tag) {
            this(tag, tag);
        }

        public int getTagLow() {
            return tagLow;
        }

        public int getTagHigh() {
            return tagHigh;
        }

        public static FrameType fromTag(int i) {
            for (FrameType v : values()) {
                if (i >= v.tagLow && i <= v.tagHigh) return v;
            }
            throw new NoSuchElementException("No constant found with tag " + i);
        }
    }
}
