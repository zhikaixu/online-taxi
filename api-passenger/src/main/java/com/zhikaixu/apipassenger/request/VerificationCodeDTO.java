package com.zhikaixu.apipassenger.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class VerificationCodeDTO {

    @NotBlank
    @Pattern(regexp = "^1[3,4,5,6,7,8,9]\\d{9}$")
    private String passengerPhone;

    private String verificationCode;

    private String driverPhone;
}
