Feature: Delete Guardians
  As a web application, I want to delete a guardian's data

  Scenario: Delete a created guardian
    Given the app deletes "a newly created guardian"
    When the app sends the Delete Guardian request
    Then API Mock Service will delete the guardian data
    And guardian data cannot be retrieve

  Scenario: Delete a nonexistent guardian
    Given the app deletes "a nonexistent guardian"
    When the app sends the Delete Guardian request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message

  Scenario Outline: Delete Guardian with invalid authorization
    Given the app deletes "a newly created guardian"
    And the "<field>" field "<errorScenario>" for the delete request
    When the app sends the Delete Guardian request
    Then API Mock Service will return a "<status>" error with "<errorMessage>" message

    Examples:
      |field              |errorScenario    |status           |errorMessage               |
      |authorizationHeader|is missing       |UNAUTHORIZED     |User is unauthorized.      |
      |authorizationHeader|is expired       |UNAUTHORIZED     |Token unverified.          |
      |authorizationHeader|is incorrect role|ACCESS_FORBIDDEN |User role is unauthorized. |