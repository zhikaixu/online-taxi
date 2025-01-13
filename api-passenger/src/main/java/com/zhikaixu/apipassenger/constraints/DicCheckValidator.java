package com.zhikaixu.apipassenger.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class DicCheckValidator implements ConstraintValidator<DicCheck, Object> {

    private List<String> dicCheckValue = null;

    @Override
    public void initialize(DicCheck constraintAnnotation) {
        dicCheckValue = Arrays.asList(constraintAnnotation.dicValue());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        if ((obj instanceof String) && dicCheckValue.contains(obj)) {
            return true;
        }
        if ((obj instanceof Integer)) {
            String str = String.valueOf(obj);
            return dicCheckValue.contains(str);
        }
        return false;
    }
}
