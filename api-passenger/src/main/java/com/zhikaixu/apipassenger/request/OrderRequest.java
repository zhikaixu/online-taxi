package com.zhikaixu.apipassenger.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhikaixu.apipassenger.constraints.DateTimeRange;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OrderRequest {

    // 订单Id
    @Positive
    private Long orderId;

    // 乘客ID
    @NotNull
    @Positive
    private Long passengerId;

    // 乘客手机号
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请填写正确手机号")
    private String passengerPhone;

    // 下单行政区划
    @Pattern(regexp = "^\\d{6}$")
    private String address;

    // 出发时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    @DateTimeRange(message = "出发时间不正确")
    private LocalDateTime departTime;

    // 下单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    // 出发地址
    private String departure;

    // 出发经度
    private String depLongitude;

    // 出发纬度
    private String depLatitude;

    // 目的地地址
    private String destination;

    // 目的地经度
    private String destLongitude;

    // 目的地纬度
    private String destLatitude;

    // 坐标加密标识：1: gcj-02, 2: wgs84, 3: bd-09, 4: cgcs2000北斗, 0: 其他
    private Integer encrypt;

    // 运价类型编码
    private String fareType;

    // 运价版本
    private Integer fareVersion;

    // 请求设备唯一码
    private String deviceCode;

    /**
     * 司机去接乘客出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toPickUpPassengerTime;

    /**
     * 去接乘客时，司机的经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 去接乘客时，司机的纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 去接乘客时，司机的地点
     */
    private String toPickUpPassengerAddress;

    /**
     * 接到乘客，乘客上车时间
     */
    private LocalDateTime pickUpPassengerTime;

    /**
     * 接到乘客，乘客上车经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 接到乘客，乘客上车纬度
     */
    private String pickUpPassengerLatitude;

    /**
     * 乘客下车时间
     */
    private LocalDateTime passengerGetoffTime;

    /**
     * 乘客下车经度
     */
    private String passengerGetoffLongitude;

    /**
     * 乘客下车纬度
     */
    private String passengerGetoffLatitude;

    /**
     * 车辆类型
     */
    private String vehicleType;
}
