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
import java.util.UUID;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfEquals;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.*;
import static com.deloitte.qa.restassured.cucumber.common.CommonSteps.responseMap;

public class UpdateGuardianStepDefinitions {

    private final RequestApi requestApi = new RequestApi();
    private String requestUrl, originalGuardianId;
    private Map<String, Object> requestMap = new HashMap<>();
    private final TestProperties testProperties = new TestProperties();
    private final TestData testData = new TestData();
    private Guardian guardian;

    @Given("the app wants {string}")
    public void initializeUpdateGuardianRequest(String request) throws JsonProcessingException {
        requestUrl = testProperties.getTestProperty("endpoint");
        switch (request) {
            case "to replace a guardian's data":
            case "to update a guardian's data":
                originalGuardianId = testProperties.getRandomTestProperty("updateGuardianId");
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath") + "/" + originalGuardianId;
                requestMap = createGuardianMap();
                break;
            case "to replace a nonexistent guardian":
            case "to update a nonexistent guardian":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath") + "/" + testProperties.getRandomInvalidTestProperty("nonexistentGuardianId");
                requestMap = createGuardianMap();
                break;
        }
    }

    @And("the app wants to update the {string} field")
    public void modifyUpdateGuardianRequest(String field) {
        Map<String, String> modifiedRequestMap = (Map<String, String>) requestMap.get("requestMap");
        switch (field) {
            case "firstName":
                modifiedRequestMap.remove("lastName");
                modifiedRequestMap.remove("keyblade");
                break;
            case "lastName":
                modifiedRequestMap.remove("firstName");
                modifiedRequestMap.remove("keyblade");
                break;
            case "keyblade":
                modifiedRequestMap.remove("firstName");
                modifiedRequestMap.remove("lastName");
                break;
            case "guardianId":
                modifiedRequestMap.put("guardianId", UUID.randomUUID().toString());
                break;
        }

        requestMap.put("requestMap", modifiedRequestMap);
    }

    @And("the {string} field {string} for the update request")
    public void modifyInvalidUpdateGuardianRequest(String field, String errorScenario) throws JsonProcessingException {
        Map<String, Object> modifiedRequestMap = (Map<String, Object>) requestMap.get("requestMap");
        List<Map<String, String>> modifiedRequestHeaderList = (List<Map<String, String>>) requestMap.get("requestHeaders");
        Map<String, String> headerMap = new HashMap<>();

        switch (field + " - " + errorScenario) {
            case "first name - has over max length characters":
                modifiedRequestMap.put("firstName", testData.getRandomAlphabeticString(51));
                break;
            case "first name - is a number":
                modifiedRequestMap.put("firstName", testData.getRandomNumber(100,999));
                break;
            case "first name - has special characters":
                modifiedRequestMap.put("firstName", testData.generateSpecialCharacterString(10));
                break;
            case "last name - has over max length characters":
                modifiedRequestMap.put("lastName", testData.getRandomAlphabeticString(51));
                break;
            case "last name - is a number":
                modifiedRequestMap.put("lastName", testData.getRandomNumber(100,999));
                break;
            case "last name - has special characters":
                modifiedRequestMap.put("lastName", testData.generateSpecialCharacterString(10));
                break;
            case "keyblade - has over max length characters":
                modifiedRequestMap.put("keyblade", testData.getRandomAlphabeticString(51));
                break;
            case "keyblade - is a number":
                modifiedRequestMap.put("keyblade", testData.getRandomNumber(100,999));
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
                headerMap.put("header", "Authorization");
                headerMap.put("value", "Bearer " + testProperties.getInvalidTestProperty("expiredToken"));
                modifiedRequestHeaderList.set(1, headerMap);
                break;
            case "authorizationHeader - is incorrect role":
                headerMap.put("header", "Authorization");
                headerMap.put("value", "Bearer " + getAuthToken("user"));
                modifiedRequestHeaderList.set(1, headerMap);
                break;
        }
        requestMap.put("requestMap", modifiedRequestMap);
        requestMap.put("requestHeaders", modifiedRequestHeaderList);
    }

    @When("the app sends the Replace Guardian request")
    public void sendReplaceGuardianRequest() {
        responseMap = requestApi.sendPutRequest(requestUrl, (Map<String, Object>) requestMap.get("requestMap"), (List<Map<String, String>>) requestMap.get("requestHeaders"));
    }

    @When("the app sends the Update Guardian request")
    public void sendUpdateGuardianRequest() {
        responseMap = requestApi.sendPatchRequest(requestUrl, (Map<String, Object>) requestMap.get("requestMap"), (List<Map<String, String>>) requestMap.get("requestHeaders"));
    }

    @Then("API Mock Service will {string}")
    public void validateUpdateGuardianResponse(String response) throws JsonProcessingException {
        validateStatusCode("SUCCESS", responseMap.get("statusCode"));
        GuardianResponse guardianResponse = new ObjectMapper().readValue(responseMap.get("response"), GuardianResponse.class);
        System.out.println(guardianResponse.getGuardian().getGuardianId());
        assertIfEquals("SUCCESS", guardianResponse.getStatus());

        if (response.equals("replace and store the new guardian data")) {
            assertIfEquals("Guardian data is replaced", guardianResponse.getMessage());
        } else if (response.equals("store the updated guardian data")) {
            assertIfEquals("Guardian data is updated", guardianResponse.getMessage());
        }

        guardian = guardianResponse.getGuardian();
        validateGuardianData(guardian);
    }

    @And("updated guardian data can be retrieve")
    public void retrieveUpdatedGuardian() throws JsonProcessingException {
        assertIfEquals(originalGuardianId, guardian.getGuardianId());
        requestUrl = testProperties.getTestProperty("endpoint") + testProperties.getTestProperty("guardiansPath");

        Map<String, String> retrieveDataMap = requestApi.sendGetRequest(requestUrl, String.valueOf(guardian.getGuardianId()));

        validateStatusCode("SUCCESSFUL", retrieveDataMap.get("statusCode"));

        Guardian retrieveGuardian = new ObjectMapper().readValue(retrieveDataMap.get("response"), Guardian.class);
        assertIfEquals(guardian.getGuardianId(), retrieveGuardian.getGuardianId());
        assertIfEquals(guardian.getFirstName(), retrieveGuardian.getFirstName());
        assertIfEquals(guardian.getLastName(), retrieveGuardian.getLastName());
        assertIfEquals(guardian.getKeyblade(), retrieveGuardian.getKeyblade());
    }
}