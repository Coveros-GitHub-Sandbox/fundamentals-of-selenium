package pages;

import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class Blogs {

    private final App app;
    private final Element header;

    public Blogs(App app) {
        this.app = app;
        header = app.newElement(Locator.CLASSNAME, "header-blog");
    }

    public void waitForLoad() {
        header.waitForState().displayed();
    }

    public void checkTitle(String title) {
        app.azzert().titleEquals(title);
    }
}
