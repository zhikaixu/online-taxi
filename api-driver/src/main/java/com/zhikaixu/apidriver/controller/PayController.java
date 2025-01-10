package com.zhikaixu.apidriver.controller;

import com.zhikaixu.apidriver.service.PayService;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @PostMapping("/push-pay-info")
    public ResponseResult pushPayInfo(@RequestParam Long orderId, @RequestParam String price, @RequestParam Long passengerId) {
        return payService.pushPayInfo(orderId, price, passengerId);
    }
}
