package com.tdd.claimantsservice.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DrivingLicenceValidator.class)
@Target({ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidDrivingLicence {
    String message() default "Driving Licence is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
