Feature: Update Guardians
  As a web application, I want to update/replace a guardian's data

  Scenario: Replace a guardian's data
    Given the app wants "to replace a guardian's data"
    When the app sends the Replace Guardian request
    Then API Mock Service will "replace and store the new guardian data"
    And updated guardian data can be retrieve

  Scenario Outline: Replace a guardian's data with invalid firstName
    Given the app wants "to replace a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |first name |has over max length characters |Max length reached for 'firstName' |
      |first name |is a number                    |Invalid field value for 'firstName'|
      |first name |has special characters         |Invalid field value for 'firstName'|

  Scenario Outline: Replace a guardian's data with invalid lastName
    Given the app wants "to replace a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |last name  |has over max length characters |Max length reached for 'lastName'  |
      |last name  |is a number                    |Invalid field value for 'lastName' |
      |last name  |has special characters         |Invalid field value for 'lastName' |

  Scenario Outline: Replace a guardian's data with invalid keyblade
    Given the app wants "to replace a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field    |errorScenario                  |errorMessage                       |
      |keyblade |has over max length characters |Max length reached for 'keyblade'  |
      |keyblade |is a number                    |Invalid field value for 'keyblade' |
      |keyblade |has special characters         |Invalid field value for 'keyblade' |

  Scenario: Nonexistent Guardian ID when replacing guardian data
    Given the app wants "to replace a nonexistent guardian"
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message

  Scenario: Replace Guardian with invalid field
    Given the app wants "to replace a nonexistent guardian"
    And the "invalid" field "in request body" for the update request
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "Invalid field input" message

  Scenario Outline: Replace Guardian with invalid authorization
    Given the app wants "to replace a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "<status>" error with "<errorMessage>" message

    Examples:
      |field              |errorScenario    |status           |errorMessage               |
      |authorizationHeader|is missing       |UNAUTHORIZED     |User is unauthorized.      |
      |authorizationHeader|is expired       |UNAUTHORIZED     |Token unverified.          |
      |authorizationHeader|is incorrect role|ACCESS_FORBIDDEN |User role is unauthorized. |

  Scenario Outline: Update a guardian's data
    Given the app wants "to update a guardian's data"
    And the app wants to update the "<field>" field
    When the app sends the Update Guardian request
    Then API Mock Service will "store the updated guardian data"
    And updated guardian data can be retrieve

    Examples:
    |field    |
    |firstName|
    |lastName |
    |keyblade |

  Scenario Outline: Update a guardian's data with invalid firstName
    Given the app wants "to update a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |first name |has over max length characters |Max length reached for 'firstName' |
      |first name |is a number                    |Invalid field value for 'firstName'|
      |first name |has special characters         |Invalid field value for 'firstName'|

  Scenario Outline: Update a guardian's data with invalid lastName
    Given the app wants "to update a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field      |errorScenario                  |errorMessage                       |
      |last name  |has over max length characters |Max length reached for 'lastName'  |
      |last name  |is a number                    |Invalid field value for 'lastName' |
      |last name  |has special characters         |Invalid field value for 'lastName' |

  Scenario Outline: Update a guardian's data with invalid keyblade
    Given the app wants "to update a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "<errorMessage>" message

    Examples:
      |field    |errorScenario                  |errorMessage                       |
      |keyblade |has over max length characters |Max length reached for 'keyblade'  |
      |keyblade |is a number                    |Invalid field value for 'keyblade' |
      |keyblade |has special characters         |Invalid field value for 'keyblade' |

  Scenario: Nonexistent Guardian ID when updating guardian data
    Given the app wants "to update a nonexistent guardian"
    And the app wants to update the "firstName" field
    When the app sends the Update Guardian request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message

  Scenario: Update Guardian with invalid field
    Given the app wants "to update a nonexistent guardian"
    And the "invalid" field "in request body" for the update request
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "Invalid field input" message

  Scenario Outline: Invalid update for Guardian Data
    Given the app wants "to update a guardian's data"
    And the app wants to update the "<field>" field
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "'guardianId' cannot be updated" message

    Examples:
      |field    |
      |guardianId|

  Scenario Outline: Replace Guardian with invalid authorization
    Given the app wants "to update a guardian's data"
    And the "<field>" field "<errorScenario>" for the update request
    When the app sends the Update Guardian request
    Then API Mock Service will return a "<status>" error with "<errorMessage>" message

    Examples:
      |field              |errorScenario    |status           |errorMessage               |
      |authorizationHeader|is missing       |UNAUTHORIZED     |User is unauthorized.      |
      |authorizationHeader|is expired       |UNAUTHORIZED     |Token unverified.          |
      |authorizationHeader|is incorrect role|ACCESS_FORBIDDEN |User role is unauthorized. |