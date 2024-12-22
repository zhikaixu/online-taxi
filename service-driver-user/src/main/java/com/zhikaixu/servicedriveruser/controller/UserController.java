package com.zhikaixu.servicedriveruser.controller;

import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private DriverUserService driverUserService;

    @PostMapping("/user")
    public ResponseResult addUser(@RequestBody DriverUser driverUser) {
        return driverUserService.addDriverUser(driverUser);
    }
}
