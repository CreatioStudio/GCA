package vip.creatio.gca;

import vip.creatio.gca.util.ByteVector;

/**
 * MethodHandle refrence kind
 * 1	REF_getField	getfield C.f:T
 * 2	REF_getStatic	getstatic C.f:T
 * 3	REF_putField	putfield C.f:T
 * 4	REF_putStatic	putstatic C.f:T
 * 5	REF_invokeVirtual	invokevirtual C.m:(A*)T
 * 6	REF_invokeStatic	invokestatic C.m:(A*)T
 * 7	REF_invokeSpecial	invokespecial C.m:(A*)T
 * 8	REF_newInvokeSpecial	new C; dup; invokespecial C.<init>:(A*)void
 * 9	REF_invokeInterface	invokeinterface C.m:(A*)T
 */
public class MethodHandleConst extends Const {
    private ReferenceKind kind;
    private RefConst ref;

    MethodHandleConst(ConstPool pool, ReferenceKind kind, RefConst ref) {
        super(pool, ConstType.METHOD_HANDLE);
        this.kind = kind;
        this.ref = ref;
    }

    MethodHandleConst(ConstPool pool) {
        super(pool, ConstType.METHOD_HANDLE);
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Const copy() {
        return new MethodHandleConst(pool, kind, ref);
    }

    @Override
    int byteSize() {
        return 4;
    }

    public String toString() {
        return "MethodHandle{reference_kind=" + getKind() + ",reference=" + getRef() + '}';
    }

    @Override
    public void write(ByteVector buffer) {
        buffer.putByte(tag());
        buffer.putByte(kind.getId());
        buffer.putShort(getRef().index());
    }

    @Override
    public void parse(ClassFileParser pool, ByteVector buffer) {
        this.kind = ReferenceKind.fromId(buffer.getByte());
        ref = (RefConst) pool.get(buffer.getShort());
    }

    public RefConst getRef() {
        return ref;
    }

    public ReferenceKind getKind() {
        return kind;
    }

    public void setKind(ReferenceKind kind) {
        this.kind = kind;
    }

    public void setRef(RefConst ref) {
        this.ref = ref;
    }

    @Override
    public int hashCode() {
        return type.hashCode() * ~kind.getId() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodHandleConst) {
            return ((MethodHandleConst) obj).pool == pool && ((MethodHandleConst) obj).kind == kind
                    && ((MethodHandleConst) obj).ref.equals(ref);
        }
        return false;
    }
}
