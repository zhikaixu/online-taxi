package com.zhikaixu.apipassenger.interceptor;

import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult validationExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseResult.fail(CommonStatusEnum.VALIDATION_EXCEPTION.getCode(), CommonStatusEnum.VALIDATION_EXCEPTION.getValue() + ": " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
