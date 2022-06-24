package com.deloitte.qa.poc.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.commons.properties.TestData;
import com.deloitte.qa.poc.restassured.cucumber.properties.TestProperties;
import com.deloitte.qa.poc.restassured.cucumber.types.Guardians;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.deloitte.qa.commons.helpers.Assertions.assertIfEquals;
import static com.deloitte.qa.commons.helpers.Assertions.validateStatusCode;
import static com.deloitte.qa.poc.restassured.cucumber.common.CommonSteps.validateGuardianData;

public class CreateGuardianStepDefinitions {

    private RequestApi requestApi = new RequestApi();
    private String requestUrl;
    private Map<String, Object> requestMap = new HashMap<>();
    private Map<String, String> responseMap = new HashMap<>();
    private List<Map<String, String>> requestHeaders = new ArrayList<>();
    private TestProperties testProperties = new TestProperties();
    private TestData testData = new TestData();
    private Guardians guardian;

    @Given("the app wants to {string}")
    public void initializeCreateGuardianRequest(String request) {
        requestUrl = testProperties.getTestProperty("endpoint");
        switch (request) {
            case "create a new guardian":
                requestUrl = requestUrl + testProperties.getTestProperty("guardiansPath");
                createGuardianMap();
                break;
        }
    }

    @When("the app sends the Create Guardian request")
    public void sendCreateGuardianRequest() {
        responseMap = requestApi.sendCreateRequest(requestUrl, requestMap, requestHeaders);
    }

    @Then("API Mock Service will create and store the guardian data")
    public void validateCreateGuardianResponse() throws JsonProcessingException {
        validateStatusCode("CREATED", responseMap.get("statusCode"));
        guardian = new ObjectMapper().readValue(responseMap.get("response"), Guardians.class);
        validateGuardianData(guardian);
    }

    @And("guardian data can be retrieve")
    public void retrieveCreatedGuardian() throws JsonProcessingException {
        Map<String, String> retrieveDataMap = requestApi.sendGetRequest(requestUrl, String.valueOf(guardian.getId()));

        validateStatusCode("SUCCESSFUL", retrieveDataMap.get("statusCode"));

        Guardians retrieveGuardian = new ObjectMapper().readValue(retrieveDataMap.get("response"), Guardians.class);
        assertIfEquals(guardian.getId(), retrieveGuardian.getId());
        assertIfEquals(guardian.getFirstName(), retrieveGuardian.getFirstName());
        assertIfEquals(guardian.getLastName(), retrieveGuardian.getLastName());
        assertIfEquals(guardian.getKeyblade(), retrieveGuardian.getKeyblade());
    }

    private void createGuardianMap() {
        Map<String, String> requestHeaderMap = new HashMap<>();
        requestHeaderMap.put("header", "Content-Type");
        requestHeaderMap.put("value", "application/json");
        requestHeaders.add(requestHeaderMap);

        requestMap.put("firstName", testData.getFirstName());
        requestMap.put("lastName", testData.getLastName());
        requestMap.put("keyblade", testProperties.getRandomTestProperty("keyblade"));
    }
}
