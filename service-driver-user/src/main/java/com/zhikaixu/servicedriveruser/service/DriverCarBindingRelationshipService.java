package com.zhikaixu.servicedriveruser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikaixu.internalcommon.constant.DriverCarConstants;
import com.zhikaixu.internalcommon.dto.DriverCarBindingRelationship;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-24
 */
@Service
public class DriverCarBindingRelationshipService {

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship) {
        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);

        driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);

        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success("");
    }
}
