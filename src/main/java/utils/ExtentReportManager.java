package utils;

// IMPORT STATEMENTS
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class ExtentReportManager {
    /*
     * CLASS VARIABLES (STATIC = SHARED BY ALL INSTANCES)
     */

    // MAIN REPORT OBJECT - Only one instance for entire test suite
    private static ExtentReports extent;

    // THREAD-SAFE TEST STORAGE - Each thread gets its own ExtentTest
    // Why ThreadLocal? - For parallel test execution, each thread needs its own test reference
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();


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


    public static void setTest(ExtentTest test) {
        extentTest.set(test);  // Store test in ThreadLocal
        System.out.println("📝 Test registered with report: " + test.getModel().getName());
    }


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


    public static void flushReports() {

        if (extent != null) {
            extent.flush();  // Write all data to HTML file
            System.out.println("✅ Reports flushed successfully - Check test-reports/ExtentReport.html");
        } else {
            System.out.println("⚠️  Warning: No report instance to flush");
        }
    }


    public static void removeTest() {
        extentTest.remove();  // Clear ThreadLocal
        System.out.println("🧹 Test removed from ThreadLocal");
    }
}

