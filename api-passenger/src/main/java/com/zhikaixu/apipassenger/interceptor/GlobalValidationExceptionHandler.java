package com.zhikaixu.apipassenger.interceptor;

import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 校验异常处理类
 */
@RestControllerAdvice
@Order(1)
public class GlobalValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult validationExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseResult.fail(CommonStatusEnum.VALIDATION_EXCEPTION.getCode(), CommonStatusEnum.VALIDATION_EXCEPTION.getValue() + ": " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = "";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation c : constraintViolations) {
            message = c.getMessage();
        }
        return ResponseResult.fail(CommonStatusEnum.VALIDATION_EXCEPTION.getCode(), CommonStatusEnum.VALIDATION_EXCEPTION.getValue() + ": " + message);
    }
}
