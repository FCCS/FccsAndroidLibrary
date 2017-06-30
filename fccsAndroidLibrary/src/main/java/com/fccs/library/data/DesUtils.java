/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by ethen on 15/12/3.
 */
public class DesUtils {

    private final static String iv = "01234567";
    private final static String encoding = "UTF-8";

    private static String secretKey = null;

    public static void setSecretKey(String secretKey) {
        DesUtils.secretKey = secretKey;
    }

    @SuppressLint("TrulyRandom")
	public static String encrypt(String plainText) throws Exception {
        if (TextUtils.isEmpty(secretKey)) {
            throw new IllegalArgumentException("You have not set the secretKey !!!");
        }
        DESedeKeySpec dks = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("desede");
        Key key = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encodeToString(encryptData, Base64.DEFAULT);
    }

    public static String decrypt(String encryptText) throws Exception {
        if (TextUtils.isEmpty(secretKey)) {
            throw new IllegalArgumentException("You have not set the secretKey !!!");
        }
        DESedeKeySpec dks = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("desede");
        Key key = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key, ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText,
                Base64.DEFAULT));
        return new String(decryptData, encoding);
    }

    /**
     * 得到MD5加密信息
     *
     * @param string
     * @return
     */
    public static String getMD5Value(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
