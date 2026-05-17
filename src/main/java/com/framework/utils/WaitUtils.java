package com.framework.utils;

import com.framework.constants.Constants;
import com.framework.manager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.function.Function;

/**
 * Centralized wait utility covering implicit, explicit, and fluent waits.
 */
public final class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);

    private WaitUtils() {}

    // ─── Explicit Wait ───────────────────────────────────────────────────────

    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
    }

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, Constants.EXPLICIT_WAIT);
    }

    public static WebElement waitForVisibility(By locator, int seconds) {
        log.debug("Waiting {}s for visibility of: {}", seconds, locator);
        return getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebElement element) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementToBeClickable(By locator) {
        return waitForElementToBeClickable(locator, Constants.EXPLICIT_WAIT);
    }

    public static WebElement waitForElementToBeClickable(By locator, int seconds) {
        log.debug("Waiting {}s for clickability of: {}", seconds, locator);
        return getWait(seconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementToBeClickable(WebElement element) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForPresence(By locator) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForInvisibility(WebElement element) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.invisibilityOf(element));
    }

    public static boolean waitForTextToBePresentInElement(By locator, String text) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForUrlToContain(String urlFragment) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.urlContains(urlFragment));
    }

    public static boolean waitForTitleContains(String title) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.titleContains(title));
    }

    public static boolean waitForAttributeToContain(By locator, String attribute, String value) {
        return getWait(Constants.EXPLICIT_WAIT).until(
            ExpectedConditions.attributeContains(locator, attribute, value));
    }

    public static void waitForAlertPresence() {
        getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.alertIsPresent());
    }

    public static boolean waitForNumberOfWindowsToBe(int count) {
        return getWait(Constants.EXPLICIT_WAIT).until(ExpectedConditions.numberOfWindowsToBe(count));
    }

    // ─── Fluent Wait ─────────────────────────────────────────────────────────

    public static WebElement fluentWait(By locator) {
        Wait<org.openqa.selenium.WebDriver> wait = new FluentWait<>(DriverManager.getDriver())
            .withTimeout(Duration.ofSeconds(Constants.FLUENT_WAIT_TIMEOUT))
            .pollingEvery(Duration.ofMillis(Constants.FLUENT_WAIT_POLLING))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .withMessage("Element not found after fluent wait: " + locator);

        log.debug("Fluent wait for: {}", locator);
        return wait.until(driver -> driver.findElement(locator));
    }

    public static <T> T fluentWait(Function<org.openqa.selenium.WebDriver, T> condition, int timeoutSeconds, String message) {
        Wait<org.openqa.selenium.WebDriver> wait = new FluentWait<>(DriverManager.getDriver())
            .withTimeout(Duration.ofSeconds(timeoutSeconds))
            .pollingEvery(Duration.ofMillis(500))
            .ignoring(Exception.class)
            .withMessage(message);
        return wait.until(condition);
    }

    // ─── Thread Sleep (use sparingly) ────────────────────────────────────────

    public static void hardWait(long millis) {
        try {
            log.warn("Hard wait of {}ms — prefer explicit waits", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
