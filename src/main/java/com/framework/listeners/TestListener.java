package com.framework.listeners;

import com.framework.reports.ExtentLogger;
import com.framework.reports.ExtentReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

/**
 * TestNG ITestListener — bridges test lifecycle events to ExtentReports and Log4j.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    // ─── Suite ──────────────────────────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        log.info("╔══════════════════════════════════════╗");
        log.info("  SUITE STARTED: {}", suite.getName());
        log.info("╚══════════════════════════════════════╝");
        ExtentReport.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("╔══════════════════════════════════════╗");
        log.info("  SUITE FINISHED: {}", suite.getName());
        log.info("╚══════════════════════════════════════╝");
        ExtentReport.flushReports();
    }

    // ─── Test ───────────────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        String testName = getTestName(result);
        String desc     = result.getMethod().getDescription();
        log.info("▶ TEST STARTED: {}", testName);
        ExtentReport.createTest(testName, desc.isEmpty() ? testName : desc);
        ExtentLogger.info("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✔ TEST PASSED: {}", getTestName(result));
        ExtentLogger.passWithScreenshot("Test PASSED: " + getTestName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        log.error("✘ TEST FAILED: {} | Reason: {}", testName, result.getThrowable().getMessage());
        ExtentLogger.failWithScreenshot("Test FAILED: " + testName);
        ExtentReport.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⚠ TEST SKIPPED: {}", getTestName(result));
        ExtentLogger.skip("Test SKIPPED: " + getTestName(result));
        if (result.getThrowable() != null) {
            ExtentReport.getTest().skip(result.getThrowable());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("TEST FAILED WITHIN SUCCESS %: {}", getTestName(result));
    }

    // ─── Class ──────────────────────────────────────────────────────────────

    @Override
    public void onStart(ITestContext context) {
        log.info("── Test class started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed  = context.getPassedTests().size();
        int failed  = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        log.info("── Test class finished: {} | Passed: {} | Failed: {} | Skipped: {}",
            context.getName(), passed, failed, skipped);
    }

    // ─── Helper ─────────────────────────────────────────────────────────────

    private String getTestName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + " :: " + result.getName();
    }
}
