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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 保存车辆
        carMapper.insert(car);

        // 获得此车辆对应的终端id：tid
        Long id = car.getId();
        ResponseResult<TerminalResponse> terminalResponseResult = serviceMapClient.addTerminal(car.getVehicleNo(), id+"");
        String tid = terminalResponseResult.getData().getTid();
        car.setTid(tid);

        // 获得此车辆的轨迹id：trid
        ResponseResult<TrackResponse> trackResponseResult = serviceMapClient.addTrack(tid);
        String trid = trackResponseResult.getData().getTrid();
        String trname = trackResponseResult.getData().getTrname();
        car.setTrid(trid);
        car.setTrname(trname);

        carMapper.updateById(car);

        return ResponseResult.success("");
    }

    public ResponseResult<Car> getCarById(Long carId) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", carId);
        List<Car> cars = carMapper.selectByMap(map);

        return ResponseResult.success(cars.get(0));
    }
}
