package com.netease.nim.uikit.app.widgets;

/**
 * @author : ljh
 * @time : 2019/12/19 17:52
 * @desc :
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public MD5Util() {
    }

    public static String getMD5(String content) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            var1.update(content.getBytes());
            return getHashString(var1);
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder var1 = new StringBuilder();
        byte[] var2 = digest.digest();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            var1.append(Integer.toHexString(var5 >> 4 & 15));
            var1.append(Integer.toHexString(var5 & 15));
        }

        return var1.toString();
    }
}