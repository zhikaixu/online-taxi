package com.zhikaixu.apipassenger.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CheckVerificationCodeDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请填写正确手机号")
    private String passengerPhone;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "请填写6位数字的验证码")
    private String verificationCode;

    private String driverPhone;
}
