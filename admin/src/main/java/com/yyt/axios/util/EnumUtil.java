package com.yyt.axios.util;

import com.yyt.axios.enums.ICode;

public class EnumUtil {
    public static <T extends ICode> T getByCode(Integer code, Class<T> enumClass) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if(code.equals(enumConstant.getCode())) {
                return enumConstant;
            }
        }
        return null;
    }
}
