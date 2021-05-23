package vip.creatio.gca;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

class Method {
    EnumSet<MethodAccessFlag> access_flags;
    int attributes_count;
    Field.FieldAttribute[] attributes;



    static class MethodAttribute extends Attribute {

        private final int data_index; //Class Name + Generics

        MethodAttribute(Bytecode reader, BufferedInputStream buffer) throws IOException {
            super(reader, buffer);

            byte[] b2 = new byte[2];

            buffer.read(b2);
            data_index = ByteBuffer.wrap(b2).getShort() & 0xFFFF;
        }

        MethodAttribute(Bytecode reader, int att_name_index, long att_length, int signature_index) {
            super(reader, att_name_index, att_length);
            this.data_index = signature_index;
        }

        public String toString() {
            return "FieldAttribute{attribute_name=" + getAttributeName() + ",length=" + skip() + "L,descriptor=" + getDescriptor() + '}';
        }

        public String getData() {
            return linked_reader.constantPool[data_index - 1].getItem();
        }

        public String getDescriptor() {
            return linked_reader.constantPool[attribute_name_index - 1].getItem();
        }

        public String printAttribute() {
            return getDescriptor() + ": " + getData();
        }
    }
}
