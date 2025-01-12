package com.zhikaixu.apipassenger.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleTypeCheckValidator.class)
public @interface VehicleTypeCheck {
    /**
     * 车辆类型的选项
     * @return
     */
    String[] vehicleTypeValue() default {};

    /**
     * 提示信息
     * @return
     */
    String message() default "车辆类型不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
