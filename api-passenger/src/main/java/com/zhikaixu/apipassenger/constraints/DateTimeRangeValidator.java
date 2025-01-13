package com.zhikaixu.apipassenger.constraints;

import org.apache.tomcat.jni.Local;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeRangeValidator implements ConstraintValidator<DateTimeRange, Object> {

    private DateTimeRange dateTimeRange;

    @Override
    public void initialize(DateTimeRange constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object paramDate, ConstraintValidatorContext constraintValidatorContext) {

        // 用户传进来的日期参数
        LocalDateTime dataValue = null;

        if (paramDate == null) {
            return false;
        }

        if (paramDate instanceof LocalDateTime) {
            dataValue = (LocalDateTime) paramDate;
        }

        if (paramDate instanceof String) {
            dataValue = LocalDateTime.parse((String) paramDate, DateTimeFormatter.ofPattern(dateTimeRange.pattern()));
        }

        LocalDateTime now = LocalDateTime.now();

        if (dataValue.isAfter(now)) {
            return true;
        }

        return false;
    }
}
