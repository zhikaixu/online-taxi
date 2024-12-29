package com.zhikaixu.apipassenger.service;

import com.zhikaixu.apipassenger.remote.ServiceOrderClient;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    public ResponseResult add(OrderRequest orderRequest) {
        return serviceOrderClient.add(orderRequest);
    }
}
