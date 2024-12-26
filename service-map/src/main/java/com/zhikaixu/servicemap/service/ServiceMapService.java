package com.zhikaixu.servicemap.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicemap.remote.ServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapService {

    @Autowired
    private ServiceClient serviceClient;

    /**
     * 创建服务
     * @param name
     * @return
     */
    public ResponseResult add(String name) {

        return serviceClient.add(name);
    }
}
