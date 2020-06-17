package com.yyt.axios.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yyt.axios.entity.Header;
import com.yyt.axios.entity.PayLoad;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;

public class TokenUtil {


    private TokenUtil () {}

    private static String base64UrlEncode(String json) throws UnsupportedEncodingException {
        return Base64UrlUtil.encode(json);
    }


}
