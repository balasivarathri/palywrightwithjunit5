@Regression_Pack @MD-Gorest-Test
Feature: Validating POST and GET API for GOREST

#  @GetData_Feature_Valid
#  Scenario: Validating the User Creation API reponse
#    When User should be to validate the response
#    Then User should validate all the responses

  @End_To_End_API_Test
  Scenario: Validating the POST request API response for user
    Given User should be hit the POST gorest API to create the user
    When User should be validate the reponse and verify user has been created
    Then User should validate the response with specific Id

  @GetUser_Api_Gorest,
  Scenario: Validating GET GOREST API reponse for Users
    Given User should be to hit the gorest api to get the user response
