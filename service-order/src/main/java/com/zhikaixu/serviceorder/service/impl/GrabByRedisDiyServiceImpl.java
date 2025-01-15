package com.zhikaixu.serviceorder.service.impl;

import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("grabByRedisDiyService")
public class GrabByRedisDiyServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        ResponseResult grab = null;
        String orderId = driverGrabRequest.getOrderId() + "";
        String driverId = driverGrabRequest.getDriverId() + "";
        // 设置加锁的key
//        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(orderId, driverId);
        // 如果此时断电，没有执行后续业务逻辑，key/val永远被存在了redis内，导致这个订单永远无法被抢 -> 死锁
        // 解决办法：1. 让别人释放这个锁 （不可行，A，B，C三人，A挂了，让B去释放A的锁，但是这个订单可能是C的）
        // 2. 让锁自动过期（可行）
//        stringRedisTemplate.expire(orderId, 20, TimeUnit.SECONDS);
        // 另一个问题：加锁完毕之后断电，没有设置过期
        // 解决方法：让这两个操作是原子的
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(orderId, driverId, 20, TimeUnit.SECONDS);

        if (b) {
            System.out.println("开始锁redis diy");
            grab = orderInfoService.grab(driverGrabRequest);
            System.out.println("结束锁redis diy");

            stringRedisTemplate.delete(orderId);
        } else {
            return ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(), CommonStatusEnum.ORDER_GRABING.getValue());
        }



        return grab;
    }
}
