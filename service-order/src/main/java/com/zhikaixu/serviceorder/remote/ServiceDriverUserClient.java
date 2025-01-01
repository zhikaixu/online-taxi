package com.zhikaixu.serviceorder.remote;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @GetMapping("/city-driver/is-driver-available")
    public ResponseResult<Boolean> isDriverAvailable(@RequestParam String cityCode);
}
