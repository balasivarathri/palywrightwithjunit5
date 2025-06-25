package org.qa.automation.apis.apimethods;

import org.qa.automation.POJO_Payloads.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;
import org.testng.Assert;

import java.io.IOException;
import java.util.Map;

import static org.qa.automation.utils.Payloads.generateThreeDigitNumber;

@Slf4j
public class PutApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;
    String jsonResponseBody;

    public PutApiCall(APIRequestContext apiRequestContext) {
        this.apiRequestContext = apiRequestContext;
    }

    public void postApi() throws JsonProcessingException {

        //creating payload by using lombok denpendency
        Users users = Users.builder()
                .name("Neshu" + generateThreeDigitNumber())
                .email("test" + generateThreeDigitNumber() + "@gmail.com")
                .gender("female")
                .status("active")
                .build();
        apiResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer cd064f420af4fb8506f62c8ee4b027fb4355d81422fde242bbfadb09c6918edb")
                        .setData(users));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assert.assertEquals(statusCode, 201);
        ObjectMapper objectMapper = new ObjectMapper();
        Users actualUsers = objectMapper.readValue(responseText, Users.class);
        System.out.println(actualUsers);


        Assert.assertNotNull(actualUsers.getId());
        Assert.assertEquals(actualUsers.getName(),users.getName());
        Assert.assertEquals(actualUsers.getEmail(),users.getEmail());
        Assert.assertEquals(actualUsers.getGender(),users.getGender());
        Assert.assertEquals(actualUsers.getStatus(),users.getStatus());

        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has not successfully extracted", 201, statusCode);
    }

    public int validatePostApiResponse() throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println(jsonBody);
        Report.log(scenario, "Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {});
        int id = (Integer) jsonResponseBody.get("id");
        System.out.println("Extracted Id is : " + id);
        return id;
    }
}

