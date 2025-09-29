package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import base.BaseTest;
import java.time.Duration;


public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;


    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")
    private WebElement errorMessage;


    private final By usernameLocator = By.name("username");
    private final By passwordLocator = By.name("password");
    private final By loginButtonLocator = By.xpath("//button[@type='submit']");
    private final By errorMessageLocator = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));


        if (!isDriverSessionValid()) {
            throw new RuntimeException("Invalid WebDriver session provided to LoginPage");
        }

        PageFactory.initElements(driver, this);


        waitForPageToLoad();

        System.out.println("LoginPage object created and elements initialized");
    }


    private boolean isDriverSessionValid() {
        try {
            if (driver == null) {
                System.err.println("WebDriver is null");
                return false;
            }


            String currentUrl = driver.getCurrentUrl();
            return currentUrl != null;

        } catch (Exception e) {
            System.err.println("WebDriver session validation failed: " + e.getMessage());
            return false;
        }
    }


    private void waitForPageToLoad() {
        try {

            if (!isDriverSessionValid()) {
                throw new RuntimeException("WebDriver session is invalid");
            }


            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameLocator));
            System.out.println("Login page loaded successfully");

        } catch (Exception e) {
            System.err.println("Login page failed to load properly: " + e.getMessage());
            throw new RuntimeException("Login page load failed", e);
        }
    }

    public void enterUsername(String username) {
        try {

            if (!isDriverSessionValid()) {
                // Try to get fresh driver from BaseTest
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }


            WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(usernameLocator));


            usernameElement.clear();
            usernameElement.sendKeys(username);
            System.out.println("Entered username: " + username);


            String enteredText = usernameElement.getAttribute("value");
            if (!username.equals(enteredText)) {
                throw new RuntimeException("Username not entered correctly. Expected: " + username + ", Actual: " + enteredText);
            }

        } catch (Exception e) {
            System.err.println("Failed to enter username: " + e.getMessage());
            throw new RuntimeException("Could not enter username: " + username, e);
        }
    }


    public void enterPassword(String password) {
        try {

            if (!isDriverSessionValid()) {
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }


            WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(passwordLocator));


            passwordElement.clear();
            passwordElement.sendKeys(password);
            System.out.println("Entered password: ***hidden***");

        } catch (Exception e) {
            System.err.println("Failed to enter password: " + e.getMessage());
            throw new RuntimeException("Could not enter password", e);
        }
    }


    public void clickLoginButton() {
        try {

            if (!isDriverSessionValid()) {
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }


            WebElement loginElement = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));


            loginElement.click();
            System.out.println("Clicked login button");


            Thread.sleep(1000);

        } catch (Exception e) {
            System.err.println("Failed to click login button: " + e.getMessage());
            throw new RuntimeException("Could not click login button", e);
        }
    }


    public void login(String username, String password) {
        System.out.println("Starting login process...");
        try {
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
            System.out.println("Login process completed successfully");

        } catch (Exception e) {
            System.err.println("Login process failed: " + e.getMessage());
            throw new RuntimeException("Login process failed for user: " + username, e);
        }
    }


    public String getErrorMessage() {
        try {

            if (!isDriverSessionValid()) {
                System.out.println("Cannot check error message - invalid driver session");
                return "";
            }


            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessageLocator)
            );

            String message = errorElement.getText().trim();
            System.out.println("Error message found: " + message);
            return message;

        } catch (Exception e) {

            System.out.println("No error message displayed (normal for successful login)");
            return "";
        }
    }


    public boolean isLoginPageDisplayed() {
        try {

            if (!isDriverSessionValid()) {
                System.out.println("Cannot check login page - invalid driver session");
                return false;
            }


            boolean usernameVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameLocator)) != null;
            boolean passwordVisible = driver.findElement(passwordLocator).isDisplayed();
            boolean loginButtonVisible = driver.findElement(loginButtonLocator).isDisplayed();

            boolean isDisplayed = usernameVisible && passwordVisible && loginButtonVisible;
            System.out.println("Login page displayed: " + isDisplayed);
            return isDisplayed;

        } catch (Exception e) {
            System.err.println("Error checking if login page displayed: " + e.getMessage());
            return false;
        }
    }


    public void waitForLoginResult() {
        try {

            if (!isDriverSessionValid()) {
                System.out.println("Cannot wait for login result - invalid driver session");
                return;
            }


            wait.until(driver -> {
                try {

                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl.contains("/dashboard")) {
                        return true;
                    }


                    return driver.findElements(errorMessageLocator).size() > 0;

                } catch (Exception e) {
                    return false;
                }
            });

            System.out.println("Login result determined");

        } catch (Exception e) {
            System.err.println("Timeout waiting for login result: " + e.getMessage());
        }
    }
}