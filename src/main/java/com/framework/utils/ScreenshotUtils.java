package com.framework.utils;

import com.framework.constants.Constants;
import com.framework.manager.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * Captures screenshots and returns the file path or Base64 string for Extent Reports.
 */
public final class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {}

    public static String captureScreenshot(String testName) {
        try {
            String timestamp = new SimpleDateFormat(Constants.SCREENSHOT_DATE_FORMAT).format(new Date());
            String fileName  = testName + "_" + timestamp + ".png";
            String filePath  = Constants.SCREENSHOTS_PATH + fileName;

            File src  = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);
            log.info("Screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            log.error("Screenshot capture failed", e);
            return "";
        }
    }

    public static String captureScreenshotAsBase64() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Base64 screenshot failed", e);
            return "";
        }
    }

    public static String captureElementScreenshot(WebElement element, String name) {
        try {
            String timestamp = new SimpleDateFormat(Constants.SCREENSHOT_DATE_FORMAT).format(new Date());
            String filePath  = Constants.SCREENSHOTS_PATH + name + "_" + timestamp + ".png";
            File src  = element.getScreenshotAs(OutputType.FILE);
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);
            log.info("Element screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            log.error("Element screenshot failed", e);
            return "";
        }
    }
}
