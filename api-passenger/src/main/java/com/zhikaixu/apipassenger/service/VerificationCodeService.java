package com.zhikaixu.apipassenger.service;

import com.zhikaixu.apipassenger.remote.ServiceVerificationcodeClient;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.NumberCodeResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    private String verificationCodePredix = "passenger-verification-code-";
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult generateCode(String passengerPhone) {
        // 1. 调用验证码服务，获得验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        // 2. 将验证码存入Redis
        stringRedisTemplate.opsForValue().set(verificationCodePredix+passengerPhone, numberCode+"", 2, TimeUnit.MINUTES);

        // 3. 通过短信服务商，将对应的验证码发送到手机上。
        return ResponseResult.success(null);
    }
}
