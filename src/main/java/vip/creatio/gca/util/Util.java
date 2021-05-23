package vip.creatio.gca.util;

public final class Util {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHex(long num) {
        return new String(new char[]{HEX_CHARS[(int)(num >>> 60)], HEX_CHARS[(int)(num >>> 56) & 0xF],
                HEX_CHARS[(int)(num >>> 52) & 0xF], HEX_CHARS[(int)(num >>> 48) & 0xF], HEX_CHARS[(int)(num >>> 44) & 0xF],
                HEX_CHARS[(int)(num >>> 40) & 0xFF], HEX_CHARS[(int)(num >>> 36) & 0xF], HEX_CHARS[(int)(num >>> 32) & 0xF],
                HEX_CHARS[(int)(num >>> 28) & 0xF], HEX_CHARS[(int)(num >>> 24) & 0xF], HEX_CHARS[(int)(num >>> 20) & 0xF],
                HEX_CHARS[(int)(num >>> 16) & 0xF], HEX_CHARS[(int)(num >>> 12) & 0xF], HEX_CHARS[(int)(num >>> 8) & 0xF],
                HEX_CHARS[(int)(num >>> 4) & 0xF], HEX_CHARS[(int)(num & 0xF)]});
    }

    public static String toHex(int num) {
        return new String(new char[]{HEX_CHARS[num >>> 28], HEX_CHARS[(num >>> 24) & 0xF], HEX_CHARS[(num >>> 20) & 0xF],
                HEX_CHARS[(num >>> 16) & 0xF], HEX_CHARS[(num >>> 12) & 0xF], HEX_CHARS[(num >>> 8) & 0xF],
                HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static String toHex(short num) {
        return new String(new char[]{HEX_CHARS[num >>> 12], HEX_CHARS[(num >>> 8) & 0xF], HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static String toHex(byte num) {
        return new String(new char[]{HEX_CHARS[(num >>> 4) & 0xF], HEX_CHARS[num & 0xF]});
    }

    public static int align(int v) {
        return (v + 3) & ~3;
    }
}
