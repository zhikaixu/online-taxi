package com.zhikaixu.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    // salt
    private static final String SIGN = "aS#j!!sfd%$$";

    // 生成Token
    public static String generatorToken(Map<String, String> map) {
        // token过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();

        JWTCreator.Builder builder = JWT.create();
        // 整合map
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        // 整合过期时间
        builder.withExpiresAt(date);

        // 生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }

    // 解析Token

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "zhikai");
        map.put("age", "18");
        String s = generatorToken(map);
        System.out.println("JWT token: " + s);
    }
}
