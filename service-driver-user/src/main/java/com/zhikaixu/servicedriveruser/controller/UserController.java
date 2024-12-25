package com.zhikaixu.servicedriveruser.controller;

import com.zhikaixu.internalcommon.dto.DriverUser;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.DriverUserExistsResponse;
import com.zhikaixu.servicedriveruser.service.DriverUserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private DriverUserService driverUserService;

    @PostMapping("/user")
    public ResponseResult addUser(@RequestBody DriverUser driverUser) {
        JSONObject jsonObject = new JSONObject(driverUser);
        log.info(jsonObject.toString());
        return driverUserService.addDriverUser(driverUser);
    }

    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser) {
        JSONObject jsonObject = new JSONObject(driverUser);
        log.info(jsonObject.toString());

        return driverUserService.updateDriverUser(driverUser);
    }

    @GetMapping("/check-driver/{driverPhone}")
    public ResponseResult getUser(@PathVariable("driverPhone") String driverPhone) {

        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        DriverUser driverUserDb = driverUserByPhone.getData();

        DriverUserExistsResponse response = new DriverUserExistsResponse();
        int ifExists = 1;
        if (driverUserDb == null) {
            ifExists = 0;
            response.setDriverPhone(driverPhone);
            response.setIfExists(ifExists);
        } else {
            response.setDriverPhone(driverUserDb.getDriverPhone());
            response.setIfExists(ifExists);
        }


        return ResponseResult.success(response);
    }
}
