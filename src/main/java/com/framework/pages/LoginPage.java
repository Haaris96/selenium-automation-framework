package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.enums.WaitStrategy;
import com.framework.reports.ExtentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the SauceDemo Login page.
 * Uses PageFactory @FindBy annotations (initialized in BasePage constructor).
 */
public class LoginPage extends BasePage {

    // ─── Locators via PageFactory ─────────────────────────────────────────

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".login_logo")
    private WebElement loginLogo;

    // By locator examples for dynamic waits
    private static final By ERROR_MSG_LOCATOR   = By.cssSelector("[data-test='error']");
    private static final By LOGIN_BTN_LOCATOR   = By.id("login-button");
    private static final By USERNAME_LOCATOR    = By.id("user-name");

    // ─── Page Actions ─────────────────────────────────────────────────────

    public LoginPage enterUsername(String username) {
        type(usernameField, username, "Username field");
        ExtentLogger.info("Entered username: " + username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password, "Password field");
        ExtentLogger.info("Entered password");
        return this;
    }

    public HomePage clickLoginButton() {
        click(loginButton, "Login button");
        ExtentLogger.info("Clicked Login button");
        return new HomePage();
    }

    public LoginPage clickLoginButtonExpectingFailure() {
        click(loginButton, "Login button");
        return this;
    }

    public LoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        ExtentLogger.pass("Login performed with user: " + username);
        return this;
    }

    public HomePage loginSuccessfully(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    // ─── Assertions / State ───────────────────────────────────────────────

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginLogo);
    }

    public String getErrorMessage() {
        return getText(ERROR_MSG_LOCATOR, WaitStrategy.VISIBLE, "Error message");
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MSG_LOCATOR);
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton);
    }

    public String getUsernamePlaceholder() {
        return getAttribute(usernameField, "placeholder");
    }

    public String getPasswordPlaceholder() {
        return getAttribute(passwordField, "placeholder");
    }

    // ─── Clear fields ─────────────────────────────────────────────────────

    public LoginPage clearUsername() {
        usernameField.clear();
        return this;
    }

    public LoginPage clearPassword() {
        passwordField.clear();
        return this;
    }
}
