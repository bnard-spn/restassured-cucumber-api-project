Feature: Update Guardians
  As a web application, I want to update/replace a guardian's data

  Scenario: Replace a guardian's data
    Given the app wants "to replace a guardian's data"
    When the app sends the Replace Guardian request
    Then API Mock Service will "replace and store the new guardian data"
    And updated guardian data can be retrieve

  Scenario: Nonexistent Guardian ID when replacing guardian data
    Given the app wants "to replace a nonexistent guardian"
    When the app sends the Replace Guardian request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message

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

  Scenario: Nonexistent Guardian ID when updating guardian data
    Given the app wants "to update a nonexistent guardian"
    And the app wants to update the "firstName" field
    When the app sends the Update Guardian request
    Then API Mock Service will return a "NOT_FOUND" error with "Guardian not found" message

  Scenario Outline: Invalid update for Guardian Data
    Given the app wants "to update a guardian's data"
    And the app wants to update the "<field>" field
    When the app sends the Update Guardian request
    Then API Mock Service will return a "BAD_REQUEST" error with "'guardianId' cannot be updated" message

    Examples:
      |field    |
      |guardianId|