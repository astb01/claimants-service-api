package com.tdd.claimantsservice.validators;

import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClaimantValidatorTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldHaveErrorsWhenDrivingLicenceIsNotValid() {
        Claimant claimant = ClaimantTestBuilder.aClaimant().drivingLicenceNo("SAD").build();

        Set<ConstraintViolation<Claimant>> result = validator.validate(claimant);
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("messageTemplate")
                .contains("Driving Licence is invalid");
    }
}
