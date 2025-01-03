package com.zhikaixu.internalcommon.response;

import lombok.Data;

@Data
public class TerminalResponse {

    private String tid;

    private Long carId;

    private Long longitude;

    private Long latitude;

}
