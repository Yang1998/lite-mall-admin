package com.yyt.axios.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64UrlUtil {
    private Base64UrlUtil() {}

    private static final String ENCODING = "UTF-8";
    /**
     *  将字符串转化为base64url编码字符串
     * @param data 字符串
     * @return 编码后的字符串
     */
    public static String encode(String data) throws UnsupportedEncodingException {
        byte[] bytes = Base64.encodeBase64(data.getBytes(ENCODING));
        return new String(bytes, ENCODING);
    }

    /**
     *  将base64Url字符串 解码为 字符串
     * @param base64Url base64URL
     * @return json
     * @throws UnsupportedEncodingException
     */
    public static String decode(String base64Url) throws UnsupportedEncodingException {
        byte[] bytes = Base64.decodeBase64(base64Url.getBytes(ENCODING));
        return new String(bytes, ENCODING);
    }
}
