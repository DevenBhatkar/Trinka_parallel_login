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
import org.testng.annotations.Test;

public class ParallelLoginTest {

    private static final String HUB_URL = "http://localhost:4445/wd/hub";

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new RemoteWebDriver(new URL(HUB_URL), options);
        driverThread.set(driver);
    }

    @Test
    public void loginTest1() {
        performLogin("Test-1");
    }

    @Test
    public void loginTest2() {
        performLogin("Test-2");
    }

    @Test
    public void loginTest3() {
        performLogin("Test-3");
    }

    @Test
    public void loginTest4() {
        performLogin("Test-4");
    }

    @Test
    public void loginTest5() {
        performLogin("Test-5");
    }

    @Test
    public void loginTest6() {
        performLogin("Test-6");
    }

    @Test
    public void loginTest7() {
        performLogin("Test-7");
    }

    @Test
    public void loginTest8() {
        performLogin("Test-8");
    }

    @Test
    public void loginTest9() {
        performLogin("Test-9");
    }

    @Test
    public void loginTest10() {
        performLogin("Test-10");
    }

    private void performLogin(String testName) {
        WebDriver driver = driverThread.get();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        long threadId = Thread.currentThread().getId();

        // Navigate to sign-in page
        String url = "https://trinkauser:Trinka%400811@uat.cloud.trinka.ai/signin";
        System.out.println("[" + testName + "] Thread " + threadId + " - Navigating to sign-in page");
        driver.get(url);

        // Enter email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")))
                .sendKeys("premium_prod_automation@yopmail.com");
        System.out.println("[" + testName + "] Thread " + threadId + " - Email entered");

        // Enter password
        driver.findElement(By.id("password")).sendKeys("Deven@21");
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
