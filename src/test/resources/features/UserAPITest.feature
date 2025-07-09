@Regression_Pack @MD-User-Test
Feature: Validating POST, GET, PUT and DELETE API for User With JWT token

  @End_To_End_User_API_Test
  Scenario: Validating the User POST GET PUT and DELETE APIs with JWT
    Given User should be hit the generate JWT API to get the token
    When User should be hit the POST user API and verify user has been created
    And User should be validate all the fields and verify user has been created
    And User should be to hit the user api to get the user response
    And User should be able to update the specific user
    Then User should be able to delete the updated user
