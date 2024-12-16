package com.zhikaixu.apipassenger.service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.TokenConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.dto.TokenResult;
import com.zhikaixu.internalcommon.response.TokenResponse;
import com.zhikaixu.internalcommon.util.JwtUtils;
import com.zhikaixu.internalcommon.util.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult refreshToken(String refreshTokenSrc) {
        // 解析refresh token
        TokenResult tokenResult = JwtUtils.checkToken(refreshTokenSrc);
        if (tokenResult == null) {
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(), CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();

        // 读取redis中的refreshToken
        String refreshTokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, TokenConstants.REFRESH_TOKEN_TYPE);
        String refreshTokenRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);

        // 校验refreshToken
        if (StringUtils.isBlank(refreshTokenRedis) || (!refreshTokenSrc.trim().equals(refreshTokenRedis.trim()))) {
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(), CommonStatusEnum.TOKEN_ERROR.getValue());
        }

        // 生成双token
        // TODO: 这里的token是跟之前一样的
        String refreshToken = JwtUtils.generatorToken(phone, identity, TokenConstants.REFRESH_TOKEN_TYPE);
        String accessToken = JwtUtils.generatorToken(phone, identity, TokenConstants.ACCESS_TOKEN_TYPE);

        // 存入redis
        String accessTokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, TokenConstants.ACCESS_TOKEN_TYPE);

        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setRefreshToken(refreshToken);
        tokenResponse.setAccessToken(accessToken);

        return ResponseResult.success(tokenResponse);
    }
}
