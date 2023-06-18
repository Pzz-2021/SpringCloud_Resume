package com.resume.auth.utils;

/*
 *@filename: SM3Util
 *@author: lyh
 *@date:2023/6/11 23:05
 *@version 1.0
 *@description TODO
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

/**
 * sm3加密算法工具类
 * 先拿到前台的密码，然后划分成数组，数组循环转码并加密，然后放入StringBuffer中，拼接完成后，
 * 拿着返回的StringBuffer然后去进行加密，加密会把字符串变为byte类型然后在通过特定的杂凑算法规则，
 * 进行最后一层返回。也就是我们最后得到的加密后的杂凑字符。
 */
@Slf4j
public class SM3Util {
    private static final String ENCODING = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     * 密码加密方法总和
     * @param pwd 密码
     * @return 逻辑加密后的返回密码
     */
    public static String pwdEncrypt(String pwd){
        if (StringUtils.isNotEmpty(pwd)){
            String pwdConvert = pwdEncryptAssembly(pwd);
            return encrypt(pwdConvert);
        }
        throw new IllegalStateException("密码不能为空!");
    }

    /**
     * 获取前端传来的密码数字，然后加密拼接
     * @param pwd 密码
     * @return 加密拼接完的密码
     */
    public static String pwdEncryptAssembly(String pwd) {
        StringBuilder stringPwd = new StringBuilder();
        for (char key : pwd.toCharArray()) {
            String s;
            if(key>='0'&&key<='9') s = SM3Rule.valueOfCode(String.valueOf(key));
            else s=String.valueOf(key);
            String encrypt = encrypt(s);
            stringPwd.append(encrypt);
        }
        String jsonPwd = stringPwd.toString();
        return jsonPwd.toUpperCase();
    }

    /**
     * sm3算法加密
     *
     * @param paramStr 待加密字符串
     * @return 返回加密后，固定长度=32的16进制字符串
     */
    public static String encrypt(String paramStr) {
        // 将返回的hash值转换成16进制字符串
        String resultHexString = "";
        try {
            // 将字符串转换成byte数组
            byte[] srcData = paramStr.getBytes(ENCODING);
            // 调用hash()
            byte[] resultHash = hash(srcData);
            // 将返回的hash值转换成16进制字符串
            resultHexString = ByteUtils.toHexString(resultHash);
            return resultHexString.toUpperCase();
        } catch (UnsupportedEncodingException e) {
            log.info("SM3算法加密失败：",e);
            throw new  IllegalStateException("SM3算法加密异常");
        }

    }

    /**
     * 返回长度=32的byte数组
     *
     * @param srcData
     * @return 数组
     * @explain 生成对应的hash值
     */
    public static byte[] hash(byte[] srcData) {

        try {
            SM3Digest digest = new SM3Digest();
            digest.update(srcData, 0, srcData.length);
            byte[] hash = new byte[digest.getDigestSize()];
            digest.doFinal(hash, 0);
            return hash;
        }catch (Exception e){
            log.info("返回byte数组失败：",e);
            throw new IllegalStateException("返回byte数组异常");
        }
    }

}
