package com.framework.base;

import com.framework.config.ConfigReader;
import com.framework.factory.DriverFactory;
import com.framework.manager.DriverManager;
import com.framework.reports.ExtentReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base test class for TestNG tests.
 * Handles WebDriver lifecycle and ExtentReport setup.
 * All TestNG test classes extend this.
 */
@Listeners({
    com.framework.listeners.TestListener.class,
    com.framework.listeners.ExtentReportListener.class
})
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void initSuite() {
        log.info("====== TEST SUITE STARTED ======");
        ExtentReport.initReports();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        ExtentReport.flushReports();
        log.info("====== TEST SUITE COMPLETED ======");
    }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("qa")     String env) {

        String browserToUse = System.getProperty("browser", browser);
        log.info("Setting up test | browser={} | env={} | thread={}",
                browserToUse, env, Thread.currentThread().getName());

        WebDriver webDriver = DriverFactory.createDriver(browserToUse);
        DriverManager.setDriver(webDriver);
        DriverManager.configureTimeouts(
            ConfigReader.getInstance().getImplicitWait(),
            ConfigReader.getInstance().getPageLoadTimeout(),
            15
        );

        if (Boolean.parseBoolean(ConfigReader.getInstance().get("browser.maximize"))) {
            DriverManager.getDriver().manage().window().maximize();
        }

        driver = DriverManager.getDriver();
        driver.get(ConfigReader.getInstance().getBaseUrl());
        log.info("Browser opened and navigated to: {}", ConfigReader.getInstance().getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        log.info("Tearing down test: {} | Status: {}", result.getName(), getStatusName(result.getStatus()));
        DriverManager.quitDriver();
    }

    private String getStatusName(int status) {
        return switch (status) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP    -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
