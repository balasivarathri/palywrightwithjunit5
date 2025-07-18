package org.qa.automation.apis.apimethods;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class GetApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;

    public GetApiCall(APIRequestContext apiRequestContext) {
        this.apiRequestContext = apiRequestContext;
    }

    public void getApi() throws IOException {
        apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assertions.assertEquals(statusCode, 200);
        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has successfully extracted", 200, statusCode);
        System.out.println(apiResponse.headers().get("content-type"));
        String url = apiResponse.url();
        Report.log(scenario, "Extracted url from Response Body is  :  " + url);
        System.out.println("Extracted url from Response Body is  : " + url);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonHeader = objectMapper.writeValueAsString(apiResponse.headers());
        System.out.println(jsonHeader);
        Report.log(scenario, "Reponse JsonHeader is : " + jsonHeader);

//        ObjectMapper om = new ObjectMapper();
//        JsonNode jsonResponse = om.readTree(apiResponse.body());
//        String jsonBody = jsonResponse.toPrettyString();
//        System.out.println(jsonBody);
//        Report.log(scenario, "Reponse JsonBody is : " + jsonBody);


        String res = apiResponse.text();
        List<Map<String, Object>> users = objectMapper.readValue(res, new TypeReference<>() {});
        Map<String, Object> firstUser = users.get(0);
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println("Full JSON object:\n" + objectMapper.writeValueAsString(firstUser));
        System.out.println(firstUser.get("id"));
        System.out.println(firstUser.get("name"));
        System.out.println(firstUser.get("email"));
    }

    public void getSpecificUser(int idNumber) throws IOException {
        apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 509d2c5e3ed6879c6e88a3438220f77cadf61957088e8495c0348bb80934f969")
                        .setQueryParam("id", idNumber));

        int statusCode = apiResponse.status();
        System.out.println("Extracted status code for get api is : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println("GET API Reponse JsonBody is : \n" + jsonBody);
        Report.log(scenario, "GET API Reponse JsonBody is : \n" + jsonBody);
    }

}