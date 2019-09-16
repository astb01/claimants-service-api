package com.tdd.claimantsservice.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RefNoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRefNo {
    String message() default "Invalid Reference Number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
