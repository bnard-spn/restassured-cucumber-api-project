package com.deloitte.qa.restassured.cucumber.types;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardianResponse {

    @NotEmpty(message = "'status' must not be empty")
    private String status;

    @NotEmpty(message = "'message' must not be empty")
    private String message;

    private Guardian guardian;
}
