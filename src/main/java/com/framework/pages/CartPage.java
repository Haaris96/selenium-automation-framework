package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.utils.WaitUtils;
import com.framework.enums.WaitStrategy;
import com.framework.reports.ExtentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the Shopping Cart page.
 */
public class CartPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item_label .inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = ".cart_item_label .inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    private static final By TITLE_LOCATOR = By.className("title");

    public boolean isCartPageLoaded() {
        return WaitUtils.waitForVisibility(TITLE_LOCATOR) != null;
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public List<String> getCartItemNames() {
        return cartItemNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public int getCartItemCount() {
        return cartItemNames.size();
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton, WaitStrategy.CLICKABLE, "Checkout button");
        ExtentLogger.info("Proceeded to checkout");
        return new CheckoutPage();
    }

    public HomePage continueShopping() {
        click(continueShoppingButton, WaitStrategy.CLICKABLE, "Continue shopping button");
        ExtentLogger.info("Continued shopping");
        return new HomePage();
    }
}
