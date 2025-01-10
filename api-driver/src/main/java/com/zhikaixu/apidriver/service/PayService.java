package com.zhikaixu.apidriver.service;

import com.google.gson.JsonObject;
import com.zhikaixu.apidriver.remote.ServiceOrderClient;
import com.zhikaixu.apidriver.remote.ServiceSsePushClient;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {
    @Autowired
    private ServiceOrderClient serviceOrderClient;

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    public ResponseResult pushPayInfo(Long orderId, String price, Long passengerId) {

        // 封装消息
        JsonObject message = new JsonObject();
        message.addProperty("price", price);
        message.addProperty("orderId", orderId);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(orderId);
        serviceOrderClient.pushPayInfo(orderRequest);
        // 推送消息
        serviceSsePushClient.push(passengerId, IdentityConstant.PASSENGER_IDENTITY, message.toString());

        return ResponseResult.success("");
    }
}
