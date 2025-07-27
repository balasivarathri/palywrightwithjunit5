package org.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.base.TestBase;
import org.qa.automation.report.Report;

@Slf4j
public class InventoryPage extends TestBase {

    private final Page page;
    private final Locator firstItem;
    private final Locator secondItem;
    private final Locator cartIcon;
    private final Locator itemsDropDown;

    public InventoryPage(Page page) {
        this.page = page;
        this.firstItem = page.locator("//button[@name='add-to-cart-sauce-labs-backpack']");
        this.secondItem = page.locator("//button[@name='add-to-cart-sauce-labs-bike-light']");
        this.cartIcon = page.locator("//a[@data-test='shopping-cart-link']");
        this.itemsDropDown = page.locator("//select[@data-test='product-sort-container']");
    }

    public void clickFirstItemButton() {
        firstItem.click();
    }

    public void clickSecondItemButton() {
        secondItem.click();
    }

    public void clickCartIconButton() {
        cartIcon.click();
    }

    public void clickItemsDropDown() {
        Locator options = itemsDropDown.locator("option");
        int count = options.count();
        log.info("Extracted Dropdown Items:");
        Report.log(TestBase.getScenario(), "Extracted Dropdown Items:");
        for (int i = 0; i < count; i++) {
            String optionText = options.nth(i).textContent().trim();
            log.info(optionText);
            Report.log(TestBase.getScenario(), optionText);
        }
    }

    public void addItemsToTheCart() {
        String expectedAddedItems = "2";
        clickFirstItemButton();
        clickSecondItemButton();
        String actualAddedItems = cartIcon.textContent().trim();

        log.info("User added items to the cart: {}", actualAddedItems);
        Assertions.assertEquals(expectedAddedItems, actualAddedItems, "Item count in cart does not match expected.");

        Report.screenshot(TestBase.getScenario());
        Report.log(TestBase.getScenario(), "Added items count in cart: " + actualAddedItems);

        clickCartIconButton();
    }
}