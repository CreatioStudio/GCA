package vip.creatio.gca;

import vip.creatio.gca.util.common.ByteVector;
import vip.creatio.gca.type.MethodInfo;

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
public class MethodHandleConst implements Const {
    private ReferenceKind kind;
    private MethodInfo ref;

    MethodHandleConst(ReferenceKind kind, MethodInfo ref) {
        this.kind = kind;
        this.ref = ref;
    }

    public String toString() {
        return "MethodHandle{reference_kind=" + getKind() + ",reference=" + getRef() + '}';
    }

    void write(ConstPool pool, ByteVector buffer) {
        buffer.putByte(kind.getId());
        buffer.putShort(pool.indexOf(ref));
    }

    void collect(ConstPool pool) {
        pool.acquire(ref);
    }

    public MethodInfo getRef() {
        return ref;
    }

    public ReferenceKind getKind() {
        return kind;
    }

    public void setKind(ReferenceKind kind) {
        this.kind = kind;
    }

    public void setRef(MethodInfo ref) {
        this.ref = ref;
    }

    @Override
    public ConstType constantType() {
        return ConstType.METHOD_HANDLE;
    }

    @Override
    public int hashCode() {
        return ref.hashCode() * ~kind.getId() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodHandleConst) {
            return ((MethodHandleConst) obj).kind == kind
                    && ((MethodHandleConst) obj).ref.equals(ref);
        }
        return false;
    }
}
