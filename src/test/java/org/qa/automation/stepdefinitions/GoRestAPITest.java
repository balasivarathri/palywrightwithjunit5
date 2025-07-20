package org.qa.automation.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.apis.apicalls.BookApiTest;
import org.qa.automation.apis.apimethods.GetApiCall;
import org.qa.automation.apis.apimethods.PostApiCall;
import org.qa.automation.contextsetup.ApiContextSetup;
import org.qa.automation.report.Report;
import org.qa.automation.base.TestBase;

import java.io.IOException;

@Slf4j
public class GoRestAPITest {

    private final ApiContextSetup apiContextSetup;
    private GetApiCall getApiCall;
    private PostApiCall postApiCall;
    private int idNumber;

    public GoRestAPITest(ApiContextSetup apiContextSetup) {
        this.apiContextSetup = apiContextSetup;
    }

    @Given("User should hit the POST gorest API to create the user")
    public void user_should_hit_the_post_gorest_api_to_create_the_user() throws IOException {
        postApiCall = apiContextSetup.apiObjectManager.postApiCall();
        postApiCall.postApi();
    }

    @Then("User should validate the response and verify user has been created")
    public void user_should_validate_the_response_and_verify_user_has_been_created() throws IOException {
        idNumber = postApiCall.validatePostApiResponse();
        Report.log(TestBase.getScenario(), "Created User ID: " + idNumber);
    }

    @Given("User should hit the gorest API to get the user response")
    public void user_should_hit_the_gorest_api_to_get_the_user_response() throws IOException {
        getApiCall = apiContextSetup.apiObjectManager.getApiCall();
        getApiCall.getApi();
    }

    @Given("User should validate the response with specific Id")
    public void user_should_validate_the_response_with_specific_id() throws IOException {
        getApiCall = apiContextSetup.apiObjectManager.getApiCall();
        getApiCall.getSpecificUser(idNumber);
        Report.log(TestBase.getScenario(), "Validated user with ID: " + idNumber);
    }

    @When("User should validate the response")
    public void user_should_validate_the_response() {
        Response response = BookApiTest.getBookRequest();
        log.info("API Response Body: {}", response.getBody().prettyPrint());
        Report.log(TestBase.getScenario(), "API Response Body:\n" + response.getBody().prettyPrint());
    }

    @Then("User should validate all the responses")
    public void user_should_validate_all_the_responses() {
        Response response = BookApiTest.getBookRequest();
        log.info("API Response Headers: {}", response.getHeaders());
        Report.log(TestBase.getScenario(), "API Response Headers:\n" + response.getHeaders());
    }
}