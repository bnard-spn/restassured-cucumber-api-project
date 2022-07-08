Feature: Create Guardians
  As a web application, I want to create and store guardian data

  Scenario: Create Guardian
    Given the app wants to "create a new guardian"
    When the app sends the Create Guardian request
    Then API Mock Service will create and store the guardian data
    And guardian data can be retrieve

  Scenario Outline: Create Guardian with invalid firstName
    Given the app wants to "create a new guardian"
    And the "<field>" field "<errorScenario>" for the create request
    When the app sends the Create Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |first name |is missing                     |'firstName' is required            |
      |first name |has over max length characters |Max length reached for 'firstName' |
      |first name |is a number                    |Invalid field value for 'firstName'|
      |first name |has special characters         |Invalid field value for 'firstName'|

  Scenario Outline: Create Guardian with invalid lastName
    Given the app wants to "create a new guardian"
    And the "<field>" field "<errorScenario>" for the create request
    When the app sends the Create Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |last name  |is missing                     |'lastName' is required             |
      |last name  |has over max length characters |Max length reached for 'lastName'  |
      |last name  |is a number                    |Invalid field value for 'lastName' |
      |last name  |has special characters         |Invalid field value for 'lastName' |

  Scenario Outline: Create Guardian with invalid keyblade
    Given the app wants to "create a new guardian"
    And the "<field>" field "<errorScenario>" for the create request
    When the app sends the Create Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field    |errorScenario                  |errorMessage                       |
      |keyblade |is missing                     |'keyblade' is required             |
      |keyblade |has over max length characters |Max length reached for 'keyblade'  |
      |keyblade |is a number                    |Invalid field value for 'keyblade' |
      |keyblade |has special characters         |Invalid field value for 'keyblade' |

  Scenario: Create Guardian with invalid field
    Given the app wants to "create a new guardian"
    And the "invalid" field "in request body" for the create request
    When the app sends the Create Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "Invalid field input" message

  Scenario Outline: Create Guardian with invalid authorization
    Given the app wants to "create a new guardian"
    And the "<field>" field "<errorScenario>" for the create request
    When the app sends the Create Guardian request
    Then API Mock Service will return a "<status>" error with "<errorMessage>" message

    Examples:
      |field              |errorScenario|status      |errorMessage          |
      |authorizationHeader|is missing   |UNAUTHORIZED|User is unauthorized. |
      |authorizationHeader|is expired   |UNAUTHORIZED|Token unverified.     |