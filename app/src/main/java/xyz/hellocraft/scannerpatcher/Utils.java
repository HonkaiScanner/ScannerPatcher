package xyz.hellocraft.scannerpatcher;

public class Utils {

    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static String bytes2HexStr(byte[] var0) {
        if (var0 != null && var0.length != 0) {
            char[] var1 = new char[var0.length * 2];

            for(int var2 = 0; var2 < var0.length; ++var2) {
                byte var3 = var0[var2];
                int var4;
                int var5 = (var4 = var2 * 2) + 1;
                char[] var6;
                char[] var10001 = var6 = digits;
                var1[var5] = var6[var3 & 15];
                byte var10002 = (byte)(var3 >>> 4);
                int var7 = var4 + 0;
                var1[var7] = var10001[var10002 & 15];
            }

            return new String(var1);
        } else {
            return null;
        }
    }

}
