package com.framework.factory;

import com.framework.config.ConfigReader;
import com.framework.enums.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.List;

/**
 * Factory class responsible for creating WebDriver instances.
 * Supports Chrome, Firefox, Edge, and Safari.
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static WebDriver createDriver(String browserName) {
        Browser browser = Browser.fromString(browserName);
        boolean headless = ConfigReader.getInstance().isHeadless();
        log.info("Creating {} driver | headless={}", browser, headless);

        return switch (browser) {
            case CHROME  -> createChromeDriver(headless);
            case FIREFOX -> createFirefoxDriver(headless);
            case EDGE    -> createEdgeDriver(headless);
            case SAFARI  -> createSafariDriver();
        };
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--disable-extensions",
            "--remote-allow-origins=*",
            "--disable-infobars",
            "--window-size=1920,1080"
        );
        if (headless) options.addArguments("--headless=new");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation", "enable-logging"));
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        if (headless) options.addArguments("--headless");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        if (headless) options.addArguments("--headless=new");
        return new EdgeDriver(options);
    }

    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }
}
