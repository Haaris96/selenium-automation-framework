package com.framework.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * Thread-local WebDriver manager for safe parallel execution.
 * Each thread gets its own WebDriver instance.
 */
public final class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
        log.debug("WebDriver set for thread: {}", Thread.currentThread().getName());
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized for thread: " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("WebDriver quit for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                log.error("Error quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }

    public static void configureTimeouts(int implicitWait, int pageLoad, int script) {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(script));
    }
}
