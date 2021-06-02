package vip.creatio.gca.test;

public final class Calculator {

    public static void main(String[] args) {
        System.out.println("Sin PI/2: " + sin(Math.PI * 44.5));
        System.out.println("Sin PI/2: " + sina(Math.PI * 44.5));
        System.out.println("Sin PI/2: " + Math.sin(Math.PI * 44.5));
    }

    public static double sin(double v /* in radians */ ) {
        double v1 = v %= Math.PI;

        boolean b = false;
        for (int i = 1; i < 7; i++) {
            double pow = 1D;
            int stage = 1;
            for (int j = 1, k = i * 2 + 1; j <= k; j++) {
                pow *= v;
                stage *= j;
            }

            if (b) {
                v1 += pow / stage;
            } else {
                v1 -= pow / stage;
            }
            b = !b;
        }

        return v1;
    }

    public static double sina(double var0) {
        double var2;
        double var10000 = var2 = var0 % 3.141592653589793D;
        boolean var4 = false;

        double var5;
        for(double var10001 = var5 = (double)1; var10001 < 8; var10000 = var5) {
            var10001 = 1.0D;
            double var10002 = (double)1;
            double var10003 = 1.0D;
            double var6 = (double)1;

            double var7;
            for(double var10004 = var7 = var5 * 2 + 1; var10003 <= var10004; var10004 = var7) {
                var10003 = var10002 * var6;
                var10002 = var10001;
                var10001 = var10003;
                var10003 = var10002 * var0;
                var10002 = var10001;
                var10001 = var10003;
                ++var6;
                var10003 = var6;
            }

            var10001 /= (double)var10002;
            boolean var8 = var4;
            var4 ^= true;
            var2 = var8 ? var10000 + var10001 : var10000 - var10001;
            ++var5;
        }

        return var2;
    }

}
