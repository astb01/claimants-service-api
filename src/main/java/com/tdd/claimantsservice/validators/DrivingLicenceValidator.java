package com.tdd.claimantsservice.validators;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DrivingLicenceValidator implements ConstraintValidator<ValidDrivingLicence, String> {
    private static final String REGEX = "^[a-zA-Z]{2,}\\d{6}[a-zA-Z0-9]{6}$";

    @Override
    public void initialize(ValidDrivingLicence validDrivingLicence) {
    }

    @Override
    public boolean isValid(String drivingLicenceNo, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isNotEmpty(drivingLicenceNo)){
            return drivingLicenceNo.matches(REGEX);
        }
        return true;
    }
}
