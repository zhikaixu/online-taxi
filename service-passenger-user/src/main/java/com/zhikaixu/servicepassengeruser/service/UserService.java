package com.zhikaixu.servicepassengeruser.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicepassengeruser.dto.PassengerUser;
import com.zhikaixu.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult loginOrRegister(String passengerPhone) {

        System.out.println("user service被调用，手机号: " + passengerPhone);
        // 根据手机号查询用户信息
        Map<String, Object> map = new HashMap<>();
        map.put("passenger_phone", passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        System.out.println(passengerUsers.isEmpty()?"无记录":passengerUsers.get(0).getPassengerName());
        // 判断用户信息是否存在

        // 如果不存在，插入用户信息

        return ResponseResult.success();
    }

}
