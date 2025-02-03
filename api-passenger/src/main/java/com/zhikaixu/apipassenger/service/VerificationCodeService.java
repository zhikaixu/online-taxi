package com.zhikaixu.apipassenger.service;

import com.alibaba.nacos.api.utils.StringUtils;
import com.zhikaixu.apipassenger.remote.ServicePassengerUserClient;
import com.zhikaixu.apipassenger.remote.ServiceVerificationcodeClient;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.constant.TokenConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.VerificationCodeDTO;
import com.zhikaixu.internalcommon.response.NumberCodeResponse;
import com.zhikaixu.internalcommon.response.TokenResponse;
import com.zhikaixu.internalcommon.util.JwtUtils;
import com.zhikaixu.internalcommon.util.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

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
        stringRedisTemplate.opsForValue().set(RedisPrefixUtils.generateKeyByPhone(passengerPhone, IdentityConstant.PASSENGER_IDENTITY), numberCode+"", 2, TimeUnit.MINUTES);

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
        String key = RedisPrefixUtils.generateKeyByPhone(passengerPhone, IdentityConstant.PASSENGER_IDENTITY);

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
        try {
            servicePassengerUserClient.loginOrRegister(verificationCodeDTO);
        } catch (RuntimeException e) {
            return ResponseResult.fail(CommonStatusEnum.CALL_USER_ADD_ERROR.getCode(), CommonStatusEnum.CALL_USER_ADD_ERROR.getValue());
        }
        // 4. 颁发令牌
        String accessToken = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        String refreshToken = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstants.REFRESH_TOKEN_TYPE);
        // 开启redis事务
        stringRedisTemplate.setEnableTransactionSupport(true);
        SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // 事务开始
                stringRedisTemplate.multi();
                try {
                    // 将token存入redis中
                    String accessTokenKey = RedisPrefixUtils.generateTokenKey(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
                    operations.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
                    int i = 1 / 0;
                    String refreshTokenKey = RedisPrefixUtils.generateTokenKey(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstants.REFRESH_TOKEN_TYPE);
                    operations.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);
                    operations.exec();
                    return true;
                } catch (Exception e) {
                    operations.discard();
                    return false;
                }

            }
        };

        Boolean execute = stringRedisTemplate.execute(callback);
        System.out.println("事务提交or回滚: " + execute);

        if (execute) {
            // 5. 响应
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setRefreshToken(refreshToken);
            return ResponseResult.success(tokenResponse);
        } else {
            return ResponseResult.fail(CommonStatusEnum.CHECK_CODE_ERROR.getCode(), CommonStatusEnum.CHECK_CODE_ERROR.getValue());
        }

    }
}
