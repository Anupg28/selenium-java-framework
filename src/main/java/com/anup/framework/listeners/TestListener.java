package com.anup.framework.listeners;

import com.anup.framework.base.DriverManager;
import com.anup.framework.utils.ExtentManager;
import com.anup.framework.utils.ExtentTestManager;
import com.anup.framework.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        ExtentTestManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.FAIL, result.getThrowable());

        try {
            String screenshotPath = ScreenshotUtil.capture(
                    DriverManager.getDriver(), result.getMethod().getMethodName());
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
    }
}
