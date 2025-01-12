package com.zhikaixu.apipassenger.request;

import com.zhikaixu.apipassenger.constraints.CheckVerificationCodeGroup;
import com.zhikaixu.apipassenger.constraints.SendVerificationCodeGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TestGroupCheckVerificationCodeDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请填写正确手机号", groups = {SendVerificationCodeGroup.class, CheckVerificationCodeGroup.class})
    private String passengerPhone;

    @NotBlank(message = "验证码不能为空", groups = {CheckVerificationCodeGroup.class})
    @Pattern(regexp = "^\\d{6}$", message = "请填写6位数字的验证码", groups = {CheckVerificationCodeGroup.class})
    private String verificationCode;
}
