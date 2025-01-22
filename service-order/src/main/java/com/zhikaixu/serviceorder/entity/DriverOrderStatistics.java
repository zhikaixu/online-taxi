package com.zhikaixu.serviceorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhikaixu
 * @since 2025-01-22
 */
@Data
@TableName("driver_order_statistics")
public class DriverOrderStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long driverId;

    /**
     * 司机抢单成功数量
     */
    private Integer grabOrderSuccessCount;

    private LocalDate grabOrderDate;
}
