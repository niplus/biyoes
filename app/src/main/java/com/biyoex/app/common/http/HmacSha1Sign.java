package com.biyoex.app.common.http;

import android.os.Build;

import java.util.Base64;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ProjectName: VBT-android$
 * @Package: com.vbt.app.common.utils$
 * @ClassName: HmacSha1Sign$
 * @Description: 生成签名文件
 * @Author: 赵伟国
 * @CreateDate: 2020-04-13$ 15:55$
 * @Version: 1.0
 */
public class HmacSha1Sign {

    /**
     * 生成签名字段
     *
     * @return
     * @throws Exception
     */

    public static String genSign() throws Exception {
        try {
            String apiKey = "";
            String secret = "";
            long currtTime = System.currentTimeMillis() / 1000;
            long expireTime = (System.currentTimeMillis() + 60 * 60 * 100) / 1000;
            int rdm = Math.abs(new Random().nextInt());
            String plainText = String.format("a=%s&b=%d&c=%d&d=%d", apiKey, expireTime, currtTime,rdm);
            byte[] hmacDigest = HmacSha1(plainText, secret);
            byte[] signContent = new byte[hmacDigest.length + plainText.getBytes().length];
            System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
            System.arraycopy(plainText.getBytes(), 0, signContent, hmacDigest.length,
                    plainText.getBytes().length);
            return encodeToBase64(signContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成 base64 编码
     *
     * @param binaryData
     * @return
     */

    public static String encodeToBase64(byte[] binaryData) {
        String encodedStr = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encodedStr = Base64.getEncoder().encodeToString(binaryData);
        }
        else {
            encodedStr = android.util.Base64.encodeToString(binaryData, android.util.Base64.NO_WRAP);
        }
        return encodedStr;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param binaryData
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] HmacSha1Digest = mac.doFinal(binaryData);
        return HmacSha1Digest;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(String plainText, String key) throws Exception {
        return HmacSha1(plainText.getBytes(), key);
    }
}
