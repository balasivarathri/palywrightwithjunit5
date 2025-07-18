package org.qa.automation.apis.apimethods;

import org.junit.jupiter.api.Assertions;
import org.qa.automation.POJO_Payloads.TokenGenerator;
import org.qa.automation.POJO_Payloads.UsersList;
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

import static org.qa.automation.utils.Payloads.generateRandomString;
import static org.qa.automation.utils.Payloads.generateThreeDigitNumber;

@Slf4j
public class UserApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;
    public String token;

    public UserApiCall(APIRequestContext apiRequestContext) {
        this.apiRequestContext = apiRequestContext;
    }

    public void generateToken() throws IOException {

        //creating payload by using lombok denpendency
        TokenGenerator tokenGenerator = TokenGenerator.builder()
                .username(generateRandomString().toLowerCase())
                .build();
        apiResponse = apiRequestContext.post("http://localhost:3000/login",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(tokenGenerator));
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assertions.assertEquals(statusCode, 200);
        Report.log(scenario, "Status Code has successfully extracted : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        Report.log(scenario, "Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {
        });
        token = (String) jsonResponseBody.get("accessToken");
        System.out.println("Extracted token is : " + token);
        Report.log(scenario, "Extracted Token is : " + token);
    }

    public void postUserApi() throws JsonProcessingException {

        //creating payload by using lombok denpendency
        UsersList usersList = UsersList.builder()
                .username(generateRandomString().toLowerCase() + generateThreeDigitNumber())
                .firstName(generateRandomString().toLowerCase())
                .lastName(generateRandomString().toLowerCase())
                .email("test" + generateThreeDigitNumber() + "@gmail.com")
                .phone("0743955" + generateThreeDigitNumber())
                .build();
        apiResponse = apiRequestContext.post("http://localhost:3000/user",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
                        .setData(usersList));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assertions.assertEquals(statusCode, 201);
        ObjectMapper objectMapper = new ObjectMapper();
        UsersList actualUsers = objectMapper.readValue(responseText, UsersList.class);
        System.out.println(actualUsers);

        Assertions.assertEquals(actualUsers.getUsername(), usersList.getUsername());
        Assertions.assertEquals(actualUsers.getFirstName(), usersList.getFirstName());
        Assertions.assertEquals(actualUsers.getLastName(), usersList.getLastName());
        Assertions.assertEquals(actualUsers.getEmail(), usersList.getEmail());
        Assertions.assertEquals(actualUsers.getPhone(), usersList.getPhone());
        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has not successfully extracted", 201, statusCode);
    }

    public int validatePostApiResponse() throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println(jsonBody);
        Report.log(scenario, "Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {
        });
        int id = (Integer) jsonResponseBody.get("id");
        System.out.println("Extracted Id is : " + id);
        return id;
    }

    public void getUser(int idNumber) throws IOException {
        apiResponse = apiRequestContext.get("http://localhost:3000/user/" + idNumber,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token));
        int statusCode = apiResponse.status();
        System.out.println("Extracted status code for get api is : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println("GET API Reponse JsonBody is : \n" + jsonBody);
        Report.log(scenario, "GET API Reponse JsonBody is : \n" + jsonBody);
    }

    public void updateUser(int idNumber) throws IOException {

        UsersList usersList = UsersList.builder()
                .username(generateRandomString().toLowerCase() + generateThreeDigitNumber())
                .firstName(generateRandomString().toLowerCase())
                .lastName(generateRandomString().toLowerCase())
                .email("test" + generateThreeDigitNumber() + "@gmail.com")
                .phone("0743955" + generateThreeDigitNumber())
                .build();
        apiResponse = apiRequestContext.put("http://localhost:3000/user/" + idNumber,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
                        .setData(usersList));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is: " + statusCode);
        Assertions.assertEquals(statusCode, 200);
        ObjectMapper objectMapper = new ObjectMapper();
        UsersList actualUsers = objectMapper.readValue(responseText, UsersList.class);
        System.out.println(actualUsers);
        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has not successfully extracted", 200, statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println("Updated API Reponse JsonBody is : \n" + jsonBody);
        Report.log(scenario, "Updated API Reponse JsonBody is : \n" + jsonBody);
    }

    public void deleteUser(int idNumber) {
        apiResponse = apiRequestContext.delete("http://localhost:3000/user/" + idNumber,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token));
        int statusCode = apiResponse.status();
        System.out.println("Extracted status code for delete api is : " + statusCode);
        Assertions.assertEquals(statusCode, 204);
    }
}

