package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import utils.ConfigReader;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class BookingPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private ConfigReader configReader;
    private static final Logger logger = LogManager.getLogger(BookingPage.class);

    // WebElements using @FindBy
    @FindBy(id = "firstName") WebElement firstNameInput; // First name input field
    @FindBy(id = "lastName") WebElement lastNameInput; // Last name input field
    @FindBy(id = "emaiId") WebElement emailInput; // Email input field
    @FindBy(id = "phoneNo") WebElement phoneInput; // Phone number input field
    @FindBy(id = "eventType") WebElement eventTypeDropdown; // Event type dropdown
    @FindBy(id = "eventDate") WebElement dateInput; // Event date input field
    @FindBy(id = "eventTime") WebElement eventTimeInput; // Event time input field
    @FindBy(id = "guestCount") WebElement guestCountInput; // Guest count input field
    @FindBy(id = "vegFood") WebElement cateringYesRadio; // Catering "Yes" radio button
    @FindBy(id = "address") WebElement addressInput; // Address input field
    @FindBy(id = "city") WebElement cityDropdown; // City dropdown
    @FindBy(id = "pincode") WebElement pincodeInput; // Pincode input field
    @FindBy(id = "eventDetail") WebElement eventDetailsTextarea; // Event details textarea
    @FindBy(id = "book-now") WebElement bookNowButton; // Book now button
    @FindBy(id = "bookingconfirm") WebElement bookingConfirmMessage; // Booking confirmation message element

    public BookingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize WebElements
        configReader = new ConfigReader(); // Load config reader
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Initialize wait
    }

    public void navigateToHomePage() {
        String url = configReader.getProperty("url"); // Get URL from config
        logger.info("Opening URL: " + url);
        driver.get(url); // Navigate to home page
    }

    public void fillBookingForm(Map<String, String> data) {
        logger.info("Filling form with data: " + data);

        firstNameInput.sendKeys(data.get("FirstName"));
        lastNameInput.sendKeys(data.get("LastName"));
        emailInput.sendKeys(data.get("Email"));
        phoneInput.sendKeys(data.get("Phone"));

        String eventType = data.get("EventType");
        if (eventType != null && !eventType.isEmpty()) {
            new Select(eventTypeDropdown).selectByVisibleText(eventType); // Select event type
        }

        dateInput.sendKeys(data.get("Date"));
        eventTimeInput.sendKeys(data.get("EventTime"));
        guestCountInput.sendKeys(data.get("GuestCount"));

        if ("Yes".equalsIgnoreCase(data.get("Catering"))) {
            cateringYesRadio.click(); // Select catering option
        }

        addressInput.sendKeys(data.get("Address"));

        String city = data.get("City");
        if (city != null && !city.isEmpty()) {
            new Select(cityDropdown).selectByVisibleText(city); // Select city
        }

        pincodeInput.sendKeys(data.get("Pincode"));
        eventDetailsTextarea.sendKeys(data.get("EventDetails"));
    }

    public void clickBookNow() {
      logger.info("Clicking 'Book Now' button with upward scroll adjustment to expose form content.");
      JavascriptExecutor js = (JavascriptExecutor) driver;

      js.executeScript(
          "const element = document.getElementById('book-now');" +
          "const yOffset = 700;" +  
          "const y = element.getBoundingClientRect().top + window.pageYOffset - yOffset;" +
          "window.scrollTo({ top: y, behavior: 'smooth' });" +
          "setTimeout(() => element.click(), 300);"
      ); // Scroll to and click book now
  }

    public String getAlertMessage() {
        logger.info("Trying to fetch confirmation message...");

        wait.until(ExpectedConditions.visibilityOf(bookingConfirmMessage)); // Wait for visibility

        wait.until(driver -> {
            String text = bookingConfirmMessage.getText().trim();
            logger.info("Waiting for message: '" + text + "'");
            return !text.isEmpty(); // Wait for non-empty message
        });

        String finalText = bookingConfirmMessage.getText().trim();
        logger.info("Final confirmation message: '" + finalText + "'");
        return finalText; // Return confirmation message
    }

    public List<String> getInvalidRequiredFields() {
        logger.info("Checking required fields for error messages");

        // Simplified fixed map of field IDs to error element IDs
        String[][] fieldErrorIds = {
            {"firstName", "fnameErr"}, {"lastName", "lnameErr"},
            {"phoneNo", "phoneErr"}, {"emaiId", "emailErr"},
            {"eventType", "eventTypeErr"}, {"eventDate", "eventDateErr"},
            {"eventTime", "eventTimeErr"}, {"guestCount", "guestCountErr"},
            {"address", "addressErr"}, {"city", "cityErr"},
            {"pincode", "pincodeErr"}, {"eventDetail", "eventDetailErr"}
        };

        List<String> invalidFields = new ArrayList<>();

        for (String[] pair : fieldErrorIds) {
            try {
                WebElement error = driver.findElement(By.id(pair[1]));
                if (!error.getText().trim().isEmpty()) {
                    invalidFields.add(pair[0]); // Add invalid field name
                }
            } catch (NoSuchElementException e) {
                logger.warn("No error element found for: " + pair[0]);
            }
        }

        logger.info("Invalid fields: " + invalidFields);
        return invalidFields; // Return list of invalid fields
    }
}
