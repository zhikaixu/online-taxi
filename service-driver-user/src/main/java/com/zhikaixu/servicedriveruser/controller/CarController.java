package com.zhikaixu.servicedriveruser.controller;


import com.zhikaixu.internalcommon.dto.Car;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicedriveruser.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-24
 */
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car) {

        return carService.addCar(car);
    }

    @GetMapping("/car/{carId}")
    public ResponseResult<Car> getCarById(@PathVariable("carId") Long carId) {
        return carService.getCarById(carId);
    }
}
