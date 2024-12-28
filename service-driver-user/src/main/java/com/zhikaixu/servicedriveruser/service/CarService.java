package com.zhikaixu.servicedriveruser.service;

import com.zhikaixu.internalcommon.dto.Car;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import com.zhikaixu.internalcommon.response.TrackResponse;
import com.zhikaixu.servicedriveruser.mapper.CarMapper;
import com.zhikaixu.servicedriveruser.remote.ServiceMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CarService {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private ServiceMapClient serviceMapClient;

    public ResponseResult addCar(Car car) {
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModified(now);

        // 获得此车辆对应的终端id：tid
        ResponseResult<TerminalResponse> terminalResponseResult = serviceMapClient.addTerminal(car.getVehicleNo());
        String tid = terminalResponseResult.getData().getTid();
        car.setTid(tid);

        // 获得此车辆的轨迹id：trid
        ResponseResult<TrackResponse> trackResponseResult = serviceMapClient.addTrack(tid);
        String trid = trackResponseResult.getData().getTrid();
        String trname = trackResponseResult.getData().getTrname();
        car.setTrid(trid);
        car.setTrname(trname);

        carMapper.insert(car);
        return ResponseResult.success("");
    }
}
