package org.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;
import org.qa.automation.urls.Url;

@Slf4j
public class SauceLoginPage extends TestBase {


    private final Locator sauceUserName;
    private final Locator saucePassword;
    private final Locator login;
    private final Locator errorMessage;

    public SauceLoginPage(Page page) {
        this.sauceUserName = page.locator("//input[@placeholder='Username']");
        this.saucePassword = page.locator("//input[@placeholder='Password']");
        this.login = page.locator("//input[@name='login-button']");
        this.errorMessage = page.locator("h3[data-test='error']");
    }

    public void enterUsername(String username) {
        sauceUserName.fill(username);
    }
    public void enterPassword(String password) {
        saucePassword.fill(password);
    }
    public void clickLogin() {
        login.click();
    }

    public void userSauceLogin(String url, String userName, String password) {
        page.navigate(Url.getUrl(url));
        enterUsername(userName);
        enterPassword(password);
        clickLogin();
        Report.log(scenario, "User has successfully logged in");
    }

    public void userLoginPageScreenShot() {
        Report.screenshot(scenario);
    }

    public void getTheUrl() {
        String actualUrl = page.url();
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        Assertions.assertEquals(actualUrl, expectedUrl);
        Report.log(scenario, "Assertion passed and Actual Url is : " + actualUrl);
    }

    public void validateErrorMessage() {
        String actualErrorMes = errorMessage.textContent();
        String expectedErrorMes = "Epic sadface: Sorry, this user has been locked out.";
        log.info("Actual Error Message is : " + actualErrorMes);
        Assertions.assertEquals(expectedErrorMes, actualErrorMes);
        Report.log(scenario, "Actual Error Message is : " + actualErrorMes);
    }

}
