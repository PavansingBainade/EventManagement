package stepDefinitions;

import BaseClass.libraryClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import pages.BookingPage;
import pages.ContactUsPage;
import utils.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Consolidated Step Definitions for all form interactions.
 */
public class FormSteps {

    protected static WebDriver driver;
    private final BookingPage bookingPage;
    private final ContactUsPage contactUsPage;
    private static final Logger logger = LogManager.getLogger(FormSteps.class);

    public FormSteps() {
        FormSteps.driver = libraryClass.getDriver(); // Get shared WebDriver
        this.bookingPage = new BookingPage(driver); // Initialize BookingPage
        this.contactUsPage = new ContactUsPage(driver); // Initialize ContactUsPage
        logger.info("FormSteps initialized with shared WebDriver instance.");
    }

    @Given("the user navigates to the Event Management System homepage")
    public void user_navigates_to_homepage() {
        logger.info("Navigating to the homepage.");
        bookingPage.navigateToHomePage(); // Navigate to homepage
    }

    // --- Booking Form Steps ---

    @When("the user enters valid data for the booking form from {string} and row {int}")
    public void user_enters_valid_booking_data(String sheetName, Integer rowNumber) throws IOException {
        logger.info("Entering valid data for Booking form.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1); // Read test data
        bookingPage.fillBookingForm(testData); // Fill booking form
    }

    @When("the user enters booking form data with an invalid email from {string} and row {int}")
    public void user_enters_invalid_email(String sheetName, Integer rowNumber) throws IOException {
        logger.warn("Entering data with invalid email.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1);
        bookingPage.fillBookingForm(testData);
        bookingPage.clickBookNow(); 
        
        String alertText = bookingPage.getAlertMessage();
        if (alertText != null && alertText.contains("Confirmed")) {
            logger.error("Form submitted successfully with invalid email. This should not happen.");
            Assert.fail("Booking succeeded with invalid email. Validation is missing.");
        } else {
            logger.info("Validation message displayed as expected for invalid email.");
        }
    }

    // --- Contact Us Form Steps ---

    @When("the user enters valid data for the contact us form from {string} and row {int}")
    public void user_enters_valid_contact_us_data(String sheetName, Integer rowNumber) throws IOException {
        logger.info("Entering valid data for Contact Us form.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1); // Read test data
        contactUsPage.fillContactUsForm(testData); // Fill contact form
    }

    // --- Generic Form Actions ---

    @When("the user clicks the {string} button")
    public void user_clicks_button(String buttonText) {
        logger.info("Clicking the '" + buttonText + "' button.");
        if (buttonText.equalsIgnoreCase("Book Now")) {
            bookingPage.clickBookNow(); // Click Book Now
        } else if (buttonText.equalsIgnoreCase("Send Message Now")) {
            contactUsPage.clickSendMessage(); // Click Send Message Now
        } else {
            throw new IllegalArgumentException("Unsupported button: " + buttonText);
        }
    }

    @Then("a success message {string} should be displayed")
    public void success_message_is_displayed(String expectedMessage) {
        logger.info("Verifying success message: '" + expectedMessage + "'");
        String actualMessage;

        if (expectedMessage.equals("Your Booking has been Confirmed !")) {
            actualMessage = bookingPage.getAlertMessage(); // Get booking success message
        } else if (expectedMessage.equals("Your message has been sent !")) {
            actualMessage = contactUsPage.getSuccessMessage();  // Get contact success message
        } else {
            throw new IllegalArgumentException("Unknown success message: " + expectedMessage);
        }

        Assert.assertEquals(actualMessage, expectedMessage, "Success message did not match."); // Assert success message
        logger.info("Success message verified successfully.");
    }

    @Then("a contact success message {string} should be displayed")
    public void contact_success_message_is_displayed(String expectedMessage) {
        logger.info("Verifying Contact Us success message: '" + expectedMessage + "'");
        String actualMessage = contactUsPage.getSuccessMessage(); // Get contact success message
        Assert.assertEquals(actualMessage, expectedMessage, "Contact Us success message did not match.");
    }

    @Then("an appropriate validation message should be displayed for the invalid field")
    public void validation_message_is_displayed() {
        logger.info("Verifying validation alert message.");
        String alertText = bookingPage.getAlertMessage(); // Get alert message
        Assert.assertNotNull(alertText, "Validation message alert was not displayed.");
        logger.warn("Validation alert displayed: " + alertText);
    }

    @Then("appropriate error messages should be displayed for all required booking fields")
    public void error_messages_for_required_fields_displayed() {
        logger.info("Verifying required field validation messages using custom error divs.");
        List<String> invalidFields = bookingPage.getInvalidRequiredFields(); // Get invalid booking fields

        logger.debug("Invalid fields found: " + invalidFields);
        Assert.assertTrue(invalidFields.isEmpty(), "No validation messages found. Form may not be validating correctly.");
        logger.info("Validation successfully detected on fields: " + invalidFields);
    }

    @When("the user enters booking form data with empty fields from {string} and row {int}")
    public void user_enters_booking_form_data_with_empty_fields(String sheetName, Integer rowNumber) throws IOException {
        logger.info("Entering empty data for Booking form.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1); // Read test data

        for (String key : testData.keySet()) {
            testData.put(key, "");  // Set fields empty
        }

        bookingPage.fillBookingForm(testData); // Fill form with empty data
    }

    @When("the user enters booking form data with an invalid phone number from {string} and row {int}")
    public void user_enters_invalid_phone_number(String sheetName, Integer rowNumber) throws IOException {
        logger.warn("Entering data with invalid phone number.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1);
        bookingPage.fillBookingForm(testData);
        bookingPage.clickBookNow(); 
        
        String alertText = bookingPage.getAlertMessage();
        if (alertText != null && alertText.contains("Confirmed")) {
            logger.error("Form submitted successfully with invalid phone number. This should not happen.");
            Assert.fail("Booking succeeded with invalid phone number. Validation is missing.");
        } else {
            logger.info("Validation message displayed as expected for invalid phone number.");
        }
    }

    @When("the user enters contact form data with empty fields from {string} and row {int}")
    public void user_enters_contact_form_data_with_empty_fields(String sheetName, Integer rowNumber) throws IOException {
        logger.info("Entering empty data for Contact Us form.");
        Map<String, String> testData = new ExcelReader().getData(
            "src/test/resources/testData/EventManagementTestData.xlsx", sheetName).get(rowNumber - 1); // Read test data

        for (String key : testData.keySet()) {
            testData.put(key, ""); // Set fields empty
        }

        contactUsPage.fillContactUsForm(testData); // Fill contact form with empty data
    }

    @Then("appropriate validation messages should be displayed for all required contact fields")
    public void error_messages_for_contact_fields_displayed() {
        logger.info("Verifying required field validation messages using error divs in Contact Us form.");
        List<String> invalidFields = contactUsPage.getInvalidRequiredFields(); // Get invalid contact fields

        logger.debug("Invalid Contact Us fields: " + invalidFields);
        Assert.assertFalse(invalidFields.isEmpty(), "No validation messages found in Contact Us form.");
        logger.info("Validation messages detected for fields: " + invalidFields);
    }
}
