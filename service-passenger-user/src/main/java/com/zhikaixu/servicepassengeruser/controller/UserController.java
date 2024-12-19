package com.zhikaixu.servicepassengeruser.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.VerificationCodeDTO;
import com.zhikaixu.servicepassengeruser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseResult loginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("手机号: " + passengerPhone);
        return userService.loginOrRegister(passengerPhone);
    }

    @GetMapping("/user/{phone}")
    public ResponseResult getUser(@PathVariable("phone") String passengerPhone) {
        System.out.println("service-passenger-user: phone:" + passengerPhone);
        return userService.getUserByPhone(passengerPhone);
    }
}
