package com.deloitte.qa.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.commons.properties.TestData;
import com.deloitte.qa.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.restassured.cucumber.types.GuardianResponse;
import com.deloitte.qa.restassured.cucumber.types.Guardian;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfEquals;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.createGuardianMap;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.validateGuardianData;
import static com.deloitte.qa.restassured.cucumber.common.CommonSteps.responseMap;

public class CreateGuardianStepDefinitions {

    private final RequestApi requestApi = new RequestApi();
    private String requestUrl;
    private Map<String, Object> requestMap = new HashMap<>();
    private final TestProperties testProperties = new TestProperties();
    private final TestData testData = new TestData();
    private Guardian guardian;

    @Given("the app wants to {string}")
    public void initializeCreateGuardianRequest(String request) throws JsonProcessingException {
        requestUrl = testProperties.getTestProperty("endpoint");
        switch (request) {
            case "create a new guardian":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath");
                requestMap = createGuardianMap();
                break;
        }
    }

    @And("the {string} field {string} for the create request")
    public void modifyCreateGuardianRequest(String field, String errorScenario) {
        Map<String, Object> modifiedRequestMap = (Map<String, Object>) requestMap.get("requestMap");
        List<Map<String, String>> modifiedRequestHeaderList = (List<Map<String, String>>) requestMap.get("requestHeaders");
        switch (field + " - " + errorScenario) {
            case "first name - is missing":
                modifiedRequestMap.remove("firstName");
                break;
            case "first name - has over max length characters":
                modifiedRequestMap.put("firstName", testData.getRandomAlphabeticString(51));
                break;
            case "first name - is a number":
                modifiedRequestMap.put("firstName", testData.getRandomNumber(3));
                break;
            case "first name - has special characters":
                modifiedRequestMap.put("firstName", testData.generateSpecialCharacterString(10));
                break;
            case "last name - is missing":
                modifiedRequestMap.remove("lastName");
                break;
            case "last name - has over max length characters":
                modifiedRequestMap.put("lastName", testData.getRandomAlphabeticString(51));
                break;
            case "last name - is a number":
                modifiedRequestMap.put("lastName", testData.getRandomNumber(3));
                break;
            case "last name - has special characters":
                modifiedRequestMap.put("lastName", testData.generateSpecialCharacterString(10));
                break;
            case "keyblade - is missing":
                modifiedRequestMap.remove("keyblade");
                break;
            case "keyblade - has over max length characters":
                modifiedRequestMap.put("keyblade", testData.getRandomAlphabeticString(51));
                break;
            case "keyblade - is a number":
                modifiedRequestMap.put("keyblade", testData.getRandomNumber(3));
                break;
            case "keyblade - has special characters":
                modifiedRequestMap.put("keyblade", testData.generateSpecialCharacterString(10));
                break;
            case "invalid - in request body":
                modifiedRequestMap.put(testData.getRandomAlphabeticString(5), testData.getRandomAlphabeticString(5));
                break;
            case "authorizationHeader - is missing":
                modifiedRequestHeaderList.remove(1);
                break;
            case "authorizationHeader - is expired":
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("header", "Authorization");
                headerMap.put("value", "Bearer " + testProperties.getInvalidTestProperty("expiredToken"));
                modifiedRequestHeaderList.set(1, headerMap);
                break;
        }
        requestMap.put("requestMap", modifiedRequestMap);
        requestMap.put("requestHeaders", modifiedRequestHeaderList);
    }

    @When("the app sends the Create Guardian request")
    public void sendCreateGuardianRequest() {
        responseMap = requestApi.sendPostRequest(requestUrl, (Map<String, Object>) requestMap.get("requestMap"), (List<Map<String, String>>) requestMap.get("requestHeaders"));
    }

    @Then("API Mock Service will create and store the guardian data")
    public void validateCreateGuardianResponse() throws JsonProcessingException {
        validateStatusCode("CREATED", responseMap.get("statusCode"));
        GuardianResponse guardianResponse = new ObjectMapper().readValue(responseMap.get("response"), GuardianResponse.class);

        assertIfEquals("CREATED", guardianResponse.getStatus());
        assertIfEquals("Added new guardian", guardianResponse.getMessage());

        guardian = guardianResponse.getGuardian();
        validateGuardianData(guardian);
    }

    @And("guardian data can be retrieve")
    public void validateRetrievedGuardianData() throws JsonProcessingException {
        Map<String, String> retrieveDataMap = requestApi.sendGetRequest(requestUrl, String.valueOf(guardian.getGuardianId()));

        validateStatusCode("SUCCESSFUL", retrieveDataMap.get("statusCode"));

        Guardian retrieveGuardian = new ObjectMapper().readValue(retrieveDataMap.get("response"), Guardian.class);
        assertIfEquals(guardian.getGuardianId(), retrieveGuardian.getGuardianId());
        assertIfEquals(guardian.getFirstName(), retrieveGuardian.getFirstName());
        assertIfEquals(guardian.getLastName(), retrieveGuardian.getLastName());
        assertIfEquals(guardian.getKeyblade(), retrieveGuardian.getKeyblade());
    }
}