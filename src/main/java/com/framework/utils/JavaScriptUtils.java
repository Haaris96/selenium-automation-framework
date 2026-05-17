package com.framework.utils;

import com.framework.manager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * JavaScriptExecutor helper — all JS interactions go through here.
 */
public final class JavaScriptUtils {

    private static final Logger log = LogManager.getLogger(JavaScriptUtils.class);

    private JavaScriptUtils() {}

    private static JavascriptExecutor js() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }

    public static Object executeScript(String script, Object... args) {
        return js().executeScript(script, args);
    }

    public static void click(WebElement element) {
        js().executeScript("arguments[0].click();", element);
        log.debug("JS click executed");
    }

    public static void scrollIntoView(WebElement element) {
        js().executeScript("arguments[0].scrollIntoView({block:'center', behavior:'smooth'});", element);
    }

    public static void scrollToBottom() {
        js().executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollToTop() {
        js().executeScript("window.scrollTo(0, 0);");
    }

    public static void scrollByPixels(int x, int y) {
        js().executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public static void highlight(WebElement element) {
        String original = element.getCssValue("border");
        js().executeScript("arguments[0].style.border='3px solid red';", element);
        try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        js().executeScript("arguments[0].style.border='" + original + "';", element);
    }

    public static void setValue(WebElement element, String value) {
        js().executeScript("arguments[0].value='" + value + "';", element);
    }

    public static String getPageTitle() {
        return (String) js().executeScript("return document.title;");
    }

    public static boolean isPageReady() {
        return "complete".equals(js().executeScript("return document.readyState;"));
    }

    public static void openNewTab() {
        js().executeScript("window.open('about:blank', '_blank');");
    }
}
