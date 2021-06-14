package vip.creatio.gca;

import vip.creatio.gca.type.Types;

import vip.creatio.gca.util.common.ByteVector;

import java.util.Arrays;

public class MethodTypeConst implements Const {
    private final TypeInfo rtype;
    private final TypeInfo[] ptype;

    private String cachedSig;

    MethodTypeConst(TypeInfo rtype, TypeInfo... ptype) {
        this.rtype = rtype;
        this.ptype = ptype;
    }

    MethodTypeConst(TypeInfo[] sigs) {
        this.rtype = sigs[0];
        this.ptype = Arrays.copyOfRange(sigs, 1, sigs.length);
    }

    @Override
    public ConstType constantType() {
        return ConstType.METHOD_TYPE;
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putShort(pool.indexOf(getDescriptor()));
    }

    void collect(ConstPool pool) {
        pool.acquireUtf(getDescriptor());
    }

    public String toString() {
        return "MethodType{discriptor=" + getDescriptor() + '}';
    }

    public String getDescriptor() {
        if (cachedSig == null) cachedSig = Types.toMethodSignature(rtype, ptype);
        return this.cachedSig;
    }

    @Override
    public int hashCode() {
        return rtype.hashCode() + Arrays.hashCode(ptype) * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodTypeConst) {
            return ((MethodTypeConst) obj).rtype.equals(rtype)
                    && Arrays.equals(((MethodTypeConst) obj).ptype, ptype);
        }
        return false;
    }
}
