import static com.google.common.base.Strings.isNullOrEmpty;

import com.applitools.eyes.*;
import com.applitools.eyes.fluent.Target;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class BasicTest {

    public static void main(String[] args) {

        EyesRunner runner;
        Eyes eyes;
        BatchInfo batch;
        WebDriver driver;
        // Must be before ALL tests (at Class-level)
        BatchInfo batchInfo = new BatchInfo("TeamCity Batch");
// If the test runs via TeamCity, set the batch ID accordingly.
        String batchId = System.getenv("APPLITOOLS_BATCH_ID");
        if (batchId != null) {
            batchInfo.setId(batchId);
        }

        // Initialize the Runner for your test.
        runner = new ClassicRunner();

        // Initialize the eyes SDK
        eyes = new Eyes(runner);

        eyes.setBatch(batchInfo);

        // Raise an error if no API Key has been found.
        if(isNullOrEmpty(System.getenv("APPLITOOLS_API_KEY"))) {
            throw new RuntimeException("No API Key found; Please set environment variable 'APPLITOOLS_API_KEY'.");
        }

        // Set your personal Applitols API Key from your environment variables.
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // set batch name
        eyes.setBatch(batch);

        // Use Chrome browser
        driver = new ChromeDriver();

        eyes.open(driver, "TeamCity App", "TeamCity Test", new RectangleSize(800, 800));

        // Navigate the browser to the "ACME" demo app.
        driver.get("https://applitools.com/helloworld?diff1");

        // Visual checkpoint #2 - Check the app page.
        eyes.check(Target.window());

        // End the test.
        eyes.closeAsync();

        // Close the browser.
        driver.quit();

        // If the test was aborted before eyes.close was called, ends the test as
        // aborted.
        eyes.abortIfNotClosed();

        // Wait and collect all test results
        TestResultsSummary allTestResults = runner.getAllTestResults();

        // Print results
        System.out.println(allTestResults);
    }
}
