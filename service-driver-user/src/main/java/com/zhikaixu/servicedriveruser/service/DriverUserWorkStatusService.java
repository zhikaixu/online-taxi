package com.zhikaixu.servicedriveruser.service;

import com.zhikaixu.internalcommon.dto.DriverUserWorkStatus;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-26
 */
@Service
public class DriverUserWorkStatusService {

    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult changeWorkStatus(Long driverId, Integer workStatus) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("driver_id", driverId);
        List<DriverUserWorkStatus> driverUserWorkStatuses = driverUserWorkStatusMapper.selectByMap(queryMap);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatuses.get(0);

        driverUserWorkStatus.setWorkStatus(workStatus);

        driverUserWorkStatusMapper.updateById(driverUserWorkStatus);

        return ResponseResult.success("");
    }
}
