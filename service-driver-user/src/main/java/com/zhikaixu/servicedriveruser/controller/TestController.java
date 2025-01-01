package com.zhikaixu.servicedriveruser.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.mapper.DriverUserMapper;
import com.zhikaixu.servicedriveruser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private DriverUserService driverUserService;

    @Autowired
    private DriverUserMapper driverUserMapper;

    @GetMapping("/test")
    public String test() {
        return "service-driver-user";
    }

    @GetMapping("/test-db")
    public ResponseResult testDb() {
        return driverUserService.testGetDriverUser();
    }

    // 测试mapper中的xml是否正常使用
    @GetMapping("/test-xml")
    public int testXml(String args) {
        return driverUserMapper.select1("1");
    }
}
