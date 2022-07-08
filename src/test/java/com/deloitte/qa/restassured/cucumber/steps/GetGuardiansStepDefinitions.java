package com.deloitte.qa.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.restassured.cucumber.types.Guardian;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static com.deloitte.qa.commons.helpers.Assertions.*;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.validateGuardianData;
import static com.deloitte.qa.restassured.cucumber.common.CommonSteps.responseMap;


public class GetGuardiansStepDefinitions {

    private final RequestApi requestApi = new RequestApi();
    private String requestUrl;
    private TestProperties testProperties = new TestProperties();

    @Given("the app wants the {string}")
    public void initializeGetGuardianRequest(String request) {
        requestUrl = testProperties.getTestProperty("endpoint");
        switch (request) {
            case "guardian characters":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath");
                break;
            case "single guardian":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath") + "/" + testProperties.getRandomTestProperty("guardianId");
                break;
            case "nonexistent guardian":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath") + "/" + testProperties.getRandomInvalidTestProperty("nonexistentGuardianId");
                break;
        }
    }

    @When("the app sends the Get Guardians request")
    public void sendGetGuardiansRequest() {
        responseMap = requestApi.sendGetRequest(requestUrl);
    }

    @Then("API Mock Service will return the list of guardian characters")
    public void validateGuardianList() throws JsonProcessingException {
        validateStatusCode("SUCCESSFUL", responseMap.get("statusCode"));
        List<Guardian>guardians = new ObjectMapper().readValue(responseMap.get("response"), new TypeReference<List<Guardian>>() {});
        for (Guardian guardian: guardians) {
            validateGuardianData(guardian);
        }
    }

    @Then("API Mock Service will return the guardian data")
    public void validateGuardian() throws JsonProcessingException {
        validateStatusCode("SUCCESSFUL", responseMap.get("statusCode"));

        Guardian guardian = new ObjectMapper().readValue(responseMap.get("response"), Guardian.class);
        validateGuardianData(guardian);
    }
}
