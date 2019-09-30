import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class CoverosHomePage {

    private final Element searchBox;

    public CoverosHomePage(App app) {
        searchBox = app.newElement(Locator.ID, "s");
    }

    public void searchFor(String searchFor) {
        searchBox.type(searchFor);
        searchBox.submit();
    }
}
