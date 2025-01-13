package com.zhikaixu.apipassenger.controller;

import com.zhikaixu.apipassenger.service.OrderService;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.apipassenger.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
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

    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId) {

        return orderService.cancel(orderId);
    }
}
