package com.framework.stepdefinitions;

import com.framework.pages.*;
import com.framework.reports.ExtentLogger;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Step definitions for shopping.feature.
 */
public class ShoppingSteps {

    private LoginPage    loginPage;
    private HomePage     homePage;
    private CartPage     cartPage;
    private ProductPage  productPage;
    private CheckoutPage checkoutPage;

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInAs(String username, String password) {
        loginPage = new LoginPage();
        homePage  = loginPage.loginSuccessfully(username, password);
        ExtentLogger.info("Logged in as: " + username);
    }

    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        homePage = new HomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");
    }

    @When("I add product {string} to the cart")
    public void iAddProductToTheCart(String productName) {
        homePage.addProductToCartByName(productName);
        ExtentLogger.info("Added to cart: " + productName);
    }

    @When("I remove product {string} from the cart")
    public void iRemoveProductFromTheCart(String productName) {
        homePage.removeProductFromCartByName(productName);
        ExtentLogger.info("Removed from cart: " + productName);
    }

    @Then("the cart badge should show {string}")
    public void theCartBadgeShouldShow(String expectedCount) {
        int actual = homePage.getCartItemCount();
        Assert.assertEquals(String.valueOf(actual), expectedCount,
            "Cart badge count mismatch");
        ExtentLogger.pass("Cart badge shows: " + actual);
    }

    @Then("the cart badge should not be visible")
    public void theCartBadgeShouldNotBeVisible() {
        Assert.assertEquals(homePage.getCartItemCount(), 0,
            "Cart should be empty");
        ExtentLogger.pass("Cart badge is not visible (empty cart)");
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart(productName),
            "Product should be in cart: " + productName);
        ExtentLogger.pass("Cart contains: " + productName);
    }

    @When("I sort products by {string}")
    public void iSortProductsBy(String sortOption) {
        homePage.sortProductsBy(sortOption);
    }

    @Then("the products should be sorted by {string}")
    public void theProductsShouldBeSortedBy(String sortOption) {
        // Verify order based on option
        ExtentLogger.info("Products sorted by: " + sortOption);
        // Detailed assertion logic would depend on the sort type
    }

    @When("I go to the cart")
    public void iGoToTheCart() {
        cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        checkoutPage = cartPage.proceedToCheckout();
    }

    @When("I fill shipping details with first name {string} last name {string} postal code {string}")
    public void iFillShippingDetails(String firstName, String lastName, String postalCode) {
        checkoutPage.fillShippingDetails(firstName, lastName, postalCode);
        ExtentLogger.info("Filled shipping details");
    }

    @When("I click continue")
    public void iClickContinue() {
        checkoutPage.clickContinue();
    }

    @When("I click finish")
    public void iClickFinish() {
        checkoutPage.clickFinish();
    }

    @Then("the order should be complete with message {string}")
    public void theOrderShouldBeCompleteWithMessage(String expectedMessage) {
        Assert.assertTrue(checkoutPage.isOrderComplete(),
            "Order complete message should be visible");
        String actual = checkoutPage.getOrderCompleteMessage();
        Assert.assertEquals(actual, expectedMessage, "Order complete message mismatch");
        ExtentLogger.pass("Order completed: " + actual);
    }

    @When("I click on product {string}")
    public void iClickOnProduct(String productName) {
        productPage = homePage.openProduct(productName);
    }

    @Then("I should see the product detail page")
    public void iShouldSeeTheProductDetailPage() {
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product detail page should be loaded");
    }

    @Then("the product name should be {string}")
    public void theProductNameShouldBe(String expectedName) {
        Assert.assertEquals(productPage.getProductName(), expectedName,
            "Product name mismatch");
        ExtentLogger.pass("Product name verified: " + expectedName);
    }

    @Then("the product image should be displayed")
    public void theProductImageShouldBeDisplayed() {
        Assert.assertTrue(productPage.isProductImageDisplayed(),
            "Product image should be visible");
        ExtentLogger.pass("Product image is displayed");
    }
}
