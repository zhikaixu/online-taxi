package com.zhikaixu.internalcommon.util;

public class SsePrefixUtils {

    public static final String seperator = "$";

    public static String generatorSseKey(Long userId, String identity) {
        return userId + seperator + identity;
    }
}
