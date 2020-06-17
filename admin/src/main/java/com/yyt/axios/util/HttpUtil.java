package com.yyt.axios.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpUtil {
    private static final String TOKEN_HEADER = "Authorization";

    public static String getRequestToken(HttpServletRequest request) {
        //从header中获取token
        String token = request.getHeader(TOKEN_HEADER);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(TOKEN_HEADER);
        }
        return token;
    }
}
