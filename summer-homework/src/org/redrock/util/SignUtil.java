package org.redrock.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by jx on 2017/7/21.
 */
public class SignUtil {

    private static final char[] digit = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static boolean checkSignature(String timestamp, String nonce, String signature, String echoStr) {
        try {
            if (!StringUtil.hasBlank(timestamp, nonce, signature, echoStr)) {
                String token = Const.Token;
                String[] arr = {timestamp, nonce, token};
                Arrays.sort(arr);
                StringBuilder temp = new StringBuilder();
                for (String str : arr) {
                    temp.append(str);
                }
                MessageDigest md = MessageDigest.getInstance("SHA1");
                String encode = byteToStr(md.digest(temp.toString().getBytes("UTF-8")));
                if (signature.equalsIgnoreCase(encode)) {
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String byteToStr(byte[] byteArray) {
        int len = byteArray.length;
        StringBuilder strDigest = new StringBuilder(len * 2);
        for (byte aByteArray : byteArray) {
            strDigest.append(byteToHexStr(aByteArray));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] tempArr = new char[2];
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = digit[mByte & 0X0F];
        return new String(tempArr);
    }
}