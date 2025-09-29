package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import utils.ExtentReportManager;
import com.aventstack.extentreports.Status;


public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();


        System.out.println("\n🚀 TEST STARTED: " + testName);
        System.out.println("📋 Class: " + className);
        System.out.println("⏰ Start Time: " + new java.util.Date());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        long executionTime = result.getEndMillis() - result.getStartMillis();


        System.out.println("✅ TEST PASSED: " + testName);
        System.out.println("⏱️ Execution Time: " + executionTime + " ms");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");


        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.PASS,
                        "Test completed successfully in " + executionTime + "ms");
            }
        } catch (Exception e) {
            System.err.println("Error updating ExtentReports for success: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Unknown error";
        long executionTime = result.getEndMillis() - result.getStartMillis();


        System.out.println("❌ TEST FAILED: " + testName);
        System.out.println("💥 Error: " + errorMessage);
        System.out.println("⏱️ Execution Time: " + executionTime + " ms");


        System.out.println("📍 Stack Trace:");
        if (result.getThrowable() != null) {
            result.getThrowable().printStackTrace();
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");


        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.FAIL,
                        "Test failed: " + errorMessage);
                ExtentReportManager.getTest().log(Status.INFO,
                        "Execution time: " + executionTime + "ms");
            }
        } catch (Exception e) {
            System.err.println("Error updating ExtentReports for failure: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ?
                result.getThrowable().getMessage() : "No reason provided";


        System.out.println("⏭️ TEST SKIPPED: " + testName);
        System.out.println("📝 Reason: " + skipReason);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");


        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.SKIP,
                        "Test skipped: " + skipReason);
            }
        } catch (Exception e) {
            System.err.println("Error updating ExtentReports for skip: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        int totalTests = context.getAllTestMethods().length;
        int passedTests = context.getPassedTests().size();
        int failedTests = context.getFailedTests().size();
        int skippedTests = context.getSkippedTests().size();


        double passPercentage = totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;


        System.out.println("\n🏁 TEST EXECUTION COMPLETED");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("📊 TEST SUMMARY:");
        System.out.println("   Total Tests: " + totalTests);
        System.out.println("   ✅ Passed: " + passedTests);
        System.out.println("   ❌ Failed: " + failedTests);
        System.out.println("   ⏭️ Skipped: " + skippedTests);
        System.out.println("   📈 Pass Rate: " + String.format("%.2f", passPercentage) + "%");
        System.out.println("═══════════════════════════════════════════");


        try {
            ExtentReportManager.flushReports();
            System.out.println("📑 ExtentReports generated successfully!");
            System.out.println("📂 Report Location: test-reports/ExtentReport.html");
            System.out.println("🌐 Open this file in your browser to view results");
        } catch (Exception e) {
            System.err.println("❌ Error generating ExtentReports: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n🎉 All tests completed!\\n");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        System.out.println("⚠️ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: " + testName);
    }


    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }


    private String formatDuration(long durationMs) {
        if (durationMs < 1000) {
            return durationMs + " ms";
        } else {
            double seconds = durationMs / 1000.0;
            return String.format("%.2f seconds", seconds);
        }
    }
}