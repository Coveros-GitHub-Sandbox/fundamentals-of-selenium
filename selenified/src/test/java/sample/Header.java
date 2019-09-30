package sample;

import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class Header {

    private final Element mainMenuBar;
    private final Element selenifiedLink;

    public Header(App app) {
        mainMenuBar = app.newElement(Locator.CSS, "#header div:nth-child(2)");
        selenifiedLink = app.newElement(Locator.LINKTEXT, "Selenified");
    }

    public void navigateToSelenified() {
        mainMenuBar.click();
        selenifiedLink.click();
    }
}
