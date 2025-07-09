@Regression_Pack @SauceLogin_Page_Test
Feature: Validating the login page functionality for Sauce Labs application

  @SauceLogin_Page_Test_Valid
  Scenario Outline: Verify that the user login page with valid credentials for sauce labs application
    Given User should be able to saucelogin with given parameters url <url> username <userName> and password <password>
    When User should be navigated to Sauce Login Page
    And User should be on Sauce Login page
    And User should be able to see the all items from dropdown
    And User can be able add the items to the cart
    And User should be on the cart page to checkout process
    Then User should successfully logged out the application

Examples:
##@externaldata@src/test/resources/data/Datasheet.xlsx@Valid_Data
| url       | userName      | password     |
| saucelabs | standard_user | secret_sauce |

  @SauceLogin_Page_Test_InValid
  Scenario Outline: Verify that the user login page with invalid credentials for sauce labs application
    Given User should be able to saucelogin with given parameters url <url> username <userName> and password <password>
    When User should be navigated to Sauce Login Page
    Then User should validate the error message

Examples:
##@externaldata@src/test/resources/data/Datasheet.xlsx@InValid_Data
| url       | userName        | password     |
| saucelabs | locked_out_user | secret_sauce |
