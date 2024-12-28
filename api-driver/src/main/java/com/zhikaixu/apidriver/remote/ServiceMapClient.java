package com.zhikaixu.apidriver.remote;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.PointRequest;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import com.zhikaixu.internalcommon.response.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {

    @RequestMapping(method = RequestMethod.POST, value = "/point/upload")
    public ResponseResult upload(@RequestBody PointRequest pointRequest);
}
