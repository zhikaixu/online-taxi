package com.zhikaixu.serviceorder.service.impl;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("grabByRedissonBasicService")
public class GrabByRedissonBasicServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    @Qualifier("redissonBootYml")
    private RedissonClient redissonClient;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        String orderId = driverGrabRequest.getOrderId() + "";
        String key = orderId;

        RLock lock = redissonClient.getLock(key);
        lock.lock();

        System.out.println("开始锁redis redisson basic");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseResult grab = orderInfoService.grab(driverGrabRequest);
        System.out.println("结束锁redis redisson basic");
        lock.unlock();
        return grab;
    }
}
