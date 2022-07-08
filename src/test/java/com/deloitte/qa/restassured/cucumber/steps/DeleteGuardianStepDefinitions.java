package com.deloitte.qa.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.restassured.cucumber.types.GuardianResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfEquals;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.*;
import static com.deloitte.qa.restassured.cucumber.common.CommonSteps.responseMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteGuardianStepDefinitions {
    private final RequestApi requestApi = new RequestApi();
    private String requestUrl, guardianId;
    private final TestProperties testProperties = new TestProperties();
    private final List<Map<String, String>> requestHeaders = new ArrayList<>();

    @Given("the app deletes {string}")
    public void initializeDeleteGuardianRequest(String request) throws JsonProcessingException {
        requestUrl = testProperties.getTestProperty("endpoint") + testProperties.getTestProperty("guardiansPath");
        switch (request) {
            case "a newly created guardian":
                guardianId = createGuardian();
                break;
            case "a nonexistent guardian":
                guardianId = testProperties.getRandomInvalidTestProperty("nonexistentGuardianId");
                break;
        }
        createDeleteHeaders();
    }

    @And("the {string} field {string} for the delete request")
    public void modifyDeleteGuardianRequest(String field, String errorScenario) throws JsonProcessingException {
        Map<String, String> headerMap = new HashMap<>();
        switch (field + " - " + errorScenario) {
            case "authorizationHeader - is missing":
                requestHeaders.remove(0);
                break;
            case "authorizationHeader - is expired":
                headerMap.put("header", "Authorization");
                headerMap.put("value", "Bearer " + testProperties.getInvalidTestProperty("expiredToken"));
                requestHeaders.set(0, headerMap);
                break;
            case "authorizationHeader - is incorrect role":
                headerMap.put("header", "Authorization");
                headerMap.put("value", "Bearer " + getAuthToken("user"));
                requestHeaders.set(0, headerMap);
                break;
        }
    }

    @When("the app sends the Delete Guardian request")
    public void sendDeleteGuardianRequest() {
        responseMap = requestApi.sendDeleteRequest(requestUrl, guardianId, requestHeaders);
    }

    @Then("API Mock Service will delete the guardian data")
    public void validateDeleteGuardianRequest() throws JsonProcessingException {
        validateStatusCode("SUCCESS", responseMap.get("statusCode"));
        GuardianResponse guardianResponse = new ObjectMapper().readValue(responseMap.get("response"), GuardianResponse.class);

        assertIfEquals("DELETED", guardianResponse.getStatus());
        assertIfEquals("Guardian data is deleted", guardianResponse.getMessage());
    }

    @And("guardian data cannot be retrieve")
    public void retrieveDeletedGuardianData() {
        requestUrl = testProperties.getTestProperty("endpoint") + testProperties.getTestProperty("guardiansPath");

        Map<String, String> retrieveDataMap = requestApi.sendGetRequest(requestUrl, guardianId);

        validateStatusCode("NOT_FOUND", retrieveDataMap.get("statusCode"));
    }

    private void createDeleteHeaders() throws JsonProcessingException {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("header", "Authorization");
        requestMap.put("value", "Bearer " + getAuthToken("publisher"));
        requestHeaders.add(requestMap);
    }
}