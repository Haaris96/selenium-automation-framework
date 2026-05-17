package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.utils.WaitUtils;
import com.framework.enums.WaitStrategy;
import com.framework.reports.ExtentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the Checkout page.
 */
public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement orderCompleteHeader;

    private static final By ORDER_COMPLETE_LOCATOR = By.cssSelector(".complete-header");

    public CheckoutPage fillShippingDetails(String firstName, String lastName, String postalCode) {
        type(firstNameField, firstName, "First Name");
        type(lastNameField, lastName, "Last Name");
        type(postalCodeField, postalCode, "Postal Code");
        ExtentLogger.info("Filled shipping details");
        return this;
    }

    public CheckoutPage clickContinue() {
        click(continueButton, WaitStrategy.CLICKABLE, "Continue button");
        return this;
    }

    public CheckoutPage clickFinish() {
        click(finishButton, WaitStrategy.CLICKABLE, "Finish button");
        return this;
    }

    public String getOrderCompleteMessage() {
        WaitUtils.waitForVisibility(ORDER_COMPLETE_LOCATOR);
        return getText(orderCompleteHeader);
    }

    public boolean isOrderComplete() {
        try {
            WaitUtils.waitForVisibility(ORDER_COMPLETE_LOCATOR);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public String getTotalAmount() {
        WaitUtils.waitForVisibility(totalLabel);
        return getText(totalLabel);
    }
}
