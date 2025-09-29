package tests;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ExtentReportManager;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Test login functionality with valid credentials")
    public void testValidLogin() {


        ExtentReportManager.setTest(
                ExtentReportManager.getInstance().createTest(
                        "Valid Login Test",
                        "Verify user can login with valid credentials and access dashboard"
                )
        );

        ExtentReportManager.getTest().log(Status.INFO, "Starting valid login test");
        ExtentReportManager.getTest().log(Status.INFO, "Thread: " + Thread.currentThread().getName());

        try {

            if (driver == null) {
                throw new RuntimeException("WebDriver is null - setup may have failed");
            }


            LoginPage loginPage = new LoginPage(driver);
            ExtentReportManager.getTest().log(Status.PASS, "Login page loaded successfully");


            String username = "Admin";
            String password = "admin123";

            ExtentReportManager.getTest().log(Status.INFO, "Attempting login with credentials: " + username);
            loginPage.login(username, password);
            ExtentReportManager.getTest().log(Status.PASS, "Login credentials submitted successfully");


            loginPage.waitForLoginResult();
            ExtentReportManager.getTest().log(Status.INFO, "Waiting for login to process...");


            Thread.sleep(3000);


            DashboardPage dashboardPage = new DashboardPage(driver);

            boolean isDashboardDisplayed = dashboardPage.isDashboardDisplayed();

            if (isDashboardDisplayed) {
                String dashboardTitle = dashboardPage.getDashboardTitle();
                ExtentReportManager.getTest().log(Status.PASS,
                        "LOGIN SUCCESSFUL - Dashboard displayed with title: " + dashboardTitle);


                boolean isUserLoggedIn = dashboardPage.isUserLoggedIn();
                ExtentReportManager.getTest().log(Status.INFO, "User logged in status: " + isUserLoggedIn);

            } else {

                String errorMsg = loginPage.getErrorMessage();
                if (!errorMsg.isEmpty()) {
                    ExtentReportManager.getTest().log(Status.FAIL,
                            "LOGIN FAILED - Error message: " + errorMsg);
                } else {
                    ExtentReportManager.getTest().log(Status.FAIL,
                            "LOGIN FAILED - Dashboard not displayed and no error message found");
                }
            }


            Assert.assertTrue(isDashboardDisplayed,
                    "Dashboard should be displayed after successful login. Check if credentials are correct or if there are network issues.");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL,
                    "Test failed due to exception: " + e.getMessage());


            String errorDetails = "Browser: " + System.getProperty("browser", "unknown") +
                    ", Thread: " + Thread.currentThread().getName() +
                    ", Error: " + e.getMessage();

            e.printStackTrace();
            Assert.fail("Valid login test failed: " + errorDetails, e);
        }
    }

    @Test(priority = 2, description = "Test login functionality with invalid credentials")
    public void testInvalidLogin() {

        ExtentReportManager.setTest(
                ExtentReportManager.getInstance().createTest(
                        "Invalid Login Test",
                        "Verify login fails with invalid credentials and shows error message"
                )
        );

        ExtentReportManager.getTest().log(Status.INFO, "Starting invalid login test");

        try {

            if (driver == null) {
                throw new RuntimeException("WebDriver is null - setup may have failed");
            }

            LoginPage loginPage = new LoginPage(driver);


            String invalidUsername = "InvalidUser";
            String invalidPassword = "WrongPassword123";

            ExtentReportManager.getTest().log(Status.INFO,
                    "Attempting login with invalid credentials: " + invalidUsername);

            loginPage.login(invalidUsername, invalidPassword);
            ExtentReportManager.getTest().log(Status.PASS, "Invalid credentials submitted");


            loginPage.waitForLoginResult();


            Thread.sleep(2000);


            String errorMessage = loginPage.getErrorMessage();
            boolean hasErrorMessage = !errorMessage.isEmpty();

            if (hasErrorMessage) {
                ExtentReportManager.getTest().log(Status.PASS,
                        "Error message displayed correctly: " + errorMessage);
            } else {
                ExtentReportManager.getTest().log(Status.FAIL,
                        "No error message displayed for invalid login");
            }


            boolean isOnLoginPage = loginPage.isLoginPageDisplayed();

            if (isOnLoginPage) {
                ExtentReportManager.getTest().log(Status.PASS,
                        "User correctly remained on login page");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL,
                        "User unexpectedly navigated away from login page");
            }


            boolean testPassed = hasErrorMessage || isOnLoginPage;

            if (testPassed) {
                ExtentReportManager.getTest().log(Status.PASS,
                        "Invalid login test passed - Either error shown or remained on login page");
            }

            Assert.assertTrue(testPassed,
                    "For invalid login, either error message should be displayed OR user should remain on login page");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL,
                    "Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Invalid login test failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 3, description = "Test login behavior with empty credential fields")
    public void testEmptyFields() {

        ExtentReportManager.setTest(
                ExtentReportManager.getInstance().createTest(
                        "Empty Fields Test",
                        "Verify login is prevented when credential fields are empty"
                )
        );

        ExtentReportManager.getTest().log(Status.INFO, "Starting empty fields test");

        try {

            if (driver == null) {
                throw new RuntimeException("WebDriver is null - setup may have failed");
            }

            LoginPage loginPage = new LoginPage(driver);

            ExtentReportManager.getTest().log(Status.INFO,
                    "Attempting to login with empty username and password fields");


            loginPage.clickLoginButton();


            Thread.sleep(2000);


            boolean isLoginPageDisplayed = loginPage.isLoginPageDisplayed();

            if (isLoginPageDisplayed) {
                ExtentReportManager.getTest().log(Status.PASS,
                        "Form validation working correctly - User remained on login page");
                ExtentReportManager.getTest().log(Status.PASS,
                        "Security check passed - No unauthorized access with empty fields");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL,
                        "SECURITY ISSUE - User was able to proceed with empty credentials");
            }


            String errorMessage = loginPage.getErrorMessage();
            if (!errorMessage.isEmpty()) {
                ExtentReportManager.getTest().log(Status.INFO,
                        "Validation message displayed: " + errorMessage);
            }

            Assert.assertTrue(isLoginPageDisplayed,
                    "User should remain on login page when credential fields are empty");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL,
                    "Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Empty fields test failed: " + e.getMessage(), e);
        }
    }
}