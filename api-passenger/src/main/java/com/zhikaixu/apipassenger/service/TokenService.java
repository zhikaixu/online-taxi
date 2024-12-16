package com.zhikaixu.apipassenger.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public ResponseResult refreshToken(String refreshTokenSrc) {
        // 解析refresh token

        // 读取redis中的refreshToken

        // 校验refreshToken

        // 生成双token

        return null;
    }
}
