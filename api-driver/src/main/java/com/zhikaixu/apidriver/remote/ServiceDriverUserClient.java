package com.zhikaixu.apidriver.remote;

import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser);
}
