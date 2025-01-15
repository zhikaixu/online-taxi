package com.zhikaixu.serviceorder.controller;


import com.zhikaixu.internalcommon.constant.HeaderParamConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.serviceorder.service.GrabService;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 创建订单
     * @param orderRequest
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest) {
        // 测试通过，通过header获取deviceCode
//        String deviceCode = httpServletRequest.getHeader(HeaderParamConstants.DEVICE_CODE);
//        orderRequest.setDeviceCode(deviceCode);

        log.info("service-order:" + orderRequest.getAddress());

        return orderInfoService.add(orderRequest);
    }

    /**
     * 创建预约单
     * @param orderRequest
     * @return
     */
    @PostMapping("/book")
    public ResponseResult book(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest) {

        log.info("service-order:" + orderRequest.getAddress());

        return orderInfoService.book(orderRequest);
    }

    /**
     * 司机去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult changeStatus(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达乘客地点
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.pickUpPassenger(orderRequest);
    }

    /**
     * 司机行程结束，到达目的地
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-get-off")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.passengerGetOff(orderRequest);
    }

    @PostMapping("/push-pay-info")
    public ResponseResult pushPayInfo(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.pushPayInfo(orderRequest);
    }

    /**
     * 支付完成
     * @param orderRequest
     * @return
     */
    @PostMapping("/pay")
    public ResponseResult pay(@RequestBody OrderRequest orderRequest) {

        return orderInfoService.pay(orderRequest);
    }

    /**
     * 订单取消
     * @param orderId
     * @param identity
     * @return
     */
    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId, @RequestParam String identity) {

        return orderInfoService.cancel(orderId, identity);
    }

    @Autowired
//    @Qualifier("grabBySingleRedisService")
    @Qualifier("grabByMultiRedisService")
    private GrabService grabService;

    /**
     * 司机抢单
     * @param driverGrabRequest
     * @return
     */
    @PostMapping("/grab")
    public ResponseResult driverGrab(@RequestBody DriverGrabRequest driverGrabRequest) {

        return grabService.grab(driverGrabRequest);
    }

}
