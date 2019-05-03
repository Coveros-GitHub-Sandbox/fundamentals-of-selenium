package functional;

import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.Blogs;
import pages.Home;
import pages.SelenifiedProduct;

public class CoverosIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "https://www.coveros.com");
    }

    @DataProvider(name = "search items", parallel = true)
    public Object[][] DataSetOptions() {
        return new Object[][]{
                new Object[]{"selenium"},
                new Object[]{"selenified"},
                new Object[]{"docker"}
        };
    }

    @DataProvider(name = "fieldErrors", parallel = true)
    public Object[][] DataSetOptionsFields() {
        return new Object[][]{
                new Object[]{"FirstName"},
                new Object[]{"LastName"},
                new Object[]{"email"},
                new Object[]{"Company"}
        };
    }

    @Test(dataProvider = "search items")
    public void seleniumSearch(String searchItem) {
        // use this object to manipulate the app
        App app = this.apps.get();
        Home home = new Home(app);
        home.searchFor(searchItem);
        Blogs blogs = new Blogs(app);
        blogs.waitForLoad();
        blogs.checkTitle("You searched for " + searchItem + " - Coveros");
        finish();
    }

    @Test
    public void selenifiedDownloadButton() {
        App app = this.apps.get();
        Home home = new Home(app);
        home.navigateToSelenified();
        SelenifiedProduct selenified = new SelenifiedProduct(app);
        selenified.assertDownloadEnabled();
        finish();
    }

    @Test
    public void selenifiedDownloadError() {
        App app = this.apps.get();
        Home home = new Home(app);
        home.navigateToSelenified();
        SelenifiedProduct selenified = new SelenifiedProduct(app);
        selenified.clickDownloadButton();
        selenified.waitForDownloadMessage();
        selenified.assertDownloadMessage("Validation errors occurred. Please confirm the fields and submit it again.");
        finish();
    }

    @Test(dataProvider = "fieldErrors")
    public void selenifiedDownloadErrorField(String className) {
        App app = this.apps.get();
        Home home = new Home(app);
        home.navigateToSelenified();
        SelenifiedProduct selenified = new SelenifiedProduct(app);
        selenified.clickDownloadButton();
        selenified.waitForDownloadMessage();
        selenified.assertFieldMessage(className, "Please fill the required field.");
        finish();
    }

    @Test
    public void selenifiedDownload() {
        App app = this.apps.get();
        Home home = new Home(app);
        home.navigateToSelenified();
        SelenifiedProduct selenified = new SelenifiedProduct(app);
        selenified.downloadSelenified("Max", "Saperstone", "max.saperstone@coveros.com", "Coveros");
        selenified.waitForDownloadMessage();
        selenified.assertDownloadMessage("Thank you for your interest in Selenified. Your download should have started.");
        finish();
    }
}
