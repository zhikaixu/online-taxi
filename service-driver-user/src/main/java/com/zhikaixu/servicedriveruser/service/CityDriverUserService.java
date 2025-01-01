package com.zhikaixu.servicedriveruser.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityDriverUserService {

    @Autowired
    private DriverUserMapper driverUserMapper;

    public ResponseResult<Boolean> isDriverAvailable(String cityCode) {
        int i = driverUserMapper.selectDriverUserCountByCityCode(cityCode);
        if (i > 0) {
            return ResponseResult.success(true);
        } else {
            return ResponseResult.success(false);
        }
    }
}
