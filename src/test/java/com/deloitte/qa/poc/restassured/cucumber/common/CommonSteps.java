package com.deloitte.qa.poc.restassured.cucumber.common;

import com.deloitte.qa.poc.restassured.cucumber.types.Guardians;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfTrue;

public class CommonSteps {
    public static ValidatorFactory validatorFactory;
    public static Validator validator;
    @Before
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public static void closeValidator() {
        validatorFactory.close();
    }

    public static void validateGuardianData(Guardians guardian) {
        Set<ConstraintViolation<Guardians>> violations = CommonSteps.validator.validate(guardian);
        try {
            assertIfTrue(violations.isEmpty());
        } catch (Exception e) {
            System.out.println(violations);
            System.out.println(e.getMessage());
        }
    }
}
