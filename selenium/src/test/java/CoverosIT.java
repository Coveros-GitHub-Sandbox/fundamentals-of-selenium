import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class CoverosIT {

    @Test
    public void seleniumSearch() {
        //for linux/mac installations
//        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
        //for windows installations
//        System.setProperty("webdriver.gecko.driver", "lib/chromedriver.exe");
        // Create a new instance of the Chrome driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("https://www.coveros.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("https://www.coveros.com");

        // Find the text input element by its name
        WebElement searchBox = driver.findElement(By.id("s"));

        // Enter something to search for
        searchBox.sendKeys("selenium");

        // Now submit the form. WebDriver will find the form for us from the element
        searchBox.submit();

        // Coveros's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("header-blog")));

        // Should see: "You searched for selenified - Coveros"
        assertEquals(driver.getTitle(), "You searched for selenium - Coveros");

        //Close the browser
        driver.quit();
    }
}
