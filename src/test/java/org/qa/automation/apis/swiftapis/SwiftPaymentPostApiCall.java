package org.qa.automation.apis.swiftapis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.POJO_Payloads.Users;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;
import org.qa.automation.utils.Pacs008RefGenerator;
import org.testng.Assert;

import java.io.IOException;
import java.util.Map;

import static org.qa.automation.utils.Payloads.generateThreeDigitNumber;

@Slf4j
public class SwiftPaymentPostApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;

    public String token;

    public SwiftPaymentPostApiCall(APIRequestContext apiRequestContext) {
        this.apiRequestContext = apiRequestContext;
    }

    public void pacs008PostCall(String payload) throws JsonProcessingException {
        apiResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
                        .setData(payload));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assert.assertEquals(statusCode, 201);
        ObjectMapper objectMapper = new ObjectMapper();
        Users actualUsers = objectMapper.readValue(responseText, Users.class);
        System.out.println(actualUsers);
        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has not successfully extracted", 201, statusCode);
    }
    public int validateSwiftPostApiResponse() throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println(jsonBody);
        Report.log(scenario, "Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {});
        int amount = (Integer) jsonResponseBody.get("amount");
        System.out.println("Extracted Id is : " + amount);
        return amount;
    }
}

