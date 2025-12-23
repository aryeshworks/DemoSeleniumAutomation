package com.ibm.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AutomatePracticeApp {
    static WebDriver driver;

    public static void setup() {
        driver = new FirefoxDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    public static void getApp(String url) {
        driver.get(url);
    }

    public static void verifyHomePage() {
        String actualTitle = driver.getTitle();
        String expectedTitle = "Practice | Practice Test Automation";
        System.out.println("Actual Title: " + actualTitle + "\nExpected Title: " + expectedTitle);
        assert actualTitle != null;
        if (actualTitle.equalsIgnoreCase(expectedTitle)) System.out.println("Page Title Correct");
        else System.out.println("Page Title Wrong");
    }

    public static void testLogin() {
        driver.findElement(By.linkText("Test Login Page")).click();
    }

    public static void verifyLogin(String username, String password, boolean isValid) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("submit")).click();

        if (isValid) verifyValidLogin();
        else verifyInvalidLogin();
    }

    public static void verifyValidLogin() {
        boolean isCorrectUrl = false, isCorrectText = false, isLogOutButtonPresent = false;
        String actualLoggedInUrl = driver.getCurrentUrl();
        String expectedLoggedInUrl = "https://practicetestautomation.com/logged-in-successfully/";
        assert actualLoggedInUrl != null;
        if (actualLoggedInUrl.equalsIgnoreCase(expectedLoggedInUrl)) isCorrectUrl = true;
        String actualText = driver.findElement(By.className("has-text-align-center")).getText();
        String expectedText = "Congratulations student. You successfully logged in!";
        if (actualText.equalsIgnoreCase(expectedText)) isCorrectText = true;

        isLogOutButtonPresent = driver.findElement(By.linkText("Log out")).isDisplayed();
        if (isCorrectText && isCorrectUrl && isLogOutButtonPresent) System.out.println("Login Successful");
    }

    public static void verifyInvalidLogin() {
        boolean isErrorDisplayed = false, isUsernameError = false, isPasswordError = false;
        isErrorDisplayed = driver.findElement(By.id("error")).isDisplayed();
        if (isErrorDisplayed) {
            System.out.println("Error message is displayed");
            String actualError = driver.findElement(By.id("error")).getText();
            String expectedUsernameError = "Your username is invalid!";
            String expectedPasswordError = "Your password is invalid!";
            if (actualError.equalsIgnoreCase(expectedUsernameError)) isUsernameError = true;
            if (isUsernameError) System.out.println("error message text verified for wrong username");
            if (actualError.equalsIgnoreCase(expectedPasswordError)) isPasswordError = true;
            if (isPasswordError) System.out.println("error message text verified for wrong password");
        }
    }

    public static void verifyLogout() {
        driver.findElement(By.linkText("Log out")).click();

        String actualLoggedOutUrl = driver.getCurrentUrl();
        String expectedLoggedOutUrl = "https://practicetestautomation.com/practice-test-login/";
        assert actualLoggedOutUrl != null;
        String actualTitle = driver.getTitle();
        String expectedTitle = "Test Login | Practice Test Automation";
        assert actualTitle != null;
        if (actualLoggedOutUrl.equalsIgnoreCase(expectedLoggedOutUrl) && actualTitle.equalsIgnoreCase(expectedTitle))
            System.out.println("Logout Successful");
    }

    public static void testExceptions() {
        driver.findElement(By.linkText("Test Exceptions")).click();
    }

    public static void verifyExceptions(int ch) {
        switch (ch) {
            case 1:
                tc_NoSuchElementException();
                break;
            case 2:
                tc_ElementNotInteractableException();
                break;
            case 3:
                tc_InvalidElementStateException();
                break;
            case 4:
                tc_StaleElementReferenceException();
                break;
            case 5:
                tc_TimeoutException();
                break;
            default:
                System.out.println("Choose between 1-5 only.");
                break;
        }
    }

    public static void tc_NoSuchElementException() {
        driver.findElement(By.id("add_btn")).click();
        try {
            boolean isRow2Displayed = driver.findElement(By.id("row2")).isDisplayed();
            if (isRow2Displayed) System.out.println("Row2 Displayed");
            else System.out.println("Row2 Not Displayed");
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] tc_NoSuchElementException: " + e.getMessage());
        }
    }

    public static void tc_ElementNotInteractableException() {
        driver.findElement(By.id("add_btn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            WebElement row2Input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='row2']//input")));
            if (row2Input != null) {
                row2Input.sendKeys("Burger");
            }
            driver.findElement(By.name("Save")).click();
            boolean isItemSaved = driver.findElement(By.id("confirmation")).isDisplayed();
            if (isItemSaved) System.out.println("Item Saved");
        } catch (ElementNotInteractableException e) {
            System.out.println("[ERROR] tc_ElementNotInteractableException: " + e.getMessage());
        }
    }

    public static void tc_InvalidElementStateException() {
        WebElement row1Input = driver.findElement(By.xpath("//div[@id='row1']//input"));
        try {
            row1Input.clear();
            row1Input.sendKeys("Burger");
        } catch (InvalidElementStateException e) {
            System.out.println("[ERROR] tc_InvalidElementStateException: " + e.getMessage());
            System.out.println("Trying again...");
            driver.findElement(By.id("edit_btn")).click();
            row1Input.clear();
            row1Input.sendKeys("Burger");
            driver.findElement(By.id("save_btn")).click();
            boolean isItemSaved = driver.findElement(By.id("confirmation")).isDisplayed();
            if (isItemSaved) System.out.println("Item Saved");
        }
    }

    public static void tc_StaleElementReferenceException() {
        WebElement instructions = driver.findElement(By.id("instructions"));
        driver.findElement(By.id("add_btn")).click();
        try {
            if (instructions.isDisplayed()) System.out.println("Element is stale");
        } catch (StaleElementReferenceException e) {
            System.out.println("[ERROR] tc_StaleElementReferenceException: " + e.getMessage());
        }
    }

    public static void tc_TimeoutException() {
        driver.findElement(By.id("add_btn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='row2']//input")));
            System.out.println("Row 2 is displayed!");
        } catch (TimeoutException e) {
            System.out.println("[ERROR] tc_TimeoutException: " + e.getMessage());
        }
    }

    public static void returnToHomePage() {
        driver.navigate().to("https://practicetestautomation.com/practice/");
    }

    public static void refreshPage() {
        driver.navigate().refresh();
    }

    public static void tearDown() throws InterruptedException {
        Thread.sleep(2500);
        driver.quit();
    }

    static void main() throws Exception {
        setup();
        getApp("https://practicetestautomation.com/practice/");
        verifyHomePage();

        testLogin();
        verifyLogin("student", "Password123", true);
        Thread.sleep(2000);
        verifyLogout();
        Thread.sleep(2000);
        verifyLogin("student123", "Password123", false);
        Thread.sleep(2000);
        verifyLogin("student", "Password", false);
        Thread.sleep(2000);
        returnToHomePage();

        Thread.sleep(2000);

        testExceptions();
        verifyExceptions(1);
        Thread.sleep(2000);
        refreshPage();
        verifyExceptions(2);
        Thread.sleep(2000);
        refreshPage();
        verifyExceptions(3);
        Thread.sleep(2000);
        refreshPage();
        verifyExceptions(4);
        Thread.sleep(2000);
        refreshPage();
        verifyExceptions(5);
        Thread.sleep(2000);
        returnToHomePage();

        tearDown();
    }
}
