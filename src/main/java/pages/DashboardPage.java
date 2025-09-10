package pages;


// IMPORT STATEMENTS
import org.openqa.selenium.WebDriver;        // Browser controller
import org.openqa.selenium.WebElement;       // Represents elements on webpage
import org.openqa.selenium.support.FindBy;  // Annotation to find elements
import org.openqa.selenium.support.PageFactory;  // Initializes page elements

/**
 * DashboardPage Class - Represents OrangeHRM Dashboard Page
 *
 * PURPOSE:
 * - Contains elements that appear after successful login
 * - Contains actions we can perform on dashboard (logout, navigate, etc.)
 * - Used to verify that login was successful
 *
 * WHEN IS THIS PAGE USED:
 * - After successful login, user lands on Dashboard
 * - We use this page to verify login worked
 * - We use this page to perform post-login actions
 */

public class DashboardPage {
    // DRIVER INSTANCE - This represents the browser
    private WebDriver driver;

    /*
     * PAGE ELEMENTS - Elements that appear on Dashboard page
     *
     * IMPORTANT: These elements only appear AFTER successful login
     * If login fails, these elements won't be found
     */

    // DASHBOARD HEADER - Shows "Dashboard" text
    @FindBy(xpath = "//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")
    private WebElement dashboardHeader;  // The "Dashboard" heading

    // USER DROPDOWN - Shows logged-in user's name in top-right corner
    @FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
    private WebElement userDropdown;  // User's name dropdown

    // LOGOUT LINK - The logout option in user dropdown
    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutLink;  // Logout link

    /**
     * CONSTRUCTOR - Runs when DashboardPage object is created
     *
     * WHAT IT DOES:
     * 1. Receives WebDriver from test class
     * 2. Initializes all page elements using PageFactory
     *
     * @param driver - The browser instance from test class
     */
    public DashboardPage(WebDriver driver) {
        this.driver = driver;  // Store driver reference

        // Initialize page elements
        PageFactory.initElements(driver, this);

        System.out.println("📊 DashboardPage object created and elements initialized");
    }

    /*
     * PAGE ACTIONS - Methods that perform actions or checks on dashboard
     */

    /**
     * Check if Dashboard is Displayed - Main verification method
     *
     * PURPOSE: This is the MOST IMPORTANT method in this class
     * - Used to verify that login was successful
     * - If dashboard is displayed = login worked
     * - If dashboard not displayed = login failed
     *
     * @return boolean - true if dashboard is displayed, false otherwise
     */
    public boolean isDashboardDisplayed() {
        try {
            // Check if dashboard header is visible and displayed
            boolean displayed = dashboardHeader.isDisplayed();

            if (displayed) {
                System.out.println("✅ Dashboard is displayed - Login successful!");
            } else {
                System.out.println("❌ Dashboard not displayed - Login may have failed");
            }

            return displayed;

        } catch (Exception e) {
            // If exception occurs, dashboard is definitely not displayed
            System.out.println("❌ Dashboard not displayed - Exception: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get Dashboard Title - Returns the dashboard header text
     *
     * USED FOR: Additional verification that we're on correct page
     *
     * @return String - The dashboard title text
     */
    public String getDashboardTitle() {
        try {
            String title = dashboardHeader.getText();  // Get header text
            System.out.println("📋 Dashboard title: " + title);
            return title;
        } catch (Exception e) {
            System.out.println("❌ Could not get dashboard title: " + e.getMessage());
            return "";  // Return empty string if can't get title
        }
    }

    /**
     * Logout - Performs logout action
     *
     * HOW IT WORKS:
     * 1. Click on user dropdown (top-right corner)
     * 2. Click on "Logout" link
     *
     * USED FOR: Ending user session, testing logout functionality
     */
    public void logout() {
        try {
            System.out.println("🚪 Starting logout process...");

            // Step 1: Click on user dropdown to show logout option
            userDropdown.click();
            System.out.println("🖱️  Clicked on user dropdown");

            // Small wait for dropdown to appear
            Thread.sleep(1000);

            // Step 2: Click on logout link
            logoutLink.click();
            System.out.println("🖱️  Clicked on logout link");

            System.out.println("✅ Logout process completed");

        } catch (Exception e) {
            System.out.println("❌ Logout failed: " + e.getMessage());
        }
    }

    /**
     * Verify User is Logged In - Additional verification method
     *
     * PURPOSE: Double-check that user is properly logged in
     *
     * @return boolean - true if user appears to be logged in
     */
    public boolean isUserLoggedIn() {
        try {
            // Check if user dropdown is displayed (means user is logged in)
            boolean loggedIn = userDropdown.isDisplayed();

            if (loggedIn) {
                System.out.println("✅ User appears to be logged in");
            } else {
                System.out.println("❌ User does not appear to be logged in");
            }

            return loggedIn;

        } catch (Exception e) {
            System.out.println("❌ Could not verify login status: " + e.getMessage());
            return false;
        }
    }
}
