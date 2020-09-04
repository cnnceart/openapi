package cn.yiidii.openapi.common.util;


import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * AES加解密
 */
public class AESUtil {

    // 加密方式
    public static String KEY_ALGORITHM = "AES";
    // 数据填充方式
    String algorithmStr = "AES/CBC/PKCS7Padding";
    // 避免重复new生成多个BouncyCastleProvider对象，因为GC回收不了，会造成内存溢出
    // 只在第一次调用decrypt()方法时才new 对象
    public static boolean initialized = false;

    public static void main(String[] args) {
        byte[] org = "Q8MKv1IJQl2Eau7/nksyJtQ4qI8/cmVyzEcgA3DA6LOT+CVY+6GQQ/QZZvNOipBc1WbSueLBM2rCwXnl1WLJU1NJu5F7DexEmP6Ao0qPxkd2z3Am/yJ5gN/XszD5gKfZmDolM3Lt2vjbehZNCmiGSw==".getBytes();
        byte[] eKey = AESUtil.parseHexStr2Byte("00000000000000000000000000000000");
        byte[] iv = "pk];pk,o876nkwdd".getBytes();
        System.out.println("org: " + new String(org));
        System.out.println("iv: " + new String(iv));

        org = Base64.decodeBase64(org);
        String addr = new String(new AESUtil().decrypt(org, eKey, iv));
        System.out.println("addr: " + addr);
    }

    public static byte[] encrypt(byte[] originalContent, byte[] encryptKey, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(ivByte));
            byte[] encrypted = cipher.doFinal(originalContent);
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密 填充模式AES/CBC/PKCS7Padding 解密模式128
     *
     */
    public static byte[] decrypt(byte[] content, byte[] aesKey, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(aesKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。
     **/
    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    // 生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * 将byte数组转换成16进制String
     *
     * @param buf
     * @return
     */

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制String转换为byte数组
     *
     * @param hexStr
     * @return
     */

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}