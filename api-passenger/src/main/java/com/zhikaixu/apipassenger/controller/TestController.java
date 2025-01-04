package com.zhikaixu.apipassenger.controller;

import com.zhikaixu.apipassenger.remote.ServiceOrderClient;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test api passenger";
    }

    /**
     * 需要有token
     * @return
     */
    @GetMapping("/authTest")
    public ResponseResult authTest() {
        return ResponseResult.success("auth test");
    }

    /**
     * 没有token也能正常返回
     * @return
     */
    @GetMapping("/noauthTest")
    public ResponseResult noauthTest() {
        return ResponseResult.success("noauth test");
    }

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    @Value("${server.port}")
    String port;
    /**
     * 测试派单逻辑
     * @param orderId
     * @return
     */
    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") long orderId) {
        System.out.println("api-passenger 端口: " + port + " 并发测试: orderId: " + orderId);
        serviceOrderClient.dispatchRealTimeOrder(orderId);
        return "test-real-time-order success";
    }
}
