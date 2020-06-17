package com.yyt.axios.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtil {
    private static final String ENCODEING = "UTF-8";
    private static final String ALGORITHM = "RSA";
    private RsaUtil() {}

    /**
     * String转公钥PublicKey
     * @param key
     * @return
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return (RSAPublicKey) publicKey;
    }

    /**
     * String转私钥PrivateKey
     * @param key
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return (RSAPrivateKey) privateKey;
    }

    /**
     *  rsa加密字符串
     * @param str
     * @param publicKey
     * @return 经过base64编码后的字符串，方便阅读，直接返回的加密结果是乱码的
     * @throws Exception
     */
    public static String encrypt(String str, String publicKey) throws Exception{
        //base64编码的公钥
//        byte[] decoded = Base64.decodeBase64(publicKey);
//        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        final RSAPublicKey pubKey = getPublicKey(publicKey);
        //RSA加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * rsa 解密字符串, 注意str要base64解密
     * @param str
     * @param privateKey
     * @return 解密字符串
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(String str, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        final byte[] strDecode = Base64.decodeBase64(str);
//        final byte[] bytes = Base64.decodeBase64(privateKey);
//        final PrivateKey generatePrivateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(bytes));
        final RSAPrivateKey generatePrivateKey = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, generatePrivateKey);
        return new String(cipher.doFinal(strDecode));
    }
}
