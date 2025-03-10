package com.zhikaixu.apipassenger.service;

import com.zhikaixu.apipassenger.remote.ServiceOrderClient;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.constant.OrderConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.apipassenger.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    public ResponseResult add(OrderRequest orderRequest) {
        return serviceOrderClient.add(orderRequest);
    }

    public ResponseResult book(OrderRequest orderRequest) {
        return serviceOrderClient.add(orderRequest);
    }

    public ResponseResult cancel(Long orderId) {
        return serviceOrderClient.cancel(orderId, IdentityConstant.PASSENGER_IDENTITY);
    }
}
