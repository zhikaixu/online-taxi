package com.zhikaixu.serviceorder.controller;

import com.zhikaixu.internalcommon.dto.OrderInfo;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.serviceorder.mapper.OrderInfoMapper;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "service-order";
    }

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Value("${server.port}")
    String port;

    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") long orderId) {
        System.out.println("service-order 端口: " + port + " 并发测试: orderId: " + orderId);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchRealTimeOrder(orderInfo);
        return "test-real-time-order success";
    }

}
