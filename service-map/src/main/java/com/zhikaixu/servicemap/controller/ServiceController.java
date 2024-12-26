package com.zhikaixu.servicemap.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicemap.service.ServiceMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceMapService serviceMapService;

    /**
     * 创建服务
     * @param name
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(String name) {

        return serviceMapService.add(name);
    }
}
