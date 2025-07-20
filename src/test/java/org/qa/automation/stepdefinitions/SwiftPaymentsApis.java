package org.qa.automation.stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.qa.automation.apis.swiftapis.SwiftPaymentPostApiCall;
import org.qa.automation.base.TestBase;
import org.qa.automation.contextsetup.ApiContextSetup;
import org.qa.automation.report.Report;
import org.qa.automation.utils.*;

import java.io.IOException;

public class SwiftPaymentsApis extends TestBase {

    ApiContextSetup apiContextSetup;
    SwiftPaymentPostApiCall swiftPaymentPostApiCall;

    public String trno;
    public String currency;
    public String uetr;
    public String amount;
    public String senderBic;
    public String receiverBic;
    public String chargetype;
    public String creationDate;
    public String valueDate;
    public String finalPayload;

    public SwiftPaymentsApis(ApiContextSetup apiContextSetup) {
        this.apiContextSetup = apiContextSetup;
    }

    @Given("^User should be validate all the mandatory fields with all parameters senderBic (.+), receiverBic (.+), chargeType (.+), currency (.+), payload (.+)$")
    public void user_should_be_validate_all_the_mandatory_fields_with_all_parameters_senderbic_receiverbic_chargetype_payload(String senderBic, String receiverBic, String chargetype, String currency, String payload) {
        this.creationDate = IsoDateTimeGenerator.generateIsoDateTime();
        this.trno = Pacs008RefGenerator.generateReference();
        this.amount = String.valueOf(RandomAmountGenerator.generateRandomAmount());
        this.currency = currency;
        this.uetr = UETRGenerator.generateUETR();
        this.valueDate = ValueDateGenerator.date1();
        this.senderBic = senderBic;
        this.receiverBic = receiverBic;
        this.chargetype = chargetype;
        payload = payload.replaceAll("~creationDate~", creationDate);
        payload = payload.replaceAll("~senderBic~", senderBic);
        payload = payload.replaceAll("~receiverBic~", receiverBic);
        payload = payload.replaceAll("~trno~", trno);
        payload = payload.replaceAll("~valueDate~", valueDate);
        payload = payload.replaceAll("~currency~", currency);
        payload = payload.replaceAll("~uetr~", uetr);
        payload = payload.replaceAll("~amount~", amount);
        System.out.println("Extracted Reference Number is : " + trno);
        payload = ExcelMultiline.unEscapeLineBreak(payload);
        finalPayload = payload;
        System.out.println(payload);
        Report.log(TestBase.getScenario(), "Generated Pacs008 message is : " + payload);
    }
    @When("User should be able to drop the pacs008 payload into the incoming queue")
    public void user_should_be_able_to_drop_the_pacs008_payload_into_the_incoming_queue() throws JsonProcessingException {
        swiftPaymentPostApiCall = apiContextSetup.apiObjectManager.swiftPaymentPostApiCall();
        swiftPaymentPostApiCall.pacs008PostCall(finalPayload);
    }
    @Then("User should be able to validate all the post api response fields")
    public void user_should_be_able_to_validate_all_the_post_api_response_fields() throws IOException {
        swiftPaymentPostApiCall.validateSwiftPostApiResponse();
    }
}