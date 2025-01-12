package com.zhikaixu.apipassenger.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class VehicleTypeCheckValidator implements ConstraintValidator<VehicleTypeCheck, String> {

    private List<String> vehicleTypeCheckValue = null;

    @Override
    public void initialize(VehicleTypeCheck constraintAnnotation) {
        vehicleTypeCheckValue = Arrays.asList(constraintAnnotation.vehicleTypeValue());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (vehicleTypeCheckValue.contains(s)) {
            return true;
        }
        return false;
    }
}
