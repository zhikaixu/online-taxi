package com.zhikaixu.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-26
 */
@Data
public class DriverUserWorkStatus implements Serializable {

    private Long id;

    private Long driverId;

    private Integer workStatus;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;
}
