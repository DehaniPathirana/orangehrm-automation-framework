package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class ExtentReportManager {

    private static ExtentReports extent;


    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();


    public static ExtentReports getInstance() {


        if (extent == null) {
            createInstance();
            System.out.println("📊 New ExtentReports instance created");
        } else {
            System.out.println("♻️  Reusing existing ExtentReports instance");
        }

        return extent;
    }


    private static void createInstance() {


        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-reports/ExtentReport.html");


        sparkReporter.config().setReportName("OrangeHRM Automation Test Report");
        sparkReporter.config().setDocumentTitle("Test Execution Report");

        System.out.println("📄 HTML report will be saved to: test-reports/ExtentReport.html");


        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);


        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Dehani Pathirana");
        extent.setSystemInfo("Browser", "Chrome & Firefox");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        System.out.println("Report configuration completed");
    }


    public static void setTest(ExtentTest test) {
        extentTest.set(test);
        System.out.println("Test registered with report: " + test.getModel().getName());
    }


    public static ExtentTest getTest() {
        ExtentTest currentTest = extentTest.get();

        if (currentTest != null) {

            return currentTest;
        } else {

            System.out.println("Warning: No test set in current thread");
            return null;
        }
    }


    public static void flushReports() {

        if (extent != null) {
            extent.flush();
            System.out.println("Reports flushed successfully - Check test-reports/ExtentReport.html");
        } else {
            System.out.println("Warning: No report instance to flush");
        }
    }


    public static void removeTest() {
        extentTest.remove();
        System.out.println("Test removed from ThreadLocal");
    }
}

