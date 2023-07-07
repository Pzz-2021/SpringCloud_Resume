package com.resume.auth.utils;

/*
 *@filename: SM3Util
 *@author: lyh
 *@date:2023/6/11 23:05
 *@version 1.0
 *@description TODO
 */

/*
 * sm3加密算法工具类
 */
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;
public class SM3Util {

    public static String encryptPassword(String password) {
        if (password==null)throw new RuntimeException("密码为空!无法加密!");
        byte[] passwordBytes = password.getBytes();

        SM3Digest digest = new SM3Digest();
        digest.update(passwordBytes, 0, passwordBytes.length);

        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        return Hex.toHexString(hash);
    }

    public static void main(String[] args) {
        String password = "123456";
        String encryptedPassword = encryptPassword(password);
        System.out.println("Encrypted password: " + encryptedPassword);
    }
}