package com.zhikaixu.serviceorder.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("grabByRedisDiyService")
public class GrabByRedisDiyServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RenewRedisLock renewRedisLock;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        ResponseResult grab = null;
        String orderId = driverGrabRequest.getOrderId() + "";
        String driverId = driverGrabRequest.getDriverId() + "";
        String key = orderId;
        String value = driverId + "-" + UUID.randomUUID();
        // 设置加锁的key
//        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(orderId, driverId);
        // 如果此时断电，没有执行后续业务逻辑，key/val永远被存在了redis内，导致这个订单永远无法被抢 -> 死锁
        // 解决办法：1. 让别人释放这个锁 （不可行，A，B，C三人，A挂了，让B去释放A的锁，但是这个订单可能是C的）
        // 2. 让锁自动过期（可行）
//        stringRedisTemplate.expire(orderId, 20, TimeUnit.SECONDS);
        // 另一个问题：加锁完毕之后断电，没有设置过期
        // 解决方法：让这两个操作是原子的
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(key, value, 20, TimeUnit.SECONDS);
        renewRedisLock.renewRedisLock(key, value, 20);
        // 如果程序超过20s还没有执行完，此时redis中的锁已经被删除，那么其他请求可以抢这个单了
        if (b) {
            System.out.println("开始锁redis diy");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            grab = orderInfoService.grab(driverGrabRequest);
            System.out.println("结束锁redis diy");

            String s = stringRedisTemplate.opsForValue().get(key);
            // 解决请求1删除请求2的锁的问题：谁加的锁谁删除
            if (s.equals(value)) {
                stringRedisTemplate.delete(key);
            }

        } else {
            return ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(), CommonStatusEnum.ORDER_GRABING.getValue());
        }

        return grab;
    }
}
