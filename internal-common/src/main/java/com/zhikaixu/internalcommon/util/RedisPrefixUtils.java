package com.zhikaixu.internalcommon.util;

public class RedisPrefixUtils {

    // 验证码redis key前缀
    private static String verificationCodePrefix = "verification-code-";

    // token redis key前缀
    private static String tokenPredix = "token-";

    // 黑名单设备号前缀
    public static String blackDeviceCodePredix = "black-device-";

    /**
     * 根据手机号，生成key
     * @param phone
     * @param identity
     * @return
     */
    public static String generateKeyByPhone(String phone, String identity) {
        return verificationCodePrefix + identity + "-" + phone;
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
