package com.zhikaixu.serviceorder.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service("grabByZookeeperCuratorService")
public class GrabByZookeeperCuratorServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        ResponseResult grab = null;
        // 创建持久节点
        Long orderId = driverGrabRequest.getOrderId();
        String parentNode = "/order-" + orderId;

        InterProcessMutex lock = new InterProcessMutex(curatorFramework, parentNode);

        try {
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                // 执行业务逻辑
                System.out.println("开始锁 zookeeper curator");
                grab = orderInfoService.grab(driverGrabRequest);
                System.out.println("结束锁 zookeeper curator");
            } else {
                grab = ResponseResult.fail(CommonStatusEnum.ORDER_CANNOT_GRAB.getCode(), CommonStatusEnum.ORDER_CANNOT_GRAB.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return grab;
    }
}
