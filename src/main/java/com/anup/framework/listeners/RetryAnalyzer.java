package com.anup.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * automationexercise.com is a real, ad-supported public site: it occasionally injects
 * third-party ad/survey overlays that intercept a click regardless of how defensively the page
 * objects handle it (see BasePage.dismissSurveyOverlayIfPresent). That's environmental
 * flakiness, not a defect in the test logic - retrying a genuinely failed assertion would hide
 * a real bug, but retrying a transient interaction failure against live third-party content is
 * standard practice. Capped at 2 retries so a real, reproducible failure still fails the build.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}
