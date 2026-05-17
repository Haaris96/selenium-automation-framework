package com.framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.reports.ExtentReport;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Secondary listener specifically for advanced ExtentReport integration.
 * Attaches test metadata: author, category, device info.
 */
public class ExtentReportListener extends TestListenerAdapter {

    private static final Logger log = LogManager.getLogger(ExtentReportListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReport.getTest();
        // Assign category based on test group
        if (result.getMethod().getGroups().length > 0) {
            for (String group : result.getMethod().getGroups()) {
                test.assignCategory(group);
            }
        }
        // Assign author from method annotation if present
        test.assignAuthor("QA Team");
        test.assignDevice(System.getProperty("os.name"));

        log.debug("ExtentReportListener: test started → {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.debug("ExtentReportListener: test passed → {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String base64 = ScreenshotUtils.captureScreenshotAsBase64();
            ExtentReport.getTest()
                .log(Status.FAIL,
                     "Failure snapshot",
                     com.aventstack.extentreports.MediaEntityBuilder
                         .createScreenCaptureFromBase64String(base64).build());
        } catch (Exception e) {
            log.error("Could not attach failure screenshot: {}", e.getMessage());
        }
        ExtentReport.getTest().fail(result.getThrowable());
        log.error("ExtentReportListener: test failed → {}", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("ExtentReportListener: test skipped → {}", result.getName());
    }
}
