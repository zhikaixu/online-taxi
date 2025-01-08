package com.zhikaixu.apidriver.service;

import com.google.gson.JsonObject;
import com.zhikaixu.apidriver.remote.ServiceSsePushClient;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    public ResponseResult pushPayInfo(String orderId, String price, Long passengerId) {

        // 封装消息
        JsonObject message = new JsonObject();
        message.addProperty("price", price);
        message.addProperty("orderId", orderId);

        // 推送消息
        serviceSsePushClient.push(passengerId, IdentityConstant.PASSENGER_IDENTITY, message.toString());

        return ResponseResult.success("");
    }
}
