package org.qa.automation.apis.bookerapis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.POJO_Payloads.BookingPayload;
import org.qa.automation.POJO_Payloads.BookingWrapper;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;
import org.qa.automation.utils.DateUtils;
import org.qa.automation.utils.Payloads;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import static org.qa.automation.utils.Payloads.generateRandomString;
import static org.qa.automation.utils.Payloads.generateThreeDigitNumber;

@Slf4j
public class BookerUserApiCall extends TestBase {

    APIRequestContext apiRequestContext;
    APIResponse apiResponse;
    public String token;

    public BookerUserApiCall(APIRequestContext apiRequestContext) {
        this.apiRequestContext = apiRequestContext;
    }

    public void generateBookerToken() throws IOException {

        apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(Payloads.bookerUserPayload()));
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code For POST Call is: " + statusCode);
        Assertions.assertEquals(statusCode, 200);
        Report.log(scenario, "Status Code has successfully extracted : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        Report.log(scenario, "Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {
        });
        token = (String) jsonResponseBody.get("token");
        System.out.println("Extracted token is : " + token);
        Report.log(scenario, "Extracted Token is : " + token);
    }

    public void postBookerUserApi() throws IOException {

        //creating payload by using lombok denpendency
        String checkin = DateUtils.generateRandomCheckinDate();
        String checkout = DateUtils.generateRandomCheckoutDate(checkin);

        BookingPayload.BookingDates bookingDates = BookingPayload.BookingDates.builder()
                .checkin(checkin)
                .checkout(checkout)
                .build();
        BookingPayload bookingPayload = BookingPayload.builder()
                .firstname(generateRandomString().toLowerCase())
                .lastname(generateRandomString().toLowerCase())
                .totalprice(generateThreeDigitNumber())
                .depositpaid(true)
                .bookingdates(bookingDates)
                .additionalneeds("Breakfast")
                .build();

        apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
                        .setData(bookingPayload));
        String responseText = apiResponse.text();
        int statusCode = apiResponse.status();
        System.out.println("Response Status Code for POST call is: " + statusCode);
        Assertions.assertEquals(statusCode, 200);
        ObjectMapper objectMapper = new ObjectMapper();
        BookingWrapper wrapper = objectMapper.readValue(responseText, BookingWrapper.class);
        BookingPayload bookingPayloadResponse = wrapper.getBooking();

        Assertions.assertEquals(bookingPayload.getFirstname(), bookingPayloadResponse.getFirstname());
        Assertions.assertEquals(bookingPayload.getLastname(), bookingPayloadResponse.getLastname());
        Assertions.assertEquals(bookingPayload.getTotalprice(), bookingPayloadResponse.getTotalprice());
        Assertions.assertEquals(bookingPayload.isDepositpaid(), bookingPayloadResponse.isDepositpaid());
        Assertions.assertEquals(bookingPayload.getBookingdates(), bookingPayloadResponse.getBookingdates());
        Assertions.assertEquals(bookingPayload.getAdditionalneeds(), bookingPayloadResponse.getAdditionalneeds());
        Report.validate(scenario, "Status Code has successfully extracted", "Status Code has not successfully extracted", 200, statusCode);
    }

    public int validateBookerPostApiResponse() throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println(jsonBody);
        Report.log(scenario, "Booker Post API Reponse JsonBody is : \n" + jsonBody);
        Map<String, Object> jsonResponseBody = om.readValue(jsonBody, new TypeReference<>() {
        });
        int bookingid = (Integer) jsonResponseBody.get("bookingid");
        System.out.println("Extracted Booking Id is : " + bookingid);
        return bookingid;
    }

    public void getBookerUser(int bookingid) throws IOException {
        apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking/" + bookingid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token));
        int statusCode = apiResponse.status();
        System.out.println("Extracted status code for get api is : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
        System.out.println("BOOKER API GET Reponse JsonBody is : \n" + jsonBody);
        Report.log(scenario, "BOOKER API GET Reponse JsonBody is : \n" + jsonBody);
    }

    public void getBookerForAllUsers() throws IOException {
        apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + token));
        int statusCode = apiResponse.status();
        System.out.println("Extracted status code for get api is : " + statusCode);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonResponse = om.readTree(apiResponse.body());
        String jsonBody = jsonResponse.toPrettyString();
//        System.out.println("BOOKER API Booking Ids are : \n" + jsonBody);
        Report.log(scenario, "BOOKER API Booking Ids are : \n" + jsonBody);
    }

    public void updateBookerUser(int bookingid) throws IOException {
        String checkin = DateUtils.generateRandomCheckinDate();
        String checkout = DateUtils.generateRandomCheckoutDate(checkin);

        BookingPayload.BookingDates bookingDates = BookingPayload.BookingDates.builder()
                .checkin(checkin)
                .checkout(checkout)
                .build();

        BookingPayload bookingPayload = BookingPayload.builder()
                .firstname("Neshu123")
                .lastname("Sivathri")
                .totalprice(131)
                .depositpaid(true)
                .bookingdates(bookingDates)
                .additionalneeds("Breakfast")
                .build();

        // Encode Basic Auth (same as Postman)
        String credentials = "admin:password123";
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());

        apiResponse = apiRequestContext.put("https://restful-booker.herokuapp.com/booking/" + bookingid,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setHeader("Authorization", "Basic " + basicAuth)
                        .setData(bookingPayload));

        int statusCode = apiResponse.status();
        String responseText = apiResponse.text();
        System.out.println("PUT Status Code: " + statusCode);
        System.out.println("PUT Response Body: " + responseText);

        Assertions.assertEquals(200, statusCode);

        ObjectMapper objectMapper = new ObjectMapper();
        BookingPayload responsePayload = objectMapper.readValue(responseText, BookingPayload.class);

        Assertions.assertEquals(bookingPayload.getFirstname(), responsePayload.getFirstname());
        Assertions.assertEquals(bookingPayload.getLastname(), responsePayload.getLastname());
        Assertions.assertEquals(bookingPayload.getTotalprice(), responsePayload.getTotalprice());
        Assertions.assertEquals(bookingPayload.isDepositpaid(), responsePayload.isDepositpaid());
        Assertions.assertEquals(bookingPayload.getBookingdates(), responsePayload.getBookingdates());
        Assertions.assertEquals(bookingPayload.getAdditionalneeds(), responsePayload.getAdditionalneeds());

        Report.validate(scenario, "Booking updated successfully", "Booking update failed", 200, statusCode);
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

