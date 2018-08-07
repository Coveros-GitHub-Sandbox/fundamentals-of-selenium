import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GoogleIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "https://www.google.com");
    }

    @Test
    public void cheeseSearch() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // identify our elements
        Element search = app.newElement(Locator.NAME, "q");
        Element searchResults = app.newElement(Locator.ID, "ires");
        // perform the search
        search.type("cheese");
        search.submit();
        // wait for the search to run
        searchResults.waitFor().displayed();
        app.azzert().titleEquals("cheese - Google Search");
        // close out the test
        finish();
    }
}
