package com.zhikaixu.apipassenger.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    public String generateCode(String passengerPhone) {
        // 1. 调用验证码服务，获得验证码
        System.out.println("调用验证码服务，获得验证码");
        String code = "111111";
        // 2. 将验证码存入Redis
        System.out.println("将验证码存入Redis");

        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("message", "success");
        return result.toString();
    }
}
