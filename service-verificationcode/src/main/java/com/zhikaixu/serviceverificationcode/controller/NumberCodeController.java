package com.zhikaixu.serviceverificationcode.controller;


import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.NumberCodeResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {

    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable("size") int size) {
        System.out.println("size: " + size);
        // 生成验证码
        double mathRandom = (Math.random()*9 + 1) * (Math.pow(10, size - 1));
        int numberCode = (int) mathRandom;

        // 定义返回值
        NumberCodeResponse response = new NumberCodeResponse();
        response.setNumberCode(numberCode);

        return ResponseResult.success(response);
    }
}
