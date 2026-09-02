package com.anup.framework.base;

import com.anup.framework.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        WebDriver webDriver;

        switch (browser) {
            case "chrome" -> webDriver = new ChromeDriver(buildChromeOptions());
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds")));
        webDriver.manage().window().maximize();
        DRIVER.set(webDriver);
    }

    /**
     * automationexercise.com is a real, ad-supported site: its ad network serves whatever
     * creative it wins that request - a survey modal one run, a fake "install this software"
     * notification the next. Dismissing each shape reactively is unwinnable since the shape
     * itself is never the same twice. The reliable fix is routing the ad-serving domains to a
     * black hole so no ad creative of any shape ever loads, rather than reacting to one after
     * it's already on the page.
     */
    private static final String AD_DOMAINS = String.join(",",
            "MAP doubleclick.net 127.0.0.1",
            "MAP *.doubleclick.net 127.0.0.1",
            "MAP googlesyndication.com 127.0.0.1",
            "MAP *.googlesyndication.com 127.0.0.1",
            "MAP googleadservices.com 127.0.0.1",
            "MAP *.googleadservices.com 127.0.0.1",
            "MAP adservice.google.com 127.0.0.1",
            "MAP *.adservice.google.com 127.0.0.1",
            "MAP googletagservices.com 127.0.0.1",
            "MAP *.googletagservices.com 127.0.0.1"
    );

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--host-resolver-rules=" + AD_DOMAINS);
        return options;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized. Call DriverManager.initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
