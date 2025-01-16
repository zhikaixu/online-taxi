package com.zhikaixu.serviceorder.service.impl;

import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("grabByRedisLuaService")
public class GrabByRedisLuaServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RenewRedisLock renewRedisLock;

    @Autowired
    @Qualifier("redisSetScript")
    private DefaultRedisScript<Boolean> redisSetScript;

    @Autowired
    @Qualifier("redisDelScript")
    DefaultRedisScript<Boolean> redisDelScript;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        ResponseResult grab = null;
        String orderId = driverGrabRequest.getOrderId() + "";
        String driverId = driverGrabRequest.getDriverId() + "";
        String key = orderId;
        String value = driverId + "-" + UUID.randomUUID();


        // 使用Lua设置加锁的key，设置过期时间，避免死锁
        List<String> strings = Arrays.asList(key, value);
        Boolean b = stringRedisTemplate.execute(redisSetScript, strings, "30");


        // 如果程序超过20s还没有执行完，此时redis中的锁已经被删除，那么其他请求可以抢这个单了
        if (b) {
            renewRedisLock.renewRedisLock(key, value, 20);
            System.out.println("开始锁redis diy");
            grab = orderInfoService.grab(driverGrabRequest);
            System.out.println("结束锁redis diy");

            // 解决请求1删除请求2的锁的问题：谁加的锁谁删除
            List<String> keyArg = Arrays.asList(key);
            Boolean dbool = stringRedisTemplate.execute(redisDelScript, keyArg, value);
            System.out.println("删除：" + dbool);

        } else {
            return ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(), CommonStatusEnum.ORDER_GRABING.getValue());
        }

        return grab;
    }
}
