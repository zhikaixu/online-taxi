package com.zhikaixu.serviceorder.controller;


import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.serviceorder.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult add(@RequestBody OrderRequest orderRequest) {
        log.info("service-order:" + orderRequest.getAddress());

        return orderInfoService.add(orderRequest);
    }

    @Autowired
    private OrderInfoService orderService;

}
