import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        // identify our elements
        Element searchBox = app.newElement(Locator.ID, "s");
        // perform the search
        searchBox.type("selenified");
        searchBox.submit();
        // wait for some search results to come back
        app.newElement(Locator.CLASSNAME, "header-blog").waitForState().displayed();
        // ensure the title shows what we expect
        app.azzert().titleEquals("You searched for selenified - Coveros");
        // close out the test
        finish();
    }
}
