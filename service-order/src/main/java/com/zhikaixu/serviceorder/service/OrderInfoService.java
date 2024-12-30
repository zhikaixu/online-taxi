package com.zhikaixu.serviceorder.service;

import com.zhikaixu.internalcommon.dto.OrderInfo;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.serviceorder.mapper.OrderInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@Service
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public ResponseResult add(OrderRequest orderRequest) {
        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest, orderInfo);

        orderInfoMapper.insert(orderInfo);
        return ResponseResult.success("");
    }
}
