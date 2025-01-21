package com.zhikaixu.serviceorder.service.impl;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("grabByRedissonRedLockService")
public class GrabByRedissonRedLockServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    @Qualifier("redissonClient1")
    private RedissonClient redissonClient1;

    @Autowired
    @Qualifier("redissonClient2")
    private RedissonClient redissonClient2;

    @Autowired
    @Qualifier("redissonClient3")
    private RedissonClient redissonClient3;

    @Autowired
    @Qualifier("redissonClient4")
    private RedissonClient redissonClient4;

    @Autowired
    @Qualifier("redissonClient5")
    private RedissonClient redissonClient5;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        String orderId = driverGrabRequest.getOrderId() + "";
        String key = orderId;

        // 红锁
        RLock rLock1 = redissonClient1.getLock(key);
        RLock rLock2 = redissonClient2.getLock(key);
        RLock rLock3 = redissonClient3.getLock(key);
        RLock rLock4 = redissonClient4.getLock(key);
        RLock rLock5 = redissonClient5.getLock(key);

        RedissonRedLock lock = new RedissonRedLock(rLock1, rLock2, rLock3, rLock4, rLock5);
        lock.lock();

        System.out.println("开始锁redis redlock");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseResult grab = orderInfoService.grab(driverGrabRequest);
        System.out.println("结束锁redis redlock");
        lock.unlock();
        return grab;
    }
}
