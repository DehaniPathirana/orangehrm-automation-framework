package pages;


// IMPORT STATEMENTS
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    // DRIVER INSTANCE - This represents the browser
    private WebDriver driver;


    // DASHBOARD HEADER - Shows "Dashboard" text
    @FindBy(xpath = "//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")
    private WebElement dashboardHeader;  // The "Dashboard" heading

    // USER DROPDOWN - Shows logged-in user's name in top-right corner
    @FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
    private WebElement userDropdown;  // User's name dropdown

    // LOGOUT LINK - The logout option in user dropdown
    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutLink;  // Logout link


    public DashboardPage(WebDriver driver) {
        this.driver = driver;  // Store driver reference

        // Initialize page elements
        PageFactory.initElements(driver, this);

        System.out.println("📊 DashboardPage object created and elements initialized");
    }

    /*
     * PAGE ACTIONS - Methods that perform actions or checks on dashboard
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
