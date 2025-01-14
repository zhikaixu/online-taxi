package com.zhikaixu.apipassenger.controller;

import com.zhikaixu.apipassenger.service.OrderService;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.apipassenger.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单/下单
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@Validated @RequestBody OrderRequest orderRequest) {
        System.out.println(orderRequest);
        return orderService.add(orderRequest);
    }

    /**
     * 创建预约单
     * @return
     */
    @PostMapping("/book")
    public ResponseResult book(@Validated @RequestBody OrderRequest orderRequest) {
        return orderService.book(orderRequest);
    }

    @PostMapping("/cancel")
    public ResponseResult cancel(@NotNull(message = "订单id不能为空") @Positive(message = "订单id要为正数") @RequestParam Long orderId) {

        return orderService.cancel(orderId);
    }
}
