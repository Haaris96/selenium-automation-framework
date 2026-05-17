package com.framework.reports;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Convenience facade for logging steps to ExtentReports.
 * All page/step classes use this instead of calling ExtentReport.getTest() directly.
 */
public final class ExtentLogger {

    private static final Logger log = LogManager.getLogger(ExtentLogger.class);

    private ExtentLogger() {}

    public static void info(String message) {
        ExtentReport.getTest().log(Status.INFO, message);
    }

    public static void pass(String message) {
        ExtentReport.getTest().log(Status.PASS, message);
    }

    public static void fail(String message) {
        ExtentReport.getTest().log(Status.FAIL, message);
    }

    public static void warning(String message) {
        ExtentReport.getTest().log(Status.WARNING, message);
    }

    public static void skip(String message) {
        ExtentReport.getTest().log(Status.SKIP, message);
    }

    public static void failWithScreenshot(String message) {
        try {
            String base64 = ScreenshotUtils.captureScreenshotAsBase64();
            ExtentReport.getTest().log(
                Status.FAIL,
                message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build()
            );
        } catch (Exception e) {
            log.error("Failed to attach screenshot to report", e);
            fail(message);
        }
    }

    public static void passWithScreenshot(String message) {
        try {
            String base64 = ScreenshotUtils.captureScreenshotAsBase64();
            ExtentReport.getTest().log(
                Status.PASS,
                message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build()
            );
        } catch (Exception e) {
            log.error("Failed to attach screenshot to report", e);
            pass(message);
        }
    }

    public static void addScreenshot(String message, String base64) {
        try {
            ExtentReport.getTest().log(
                Status.INFO,
                message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build()
            );
        } catch (Exception e) {
            log.error("Failed to attach screenshot: {}", e.getMessage());
        }
    }
}
