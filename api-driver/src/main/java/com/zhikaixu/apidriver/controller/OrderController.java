package com.zhikaixu.apidriver.controller;

import com.zhikaixu.apidriver.service.ApiDriverOrderInfoService;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.dto.TokenResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.internalcommon.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ApiDriverOrderInfoService apiDriverOrderInfoService;

    /**
     * 司机抢单
     * @param orderRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/grab")
    public ResponseResult grab(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

        TokenResult tokenResult = JwtUtils.parseToken(token);
        String identity = tokenResult.getIdentity();
        String phone = tokenResult.getPhone();
        Long orderId = orderRequest.getOrderId();

        return ResponseResult.success("");
    }

    /**
     * 司机去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest) {

        return apiDriverOrderInfoService.toPickUpPassenger(orderRequest);
    }

    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest) {

        return apiDriverOrderInfoService.arrivedDeparture(orderRequest);
    }

    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest) {

        return apiDriverOrderInfoService.pickUpPassenger(orderRequest);
    }

    @PostMapping("/passenger-get-off")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest) {

        return apiDriverOrderInfoService.passengerGetOff(orderRequest);
    }

    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId) {

        return apiDriverOrderInfoService.cancel(orderId);
    }

}
