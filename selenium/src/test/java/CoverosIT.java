import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class CoverosIT {

    @FindBy(id = "s")
    WebElement searchElement;

    @FindBy(css = "#header div:nth-child(2)")
    WebElement header;

    @FindBy(linkText = "Selenified")
    WebElement selenifiedLink;

    @FindBy(name = "FirstName")
    WebElement firstNameInput;

    @FindBy(name = "LastName")
    WebElement lastNameInput;

    @FindBy(name = "email")
    WebElement emailInput;

    @FindBy(name = "Company")
    WebElement companyInput;

    @FindAll({
            @FindBy(className = "wpcf7-submit"),
            @FindBy(tagName = "input")
    })
    WebElement submitInput;

    By downloadMessage = By.className("wpcf7-response-output");


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

        // instantiate our elements
        PageFactory.initElements(driver, this);

        // And now use this to visit Google
        driver.get("https://www.coveros.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("https://www.coveros.com");

        // Enter something to search for
        searchElement.sendKeys("selenium");

        // Now submit the form. WebDriver will find the form for us from the element
        searchElement.submit();

        // Coveros's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("header-blog")));

        //take our screenshot
        takeScreenshot(driver, "Selenium Search");

        // Should see: "You searched for selenified - Coveros"
        assertEquals(driver.getTitle(), "You searched for selenium - Coveros");

        //Close the browser
        driver.quit();
    }

    @Test
    public void selenifiedDownload() {
        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.coveros.com");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        PageFactory.initElements(driver, this);
        header.click();
        selenifiedLink.click();
        PageFactory.initElements(driver, this);
        firstNameInput.sendKeys("Max");
        lastNameInput.sendKeys("Saperstone");
        emailInput.sendKeys("max.saperstone@coveros.com");
        companyInput.sendKeys("Coveros");
        submitInput.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(downloadMessage));
        assertEquals(driver.findElement(downloadMessage).getText(), "Thank you for your interest in Selenified. Your download should have started.");
        takeScreenshot(driver, "Selenified Download");
        driver.quit();
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        Reporter.log(testName + "<br/><br/>\n\n<img src=\"data:image/png;base64," + screenshot + "\"/>");

    }
}
