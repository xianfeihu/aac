package com.yz.aac.common.util;

import java.security.MessageDigest;
/**
 * 编码
 * @author Xian.Fei
 */
public final class EncodeUtil {
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
	private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    
    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }
    
    // 计算并获取sha1值
    public static String getSha1(String requestBody) {
        return encode("sha1", requestBody);
    }

}
