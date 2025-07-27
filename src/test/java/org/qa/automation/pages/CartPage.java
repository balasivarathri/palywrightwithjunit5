package org.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;

@Slf4j
public class CartPage extends TestBase {

    private final Locator checkOutButton;
    private final Locator enterFirstName;
    private final Locator enterLastName;
    private final Locator enterPostalCode;
    private final Locator continueButton;

    public CartPage(Page page) {
        this.checkOutButton = page.locator("//button[@name='checkout']");
        this.enterFirstName = page.locator("input[placeholder='First Name']");
        this.enterLastName = page.locator("//input[@placeholder='Last Name']");
        this.enterPostalCode = page.locator("//input[@placeholder='Zip/Postal Code']");
        this.continueButton = page.locator("//input[@name='continue']");
    }

    public void clickCheckOutButton() {
        checkOutButton.click();
    }

    public void enterFirstName(String firstName) {
        enterFirstName.fill(firstName);
    }

    public void enterLastName(String lastName) {
        enterLastName.fill(lastName);
    }

    public void enterPostalCode(String postalCode) {
        enterPostalCode.fill(postalCode);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void doTheCheckOutProcess() {
        Report.log(TestBase.getScenario(), "Verify that the two added items are displayed in the cart with the correct names and prices");
        Report.screenshot(TestBase.getScenario());
        clickCheckOutButton();
    }

    public void enterTheCheckOutDetails() {
        enterFirstName("Balu");
        enterLastName("Sivarathri");
        enterPostalCode("1686");
        clickContinueButton();
        Report.screenshot(TestBase.getScenario());
    }
}
