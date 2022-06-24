package com.deloitte.qa.poc.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.poc.restassured.cucumber.common.CommonSteps;
import com.deloitte.qa.poc.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.poc.restassured.cucumber.types.Guardians;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.validation.ConstraintViolation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.deloitte.qa.commons.helpers.Assertions.*;
import static com.deloitte.qa.poc.restassured.cucumber.common.CommonSteps.validateGuardianData;


public class GetGuardiansStepDefinitions {

    private RequestApi requestApi = new RequestApi();
    private String requestUrl;
    private Map<String, String> responseMap = new HashMap<>();
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
        List<Guardians>guardians = new ObjectMapper().readValue(responseMap.get("response"), new TypeReference<List<Guardians>>() {});
        for (Guardians guardian: guardians) {
            validateGuardianData(guardian);
        }
    }

    @Then("API Mock Service will return the guardian data")
    public void validateGuardian() throws JsonProcessingException {
        validateStatusCode("SUCCESSFUL", responseMap.get("statusCode"));

        Guardians guardian = new ObjectMapper().readValue(responseMap.get("response"), Guardians.class);
        validateGuardianData(guardian);
    }

    @Then("API Mock Service will return a {string} error")
    public void validateInvalidGuardianResponse(String status) {
        validateStatusCode(status, responseMap.get("statusCode"));
    }
}
