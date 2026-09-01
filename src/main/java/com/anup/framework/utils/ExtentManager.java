package com.anup.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public final class ExtentManager {

    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = "test-output/ExtentReport/Report_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Selenium Java Framework - Test Report");
            sparkReporter.config().setReportName("E-Commerce Automation Suite");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Author", "Anup Ghodake");
            extentReports.setSystemInfo("Framework", "Selenium + Java + TestNG");
            extentReports.setSystemInfo("Application Under Test", "automationexercise.com");
        }
        return extentReports;
    }
}
