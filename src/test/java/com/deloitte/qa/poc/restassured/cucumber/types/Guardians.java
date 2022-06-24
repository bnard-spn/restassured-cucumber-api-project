package com.deloitte.qa.poc.restassured.cucumber.types;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
@Getter
@Setter
public class Guardians {
    @NotNull(message = "'id' must not be null")
    @Positive(message = "'id' must not be a negative value")
    private int id;

    @NotEmpty(message = "'firstName' must not be empty")
    private String firstName;

    @NotEmpty(message = "'lastName' must not be empty")
    private String lastName;

    @NotEmpty(message = "'keyblade' must not be empty")
    private String keyblade;
}
