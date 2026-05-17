package com.framework.hooks;

import com.framework.config.ConfigReader;
import com.framework.factory.DriverFactory;
import com.framework.manager.DriverManager;
import com.framework.reports.ExtentLogger;
import com.framework.reports.ExtentReport;
import com.framework.utils.ScreenshotUtils;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Cucumber Hooks — control WebDriver lifecycle and ExtentReport steps
 * around each scenario.
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    // ─── Suite-level ──────────────────────────────────────────────────────

    @BeforeAll
    public static void beforeAll() {
        log.info("╔═══════════════════════════════════════════╗");
        log.info("         CUCUMBER SUITE STARTING             ");
        log.info("╚═══════════════════════════════════════════╝");
        ExtentReport.initReports();
    }

    @AfterAll
    public static void afterAll() {
        ExtentReport.flushReports();
        log.info("╔═══════════════════════════════════════════╗");
        log.info("         CUCUMBER SUITE COMPLETED            ");
        log.info("╚═══════════════════════════════════════════╝");
    }

    // ─── Scenario-level ───────────────────────────────────────────────────

    @Before
    public void beforeScenario(Scenario scenario) {
        String name = scenario.getName();
        log.info("▶ SCENARIO: {} [Tags: {}]", name, scenario.getSourceTagNames());

        // Create ExtentTest for this scenario
        ExtentReport.createTest(name, "Tags: " + scenario.getSourceTagNames());
        ExtentLogger.info("Scenario started: " + name);

        // Launch browser
        String browser = System.getProperty("browser",
            ConfigReader.getInstance().getBrowser());
        DriverManager.setDriver(DriverFactory.createDriver(browser));
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(ConfigReader.getInstance().getImplicitWait()))
            .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInstance().getPageLoadTimeout()));

        // Open base URL
        String url = ConfigReader.getInstance().getBaseUrl();
        DriverManager.getDriver().get(url);
        log.info("Browser launched | URL: {} | Thread: {}", url, Thread.currentThread().getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        String name   = scenario.getName();
        boolean failed = scenario.isFailed();

        if (failed) {
            log.error("✘ SCENARIO FAILED: {}", name);
            // Attach screenshot to Cucumber HTML report
            byte[] screenshot = ScreenshotUtils.captureScreenshotAsBase64().getBytes();
            scenario.attach(screenshot, "image/png", "Failure Screenshot - " + name);

            // Attach to ExtentReport
            ExtentLogger.failWithScreenshot("Scenario FAILED: " + name);
        } else {
            log.info("✔ SCENARIO PASSED: {}", name);
            ExtentLogger.pass("Scenario PASSED: " + name);
        }

        // Quit driver
        DriverManager.quitDriver();
    }

    // ─── Tagged hooks ─────────────────────────────────────────────────────

    @Before("@db")
    public void setupDatabase() {
        log.info("[Hook] DB setup for @db tagged scenario");
        // Insert test data or set up DB state here
    }

    @After("@db")
    public void teardownDatabase() {
        log.info("[Hook] DB teardown for @db tagged scenario");
        // Clean up test data
    }

    @Before("@smoke")
    public void smokeSetup(Scenario scenario) {
        log.info("[Hook] Smoke test setup for: {}", scenario.getName());
        ExtentLogger.info("[Smoke] Starting smoke scenario");
    }
}
