Feature: Authorization
  As a web application, certain APIs need to have proper authorization in order to be used

  Scenario Outline: Get Authorization Token for a User Role
    Given the app wants to request "<authRole>"
    When the app sends the Get Auth Token request
    Then API Mock Service will return the authorization details

    Examples:
    |authRole                         |
    |authorization token for user     |
    |authorization token for publisher|

  Scenario Outline: Get Authorization Token for invalid credentials
    Given the app wants to request "authorization token for user"
    And "<field>" in auth request has "<errorScenario>"
    When the app sends the Get Auth Token request
    Then API Mock Service will return a "<status>" error with "<message>" message

    Examples:
    |field    |errorScenario    |status       |message                                      |
    |user_id  |missing value    |BAD_REQUEST  |'user_id' and 'api_key' headers are required.|
    |user_id  |nonexistent value|UNAUTHORIZED |Invalid access credentials                   |
    |api_key  |missing value    |BAD_REQUEST  |'user_id' and 'api_key' headers are required.|
    |api_key  |nonexistent value|UNAUTHORIZED |Invalid access credentials                   |
