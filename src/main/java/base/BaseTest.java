package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import java.time.Duration;


public class BaseTest {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();

    protected WebDriver driver;
    protected WebDriverWait wait;

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static WebDriverWait getWait() {
        WebDriverWait currentWait = waitThreadLocal.get();
        if (currentWait == null && getDriver() != null) {
            currentWait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
            waitThreadLocal.set(currentWait);
        }
        return currentWait;
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Setting up " + browser + " browser for thread: " + Thread.currentThread().getName());
        System.out.println("=".repeat(50));

        try {
            WebDriver webDriver = initializeDriver(browser);

            driverThreadLocal.set(webDriver);
            this.driver = webDriver;

            WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            waitThreadLocal.set(webDriverWait);
            this.wait = webDriverWait;

            configureBrowser();
            navigateToApplication();
            verifyDriverSession();

            System.out.println("Browser setup completed successfully for: " + browser);

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR during browser setup: " + e.getMessage());
            e.printStackTrace();
            cleanupDriver();
            throw new RuntimeException("Failed to initialize browser: " + e.getMessage(), e);
        }
    }

    private WebDriver initializeDriver(String browser) {
        WebDriver webDriver;

        switch (browser.toLowerCase()) {
            case "chrome":
                webDriver = initializeChromeDriver();
                break;
            case "firefox":
                webDriver = initializeFirefoxDriver();
                break;
            default:
                System.out.println("Browser not specified or invalid. Defaulting to Chrome.");
                webDriver = initializeChromeDriver();
                break;
        }

        return webDriver;
    }

    private WebDriver initializeChromeDriver() {
        System.out.println("Initializing Chrome driver...");

        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-extensions");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");

            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

            WebDriver driver = new ChromeDriver(options);
            System.out.println("Chrome driver initialized successfully");
            return driver;

        } catch (Exception e) {
            System.err.println("Failed to initialize Chrome driver: " + e.getMessage());
            throw new RuntimeException("Chrome driver initialization failed", e);
        }
    }

    private WebDriver initializeFirefoxDriver() {
        System.out.println("Initializing Firefox driver...");

        try {
            WebDriverManager.firefoxdriver().setup();

            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("dom.webnotifications.enabled", false);
            options.addPreference("dom.push.enabled", false);
            options.addPreference("geo.enabled", false);
            options.addPreference("dom.disable_beforeunload", true);

            WebDriver driver = new FirefoxDriver(options);
            System.out.println("Firefox driver initialized successfully");
            return driver;

        } catch (Exception e) {
            System.err.println("Failed to initialize Firefox driver: " + e.getMessage());
            throw new RuntimeException("Firefox driver initialization failed", e);
        }
    }

    private void configureBrowser() {
        try {
            driver.manage().window().maximize();
            System.out.println("Browser window maximized");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

            System.out.println("Timeouts configured: implicit(10s), pageLoad(30s), script(30s)");

        } catch (Exception e) {
            System.err.println("Error configuring browser: " + e.getMessage());
            throw new RuntimeException("Browser configuration failed", e);
        }
    }

    private void navigateToApplication() {
        String url = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
        System.out.println("Navigating to: " + url);

        try {
            driver.get(url);
            Thread.sleep(3000);
            System.out.println("Successfully navigated to OrangeHRM login page");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during page load wait", e);
        } catch (Exception e) {
            System.err.println("Failed to navigate to application: " + e.getMessage());
            throw new RuntimeException("Navigation failed", e);
        }
    }

    private void verifyDriverSession() {
        try {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Driver session verified. Current URL: " + currentUrl);
        } catch (Exception e) {
            System.err.println("Driver session verification failed: " + e.getMessage());
            throw new RuntimeException("Driver session is not active", e);
        }
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("Starting cleanup for thread: " + Thread.currentThread().getName());
        System.out.println("-".repeat(30));

        cleanupDriver();

        System.out.println("Cleanup completed successfully");
        System.out.println("-".repeat(30) + "\n");
    }

    private void cleanupDriver() {
        try {
            WebDriver currentDriver = driverThreadLocal.get();
            if (currentDriver != null) {
                try {
                    currentDriver.quit();
                    System.out.println("Browser closed successfully");
                } catch (Exception e) {
                    System.err.println("Error closing browser: " + e.getMessage());
                }
            } else {
                System.out.println("No driver to cleanup for current thread");
            }
        } catch (Exception e) {
            System.err.println("Error during browser cleanup: " + e.getMessage());
        } finally {
            try {
                driverThreadLocal.remove();
                waitThreadLocal.remove();
                driver = null;
                wait = null;
            } catch (Exception e) {
                System.err.println("Error clearing ThreadLocal variables: " + e.getMessage());
            }
        }
    }
}