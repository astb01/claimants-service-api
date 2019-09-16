package com.tdd.claimantsservice.validators;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RefNoValidator implements ConstraintValidator<ValidRefNo, String> {
    public static final String REFNO_REGEX = "^\\s*[a-zA-Z]{2}(?:\\s*\\d\\s*){6}[a-zA-Z]?\\s*$";

    @Override
    public void initialize(ValidRefNo validRefNo) {
    }

    @Override
    public boolean isValid(String nino, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(nino)){
            return false;
        }

        return nino.matches(REFNO_REGEX);
    }
}
