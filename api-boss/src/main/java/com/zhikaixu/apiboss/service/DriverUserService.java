package com.zhikaixu.apiboss.service;

import com.zhikaixu.apiboss.remote.ServiceDriverUserClient;
import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverUserService {

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult addDriverUser(DriverUser driverUser) {
        return serviceDriverUserClient.addDriverUser(driverUser);
    }

    public ResponseResult updateDriverUser(DriverUser driverUser) {
        return serviceDriverUserClient.updateDriverUser(driverUser);
    }
}
