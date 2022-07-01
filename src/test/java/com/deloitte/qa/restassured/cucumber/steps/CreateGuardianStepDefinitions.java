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
    private TestProperties testProperties = new TestProperties();
    private TestData testData = new TestData();
    private Guardian guardian;

    @Given("the app wants to {string}")
    public void initializeCreateGuardianRequest(String request) {
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
        Map<String, String> modifiedRequestMap = (Map<String, String>) requestMap.get("requestMap");
        switch (field + " - " + errorScenario) {
            case "first name - is missing":
                modifiedRequestMap.remove("firstName");
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "first name - has over max length characters":
                modifiedRequestMap.put("firstName", testData.getRandomAlphabeticString(51));
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "last name - is missing":
                modifiedRequestMap.remove("lastName");
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "last name - has over max length characters":
                modifiedRequestMap.put("lastName", testData.getRandomAlphabeticString(51));
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "keyblade - is missing":
                modifiedRequestMap.remove("keyblade");
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "keyblade - has over max length characters":
                modifiedRequestMap.put("keyblade", testData.getRandomAlphabeticString(51));
                requestMap.put("requestMap", modifiedRequestMap);
                break;
            case "invalid - in request body":
                modifiedRequestMap.put(testData.getRandomAlphabeticString(5), testData.getRandomAlphabeticString(5));
                requestMap.put("requestMap", modifiedRequestMap);
                break;
        }
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