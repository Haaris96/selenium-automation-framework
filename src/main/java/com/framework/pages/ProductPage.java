package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.utils.WaitUtils;
import com.framework.enums.WaitStrategy;
import com.framework.reports.ExtentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the individual Product Detail page.
 */
public class ProductPage extends BasePage {

    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = ".inventory_details_img")
    private WebElement productImage;

    @FindBy(css = "button[data-test^='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    private static final By PRODUCT_NAME_LOCATOR = By.cssSelector(".inventory_details_name");

    public boolean isProductPageLoaded() {
        return WaitUtils.waitForVisibility(PRODUCT_NAME_LOCATOR) != null;
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean isProductImageDisplayed() {
        return isDisplayed(productImage);
    }

    public ProductPage addToCart() {
        click(addToCartButton, WaitStrategy.CLICKABLE, "Add to Cart");
        ExtentLogger.info("Added product to cart from detail page");
        return this;
    }

    public HomePage goBackToProducts() {
        click(backToProductsButton, WaitStrategy.CLICKABLE, "Back to Products");
        ExtentLogger.info("Navigated back to products");
        return new HomePage();
    }
}
