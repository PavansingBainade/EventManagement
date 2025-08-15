package utils;

// Import required Selenium classes for JavaScript execution and element handling
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Provides utility methods for interacting with WebElements,
 * such as scrolling and performing JavaScript clicks.
 */
public class ElementUtils {

    private final JavascriptExecutor js; // JavaScript executor for custom browser actions

    public ElementUtils(WebDriver driver) {
        this.js = (JavascriptExecutor) driver; // Casting WebDriver to JavascriptExecutor
    }

    /**
     * Uses JavaScript to scroll the page until the given WebElement is in view.
     * This is essential for interacting with elements that are not visible on the screen initially.
     * @param element The WebElement to scroll to.
     */
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element); // Scroll element into view using JS
        try {
            Thread.sleep(500); // A small pause to ensure scrolling is complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    /**
     * Uses JavaScript to click on an element. This is a robust way to click
     * elements that might be obscured by other elements (like sticky headers).
     * @param element The WebElement to click.
     */
    public void clickElementByJs(WebElement element) {
        js.executeScript("arguments[0].click();", element); // Perform click via JavaScript
    }
}
