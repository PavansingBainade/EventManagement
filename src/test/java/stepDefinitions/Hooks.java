package stepDefinitions;

import BaseClass.libraryClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {

    WebDriver driver;

    @Before
    public void setUp() {
        // Start the browser before each scenario
        libraryClass.initializeBrowser(); // Initialize browser
        driver = libraryClass.getDriver(); // Get WebDriver instance
        System.out.println("Browser opened.");
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (driver != null) {
                // Take a screenshot
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // Capture screenshot

                // Create file name
                String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // Format timestamp
                String status = scenario.isFailed() ? "FAILED" : "PASSED"; // Determine scenario status
                String name = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"); // Sanitize scenario name
                String fileName = name + "_" + status + "_" + time + ".png"; // Build screenshot filename

                // Save screenshot to folder
                File folder = new File("src/test/resources/Screenshots"); // Define screenshot folder
                if (!folder.exists()) {
                    folder.mkdirs(); // Create folder if it doesn't exist
                }
                File savedFile = new File(folder, fileName); // File destination
                Files.copy(screenshot.toPath(), savedFile.toPath()); // Save file

                // Attach screenshot to report
                scenario.attach(Files.readAllBytes(savedFile.toPath()), "image/png", fileName); // Attach to Cucumber report
                System.out.println("Screenshot saved: " + savedFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Could not take screenshot: " + e.getMessage()); // Log failure
        } finally {
            // Close the browser after each scenario
            libraryClass.closeBrowser(); // Close browser
            System.out.println("Browser closed.");
        }
    }
}
