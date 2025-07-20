package org.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;

@Slf4j
public class CheckOutOverViewPage {

    private final Page page;
    private final Locator finishButton;
    private final Locator successMessage;
    private final Locator clickOnOpenMenu;
    private final Locator clickLogOut;

    public CheckOutOverViewPage(Page page) {
        this.page = page;
        this.finishButton = page.locator("//button[@name='finish']");
        this.successMessage = page.locator("//h2[normalize-space()='Thank you for your order!']");
        this.clickOnOpenMenu = page.locator("//button[normalize-space()='Open Menu']");
        this.clickLogOut = page.locator("//a[normalize-space()='Logout']");
    }

    public void clickFinishButton() {
        finishButton.click();
    }

    public String getSuccessMessage() {
        return successMessage.textContent();
    }

    public void clickOnOpenMenuButton() {
        clickOnOpenMenu.click();
    }

    public void clickLogOutButton() {
        clickLogOut.click();
    }

    public String getUrl() {
        return page.url();
    }

    public void finishCheckOut() {
        clickFinishButton();
        Report.screenshot(TestBase.getScenario());
        String successMsg = getSuccessMessage();
        Report.log(TestBase.getScenario(), "Success Message is: " + successMsg);
    }

    public void logout() {
        String expectedUrl = "https://www.saucedemo.com/";
        clickOnOpenMenuButton();
        clickLogOutButton();
        String actualUrl = getUrl();
        Assertions.assertEquals(expectedUrl, actualUrl, "Logout failed. URL mismatch.");
        Report.log(TestBase.getScenario(), "User logged out successfully, current URL: " + actualUrl);
    }
}