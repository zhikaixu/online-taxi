package com.zhikaixu.internalcommon.response;

import lombok.Data;

@Data
public class OrderDriverResponse {

    private Long driverId;

    private String driverPhone;

    private Long carId;

    private String licenseId;

    private String vehicleNo;

    private String vehicleType;
}
