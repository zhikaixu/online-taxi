package com.zhikaixu.apidriver.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    public ResponseResult checkAndSendVerification(String driverPhone) {
        // 查询service-driver-user表，该手机号的司机是否存在

        // 获取验证码

        // 调用第三方发送验证码

        // 存入redis

        return ResponseResult.success("");
    }
}
