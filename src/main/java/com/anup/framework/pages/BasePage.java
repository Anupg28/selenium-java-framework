package com.anup.framework.pages;

import com.anup.framework.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * automationexercise.com occasionally injects a full-page "Powered by Google" survey
     * overlay with a document-level click interceptor that swallows clicks on background
     * content entirely - a plain JS-dispatched click doesn't bypass it like it does an ad
     * iframe simply overlapping an element. Best-effort dismiss before every click: click any
     * visible leaf element whose text is exactly "Close", then strip any residual ultra-high
     * z-index overlay as a fallback. No-op (and cheap) on the vast majority of clicks where no
     * such overlay is present.
     */
    private void dismissSurveyOverlayIfPresent() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var closers = Array.from(document.querySelectorAll('body *')).filter(function(el) {"
                            + "  return el.children.length === 0 && (el.textContent || '').trim() === 'Close' && el.offsetParent !== null;"
                            + "});"
                            + "closers.forEach(function(el) { try { el.click(); } catch (e) {} });"
                            + "document.querySelectorAll('div, iframe').forEach(function(el) {"
                            + "  var z = parseInt(window.getComputedStyle(el).zIndex, 10);"
                            + "  if (!isNaN(z) && z >= 999999) { try { el.remove(); } catch (e) {} }"
                            + "});"
            );
        } catch (Exception ignored) {
        }
    }

    /**
     * automationexercise.com serves real, dynamically-positioned ad iframes that can
     * momentarily overlap a target element and intercept a native click. Falling back to a
     * JS-dispatched click keeps the suite reliable without weakening what's actually verified
     * (element still has to exist, be visible, and be "clickable" per the explicit wait).
     */
    protected void click(WebElement element) {
        dismissSurveyOverlayIfPresent();
        WebElement target = waitForClickable(element);
        try {
            target.click();
        } catch (ElementClickInterceptedException e) {
            dismissSurveyOverlayIfPresent();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        }
    }

    protected void type(WebElement element, String text) {
        WebElement el = waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return waitForVisible(element).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Use for state that only appears after a navigation/redirect (e.g. a logged-in header).
     * An instant isDisplayed() check races the page load; this polls up to the explicit wait
     * timeout and only returns false once it's actually given up, not on the first frame checked.
     */
    protected boolean waitUntilDisplayed(WebElement element) {
        try {
            return wait.until(driver -> {
                dismissSurveyOverlayIfPresent();
                try {
                    return element.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public void open(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
