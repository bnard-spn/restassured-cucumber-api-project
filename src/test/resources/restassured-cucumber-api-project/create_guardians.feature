Feature: Create Guardians
  As a web application, I want to create and store guardian data

  Scenario: Create Guardian
    Given the app wants to "create a new guardian"
    When the app sends the Create Guardian request
    Then API Mock Service will create and store the guardian data
    And guardian data can be retrieve