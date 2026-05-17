package com.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Manages the ExtentReports singleton and per-thread ExtentTest instances
 * to support parallel test execution safely.
 */
public final class ExtentReport {

    private static final Logger log = LogManager.getLogger(ExtentReport.class);

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ExtentReport() {}

    public static synchronized void initReports() {
        if (extentReports != null) return;

        new File(Constants.REPORTS_PATH).mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter(Constants.EXTENT_REPORT_PATH);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle(Constants.REPORT_TITLE);
        spark.config().setReportName(Constants.REPORT_NAME);
        spark.config().setEncoding("UTF-8");
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);
        extentReports.setSystemInfo("OS",          Constants.SYSTEM_INFO_OS);
        extentReports.setSystemInfo("Java Version", Constants.SYSTEM_INFO_JAVA);
        extentReports.setSystemInfo("Framework",   "Selenium + TestNG + Cucumber");
        extentReports.setSystemInfo("Author",      "QA Team");

        log.info("ExtentReports initialized → {}", Constants.EXTENT_REPORT_PATH);
    }

    public static ExtentTest createTest(String testName) {
        return createTest(testName, "");
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extentReports.createTest(testName, description);
        testThreadLocal.set(test);
        log.debug("ExtentTest created: {}", testName);
        return test;
    }

    public static ExtentTest getTest() {
        ExtentTest test = testThreadLocal.get();
        if (test == null) {
            log.warn("ExtentTest not initialized for thread {}; creating default",
                Thread.currentThread().getName());
            return extentReports.createTest("Uninitialized Test");
        }
        return test;
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            testThreadLocal.remove();
            log.info("ExtentReports flushed → {}", Constants.EXTENT_REPORT_PATH);
        }
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }
}
