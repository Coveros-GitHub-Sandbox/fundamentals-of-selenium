import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CoverosIT {

    @Test
    public void seleniumSearch() {
        //for linux/mac installations
        System.setProperty("webdriver.gecko.driver", "lib/geckodriver");
        //for windows installations
//        System.setProperty("webdriver.gecko.driver", "lib/geckodriver.exe");
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = new FirefoxDriver();

        // And now use this to visit Google
        driver.get("https://www.coveros.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("https://www.coveros.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.id("s"));

        // Enter something to search for
        element.sendKeys("selenium");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Coveros's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().startsWith("You searched for");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
        driver.quit();
    }

    @Test
    public void selenifiedDownload() {
        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.coveros.com");
        driver.findElement(By.xpath("//*[@id='header']/div[2]")).click();
        driver.findElement(By.linkText("Selenified")).click();
        driver.findElement(By.name("FirstName")).sendKeys("Max");
        driver.findElement(By.name("LastName")).sendKeys("Saperstone");
        driver.findElement(By.name("email")).sendKeys("max.saperstone@coveros.com");
        driver.findElement(By.name("Company")).sendKeys("Coveros");
        driver.findElement(By.xpath("//form/p/input")).click();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Thank you for your interest in Selenified. Your download should have started.");
            }
        });
        assertEquals(driver.findElement(By.xpath("//form/div[2]")).getText(), "Thank you for your interest in Selenified. Your download should have started.");
        driver.quit();
    }
}
