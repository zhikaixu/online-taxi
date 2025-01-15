package com.zhikaixu.serviceorder.service.impl;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("grabByMultiRedisService")
public class GrabByMultiRedisServiceImpl implements GrabService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {

        System.out.println("开始锁multi redis");
        ResponseResult grab = orderInfoService.grab(driverGrabRequest);
        System.out.println("结束锁multi redis");

        return grab;
    }
}
