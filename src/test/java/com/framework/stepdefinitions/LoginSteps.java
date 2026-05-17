package com.framework.stepdefinitions;

import com.framework.config.ConfigReader;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.reports.ExtentLogger;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Step definitions for login.feature.
 * Each step maps directly to a LoginPage or HomePage action.
 */
public class LoginSteps {

    private LoginPage loginPage;
    private HomePage  homePage;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be displayed");
        ExtentLogger.info("Verified: Login page is displayed");
    }

    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("I click the login button")
    public void iClickTheLoginButton() {
        loginPage.clickLoginButtonExpectingFailure();
        // homePage is set lazily in @Then steps that need it
    }

    @Then("I should be on the home page")
    public void iShouldBeOnTheHomePage() {
        homePage = new HomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(),
            "Should be on the home page after login");
        ExtentLogger.pass("Successfully landed on Home page");
    }

    @Then("the page heading should be {string}")
    public void thePageHeadingShouldBe(String expectedHeading) {
        String actual = homePage.getPageHeading();
        Assert.assertEquals(actual, expectedHeading,
            "Page heading mismatch");
        ExtentLogger.pass("Page heading verified: " + actual);
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedError) {
        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error message should be visible");
        String actual = loginPage.getErrorMessage();
        Assert.assertEquals(actual, expectedError,
            "Error message mismatch");
        ExtentLogger.pass("Error message verified: " + actual);
    }
}
