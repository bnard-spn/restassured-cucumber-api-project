package com.deloitte.qa.restassured.cucumber.common;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.commons.properties.TestData;
import com.deloitte.qa.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.restassured.cucumber.types.GuardianResponse;
import com.deloitte.qa.restassured.cucumber.types.Guardian;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfTrue;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;
import static junit.framework.TestCase.fail;

public class CommonActions {
    private static TestData testData = new TestData();
    private static TestProperties testProperties = new TestProperties();
    private static final RequestApi requestApi = new RequestApi();

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

    public static void validateGuardianData(Guardian guardian) {
        Set<ConstraintViolation<Guardian>> violations = CommonActions.validator.validate(guardian);
        try {
            assertIfTrue(violations.isEmpty());
        } catch (AssertionError e) {
            fail(String.valueOf(violations));
        }
    }

    public static Map<String, Object> createGuardianMap() {
        Map<String, String> requestHeaderMap = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        List<Map<String, String>> requestHeaders = new ArrayList<>();

        requestHeaderMap.put("header", "Content-Type");
        requestHeaderMap.put("value", "application/json");
        requestHeaders.add(requestHeaderMap);

        requestMap.put("firstName", testData.getFirstName());
        requestMap.put("lastName", testData.getLastName());
        requestMap.put("keyblade", testProperties.getRandomTestProperty("keyblade"));

        request.put("requestMap", requestMap);
        request.put("requestHeaders", requestHeaders);

        return request;
    }

    public static String createGuardian() throws JsonProcessingException {
        Map<String, Object> createRequestMap;
        Map<String, String> createResponseMap;
        String requestUrl = testProperties.getTestProperty("endpoint") + testProperties.getTestProperty("guardiansPath");
        createRequestMap = createGuardianMap();

        createResponseMap = requestApi.sendPostRequest(requestUrl, (Map<String, Object>) createRequestMap.get("requestMap"), (List<Map<String, String>>) createRequestMap.get("requestHeaders"));
        validateStatusCode("CREATED", createResponseMap.get("statusCode"));
        GuardianResponse guardianResponse = new ObjectMapper().readValue(createResponseMap.get("response"), GuardianResponse.class);

        return guardianResponse.getGuardian().getGuardianId();
    }
}