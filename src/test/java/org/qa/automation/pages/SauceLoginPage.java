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

    private final Page page;
    private final Locator sauceUserName;
    private final Locator saucePassword;
    private final Locator login;
    private final Locator errorMessage;

    public SauceLoginPage(Page page) {
        this.page = page;
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
        Report.log(TestBase.getScenario(), "User has successfully logged in with username: " + userName);
    }

    public void userLoginPageScreenShot() {
        Report.screenshot(TestBase.getScenario());
    }

    public void getTheUrl() {
        String actualUrl = page.url();
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        Assertions.assertEquals(expectedUrl, actualUrl, "URL mismatch after login.");
        Report.log(TestBase.getScenario(), "Assertion passed. Actual URL: " + actualUrl);
    }

    public void validateErrorMessage() {
        String actualErrorMes = errorMessage.textContent();
        String expectedErrorMes = "Epic sadface: Sorry, this user has been locked out.";
        log.info("Actual Error Message: {}", actualErrorMes);
        Assertions.assertEquals(expectedErrorMes, actualErrorMes, "Login error message did not match.");
        Report.log(TestBase.getScenario(), "Actual Error Message: " + actualErrorMes);
    }
}