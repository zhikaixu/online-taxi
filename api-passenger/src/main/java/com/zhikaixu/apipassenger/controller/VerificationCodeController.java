package com.zhikaixu.apipassenger.controller;

import com.zhikaixu.apipassenger.service.VerificationCodeService;
import com.zhikaixu.apipassenger.request.VerificationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/verification-code")
    public String verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("接收到用户的手机号：" + passengerPhone);

        return verificationCodeService.generateCode(passengerPhone);
    }
}
