package com.deloitte.qa.restassured.cucumber.types;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Getter
@Setter
public class Guardian {
    @NotNull(message = "'guardianId' must not be null")
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message = "'guardianId' is not in UUID format")
    private String guardianId;

    @NotEmpty(message = "'firstName' must not be empty")
    private String firstName;

    @NotEmpty(message = "'lastName' must not be empty")
    private String lastName;

    @NotEmpty(message = "'keyblade' must not be empty")
    private String keyblade;

    @Pattern(regexp = "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])?$", message = "'createdAt' is not in proper datetime format")
    private String createdAt;
}
