package org.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.qa.automation.report.Report;
import static org.qa.automation.base.TestBase.scenario;

@Slf4j
public class InventoryPage {

    private final Locator firstItem;
    private final Locator secondItem;
    private final Locator cartIcon;
    private final Locator itemsDropDown;

    public InventoryPage(Page page) {
        this.firstItem = page.locator("//button[@name='add-to-cart-sauce-labs-backpack']");
        this.secondItem = page.locator("//button[@name='add-to-cart-sauce-labs-bike-light']");
        this.cartIcon = page.locator("//a[@data-test='shopping-cart-link']");
        this.itemsDropDown = page.locator("//select[@data-test='product-sort-container']");
    }

    public void clickFirstItemButton() {
        firstItem.click();
    }
    public void clickiSecondItemButton() {
        secondItem.click();
    }
    public void clickCartIconButton() {
        cartIcon.click();
    }
    public void clickItemsDropDown() {
        Locator options = itemsDropDown.locator("option");
        int count = options.count();
        System.out.println("Extracted Dropdown Items are : ");
        Report.log(scenario,"Extracted Dropdown Items are : ");
        for (int i = 0; i < count; i++) {
            String optionText = options.nth(i).textContent().trim();
            System.out.println(optionText);
            Report.log(scenario, optionText);
        }
    }
    public void addItemsToTheCart() {
        String expectedAddedItems = "2";
        clickFirstItemButton();
        clickiSecondItemButton();
        String actualaAddedItems = cartIcon.textContent();
        log.info("User has add the items to the cart");
        Assertions.assertEquals(actualaAddedItems, expectedAddedItems);
        Report.screenshot(scenario);
        Report.log(scenario, "Added items are : " + actualaAddedItems);
        clickCartIconButton();
    }
}
