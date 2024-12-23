package com.zhikaixu.apidriver.service;

import com.zhikaixu.apidriver.remote.ServiceDriverUserClient;
import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult updateUser(@RequestBody DriverUser driverUser) {
        return serviceDriverUserClient.updateUser(driverUser);
    }
}
