package com.zhikaixu.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhikaixu.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    // salt
    private static final String SIGN = "aS#j!!sfd%$$";

    private static final String JWT_KEY_PHONE = "phone";

    // 乘客是1，司机是2
    private static final String JWT_KEY_IDENTITY = "identity";

    // 双token类型
    private static final String JWT_TOKEN_TYPE = "tokenType";

    // tokenTime
    private static final String JWT_TOKEN_TIME = "tokenTime";

    // 生成Token
    public static String generatorToken(String phone, String identity, String tokenType) {
        Map<String, String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE, phone);
        map.put(JWT_KEY_IDENTITY, identity);
        map.put(JWT_TOKEN_TYPE, tokenType);

        // token过期时间
        map.put(JWT_TOKEN_TIME, Calendar.getInstance().getTime().toString());

        JWTCreator.Builder builder = JWT.create();
        // 整合map
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        // 整合过期时间
        // 现在因为将token存入redis，所以不需要过期时间了
//        builder.withExpiresAt(date);

        // 生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }

    // 遇到的问题：在测试时，发现Redis中的token与生成的token一致，但是请求/authTest却报invalid token
    // 排查步骤：断点->发现拼接key时的手机号以及identity有两个引号了->parseToken的问题->发现是toString()
    // 原因：拦截器拦截时parseToken的使用了toString(),因此在引号外又包了一层引号，改成asString()
    // 解决方法：改成asString()

    // 解析Token
    public static TokenResult parseToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();
        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;
    }

    public static TokenResult checkToken(String token) {
        TokenResult tokenResult = null;
        try {
            tokenResult = JwtUtils.parseToken(token);
        } catch (Exception e) {
        }
        return tokenResult;
    }


    public static void main(String[] args) {

        String s = generatorToken("15906669101", "1", "accessToken");
        System.out.println("生成的JWT token: " + s);

        System.out.println("解析----------------");
        TokenResult tokenResult = parseToken(s);
        System.out.println("手机号：" + tokenResult.getPhone());
        System.out.println("身份：" + tokenResult.getIdentity());
    }
}
