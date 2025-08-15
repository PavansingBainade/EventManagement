package BaseClass;

// Importing necessary libraries for file handling, time, and properties
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

// Importing Log4j logger for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Importing Selenium WebDriver classes
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

// Importing WebDriverManager to automatically handle driver binaries
import io.github.bonigarcia.wdm.WebDriverManager;

public class libraryClass {
    protected static WebDriver driver; // WebDriver instance used across framework
    protected static Properties config = new Properties(); // Properties object for reading config file
    private static final Logger logger = LogManager.getLogger(libraryClass.class); // Logger instance for this class

    public static WebDriver getDriver() {
        return driver; // Getter method for WebDriver
    }

    public static void loadConfig() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties")) {
            config.load(fis); // Loading config file from path
            logger.info("Configuration file loaded successfully."); // Log success
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + e.getMessage(), e); // Log file-related exception
        } catch (Exception e) {
            logger.error("Unexpected error while loading config: " + e.getMessage(), e); // Log any other exception
        }
    }

    public static void initializeBrowser() {
        try {
            loadConfig(); // Load configuration settings
            String browser = config.getProperty("browser", "chrome"); // Get browser name from config
            logger.info("Selected browser from config: " + browser); // Log selected browser
            int implicitWait = Integer.parseInt(config.getProperty("implicitWait", "30")); // Get wait time

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup(); // Setup Chrome driver
                    driver = new ChromeDriver(); // Launch Chrome
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup(); // Setup Chrome driver for headless
                    ChromeOptions options = new ChromeOptions(); // Create ChromeOptions
                    options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080"); // Add headless args
                    driver = new ChromeDriver(options); // Launch headless Chrome
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup(); // Setup Firefox driver
                    driver = new FirefoxDriver(); // Launch Firefox
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup(); // Setup Edge driver
                    driver = new EdgeDriver(); // Launch Edge
                    break;
                default:
                    logger.error("Unsupported browser specified in config: " + browser); // Log unsupported browser
                    return;
            }

            driver.manage().window().maximize(); // Maximize browser window
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); // Set implicit wait
            logger.info("Browser initialized and maximized with implicit wait: " + implicitWait); // Log success
        } catch (Exception e) {
            logger.error("Failed to initialize browser: " + e.getMessage(), e); // Log exception
        }
    }

    public static void closeBrowser() {
        try {
            if (driver != null) {
                driver.quit(); // Close and quit browser
                logger.info("Browser closed successfully."); // Log success
            }
        } catch (Exception e) {
            logger.error("Error occurred while closing the browser: " + e.getMessage(), e); // Log exception
        }
    }
}
