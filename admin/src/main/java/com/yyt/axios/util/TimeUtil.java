package com.yyt.axios.util;

public class TimeUtil {
    private TimeUtil() {}

    public static long getNow() {
        return System.currentTimeMillis() / 1000;
    }
}
