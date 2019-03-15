package functional;

import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.Home;
import pages.SelenifiedProduct;

public class CoverosIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "https://www.coveros.com");
    }

    @Test
    public void seleniumSearch() {
        // use this object to manipulate the app
        App app = this.apps.get();
        Home home = new Home(app);
        home.searchFor("selenium");
        // wait for the search to run
        app.waitFor().titleEquals("You searched for selenium - Coveros");
        app.azzert().titleEquals("You searched for selenium - Coveros");
        // close out the test
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

    @Test
    public void selenifiedDownloadErrorField() {
        App app = this.apps.get();
        Home home = new Home(app);
        home.navigateToSelenified();
        SelenifiedProduct selenified = new SelenifiedProduct(app);
        selenified.clickDownloadButton();
        selenified.waitForDownloadMessage();
        selenified.assertFieldMessage("FirstName", "Please fill the required field.");
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
