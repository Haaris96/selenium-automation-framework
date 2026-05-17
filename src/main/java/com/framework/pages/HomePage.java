package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.enums.WaitStrategy;
import com.framework.reports.ExtentLogger;
import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the SauceDemo Products / Home page.
 */
public class HomePage extends BasePage {

    // ─── Locators ─────────────────────────────────────────────────────────

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    private static final By CART_BADGE_LOCATOR = By.cssSelector(".shopping_cart_badge");
    private static final By PRODUCTS_TITLE      = By.className("title");

    // ─── Page Actions ─────────────────────────────────────────────────────

    public boolean isHomePageLoaded() {
        WaitUtils.waitForVisibility(PRODUCTS_TITLE);
        boolean loaded = "Products".equalsIgnoreCase(getText(pageTitle));
        ExtentLogger.info("Home page loaded: " + loaded);
        return loaded;
    }

    public String getPageHeading() {
        return getText(pageTitle);
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }

    public List<String> getAllProductPrices() {
        return productPrices.stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }

    public int getProductCount() {
        return inventoryItems.size();
    }

    public HomePage sortProductsBy(String option) {
        selectByVisibleText(sortDropdown, option);
        ExtentLogger.info("Sorted products by: " + option);
        return this;
    }

    public HomePage addProductToCartByName(String productName) {
        By addButton = By.xpath(
            "//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']" +
            "//button[contains(text(),'Add to cart')]");
        click(addButton, WaitStrategy.CLICKABLE, "Add to cart: " + productName);
        ExtentLogger.info("Added product to cart: " + productName);
        return this;
    }

    public HomePage removeProductFromCartByName(String productName) {
        By removeButton = By.xpath(
            "//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']" +
            "//button[contains(text(),'Remove')]");
        click(removeButton, WaitStrategy.CLICKABLE, "Remove from cart: " + productName);
        ExtentLogger.info("Removed product from cart: " + productName);
        return this;
    }

    public CartPage goToCart() {
        click(cartIcon, "Cart icon");
        ExtentLogger.info("Navigated to cart");
        return new CartPage();
    }

    public int getCartItemCount() {
        if (!isDisplayed(CART_BADGE_LOCATOR)) return 0;
        return Integer.parseInt(getText(cartBadge));
    }

    public ProductPage openProduct(String productName) {
        By productLink = By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']");
        click(productLink, WaitStrategy.CLICKABLE, "Product: " + productName);
        ExtentLogger.info("Opened product: " + productName);
        return new ProductPage();
    }

    public void openHamburgerMenu() {
        click(hamburgerMenu, "Hamburger menu");
    }

    public LoginPage logout() {
        openHamburgerMenu();
        WaitUtils.waitForElementToBeClickable(logoutLink);
        click(logoutLink, "Logout link");
        ExtentLogger.info("Logged out");
        return new LoginPage();
    }
}
