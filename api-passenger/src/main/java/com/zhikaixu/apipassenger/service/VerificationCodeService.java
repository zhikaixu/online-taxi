package com.zhikaixu.apipassenger.service;

import com.zhikaixu.apipassenger.remote.ServiceVerificationcodeClient;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.NumberCodeResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    public String generateCode(String passengerPhone) {
        // 1. 调用验证码服务，获得验证码
        System.out.println("调用验证码服务，获得验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode();
        int numberCode = numberCodeResponse.getData().getNumberCode();

        System.out.println("remote number code: " + numberCode);
        // 2. 将验证码存入Redis
        System.out.println("将验证码存入Redis");

        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("message", "success");
        return result.toString();
    }
}
