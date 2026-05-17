package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.config.ConfigReader;
import com.framework.dataprovider.ExcelDataProvider;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.reports.ExtentLogger;
import com.framework.reports.ExtentReport;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * TestNG test class for Login module.
 * Demonstrates DataProvider, groups, priority, and retry.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage();
    }

    // ─── Smoke / Positive ─────────────────────────────────────────────────

    @Test(
        groups      = {"smoke", "regression", "login"},
        priority    = 1,
        description = "Verify successful login with valid credentials"
    )
    public void testSuccessfulLogin() {
        ExtentReport.createTest("testSuccessfulLogin", "Verify login with valid credentials");
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();

        HomePage homePage = loginPage.loginSuccessfully(username, password);

        Assert.assertTrue(homePage.isHomePageLoaded(),
            "Home page should load after successful login");
        Assert.assertEquals(homePage.getPageHeading(), "Products",
            "Page heading should be 'Products'");

        ExtentLogger.passWithScreenshot("Login test passed — Home page loaded");
        log.info("testSuccessfulLogin: PASSED");
    }

    // ─── Negative ────────────────────────────────────────────────────────

    @Test(
        groups      = {"regression", "login", "negative"},
        priority    = 2,
        description = "Verify error shown for invalid credentials"
    )
    public void testInvalidCredentials() {
        ExtentReport.createTest("testInvalidCredentials", "Verify error message for invalid creds");

        loginPage.enterUsername("wrong_user")
                 .enterPassword("wrong_pass")
                 .clickLoginButtonExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be visible");
        String error = loginPage.getErrorMessage();
        Assert.assertTrue(
            error.contains("Username and password do not match"),
            "Error message should indicate credential mismatch. Actual: " + error
        );

        ExtentLogger.passWithScreenshot("Invalid credentials test passed");
    }

    @Test(
        groups      = {"regression", "login", "negative"},
        priority    = 3,
        description = "Verify error shown for locked-out user"
    )
    public void testLockedOutUser() {
        ExtentReport.createTest("testLockedOutUser", "Verify locked-out user error");

        loginPage.enterUsername("locked_out_user")
                 .enterPassword("secret_sauce")
                 .clickLoginButtonExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be visible");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("locked out"),
            "Error should indicate locked-out user"
        );
        ExtentLogger.passWithScreenshot("Locked-out user test passed");
    }

    @Test(
        groups      = {"regression", "login", "negative"},
        priority    = 4,
        description = "Verify error shown for empty username field"
    )
    public void testEmptyUsername() {
        ExtentReport.createTest("testEmptyUsername", "Verify error for empty username");

        loginPage.enterPassword("secret_sauce").clickLoginButtonExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
        ExtentLogger.passWithScreenshot("Empty username test passed");
    }

    @Test(
        groups      = {"regression", "login", "negative"},
        priority    = 5,
        description = "Verify error shown for empty password field"
    )
    public void testEmptyPassword() {
        ExtentReport.createTest("testEmptyPassword", "Verify error for empty password");

        loginPage.enterUsername("standard_user").clickLoginButtonExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
        ExtentLogger.passWithScreenshot("Empty password test passed");
    }

    // ─── Data-driven ─────────────────────────────────────────────────────

    @Test(
        dataProvider      = "loginData",
        dataProviderClass = ExcelDataProvider.class,
        groups            = {"regression", "login", "data-driven"},
        priority          = 6,
        description       = "Data-driven login test from Excel"
    )
    public void testLoginWithExcelData(Map<String, String> testData) {
        String username = testData.get("username");
        String password = testData.get("password");
        String expected = testData.get("expectedResult");

        ExtentReport.createTest("testLoginWithExcelData - " + username,
            "Data-driven test | user=" + username);

        if ("pass".equalsIgnoreCase(expected)) {
            HomePage home = loginPage.loginSuccessfully(username, password);
            Assert.assertTrue(home.isHomePageLoaded(), "Home page should load");
            ExtentLogger.passWithScreenshot("Excel data test PASSED for: " + username);
        } else {
            loginPage.enterUsername(username)
                     .enterPassword(password)
                     .clickLoginButtonExpectingFailure();
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown");
            ExtentLogger.passWithScreenshot("Excel data negative test PASSED for: " + username);
        }
    }
}
