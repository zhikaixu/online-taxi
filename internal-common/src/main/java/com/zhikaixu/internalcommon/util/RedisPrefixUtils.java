package com.zhikaixu.internalcommon.util;

public class RedisPrefixUtils {

    // 验证码redis key前缀
    private static String verificationCodePrefix = "passenger-verification-code-";

    // token redis key前缀
    private static String tokenPredix = "token-";

    /**
     * 根据手机号，生成key
     * @param passengerPhone
     * @return
     */
    public static String generateKeyByPhone(String passengerPhone) {
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 根据手机号和身份，生成token key
     * @param phone
     * @param identity
     * @return
     */
    public static String generateTokenKey(String phone, String identity, String tokenType) {
        return tokenPredix + phone + "-" + identity + "-" + tokenType;
    }
}
