package pages;


// IMPORT STATEMENTS
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {

    private WebDriver driver;



    @FindBy(xpath = "//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")
    private WebElement dashboardHeader;  // The "Dashboard" heading


    @FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
    private WebElement userDropdown;  // User's name dropdown


    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutLink;


    public DashboardPage(WebDriver driver) {
        this.driver = driver;


        PageFactory.initElements(driver, this);

        System.out.println("📊 DashboardPage object created and elements initialized");
    }



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
            return "";
        }
    }


    public void logout() {
        try {
            System.out.println("🚪 Starting logout process...");


            userDropdown.click();
            System.out.println("🖱️  Clicked on user dropdown");


            Thread.sleep(1000);


            logoutLink.click();
            System.out.println("🖱️  Clicked on logout link");

            System.out.println("✅ Logout process completed");

        } catch (Exception e) {
            System.out.println("❌ Logout failed: " + e.getMessage());
        }
    }

    public boolean isUserLoggedIn() {
        try {

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
