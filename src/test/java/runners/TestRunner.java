package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


/**
 * This class acts as the TestNG runner for executing Cucumber features.
 * It integrates Cucumber with TestNG and specifies various options for test execution.
 */
@CucumberOptions(
    // Path to the feature files
    features = "src/test/resources/features",
    
    //path to failed test cases
    //features = "@target/failed-scenario.txt",

    // Glue path explicitly points to the packages containing step definitions and hooks.
    glue = {"stepDefinitions", "hooks"},

    // Generates different types of reports
    plugin = {
        "pretty", // Prints Gherkin steps in a readable format in the console
        "html:target/cucumber-reports/cucumber-pretty.html", // Generates a standard HTML report
        "json:target/cucumber-reports/CucumberTestReport.json", // Generates a JSON report
        "timeline:target/test-output-thread/", // Generates a timeline report for parallel execution
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", // Integrates with ExtentReports
        "rerun:target/failed-scenario.txt"
    },

    // Specifies which tags to execute or ignore
    tags = "",

    // If true, checks if all feature file steps have corresponding step definitions
    dryRun = false,

    // If true, makes the console output more readable by removing irrelevant information
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
	 	
}
