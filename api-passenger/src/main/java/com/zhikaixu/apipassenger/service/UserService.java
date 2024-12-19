package com.zhikaixu.apipassenger.service;

import com.zhikaixu.apipassenger.remote.ServicePassengerUserClient;
import com.zhikaixu.internalcommon.dto.PassengerUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.dto.TokenResult;
import com.zhikaixu.internalcommon.request.VerificationCodeDTO;
import com.zhikaixu.internalcommon.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    ServicePassengerUserClient servicePassengerUserClient;

    public ResponseResult getUserByAccessToken(String accessToken) {
        log.info("accessToken:" + accessToken);
        // 解析accessToken，拿到手机号
        TokenResult tokenResult = JwtUtils.checkToken(accessToken);
        String phone = tokenResult.getPhone(); // TODO: need check null?
        log.info("手机号: " + phone);
        // 根据手机号查询用户信息
        // 调用service-passenger-user的接口
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(phone);
        ResponseResult<PassengerUser> userByPhone = servicePassengerUserClient.getUserByPhone(phone);

        return ResponseResult.success(userByPhone.getData());
    }
}
