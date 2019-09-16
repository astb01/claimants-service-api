package com.tdd.claimantsservice.validators;

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

import static com.tdd.claimantsservice.builders.ClaimantTestBuilder.aClaimant;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class RefNoValidatorTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldHaveNoErrorsWhenNinoIsValid() {
        Claimant claimant = createWithRefNo("AB123456D");

        Set<ConstraintViolation<Claimant>> result = validator.validate(claimant);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldHaveErrorsWhenNinoIsNull() {
        Claimant claimant = createWithRefNo(null);

        Set<ConstraintViolation<Claimant>> result = validator.validate(claimant);
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("messageTemplate").contains("Reference Number is required");
    }

    @Test
    public void shouldHaveErrorsWhenNinoFormatIsIncorrect() {
        Claimant claimant = createWithRefNo("ASDADADASDADSASDASDASDASDASD");

        Set<ConstraintViolation<Claimant>> result = validator.validate(claimant);
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("messageTemplate").contains("Invalid Reference Number format");
    }

    private Claimant createWithRefNo(String refNo){
        return aClaimant().refNo(refNo).build();
    }
}
