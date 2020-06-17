package com.yyt.axios.annotation;

import java.lang.annotation.*;

/**
 * @author 0x7ce
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Logging {
    String message() default "日志操作";
}
