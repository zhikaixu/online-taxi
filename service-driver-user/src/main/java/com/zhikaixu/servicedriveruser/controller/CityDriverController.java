package com.zhikaixu.servicedriveruser.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.service.CityDriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverController {

    @Autowired
    private CityDriverUserService cityDriverUserService;

    /**
     * 根据cityCode查询当前城市是否有可用司机
     * @param cityCode
     * @return
     */
    @GetMapping("/is-driver-available")
    public ResponseResult isDriverAvailable(String cityCode) {
        return cityDriverUserService.isDriverAvailable(cityCode);
    }
}
