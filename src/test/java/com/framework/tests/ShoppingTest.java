package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.config.ConfigReader;
import com.framework.pages.*;
import com.framework.reports.ExtentLogger;
import com.framework.reports.ExtentReport;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG test class for Shopping Cart and Checkout modules.
 */
public class ShoppingTest extends BaseTest {

    private LoginPage    loginPage;
    private HomePage     homePage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        loginPage = new LoginPage();
        homePage  = loginPage.loginSuccessfully(
            ConfigReader.getInstance().getUsername(),
            ConfigReader.getInstance().getPassword()
        );
    }

    @Test(
        groups      = {"smoke", "regression", "cart"},
        priority    = 1,
        description = "Add a product to cart and verify cart badge"
    )
    public void testAddProductToCart() {
        ExtentReport.createTest("testAddProductToCart",
            "Verify product can be added to cart");

        homePage.addProductToCartByName("Sauce Labs Backpack");

        Assert.assertEquals(homePage.getCartItemCount(), 1,
            "Cart should contain 1 item");
        ExtentLogger.passWithScreenshot("Product added to cart successfully");
    }

    @Test(
        groups      = {"regression", "cart"},
        priority    = 2,
        description = "Add multiple products and verify cart count"
    )
    public void testAddMultipleProductsToCart() {
        ExtentReport.createTest("testAddMultipleProductsToCart",
            "Add multiple products and verify cart count");

        homePage.addProductToCartByName("Sauce Labs Backpack");
        homePage.addProductToCartByName("Sauce Labs Bike Light");

        Assert.assertEquals(homePage.getCartItemCount(), 2,
            "Cart should contain 2 items");
        ExtentLogger.passWithScreenshot("Multiple products added to cart");
    }

    @Test(
        groups      = {"regression", "cart"},
        priority    = 3,
        description = "Remove a product from cart"
    )
    public void testRemoveProductFromCart() {
        ExtentReport.createTest("testRemoveProductFromCart",
            "Remove product and verify cart is empty");

        homePage.addProductToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(homePage.getCartItemCount(), 1);

        homePage.removeProductFromCartByName("Sauce Labs Backpack");
        Assert.assertEquals(homePage.getCartItemCount(), 0,
            "Cart should be empty after removal");
        ExtentLogger.passWithScreenshot("Product removed from cart");
    }

    @Test(
        groups      = {"smoke", "regression", "checkout"},
        priority    = 4,
        description = "Complete end-to-end checkout flow"
    )
    public void testCompleteCheckoutFlow() {
        ExtentReport.createTest("testCompleteCheckoutFlow",
            "Full E2E: add product → cart → checkout → order complete");

        // Add product
        homePage.addProductToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(homePage.getCartItemCount(), 1);

        // Go to cart
        CartPage cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.isCartPageLoaded());
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"));

        // Checkout
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillShippingDetails("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Verify total is shown
        String total = checkoutPage.getTotalAmount();
        ExtentLogger.info("Order total: " + total);
        Assert.assertFalse(total.isEmpty(), "Total should be displayed");

        // Finish
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(),
            "Order complete confirmation should be shown");
        Assert.assertEquals(checkoutPage.getOrderCompleteMessage(),
            "Thank you for your order!");

        ExtentLogger.passWithScreenshot("E2E checkout completed successfully");
    }

    @Test(
        groups      = {"regression", "product"},
        priority    = 5,
        description = "View product detail page and verify information"
    )
    public void testProductDetailPage() {
        ExtentReport.createTest("testProductDetailPage",
            "Navigate to product detail and verify content");

        String productName = "Sauce Labs Backpack";
        ProductPage productPage = homePage.openProduct(productName);

        Assert.assertTrue(productPage.isProductPageLoaded());
        Assert.assertEquals(productPage.getProductName(), productName);
        Assert.assertTrue(productPage.isProductImageDisplayed(),
            "Product image should be visible");
        Assert.assertFalse(productPage.getProductDescription().isEmpty(),
            "Product description should not be empty");
        Assert.assertFalse(productPage.getProductPrice().isEmpty(),
            "Product price should not be empty");

        ExtentLogger.passWithScreenshot("Product detail page verified");
    }

    @Test(
        groups      = {"regression", "sort"},
        priority    = 6,
        description = "Sort products by Name A to Z"
    )
    public void testSortProductsByName() {
        ExtentReport.createTest("testSortProductsByName",
            "Sort products alphabetically and verify order");

        homePage.sortProductsBy("Name (A to Z)");
        java.util.List<String> names = homePage.getAllProductNames();

        // Verify sorted
        java.util.List<String> sorted = new java.util.ArrayList<>(names);
        java.util.Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Products should be sorted A to Z");

        ExtentLogger.passWithScreenshot("Products sorted A to Z verified");
    }
}
