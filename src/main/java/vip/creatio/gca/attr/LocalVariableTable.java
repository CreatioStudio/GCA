package vip.creatio.gca.attr;

import org.jetbrains.annotations.Nullable;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.ConstPool;

import vip.creatio.gca.DeclaredSignature;
import vip.creatio.gca.code.OpCode;
import vip.creatio.gca.util.ByteVector;
import vip.creatio.gca.util.ClassUtil;

/**
 * The LocalVariableTable attribute is an optional variable-length attribute in the
 * attributes table of a Code attribute (§4.7.3). It may be used by debuggers to
 * determine the value of a given local variable during the execution of a method.
 * <br><br>
 *
 * If multiple LocalVariableTable attributes are present in the attributes table
 * of a Code attribute, then they may appear in any order.<br><br>
 *
 * There may be no more than one LocalVariableTable attribute per local variable
 * in the attributes table of a Code attribute.<br><br>
 *
 * The LocalVariableTable attribute has the following format:<br>
 * <pre><code>
 * LocalVariableTable_attribute {
 *     u2 attribute_name_index;
 *     u4 attribute_length;
 *     u2 local_variable_table_length;
 *     {   u2 start_pc;
 *         u2 length;
 *         u2 name_index;
 *         u2 descriptor_index;
 *         u2 index;
 *     } local_variable_table[local_variable_table_length];
 * }
 * </code></pre>
 * <br>
 *
 * The items of the LocalVariableTable_attribute structure are as follows:
 * <ul>
 *     <li>
 *         <b>attribute_name_index</b><br>
 *         The value of the attribute_name_index item must be a valid index
 *         into the constant_pool table. The constant_pool entry at that index
 *         must be a CONSTANT_Utf8_info structure (§4.4.7) representing the
 *         string "LocalVariableTable".
 *     </li>
 *     <li>
 *          <b>attribute_length</b><br>
 *          The value of the attribute_length item indicates the length of the
 *          attribute, excluding the initial six bytes.
 *     </li>
 *     <li>
 *          <b>local_variable_table_length</b><br>
 *          The value of the local_variable_table_length item indicates the number
 *          of entries in the local_variable_table array.
 *     </li>
 *     <li>
 *          <b>local_variable_table[]</b><br>
 *          Each entry in the local_variable_table array indicates a range of code
 *          array offsets within which a local variable has a value, and indicates
 *          the index into the local variable array of the current frame at which
 *          that local variable can be found. Each entry must contain the following
 *          five items:<br>
 *          <ul>
 *              <li>
 *                  <b>start_pc, length</b><br>
 *                  The value of the start_pc item must be a valid index into the
 *                  code array of this Code attribute and must be the index of the
 *                  opcode of an instruction.<br><br>
 *
 *                  The value of start_pc + length must either be a valid index into
 *                  the code array of this Code attribute and be the index of the
 *                  opcode of an instruction, or it must be the first index beyond the
 *                  end of that code array.<br><br>
 *
 *                  The start_pc and length items indicate that the given local variable
 *                  has a value at indices into the code array in the interval
 *                  [start_pc, start_pc + length), that is, between start_pc inclusive
 *                  and start_pc + length exclusive.
 *              </li>
 *              <li>
 *                  <b>name_index</b><br>
 *                  The value of the name_index item must be a valid index into the
 *                  constant_pool table. The constant_pool entry at that index must contain
 *                  a CONSTANT_Utf8_info structure representing a valid unqualified name
 *                  denoting a local variable (§4.2.2).
 *              </li>
 *              <li>
 *                  <b>descriptor_index</b><br>
 *                  The value of the descriptor_index item must be a valid index into the
 *                  constant_pool table. The constant_pool entry at that index must contain
 *                  a CONSTANT_Utf8_info structure representing a field descriptor which
 *                  encodes the type of a local variable in the source program (§4.3.2).
 *              </li>
 *              <li>
 *                  <b>index</b><br>
 *                  The value of the index item must be a valid index into the local
 *                  variable array of the current frame. The given local variable is at
 *                  index in the local variable array of the current frame.<br><br>
 *
 *                  If the given local variable is of type double or long, it occupies both
 *                  index and index + 1.
 *              </li>
 *          </ul>
 *     </li>
 * </ul>
 *
 * @author HenryRenYz
 */
public class LocalVariableTable extends TableAttribute<LocalVariableTable.Variable> {

    private LocalVariableTable(AttributeContainer container) {
        super(container);
    }

    public LocalVariableTable(Code c) {
        this((AttributeContainer) c);
    }

    public static LocalVariableTable parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        LocalVariableTable inst = new LocalVariableTable(container);

        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            inst.items.add(inst.new Variable((Code) container, pool, buffer));
        }
        return inst;
    }

    public void add(OpCode start, OpCode end, String name, String descriptor, int index) {
        items.add(new Variable(start, end, name, descriptor, index));
    }

    public Variable get(String name) {
        for (Variable i : items) {
            if (i.getName().equals(name)) return i;
        }
         return null;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, Code.class);
    }

    @Override
    public String name() {
        return "LocalVariableTable";
    }

    @Override
    protected void collect() {
        super.collect();
        for (Variable item : items) {
            item.collect();
        }
    }

    @Override
    protected void writeData(ByteVector buffer) {
        buffer.putShort((short) items.size());
        for (Variable i : items) {
            i.write(buffer);
        }
    }

    public final class Variable implements DeclaredSignature {

        private OpCode start;
        private OpCode end;
        private String name;
        private String descriptor;
        private int index;

        private @Nullable String signature; // From LocalVariableTypeTable, might be null
        private @Nullable String[] signatures;

        Variable(OpCode start, OpCode end, String name, String descriptor, int index) {
            this.start = start;
            this.end = end;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        Variable(Code c, ClassFileParser pool, ByteVector buffer) {
            int startpc = buffer.getUShort();
            start = c.fromOffset(startpc);
            end = c.fromOffsetNearest(startpc + buffer.getUShort());
            name = pool.getString(buffer.getUShort());
            descriptor = pool.getString(buffer.getUShort());
            index = buffer.getUShort();
        }

        public OpCode getStart() {
            return start;
        }

        public void setStart(OpCode start) {
            this.start = start;
        }

        public OpCode getEnd() {
            return end;
        }

        public void setEnd(OpCode end) {
            this.end = end;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        public @Nullable String getSignature() {
            return signature;
        }

        public void setSignature(@Nullable String signature) {
            this.signature = signature;
            recache();
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private void write(ByteVector buffer) {
            int startPc = start.offset();
            buffer.putShort(startPc);
            buffer.putShort(end.offset() - startPc + end.byteSize());
            buffer.putShort(constPool().acquireUtf(name).index());
            buffer.putShort(constPool().acquireUtf(descriptor).index());
            buffer.putShort(index);
        }

        private void collect() {
            constPool().acquireUtf(name);
            constPool().acquireUtf(descriptor);
            if (signature != null) {
                constPool().acquireUtf(signature);
                container.getOrAddAttribute("LocalVariableTypeTable",
                        () -> new LocalVariableTypeTable((Code) container));
            }
        }

        @Override
        public String toString() {
            return "{start_pc=" + start + ",length=" + end + ",name=" + name + ",descriptor="
                    + descriptor + (signature == null ? "" : ",signature=" + signature) +
                    ",index=" + index + '}';
        }

        @Override
        public @Nullable String[] getSignatures() {
            return signatures;
        }

        @Override
        public void recache() {
            if (signature != null)
                this.signatures = ClassUtil.fromSignature(signature);
        }
    }
}
