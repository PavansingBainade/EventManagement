package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ContactUsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(ContactUsPage.class);

    // Page elements
    @FindBy(id = "contact_name")
    private WebElement nameInput; // Name input field

    @FindBy(id = "contact_email")
    private WebElement emailInput; // Email input field

    @FindBy(id = "contact_subject")
    private WebElement subjectInput; // Subject input field

    @FindBy(id = "contact_message")
    private WebElement messageInput; // Message input field

    @FindBy(id = "mesgtab")
    private WebElement messageTab; // Message display tab

    public ContactUsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize web elements
        ConfigReader configReader = new ConfigReader(); // Load configuration
        long timeout = Long.parseLong(configReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout)); // Initialize wait
    }

    // Fill the form using Map data
    public void fillContactUsForm(Map<String, String> data) {
        logger.info("Filling Contact Us form with data: " + data);

        wait.until(ExpectedConditions.visibilityOf(nameInput)).sendKeys(data.get("Name"));
        wait.until(ExpectedConditions.visibilityOf(emailInput)).sendKeys(data.get("Email"));
        wait.until(ExpectedConditions.visibilityOf(subjectInput)).sendKeys(data.get("Subject"));
        wait.until(ExpectedConditions.visibilityOf(messageInput)).sendKeys(data.get("Message"));
    }

    // Click the "Send Message Now" button using JavaScript
    public void clickSendMessage() {
        logger.info("Clicking 'Send Message Now' button using JavaScript.");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("handleContact(event)"); // Trigger form submission
    }

    // Get the success message after form submission
    public String getSuccessMessage() {
        logger.info("Waiting for success message...");
        wait.until(ExpectedConditions.textToBePresentInElement(messageTab, "Your message has been sent !")); // Wait for confirmation text

        String message = messageTab.getText().trim();
        logger.info("Success message: " + message);
        return message; // Return success message
    }

    // Get validation message for a specific field
    public String getValidationMessage(String fieldId) {
        String errorId = fieldId + "Err"; // Derive error message element ID
        logger.info("Looking for validation message with ID: " + errorId);

        try {
            WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(errorId)));
            String message = errorDiv.getText().trim();
            logger.info("Validation message for '" + fieldId + "': " + message);
            return message; // Return validation message
        } catch (Exception e) {
            logger.warn("No validation message found for field: " + fieldId);
            return null;
        }
    }

    // Get all invalid fields that show validation errors
    public List<String> getInvalidRequiredFields() {
        logger.info("Checking validation errors...");

        String[][] fieldErrorIds = {
            {"contact_name", "contactNameErr"},
            {"contact_email", "contactEmailErr"},
            {"contact_subject", "contactSubjectErr"},
            {"contact_message", "contactMessageErr"}
        };

        List<String> invalidFields = new ArrayList<>();

        for (String[] pair : fieldErrorIds) {
            try {
                WebElement errorElement = driver.findElement(By.id(pair[1]));
                if (!errorElement.getText().trim().isEmpty()) {
                    logger.debug("Error in field: " + pair[0]);
                    invalidFields.add(pair[0]); // Add field with error
                }
            } catch (NoSuchElementException e) {
                logger.warn("Error element not found for field: " + pair[0]);
            }
        }

        logger.info("Invalid fields found: " + invalidFields);
        return invalidFields; // Return list of invalid fields
    }

}
