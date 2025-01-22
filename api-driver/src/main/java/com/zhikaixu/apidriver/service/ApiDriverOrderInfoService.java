package com.zhikaixu.apidriver.service;

import com.zhikaixu.apidriver.remote.ServiceDriverUserClient;
import com.zhikaixu.apidriver.remote.ServiceOrderClient;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.DriverCarConstants;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.constant.OrderConstants;
import com.zhikaixu.internalcommon.dto.DriverCarBindingRelationship;
import com.zhikaixu.internalcommon.dto.DriverUserWorkStatus;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.internalcommon.response.OrderDriverResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApiDriverOrderInfoService {

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {
        return serviceOrderClient.toPickUpPassenger(orderRequest);
    }

    public ResponseResult arrivedDeparture(OrderRequest orderRequest) {
        return serviceOrderClient.arrivedDeparture(orderRequest);
    }

    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
        return serviceOrderClient.pickUpPassenger(orderRequest);
    }

    public ResponseResult passengerGetOff(OrderRequest orderRequest) {
        return serviceOrderClient.passengerGetOff(orderRequest);
    }

    public ResponseResult cancel(Long orderId) {
        return serviceOrderClient.cancel(orderId, IdentityConstant.DRIVER_IDENTITY);
    }

    /**
     * 司机抢单
     * @param driverPhone
     * @param orderId
     * @return
     */
    public ResponseResult grab(String driverPhone, Long orderId, String receiveOrderCarLongitude, String receiveOrderCarLatitude) {
        // 根据司机的电话号码 查询车辆信息，为了向订单中补全司机和车辆信息
        ResponseResult<DriverCarBindingRelationship> driverCarBindingRelationshipResponseResult = serviceDriverUserClient.getDriverCarRelationship(driverPhone);
        if (driverCarBindingRelationshipResponseResult == null) {
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXISTS.getCode(), CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXISTS.getValue());
        }
        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipResponseResult.getData();
        Long carId = driverCarBindingRelationship.getCarId();

        ResponseResult<OrderDriverResponse> availableDriverResult = serviceDriverUserClient.getAvailableDriver(carId);
        if (availableDriverResult == null) {
            return ResponseResult.fail(CommonStatusEnum.CAR_NOT_EXISTS.getCode(), CommonStatusEnum.CAR_NOT_EXISTS.getValue());
        }

        OrderDriverResponse orderDriverResponse = availableDriverResult.getData();
        Long driverId = orderDriverResponse.getDriverId();
        String licenseId = orderDriverResponse.getLicenseId();
        String vehicleNo = orderDriverResponse.getVehicleNo();
        String vehicleType = orderDriverResponse.getVehicleType();

//        orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
        // 执行抢单动作
        DriverGrabRequest driverGrabRequest = new DriverGrabRequest();
        driverGrabRequest.setOrderId(orderId);
        driverGrabRequest.setDriverId(driverId);
        driverGrabRequest.setCarId(carId);
        driverGrabRequest.setDriverPhone(driverPhone);
        driverGrabRequest.setLicenseId(licenseId);
        driverGrabRequest.setVehicleNo(vehicleNo);
        driverGrabRequest.setVehicleType(vehicleType);
        driverGrabRequest.setReceiveOrderCarLatitude(receiveOrderCarLatitude);
        driverGrabRequest.setReceiveOrderCarLongitude(receiveOrderCarLongitude);

        ResponseResult responseResult = serviceOrderClient.driverGrab(driverGrabRequest);
        if (responseResult.getCode()!=CommonStatusEnum.SUCCESS.getCode()) {
            return ResponseResult.fail(CommonStatusEnum.ORDER_UPDATE_ERROR.getCode(), CommonStatusEnum.ORDER_UPDATE_ERROR.getValue());
        }
        // 修改司机工作状态
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverId);
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_SERVING);
        int i = 1 / 0;
        responseResult = serviceDriverUserClient.changeWorkStatus(driverUserWorkStatus);

        if (responseResult.getCode()!=CommonStatusEnum.SUCCESS.getCode()) {
            return ResponseResult.fail(CommonStatusEnum.DRIVER_STATUS_UPDATE_ERROR.getCode(), CommonStatusEnum.DRIVER_CAR_BIND_EXISTS.getValue());
        }

        return responseResult.success("");
    }

}
