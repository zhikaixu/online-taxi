package com.zhikaixu.serviceverificationcode.controller;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
public class testController {

    @GetMapping("/test")
    public String test() {
        return "service-verification code";
    }
}
