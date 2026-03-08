package com.trinka.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ParallelLoginTest {

    private static final String HUB_URL = "http://localhost:4445/wd/hub";
    private static final String PASSWORD = "Deven@21";

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new RemoteWebDriver(new URL(HUB_URL), options);
        driverThread.set(driver);
    }

    @DataProvider(name = "users", parallel = true)
    public Object[][] userData() {
        Object[][] data = new Object[10][2];
        for (int i = 1; i <= 10; i++) {
            data[i - 1][0] = "User-" + i;
            data[i - 1][1] = "trinka_parallel_execution_user" + i + "@yopmail.com";
        }
        return data;
    }

    @Test(dataProvider = "users")
    public void loginTest(String testName, String email) {
        WebDriver driver = driverThread.get();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        long threadId = Thread.currentThread().getId();

        // Navigate to sign-in page
        String url = "https://trinkauser:Trinka%400811@uat.cloud.trinka.ai/signin";
        System.out.println("[" + testName + "] Thread " + threadId + " - Navigating to sign-in page");
        driver.get(url);

        // Enter email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
        System.out.println("[" + testName + "] Thread " + threadId + " - Email: " + email);

        // Enter password
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        System.out.println("[" + testName + "] Thread " + threadId + " - Password entered");

        // Click submit
        driver.findElement(By.xpath("//button[@class='submit-btn']")).click();
        System.out.println("[" + testName + "] Thread " + threadId + " - Submit clicked");

        // Wait for left panel to be visible (15 sec)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='leftPanelSection']")));
        System.out.println("[" + testName + "] Thread " + threadId + " - Left panel visible - LOGIN SUCCESS");
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}
