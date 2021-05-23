package vip.creatio.gca;

import java.util.EnumSet;

enum MethodAccessFlag {
    ACC_PUBLIC          (0x0001),
    ACC_PRIVATE         (0x0002),
    ACC_PROTECTED       (0x0004),
    ACC_STATIC          (0x0008),
    ACC_FINAL           (0x0010),
    ACC_SYNCHRONIZED    (0x0020),
    ACC_BRIDGE          (0x0040),
    ACC_VARARGS         (0x0080),
    ACC_NATIVE          (0x0100),
    ACC_ABSTRACT        (0x0400),
    ACC_STRICTFP        (0x0800),
    ACC_SYNTHETIC       (0x1000);

    final int mask;

    MethodAccessFlag(int mask) {
        this.mask = mask;
    }

    static EnumSet<MethodAccessFlag> getAccessFlags(short flag) {
        EnumSet<MethodAccessFlag> set = EnumSet.noneOf(MethodAccessFlag.class);
        for (MethodAccessFlag flags : values()) {
            if ((flag & flags.mask) == flags.mask) set.add(flags);
        }
        return set;
    }
}
