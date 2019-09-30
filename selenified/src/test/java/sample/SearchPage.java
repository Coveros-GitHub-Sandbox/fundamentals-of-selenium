package sample;

import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class SearchPage {

    private final Element blogHeader;

    public SearchPage(App app) {
        blogHeader = app.newElement(Locator.CLASSNAME, "header-blog");
    }

    public void waitForPageToLoad() {
        blogHeader.waitForState().displayed();
    }
}
