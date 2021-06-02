package vip.creatio.gca.attr;

import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFileParser;
import vip.creatio.gca.util.ByteVector;

import java.util.ArrayList;
import java.util.List;

/**
 * The LocalVariableTypeTable attribute is an optional variable-length attribute in
 * the attributes table of a Code attribute (§4.7.3). It may be used by debuggers
 * to determine the value of a given local variable during the execution of a method.
 * <br><br>
 *
 * If multiple LocalVariableTypeTable attributes are present in the attributes table
 * of a given Code attribute, then they may appear in any order.<br><br>
 *
 * There may be no more than one LocalVariableTypeTable attribute per local variable
 * in the attributes table of a Code attribute.<br><br>
 *
 * <i>The LocalVariableTypeTable attribute differs from the LocalVariableTable attribute
 * (§4.7.13) in that it provides signature information rather than descriptor information.
 * This difference is only significant for variables whose type uses a type variable or
 * parameterized type. Such variables will appear in both tables, while variables of other
 * types will appear only in LocalVariableTable.</i><br><br>
 *
 * The LocalVariableTypeTable attribute has the following format:<br>
 * <pre><code>
 * LocalVariableTypeTable_attribute {
 *     u2 attribute_name_index;
 *     u4 attribute_length;
 *     u2 local_variable_type_table_length;
 *     {   u2 start_pc;
 *         u2 length;
 *         u2 name_index;
 *         u2 signature_index;
 *         u2 index;
 *     } local_variable_type_table[local_variable_type_table_length];
 * }
 * </code></pre><br>
 *
 * The items of the LocalVariableTypeTable_attribute structure are as follows:
 * <ul>
 *     <li>
 *         <b>attribute_name_index</b><br>
 *         The value of the attribute_name_index item must be a valid index into
 *         the constant_pool table. The constant_pool entry at that index must be
 *         a CONSTANT_Utf8_info structure (§4.4.7) representing the string
 *         "LocalVariableTypeTable".
 *     </li>
 *     <li>
 *         <b>attribute_length</b><br>
 *         The value of the attribute_length item indicates the length of the
 *         attribute, excluding the initial six bytes.
 *     </li>
 *     <li>
 *         <b>local_variable_type_table_length</b><br>
 *         The value of the local_variable_type_table_length item indicates the
 *         number of entries in the local_variable_type_table array.
 *     </li>
 *     <li>
 *         <b>local_variable_type_table[]</b><br>
 *         Each entry in the local_variable_type_table array indicates a range of
 *         code array offsets within which a local variable has a value, and indicates
 *         the index into the local variable array of the current frame at which
 *         that local variable can be found. Each entry must contain the following
 *         five items:<br>
 *         <ul>
 *             <li>
 *                 <b>start_pc, length</b><br>
 *                 The value of the start_pc item must be a valid index into the code
 *                 array of this Code attribute and must be the index of the opcode
 *                 of an instruction.<br><br>
 *
 *                 The value of start_pc + length must either be a valid index into the
 *                 code array of this Code attribute and be the index of the opcode of
 *                 an instruction, or it must be the first index beyond the end of that
 *                 code array.<br><br>
 *
 *                 The start_pc and length items indicate that the given local variable
 *                 has a value at indices into the code array in the interval
 *                 [start_pc, start_pc + length), that is, between start_pc inclusive
 *                 and start_pc + length exclusive.
 *             </li>
 *             <li>
 *                 <b>name_index</b><br>
 *                 The value of the name_index item must be a valid index into the
 *                 constant_pool table. The constant_pool entry at that index must contain
 *                 a CONSTANT_Utf8_info structure representing a valid unqualified name
 *                 denoting a local variable (§4.2.2).
 *             </li>
 *             <li>
 *                 <b>signature_index</b><br>
 *                 The value of the signature_index item must be a valid index into the
 *                 constant_pool table. The constant_pool entry at that index must contain
 *                 a CONSTANT_Utf8_info structure representing a field signature which
 *                 encodes the type of a local variable in the source program (§4.7.9.1).
 *             </li>
 *             <li>
 *                 <b>index</b><br>
 *                 The value of the index item must be a valid index into the local variable
 *                 array of the current frame. The given local variable is at index in the
 *                 local variable array of the current frame.<br><br>
 *
 *                 If the given local variable is of type double or long, it occupies both
 *                 index and index + 1.
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author HenryRenYz
 */
public class LocalVariableTypeTable extends TableAttribute<LocalVariableTable.Variable> {

    private LocalVariableTypeTable(AttributeContainer container) {
        super(container);
        this.items = ((Code) container).variableTable().items;
    }

    public LocalVariableTypeTable(Code c) {
        this((AttributeContainer) c);
    }

    public static LocalVariableTypeTable parse(AttributeContainer container, ClassFileParser pool, ByteVector buffer) {
        LocalVariableTypeTable inst = new LocalVariableTypeTable(container);

        int len = buffer.getUShort();
        for (int i = 0; i < len; i++) {
            buffer.skip(4);
            String name = pool.getString(buffer.getUShort());
            String signature = pool.getString(buffer.getUShort());
            int index = buffer.getUShort();
            for (LocalVariableTable.Variable item : inst.items) {
                if (item.getIndex() == index && item.getName().equals(name)) {
                    item.setDescriptor(signature);
                }
            }
        }
        return inst;
    }

    @Override
    protected void checkContainerType(AttributeContainer container) {
        checkContainerType(container, Code.class);
    }

    @Override
    public String name() {
        return "LocalVariableTypeTable";
    }

    @Override
    protected void writeData(ByteVector buffer) {
        List<LocalVariableTable.Variable> list = new ArrayList<>();
        for (LocalVariableTable.Variable item : items) {
            if (item.getDescriptor() != null) list.add(item);
        }
        buffer.putShort(list.size());
        for (LocalVariableTable.Variable item : list) {
            int startPc = item.getStart().offset();
            buffer.putShort(startPc);
            buffer.putShort(item.getEnd().offset() - startPc + item.getEnd().byteSize());
            buffer.putShort(constPool().acquireUtf(item.getName()).index());
            buffer.putShort(constPool().acquireUtf(item.getDescriptor()).index());
            buffer.putShort(item.getIndex());
        }
    }

    @Override
    public boolean isEmpty() {
        for (LocalVariableTable.Variable item : items) {
            if (item.getDescriptor() != null) return false;
        }
        return true;
    }
}
