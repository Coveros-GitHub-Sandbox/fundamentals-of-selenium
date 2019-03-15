package pages;

import com.coveros.selenified.Locator;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;

public class SelenifiedProduct {

    private final App app;
    private final Element downloadButton;
    private final Element firstNameInput;
    private final Element lastNameInput;
    private final Element emailInput;
    private final Element companyInput;
    private final Element downloadMessage;

    public SelenifiedProduct(App app) {
        this.app = app;
        downloadButton = app.newElement(Locator.XPATH, "//form/p/input");
        firstNameInput = app.newElement(Locator.NAME, "FirstName");
        lastNameInput = app.newElement(Locator.NAME, "LastName");
        emailInput = app.newElement(Locator.NAME, "email");
        companyInput = app.newElement(Locator.NAME, "Company");
        downloadMessage = app.newElement(Locator.XPATH, "//form/div[2]");

    }

    public void assertDownloadEnabled() {
        downloadButton.assertState().enabled();
    }

    public void clickDownloadButton() {
        downloadButton.click();
    }

    public void fillOutForm(String firstName, String lastName, String email, String company) {
        firstNameInput.type(firstName);
        lastNameInput.type(lastName);
        emailInput.type(email);
        companyInput.type(company);
    }

    public void downloadSelenified(String firstName, String lastName, String email, String company) {
        fillOutForm(firstName, lastName, email, company);
        clickDownloadButton();
    }

    public void waitForDownloadMessage() {
        downloadMessage.waitForState().displayed();
    }

    public void assertDownloadMessage(String message) {
        downloadMessage.assertEquals().text(message);
    }

    public void assertFieldMessage(String fieldClass, String message) {
        app.newElement(Locator.CLASSNAME, fieldClass).assertEquals().text(message);

    }
}
