package org.qa.automation.apis.apimethods;

import org.junit.jupiter.api.Assertions;
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
import java.io.IOException;
import java.util.Map;
import static org.qa.automation.utils.Payloads.generateThreeDigitNumber;

@Slf4j
public class PostApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;

    public PostApiCall(APIRequestContext apiRequestContext) {
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
                        .setHeader("Authorization", "Bearer 509d2c5e3ed6879c6e88a3438220f77cadf61957088e8495c0348bb80934f969")
                        .setData(users));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assertions.assertEquals(statusCode, 201);
        ObjectMapper objectMapper = new ObjectMapper();
        Users actualUsers = objectMapper.readValue(responseText, Users.class);
        System.out.println(actualUsers);


        Assertions.assertNotNull(actualUsers.getId());
        Assertions.assertEquals(actualUsers.getName(),users.getName());
        Assertions.assertEquals(actualUsers.getEmail(),users.getEmail());
        Assertions.assertEquals(actualUsers.getGender(),users.getGender());
        Assertions.assertEquals(actualUsers.getStatus(),users.getStatus());

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

