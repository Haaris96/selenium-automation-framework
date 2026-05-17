package com.framework.base;

import com.framework.enums.WaitStrategy;
import com.framework.manager.DriverManager;
import com.framework.reports.ExtentLogger;
import com.framework.utils.JavaScriptUtils;
import com.framework.utils.ScreenshotUtils;
import com.framework.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for all Page Objects.
 * Initializes PageFactory and exposes all reusable Selenium interactions inline
 * (click, type, dropdown, window, frame, alert, scroll, waits, JS, screenshot).
 */
public abstract class BasePage {

    protected static final Logger log = LogManager.getLogger(BasePage.class);

    protected BasePage() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Navigation
    // ══════════════════════════════════════════════════════════════════════════

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        ExtentLogger.info("Navigated to: " + url);
        DriverManager.getDriver().get(url);
    }

    protected String getCurrentUrl() { return DriverManager.getDriver().getCurrentUrl(); }
    protected String getTitle()      { return DriverManager.getDriver().getTitle(); }

    protected void navigateBack()    { DriverManager.getDriver().navigate().back(); }
    protected void navigateForward() { DriverManager.getDriver().navigate().forward(); }
    protected void refreshPage()     { DriverManager.getDriver().navigate().refresh(); }

    // ══════════════════════════════════════════════════════════════════════════
    //  Click
    // ══════════════════════════════════════════════════════════════════════════

    protected void click(By locator, WaitStrategy strategy, String elementName) {
        log.info("Clicking: {}", elementName);
        ExtentLogger.info("Click on: " + elementName);
        resolveElement(locator, strategy).click();
    }

    protected void click(WebElement element, String elementName) {
        log.info("Clicking: {}", elementName);
        ExtentLogger.info("Click on: " + elementName);
        element.click();
    }

    protected void jsClick(WebElement element, String elementName) {
        log.info("JS Click: {}", elementName);
        JavaScriptUtils.click(element);
    }

    protected void doubleClick(WebElement element) {
        new Actions(DriverManager.getDriver()).doubleClick(element).perform();
        log.debug("Double-clicked element");
    }

    protected void rightClick(WebElement element) {
        new Actions(DriverManager.getDriver()).contextClick(element).perform();
        log.debug("Right-clicked element");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Type / Keyboard
    // ══════════════════════════════════════════════════════════════════════════

    protected void type(By locator, String text, WaitStrategy strategy, String elementName) {
        WebElement el = resolveElement(locator, strategy);
        el.clear();
        el.sendKeys(text);
        log.info("Typed '{}' into: {}", text, elementName);
        ExtentLogger.info("Typed '" + text + "' into: " + elementName);
    }

    protected void type(WebElement element, String text, String elementName) {
        element.clear();
        element.sendKeys(text);
        log.info("Typed '{}' into: {}", text, elementName);
    }

    protected void clearAndType(WebElement element, String text) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    protected void pressKey(WebElement element, Keys key) {
        element.sendKeys(key);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Read Element Data
    // ══════════════════════════════════════════════════════════════════════════

    protected String getText(By locator, WaitStrategy strategy, String elementName) {
        String text = resolveElement(locator, strategy).getText();
        log.debug("Text from '{}': '{}'", elementName, text);
        return text;
    }

    protected String getText(WebElement element) { return element.getText(); }

    protected String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    protected String getCssValue(WebElement element, String property) {
        return element.getCssValue(property);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  State Checks
    // ══════════════════════════════════════════════════════════════════════════

    protected boolean isDisplayed(By locator) {
        try { return DriverManager.getDriver().findElement(locator).isDisplayed(); }
        catch (NoSuchElementException | StaleElementReferenceException e) { return false; }
    }

    protected boolean isDisplayed(WebElement element) {
        try { return element.isDisplayed(); }
        catch (Exception e) { return false; }
    }

    protected boolean isEnabled(WebElement element) {
        try { return element.isEnabled(); }
        catch (Exception e) { return false; }
    }

    protected boolean isSelected(WebElement element) {
        try { return element.isSelected(); }
        catch (Exception e) { return false; }
    }

    protected List<WebElement> getElements(By locator) {
        return DriverManager.getDriver().findElements(locator);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Dropdown (Select element)
    // ══════════════════════════════════════════════════════════════════════════

    protected void selectByVisibleText(WebElement element, String text) {
        log.info("Select by text: '{}'", text);
        new Select(element).selectByVisibleText(text);
    }

    protected void selectByValue(WebElement element, String value) {
        log.info("Select by value: '{}'", value);
        new Select(element).selectByValue(value);
    }

    protected void selectByIndex(WebElement element, int index) {
        log.info("Select by index: {}", index);
        new Select(element).selectByIndex(index);
    }

    protected String getSelectedOption(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText();
    }

    protected List<String> getAllDropdownOptions(WebElement element) {
        return new Select(element).getOptions()
            .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    protected void selectByVisibleText(By locator, String text) {
        selectByVisibleText(DriverManager.getDriver().findElement(locator), text);
    }

    protected void selectByValue(By locator, String value) {
        selectByValue(DriverManager.getDriver().findElement(locator), value);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Window Handling
    // ══════════════════════════════════════════════════════════════════════════

    protected String getMainWindowHandle() {
        return DriverManager.getDriver().getWindowHandle();
    }

    protected void switchToWindowByTitle(String title) {
        Set<String> handles = DriverManager.getDriver().getWindowHandles();
        for (String handle : handles) {
            DriverManager.getDriver().switchTo().window(handle);
            if (DriverManager.getDriver().getTitle().equals(title)) {
                log.info("Switched to window: '{}'", title);
                return;
            }
        }
        throw new NoSuchWindowException("Window with title '" + title + "' not found");
    }

    protected void switchToNewWindow() {
        String main = DriverManager.getDriver().getWindowHandle();
        WaitUtils.waitForNumberOfWindowsToBe(2);
        for (String handle : DriverManager.getDriver().getWindowHandles()) {
            if (!handle.equals(main)) {
                DriverManager.getDriver().switchTo().window(handle);
                log.info("Switched to new window: {}", handle);
                return;
            }
        }
    }

    protected void switchToWindowByIndex(int index) {
        List<String> handles = new ArrayList<>(DriverManager.getDriver().getWindowHandles());
        DriverManager.getDriver().switchTo().window(handles.get(index));
        log.info("Switched to window index: {}", index);
    }

    protected void closeCurrentWindowAndSwitchToMain(String mainHandle) {
        DriverManager.getDriver().close();
        DriverManager.getDriver().switchTo().window(mainHandle);
        log.info("Closed window and switched back to main");
    }

    protected int getWindowCount() {
        return DriverManager.getDriver().getWindowHandles().size();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Frame Handling
    // ══════════════════════════════════════════════════════════════════════════

    protected void switchToFrame(By frameLocator) {
        WebElement frame = DriverManager.getDriver().findElement(frameLocator);
        DriverManager.getDriver().switchTo().frame(frame);
        log.info("Switched to frame: {}", frameLocator);
    }

    protected void switchToFrame(int index) {
        DriverManager.getDriver().switchTo().frame(index);
        log.info("Switched to frame index: {}", index);
    }

    protected void switchToFrame(String nameOrId) {
        DriverManager.getDriver().switchTo().frame(nameOrId);
        log.info("Switched to frame: '{}'", nameOrId);
    }

    protected void switchToNestedFrame(By parentFrame, By childFrame) {
        switchToFrame(parentFrame);
        switchToFrame(childFrame);
    }

    protected void switchToDefaultContent() {
        DriverManager.getDriver().switchTo().defaultContent();
        log.debug("Switched to default content");
    }

    protected void switchToParentFrame() {
        DriverManager.getDriver().switchTo().parentFrame();
        log.debug("Switched to parent frame");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Alert Handling
    // ══════════════════════════════════════════════════════════════════════════

    protected String acceptAlert() {
        WaitUtils.waitForAlertPresence();
        Alert alert = DriverManager.getDriver().switchTo().alert();
        String text = alert.getText();
        log.info("Accepting alert: '{}'", text);
        alert.accept();
        return text;
    }

    protected String dismissAlert() {
        WaitUtils.waitForAlertPresence();
        Alert alert = DriverManager.getDriver().switchTo().alert();
        String text = alert.getText();
        log.info("Dismissing alert: '{}'", text);
        alert.dismiss();
        return text;
    }

    protected String getAlertText() {
        WaitUtils.waitForAlertPresence();
        return DriverManager.getDriver().switchTo().alert().getText();
    }

    protected void typeInAlert(String text) {
        WaitUtils.waitForAlertPresence();
        DriverManager.getDriver().switchTo().alert().sendKeys(text);
        log.info("Typed '{}' in alert", text);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Actions / Mouse
    // ══════════════════════════════════════════════════════════════════════════

    protected void hoverOver(WebElement element) {
        new Actions(DriverManager.getDriver()).moveToElement(element).perform();
        log.debug("Hovered over element");
    }

    protected void hoverAndClick(WebElement hover, WebElement click) {
        new Actions(DriverManager.getDriver())
            .moveToElement(hover)
            .click(click)
            .perform();
    }

    protected void dragAndDrop(WebElement source, WebElement target) {
        new Actions(DriverManager.getDriver()).dragAndDrop(source, target).perform();
        log.info("Drag and drop performed");
    }

    protected void dragAndDropByOffset(WebElement element, int x, int y) {
        new Actions(DriverManager.getDriver()).dragAndDropBy(element, x, y).perform();
    }

    protected void clickAndHold(WebElement element) {
        new Actions(DriverManager.getDriver()).clickAndHold(element).perform();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Scroll
    // ══════════════════════════════════════════════════════════════════════════

    protected void scrollIntoView(WebElement element) {
        JavaScriptUtils.scrollIntoView(element);
    }

    protected void scrollToBottom() {
        JavaScriptUtils.scrollToBottom();
    }

    protected void scrollToTop() {
        JavaScriptUtils.scrollToTop();
    }

    protected void scrollByPixels(int x, int y) {
        JavaScriptUtils.scrollByPixels(x, y);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  JavaScript
    // ══════════════════════════════════════════════════════════════════════════

    protected Object executeScript(String script, Object... args) {
        return JavaScriptUtils.executeScript(script, args);
    }

    protected void highlightElement(WebElement element) {
        JavaScriptUtils.highlight(element);
    }

    protected String getInnerText(WebElement element) {
        return (String) JavaScriptUtils.executeScript("return arguments[0].innerText;", element);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Screenshot
    // ══════════════════════════════════════════════════════════════════════════

    protected String captureScreenshot(String name) {
        return ScreenshotUtils.captureScreenshot(name);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Internal resolver
    // ══════════════════════════════════════════════════════════════════════════

    private WebElement resolveElement(By locator, WaitStrategy strategy) {
        return switch (strategy) {
            case CLICKABLE -> WaitUtils.waitForElementToBeClickable(locator);
            case VISIBLE   -> WaitUtils.waitForVisibility(locator);
            case PRESENCE  -> WaitUtils.waitForPresence(locator);
            case NONE      -> DriverManager.getDriver().findElement(locator);
        };
    }
}
