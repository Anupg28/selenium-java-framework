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
     * served inside an iframe, with a document-level click interceptor that swallows clicks on
     * background content entirely - a plain JS-dispatched click doesn't bypass it like it does
     * an ordinary ad iframe simply overlapping an element. Its "Close"/"Next"/"Done" controls
     * live *inside* that iframe's document, which querySelectorAll from the parent page can
     * never see, so the fix is removing the overlay's wrapper directly from the parent DOM -
     * that part is always reachable even cross-origin.
     * <p>
     * The page also serves ordinary in-flow Google ad banners on every load, which must be left
     * alone. The distinguishing signal is CSS: a true full-page overlay is always
     * {@code position: fixed} so it stays put regardless of scroll; an in-flow banner is not.
     * Only remove an ancestor when that specific signal is found nearby - never a blind
     * ancestor-walk-and-remove, which risks tearing out shared layout containers.
     */
    private void dismissSurveyOverlayIfPresent() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('iframe').forEach(function(f) {"
                            + "  var src = (f.getAttribute('src') || '').toLowerCase();"
                            + "  if (src.indexOf('google') === -1) { return; }"
                            + "  var node = f;"
                            + "  var overlayRoot = null;"
                            + "  for (var i = 0; i < 6 && node && node !== document.body; i++) {"
                            + "    if (window.getComputedStyle(node).position === 'fixed') { overlayRoot = node; break; }"
                            + "    node = node.parentElement;"
                            + "  }"
                            + "  if (overlayRoot) { try { overlayRoot.remove(); } catch (e) {} }"
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
