package com.zhikaixu.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.DriverCarConstants;
import com.zhikaixu.internalcommon.dto.DriverCarBindingRelationship;
import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.DriverUserWorkStatus;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.OrderDriverResponse;
import com.zhikaixu.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.zhikaixu.servicedriveruser.mapper.DriverUserMapper;
import com.zhikaixu.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserService {

    @Autowired
    private DriverUserMapper driverUserMapper;

    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult testGetDriverUser() {

        DriverUser driverUser = driverUserMapper.selectById(1);
        return ResponseResult.success(driverUser);
    }

    public ResponseResult addDriverUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);
        driverUserMapper.insert(driverUser);

        // 初始化 司机工作状态表
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_STOP);
        driverUserWorkStatus.setGmtCreate(now);
        driverUserWorkStatus.setGmtModified(now);

        driverUserWorkStatusMapper.insert(driverUserWorkStatus);

        return ResponseResult.success("");
    }

    public ResponseResult updateDriverUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModified(now);
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success("");
    }

    public ResponseResult<DriverUser> getDriverUserByPhone(String driverPhone) {
        Map<String, Object> map = new HashMap<>();
        map.put("driver_phone", driverPhone);
        map.put("state", DriverCarConstants.DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if (driverUsers.isEmpty()) {
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXISTS.getCode(), CommonStatusEnum.DRIVER_NOT_EXISTS.getValue());
        }
        DriverUser driverUser = driverUsers.get(0);
        return ResponseResult.success(driverUser);
    }

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult<OrderDriverResponse> getAvailableDriver(Long carId) {
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", carId);
        queryWrapper.eq("bind_state", DriverCarConstants.DRIVER_CAR_BIND);

        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(queryWrapper);
        Long driverId = driverCarBindingRelationship.getDriverId();

        QueryWrapper<DriverUserWorkStatus> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("driver_id", driverId);
        queryWrapper1.eq("work_status", DriverCarConstants.DRIVER_WORK_STATUS_START);

        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(queryWrapper1);
        if (driverUserWorkStatus == null) {
            return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(), CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
        } else {
            QueryWrapper<DriverUser> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("id", driverId);
            DriverUser driverUser = driverUserMapper.selectOne(queryWrapper2);
            OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
            orderDriverResponse.setCarId(carId);
            orderDriverResponse.setDriverId(driverId);
            orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());

            return ResponseResult.success(orderDriverResponse);
        }
    }
}
