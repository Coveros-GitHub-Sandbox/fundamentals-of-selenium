package pages;

import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class Home {

    private final Element search;
    private final Element header;
    private final Element selenified;

    public Home(App app) {
        search = app.newElement(Locator.ID, "s");
        header = app.newElement(Locator.CSS, "#header div:nth-child(2)");
        selenified = app.newElement(Locator.LINKTEXT, "Selenified");
    }

    public void searchFor(String term) {
        // perform the search
        search.type(term);
        search.submit();
    }

    public void navigateToSelenified() {
        header.click();
        selenified.click();
    }
}
