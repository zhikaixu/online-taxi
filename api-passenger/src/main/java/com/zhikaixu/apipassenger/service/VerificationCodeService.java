package com.zhikaixu.apipassenger.service;

import com.alibaba.nacos.api.utils.StringUtils;
import com.zhikaixu.apipassenger.remote.ServicePassengerUserClient;
import com.zhikaixu.apipassenger.remote.ServiceVerificationcodeClient;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.VerificationCodeDTO;
import com.zhikaixu.internalcommon.response.NumberCodeResponse;
import com.zhikaixu.internalcommon.response.TokenResponse;
import com.zhikaixu.internalcommon.util.JwtUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    // 验证码redis key前缀
    private String verificationCodePrefix = "passenger-verification-code-";

    // token redis key前缀
    private String tokenPredix = "token-";

    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate; // key 和 value 都是string

    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;

    /**
     * 获取验证码
     * @param passengerPhone
     * @return
     */
    public ResponseResult generateCode(String passengerPhone) {
        // 1. 调用验证码服务，获得验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("生成的验证码: " + numberCode);
        // 2. 将验证码存入Redis
        stringRedisTemplate.opsForValue().set(generateKeyByPhone(passengerPhone), numberCode+"", 2, TimeUnit.MINUTES);

        // 3. 通过短信服务商，将对应的验证码发送到手机上。
        return ResponseResult.success(null);
    }

    /**
     * 校验验证码
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        // 1. 根据手机号，去Redis读取验证码
        System.out.println("根据手机号，去Redis读取验证码");

        // 生成key
        String key = generateKeyByPhone(passengerPhone);

        // 根据key获取value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value: " + codeRedis);

        // 2. 校验验证码
        System.out.println("校验验证码");
        if (StringUtils.isBlank(codeRedis)) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        if (!verificationCode.trim().equals(codeRedis.trim())) {
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        // 3. 判断原来是否有用户，并做对应处理
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);

        // 4. 颁发令牌
        String token = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY);

        // 将token存入redis中
        String tokenKey = generateTokenKey(passengerPhone, IdentityConstant.PASSENGER_IDENTITY);
        stringRedisTemplate.opsForValue().set(tokenKey, token, 30, TimeUnit.DAYS);


        // 5. 响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        return ResponseResult.success(tokenResponse);
    }

    /**
     * 根据手机号，生成key
     * @param passengerPhone
     * @return
     */
    private String generateKeyByPhone(String passengerPhone) {
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 根据手机号和身份，生成token key
     * @param phone
     * @param identity
     * @return
     */
    private String generateTokenKey(String phone, String identity) {
        return tokenPredix + phone + "-" + identity;
    }
}
