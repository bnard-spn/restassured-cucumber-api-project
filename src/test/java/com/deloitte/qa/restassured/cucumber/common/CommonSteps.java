package com.deloitte.qa.restassured.cucumber.common;

import com.deloitte.qa.commons.types.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;

import java.util.Map;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfEquals;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;

public class CommonSteps {
    public static Map<String, String> responseMap;

    @And("API Mock Service will return a {string} error with {string} message")
    public void validateErrorResponse(String status, String message) throws JsonProcessingException {
        validateStatusCode(status, responseMap.get("statusCode"));
        ErrorResponse errorResponse = new ObjectMapper().readValue(responseMap.get("response"), ErrorResponse.class);

        assertIfEquals(status, errorResponse.getStatus());
        assertIfEquals(message, errorResponse.getMessage());
    }
}
