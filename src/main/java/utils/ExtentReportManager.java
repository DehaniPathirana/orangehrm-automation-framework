package utils;

// IMPORT STATEMENTS
import com.aventstack.extentreports.ExtentReports;      // Main report object
import com.aventstack.extentreports.ExtentTest;         // Individual test report
import com.aventstack.extentreports.reporter.ExtentSparkReporter;  // HTML report generator

/**
 * ExtentReportManager Class - Manages Test Reports
 *
 * PURPOSE:
 * - Creates beautiful HTML reports showing test results
 * - Each test gets its own section in the report
 * - Shows pass/fail status, execution time, error details
 * - Can attach screenshots to failed tests
 *
 * HOW IT WORKS:
 * 1. Creates one ExtentReports instance for entire test suite
 * 2. Each test method creates its own ExtentTest
 * 3. At end, generates HTML report file
 *
 * SINGLETON PATTERN:
 * - Only one ExtentReports instance exists throughout test execution
 * - All tests share the same report instance
 */

public class ExtentReportManager {
    /*
     * CLASS VARIABLES (STATIC = SHARED BY ALL INSTANCES)
     */

    // MAIN REPORT OBJECT - Only one instance for entire test suite
    private static ExtentReports extent;

    // THREAD-SAFE TEST STORAGE - Each thread gets its own ExtentTest
    // Why ThreadLocal? - For parallel test execution, each thread needs its own test reference
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Get Report Instance - SINGLETON PATTERN
     *
     * WHAT IT DOES:
     * - If report doesn't exist, create it
     * - If report exists, return existing one
     *
     * WHY SINGLETON?
     * - We want only ONE report for all our tests
     * - All tests add their results to this single report
     *
     * @return ExtentReports - The main report instance
     */
    public static ExtentReports getInstance() {

        // If extent is null (first time), create new instance
        if (extent == null) {
            createInstance();  // Create new report instance
            System.out.println("📊 New ExtentReports instance created");
        } else {
            System.out.println("♻️  Reusing existing ExtentReports instance");
        }

        return extent;  // Return the report instance
    }

    /**
     * Create Report Instance - PRIVATE METHOD
     *
     * WHAT IT DOES:
     * 1. Creates HTML report file in test-reports folder
     * 2. Sets report title and configuration
     * 3. Adds system information to report
     *
     * CALLED BY: getInstance() method when report doesn't exist
     */
    private static void createInstance() {

        // STEP 1: CREATE HTML REPORT FILE
        // ExtentSparkReporter = Generates HTML report
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-reports/ExtentReport.html");

        // STEP 2: CONFIGURE REPORT APPEARANCE
        sparkReporter.config().setReportName("OrangeHRM Automation Test Report");  // Report title
        sparkReporter.config().setDocumentTitle("Test Execution Report");          // Browser tab title

        System.out.println("📄 HTML report will be saved to: test-reports/ExtentReport.html");

        // STEP 3: CREATE MAIN REPORT OBJECT
        extent = new ExtentReports();  // Create main report instance
        extent.attachReporter(sparkReporter);  // Connect HTML generator to report

        // STEP 4: ADD SYSTEM INFORMATION TO REPORT
        // This information appears in the report header
        extent.setSystemInfo("Environment", "QA");                    // Test environment
        extent.setSystemInfo("Tester", "Dehani Pathirana");          // Your name
        extent.setSystemInfo("Browser", "Chrome & Firefox");         // Browsers tested
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));  // OS info
        extent.setSystemInfo("Java Version", System.getProperty("java.version")); // Java version

        System.out.println("⚙️  Report configuration completed");
    }

    /**
     * Set Current Test - Stores test for current thread
     *
     * WHAT IT DOES:
     * - Each test method calls this to register itself with report
     * - Uses ThreadLocal for parallel execution safety
     *
     * WHEN TO USE:
     * - Call this at the beginning of each test method
     * - Pass the ExtentTest object created for that test
     *
     * @param test - The ExtentTest object for current test
     */
    public static void setTest(ExtentTest test) {
        extentTest.set(test);  // Store test in ThreadLocal
        System.out.println("📝 Test registered with report: " + test.getModel().getName());
    }

    /**
     * Get Current Test - Retrieves test for current thread
     *
     * WHAT IT DOES:
     * - Returns the ExtentTest object for current test method
     * - Used to add logs, pass/fail status to the test
     *
     * WHEN TO USE:
     * - Call this when you want to add information to current test
     * - Example: ExtentReportManager.getTest().log(Status.PASS, "Test passed");
     *
     * @return ExtentTest - The current test object
     */
    public static ExtentTest getTest() {
        ExtentTest currentTest = extentTest.get();  // Get test from ThreadLocal

        if (currentTest != null) {
            // Test exists, return it
            return currentTest;
        } else {
            // No test set, this shouldn't happen in proper usage
            System.out.println("⚠️  Warning: No test set in current thread");
            return null;
        }
    }

    /**
     * Flush Reports - Writes report to file
     *
     * WHAT IT DOES:
     * - Takes all collected test data and writes it to HTML file
     * - Must be called at the end of test execution
     * - Without this, no report file will be generated
     *
     * WHEN TO CALL:
     * - After all tests are completed
     * - Usually called in TestNG listener's onFinish() method
     *
     * IMPORTANT: If you forget to call this, you won't get a report!
     */
    public static void flushReports() {

        if (extent != null) {
            extent.flush();  // Write all data to HTML file
            System.out.println("✅ Reports flushed successfully - Check test-reports/ExtentReport.html");
        } else {
            System.out.println("⚠️  Warning: No report instance to flush");
        }
    }

    /**
     * Clean Up - Removes current test from ThreadLocal
     *
     * WHAT IT DOES:
     * - Clears ThreadLocal to prevent memory leaks
     * - Good practice for parallel execution
     *
     * WHEN TO CALL:
     * - After each test completes (in tearDown or listener)
     */
    public static void removeTest() {
        extentTest.remove();  // Clear ThreadLocal
        System.out.println("🧹 Test removed from ThreadLocal");
    }
}

