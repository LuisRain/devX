package com.william.devx.core.validation;

import com.william.devx.common.$;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation of the IdNumber.
 */
public class IdNumberValidator implements ConstraintValidator<IdNumber, String> {

    @Override
    public void initialize(IdNumber constraintAnnotation) {
        // doNothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(value) || $.field.validateIdNumber(value);
    }
}
