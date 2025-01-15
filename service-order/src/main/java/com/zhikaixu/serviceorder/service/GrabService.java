package com.zhikaixu.serviceorder.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;

public interface GrabService {

    public ResponseResult grab(DriverGrabRequest driverGrabRequest);
}
