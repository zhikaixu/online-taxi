package com.zhikaixu.apipassenger.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhikaixu.apipassenger.constraints.DateTimeRange;
import com.zhikaixu.apipassenger.constraints.DicCheck;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class OrderRequest {

    // 订单Id
    @Positive(message = "订单号必须为正数")
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
    @Pattern(regexp = "^\\d{6}$", message = "行政区域不正确")
    private String address;

    // 出发时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "出发时间为空")
    @DateTimeRange(judge = DateTimeRange.IS_AFTER, message = "出发时间不正确")
    private LocalDateTime departTime;

    // 下单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime = LocalDateTime.now();

    // 出发地址
    @NotNull(message = "出发地址为空")
    @Length(min = 2, message = "出发地址长度要大于2")
    private String departure;

    // 出发经度
    @NotBlank(message = "起点经度不能为空")
    @Pattern(regexp = "^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}.\\d{1,6}|180\\.0{1,6})$", message = "请输入正确的起点经度")
    private String depLongitude;

    // 出发纬度
    @NotBlank(message = "起点纬度不能为空")
    @Pattern(regexp = "^[\\-\\+]?([0-8]\\d{1}\\.\\d{1,6}|90\\.0{1,6})$", message = "请输入正确的起点纬度")
    private String depLatitude;

    // 目的地地址
    @NotNull(message = "目的地地址为空")
    @Length(min = 2, message = "目的地地址长度太短")
    private String destination;

    // 目的地经度
    @NotBlank(message = "终点经度不能为空")
    @Pattern(regexp = "^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,6}|1[0-7]\\d{1}.\\d{1,6}|180\\.0{1,6})$", message = "请输入正确的终点经度")
    private String destLongitude;

    // 目的地纬度
    @NotBlank(message = "终点纬度不能为空")
    @Pattern(regexp = "^[\\-\\+]?([0-8]\\d{1}\\.\\d{1,6}|90\\.0{1,6})$", message = "请输入正确的终点纬度")
    private String destLatitude;

    // 坐标加密标识：1: gcj-02, 2: wgs84, 3: bd-09, 4: cgcs2000北斗, 0: 其他
    @NotNull(message = "坐标加密标识为空")
    @DicCheck(dicValue = {"1", "2", "3", "4", "0"}, message = "坐标加密标识不正确")
    private Integer encrypt;

    // 运价类型编码
    @NotBlank(message = "运价类型编码为空")
    @Pattern(regexp = "^\\d{6}\\$\\d{1}$", message = "运价类型编码不正确")
    private String fareType;

    // 运价版本
    @NotNull(message = "运价版本编码为空")
    @Positive(message = "运价版本要为正数")
    private Integer fareVersion;

    // 请求设备唯一码
    @NotBlank(message = "请求设备唯一码为空")
    @Length(min = 8, message = "出发地址长度要大于8")
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
