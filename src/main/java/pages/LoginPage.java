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

    // Primary locators using @FindBy
    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")
    private WebElement errorMessage;

    // Alternative locators as backup
    private final By usernameLocator = By.name("username");
    private final By passwordLocator = By.name("password");
    private final By loginButtonLocator = By.xpath("//button[@type='submit']");
    private final By errorMessageLocator = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // FIXED: Validate driver session before initializing elements
        if (!isDriverSessionValid()) {
            throw new RuntimeException("Invalid WebDriver session provided to LoginPage");
        }

        PageFactory.initElements(driver, this);

        // Wait for login page to load completely
        waitForPageToLoad();

        System.out.println("LoginPage object created and elements initialized");
    }


    private boolean isDriverSessionValid() {
        try {
            if (driver == null) {
                System.err.println("WebDriver is null");
                return false;
            }

            // Try to get current URL to verify session is active
            String currentUrl = driver.getCurrentUrl();
            return currentUrl != null;

        } catch (Exception e) {
            System.err.println("WebDriver session validation failed: " + e.getMessage());
            return false;
        }
    }


    private void waitForPageToLoad() {
        try {
            // Validate driver session before waiting
            if (!isDriverSessionValid()) {
                throw new RuntimeException("WebDriver session is invalid");
            }

            // Wait for username field to be present and visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameLocator));
            System.out.println("Login page loaded successfully");

        } catch (Exception e) {
            System.err.println("Login page failed to load properly: " + e.getMessage());
            throw new RuntimeException("Login page load failed", e);
        }
    }

    public void enterUsername(String username) {
        try {
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                // Try to get fresh driver from BaseTest
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }

            // Wait for element to be clickable
            WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(usernameLocator));

            // Clear and enter username
            usernameElement.clear();
            usernameElement.sendKeys(username);
            System.out.println("Entered username: " + username);

            // Verify text was entered correctly
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
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }

            // Wait for element to be clickable
            WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(passwordLocator));

            // Clear and enter password
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
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                this.driver = BaseTest.getDriver();
                this.wait = BaseTest.getWait();

                if (driver == null) {
                    throw new RuntimeException("Unable to obtain valid WebDriver session");
                }
            }

            // Wait for button to be clickable
            WebElement loginElement = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));

            // Click the button
            loginElement.click();
            System.out.println("Clicked login button");

            // Wait a moment for page to start processing
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
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                System.out.println("Cannot check error message - invalid driver session");
                return "";
            }

            // Wait for error message to appear (shorter timeout for error messages)
            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessageLocator)
            );

            String message = errorElement.getText().trim();
            System.out.println("Error message found: " + message);
            return message;

        } catch (Exception e) {
            // No error message found - this is normal for successful login
            System.out.println("No error message displayed (normal for successful login)");
            return "";
        }
    }


    public boolean isLoginPageDisplayed() {
        try {
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                System.out.println("Cannot check login page - invalid driver session");
                return false;
            }

            // Check multiple indicators that we're on login page
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
            // Validate session before proceeding
            if (!isDriverSessionValid()) {
                System.out.println("Cannot wait for login result - invalid driver session");
                return;
            }

            // Wait for either dashboard or error message to appear
            wait.until(driver -> {
                try {
                    // Check if we're redirected to dashboard (URL change)
                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl.contains("/dashboard")) {
                        return true;
                    }

                    // Check if error message appeared
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