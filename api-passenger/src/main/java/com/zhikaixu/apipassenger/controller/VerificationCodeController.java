package com.zhikaixu.apipassenger.controller;

import com.zhikaixu.apipassenger.request.VerificationCodeDTO;
import com.zhikaixu.apipassenger.service.VerificationCodeService;
import com.zhikaixu.internalcommon.dto.ResponseResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@Validated @RequestBody VerificationCodeDTO verificationCodeDTO) {
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("接受到的手机号参数:" + passengerPhone);
        return verificationCodeService.generateCode(passengerPhone);
    }

    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();

        System.out.println("手机号: " + passengerPhone + " 验证码: " + verificationCode);

        return verificationCodeService.checkCode(passengerPhone, verificationCode);
    }
}
