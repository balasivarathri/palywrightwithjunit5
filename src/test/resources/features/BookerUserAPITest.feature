@Regression_Pack @MD-User-Test
Feature: Validating POST, GET, PUT and DELETE API for Booker User With token

  @End_To_End_Booker_User_API_Test
  Scenario: Validating the User POST GET PUT and DELETE BOOKER APIs with Token
    Given User should be hit the booker user API to get the token
    When User should be hit the POST Booker User API and verify user has been created
    And User should be validate all the fields for booker API and verify user has been created
    And User should be to hit the Booker api to get the user response
#    And User should be to hit the Booker api to get for all users response
    And User should be able to update the specific Booker user
#    Then User should be able to delete the updated user