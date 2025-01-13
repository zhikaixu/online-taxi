package com.zhikaixu.apipassenger.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SendVerificationCodeDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请填写正确手机号")
    private String passengerPhone;

    private String verificationCode;

    private String driverPhone;
}
