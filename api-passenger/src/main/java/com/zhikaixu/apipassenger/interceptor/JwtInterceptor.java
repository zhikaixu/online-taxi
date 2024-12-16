package com.zhikaixu.apipassenger.interceptor;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zhikaixu.internalcommon.constant.TokenConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.dto.TokenResult;
import com.zhikaixu.internalcommon.util.JwtUtils;
import com.zhikaixu.internalcommon.util.RedisPrefixUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean result = true;
        String resultString = "";

        // 解析token
        String token = request.getHeader("Authorization");
        TokenResult tokenResult = JwtUtils.checkToken(token);

        // 从redis中取出token
        if (tokenResult == null) {
            resultString = "token invalid";
            result = false;
        } else {
            // 拼接key
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, TokenConstants.ACCESS_TOKEN_TYPE);
            // 从redis中取出token
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            if (StringUtils.isBlank(tokenRedis) || (!token.trim().equals(tokenRedis.trim()))) {
                resultString = "token invalid";
                result = false;
            }
        }

        if (!result) {
            PrintWriter out = response.getWriter();
            JSONObject jsonObject = new JSONObject(ResponseResult.fail(resultString));
            out.print(jsonObject);
        }

        return result;
    }
}
