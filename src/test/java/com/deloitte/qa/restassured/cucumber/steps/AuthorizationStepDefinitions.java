package com.deloitte.qa.restassured.cucumber.steps;

import com.deloitte.qa.commons.helpers.RequestApi;
import com.deloitte.qa.commons.types.AuthResponse;
import com.deloitte.qa.restassured.cucumber.properties.TestProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import static com.deloitte.qa.commons.helpers.Assertions.*;
import static com.deloitte.qa.restassured.cucumber.common.CommonActions.createAuthHeaders;
import static com.deloitte.qa.restassured.cucumber.common.CommonSteps.responseMap;

public class AuthorizationStepDefinitions {
    private final RequestApi requestApi = new RequestApi();
    private String requestUrl;
    private final TestProperties testProperties = new TestProperties();
    private List<Map<String, String>> requestHeaders = new ArrayList<>();

    @Given("the app wants to request {string}")
    public void initializeGetAuthTokenRequest(String authRole) {
        requestUrl = testProperties.getTestProperty("endpoint") + testProperties.getTestProperty("authPath") + "/token";
        createAuthRequestHeaders(authRole);
    }

    @And("{string} in auth request has {string}")
    public void modifyGetAuthTokenRequest(String field, String errorScenario) {
        Map<String, String> headerMap = new HashMap<>();
        switch (field + " - " + errorScenario) {
            case "user_id - missing value":
                requestHeaders.remove(0);
                break;
            case "user_id - nonexistent value":
                headerMap.put("header", "user_id");
                headerMap.put("value", UUID.randomUUID().toString());
                requestHeaders.set(0, headerMap);
                break;
            case "api_key - missing value":
                requestHeaders.remove(1);
                break;
            case "api_key - nonexistent value":
                headerMap.put("header", "api_key");
                headerMap.put("value", UUID.randomUUID().toString());
                requestHeaders.set(1, headerMap);
                break;
        }
    }

    @When("the app sends the Get Auth Token request")
    public void sendGetAuthRequest() {
        responseMap = requestApi.sendGetAuthorizationRequest(requestUrl, requestHeaders);
    }

    @Then("API Mock Service will return the authorization details")
    public void validateGetAuthResponse() throws JsonProcessingException {
        validateStatusCode("SUCCESSFUL", responseMap.get("statusCode"));
        AuthResponse authResponse = new ObjectMapper().readValue(responseMap.get("response"), AuthResponse.class);

        assertIfEquals("ACCESS_GRANTED", authResponse.getStatus());
        assertIfNotNull(authResponse.getToken());
        assertIfEquals(180000, authResponse.getExpiry());
    }

    private void createAuthRequestHeaders(String authRole) {
        switch (authRole) {
            case "authorization token for user":
                requestHeaders = createAuthHeaders("user");
                break;
            case "authorization token for publisher":
                requestHeaders = createAuthHeaders("publisher");
                break;
        }
    }
}