package utils;

// Importing logger for logging configuration load status
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read properties from the config.properties file.
 * This ensures that configuration is centralized and easy to manage.
 */
public class ConfigReader {

    private final Properties properties; // Stores loaded key-value pairs from config file
    private static final Logger logger = LogManager.getLogger(ConfigReader.class); // Logger for this class
    private static final String CONFIG_PATH = "src/test/resources/config/config.properties"; // Path to the config file

    public ConfigReader() {
        properties = new Properties(); // Creating empty Properties object

        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis); // Loading properties from the file
            logger.info("Configuration properties loaded successfully from: " + CONFIG_PATH); // Log success
        } catch (IOException e) {
            logger.error("Failed to load configuration properties file.", e); // Log failure
            throw new RuntimeException("Configuration properties file not found at " + CONFIG_PATH); // Stop test if config not found
        }
    }

    /**
     * Gets a property value by its key.
     * @param key The key of the property to retrieve.
     * @return The value of the property as a String. Returns null if key is not found.
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key); // Get value for the provided key
        if (value == null) {
            logger.warn("Property with key '" + key + "' not found in config file."); // Warn if key not present
        }
        return value; // Return the value (or null)
    }
}
