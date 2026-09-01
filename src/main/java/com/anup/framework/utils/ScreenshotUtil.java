package com.anup.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Path screenshotDir = Path.of("test-output", "screenshots");
            Files.createDirectories(screenshotDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            Path destination = screenshotDir.resolve(fileName);

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination);

            return destination.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
