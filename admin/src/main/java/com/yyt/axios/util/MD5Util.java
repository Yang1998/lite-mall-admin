package com.yyt.axios.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class MD5Util {


    public MD5Util() {
    }

    private static final String ENCODEING = "UTF-8";

    public String encrypByMD5(String target) throws UnsupportedEncodingException {
        return DigestUtils.md5DigestAsHex(target.getBytes(ENCODEING));
    }

    public boolean verify(String password, String passwordFromDb, String salt) {
        try {
            return encrypByMD5(salt + password).equals(passwordFromDb);
        } catch (UnsupportedEncodingException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }
}
