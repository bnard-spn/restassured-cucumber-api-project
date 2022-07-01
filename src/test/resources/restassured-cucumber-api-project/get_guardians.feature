Feature: Get Guardians
  As a web application, I want to send a request to retrieve the list of guardian characters

  Scenario: Send request to retrieve Guardians
    Given the app wants the "guardian characters"
    When the app sends the Get Guardians request
    Then API Mock Service will return the list of guardian characters

  Scenario: Send request to retrieve a single Guardian
    Given the app wants the "single guardian"
    When the app sends the Get Guardians request
    Then API Mock Service will return the guardian data

  Scenario: Send request to retrieve a nonexistent Guardian
    Given the app wants the "nonexistent guardian"
    When the app sends the Get Guardians request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message