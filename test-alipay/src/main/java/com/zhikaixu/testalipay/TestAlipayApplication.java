package com.zhikaixu.testalipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TestAlipayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestAlipayApplication.class);
    }

    @PostMapping("/test")
    public String test() {
        System.out.println("支付宝回调了");
        return "外网穿透测试";
    }
}
