package com.zhikaixu.apidriver.service;

import com.zhikaixu.apidriver.remote.ServiceDriverUserClient;
import com.zhikaixu.apidriver.remote.ServiceMapClient;
import com.zhikaixu.internalcommon.dto.Car;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.ApiDriverPointRequest;
import com.zhikaixu.internalcommon.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    private ServiceMapClient serviceMapClient;

    public ResponseResult upload(ApiDriverPointRequest apiDriverPointRequest) {
        // 获取carId
        Long carId = apiDriverPointRequest.getCarId();

        // 通过carId，获取tid，trid，调用service-driver-user接口
        ResponseResult<Car> carById = serviceDriverUserClient.getCarById(carId);
        Car car = carById.getData();
        String tid = car.getTid();
        String trid = car.getTrid();

        // 调用地图去上传
        PointRequest pointRequest = new PointRequest();
        pointRequest.setTid(tid);
        pointRequest.setTrid(trid);
        pointRequest.setPoints(apiDriverPointRequest.getPoints());
        return serviceMapClient.upload(pointRequest);

    }
}
