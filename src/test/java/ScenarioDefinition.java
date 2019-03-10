import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;


public class ScenarioDefinition {

    private static WebDriver driver;

    // local paths

    private static final String PATH_CHROME_DRIVER = System.getProperty("user.dir") + "/chromedriver";
    private static final String PATH_REGULAR_IMAGE = System.getProperty("user.dir") + "/images/regular_montreal.jpg";
    private static final String PATH_MEDIUM_IMAGE = System.getProperty("user.dir") + "/images/medium_colorado.jpg";
    private static final String PATH_LARGE_IMAGE = System.getProperty("user.dir") + "/images/large_airport.jpg";


    private static final String EMAIL_SUBJECT_BODY_FIELD = "whaleshark";
    // log in info

    private static final String USER_EMAIL = "ECSE428AB@gmail.com";
    private static final String USER_PASSWORD = "gulugulu";

    private static final String URL_INBOX = "https://mail.google.com/mail/u/0/#inbox";


    // HTML DOMs

    private static final String ACTION_COMPOSE_EMAIL = "z0";
    private static final String ACTION_SEND_EMAIL = "gU";
    private static final String ACTION_ATTACH_BUTTON = "//input[@type='file']";


    private static final String ACTION_SIGN_IN_NEXT = "qhFLie";
    private static final String ACTION_SIGN_IN_FIELD = "whsOnd";


    private static final String FIELD_EMAIL_RECIPIENT = "vO";
    private static final String FIELD_EMAIL_SUBJECT = "aoT";
    private static final String FIELD_EMAIL_BODY = "Am";
    private static final String FIELD_EMAIL_BODY_WITH_ATTACHMENT = "vI";
    private static final String FIELD_EMAIL_BODY_WITH_DRIVE = "Kj-JD-K7-K0";


    private static final String NOTICE_EMAIL_SENT = "aT";


    // Shared Logic


    @Given("^I am a user on the Gmail page$")
    public void given() throws MalformedURLException {
        instantiateWebDrivers();
        readLink(URL_INBOX);
        logIn();
    }


    @And("^I click the compose button$")
    public void composeEmail() {
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_COMPOSE_EMAIL))).click();
    }

    @And("^I enter a recipient and subject$")
    public void inputFields() {
        WebElement recipient = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_RECIPIENT)));
        WebElement subject = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_SUBJECT)));
        WebElement body = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY)));

        recipient.sendKeys(USER_EMAIL);
        subject.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
        body.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
    }


    // Normal Flow

    @When("^I press the paperclip icon and select an image$")
    public void addRegularAttachment() {
        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(PATH_REGULAR_IMAGE);

    }


    @Then("^I upload an attachment that is under maximum size limit$")
    public void checkRegularAttachStatus() {
        WebElement bodyAttachment = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_ATTACHMENT)));

        // the existence of this field automatically confirms that the image is below 25mb
        if (bodyAttachment == null || bodyAttachment.getText().equals("")) {  // If no string it means attachment wasn't properly added
            System.out.println("Image was not successfully uploaded");
        }

    }

    @And("^I press the send button$")
    public void sendRegularEmail() {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();

    }


    @Then("^the email will be sent to the recipient and will include a regular attachment$")
    public void verifyRegularEmail() {

        WebElement emailConfirmation;

        try {
            emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
        }

        if (emailConfirmation.getText().equals("Message sent.")) {
            System.out.println("Email sent successfully.");
        }


    }


    // alternate flow


    @When("^I press the paperclip icon and select an image whose size is over the maximum limit$")
    public void addLargeAttachment() {
        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(PATH_LARGE_IMAGE);

    }


    @Then("^my image will be uploaded to Google Drive and a link to it will be provided in the email$")
    public void checkAttachStatus() {
        WebElement driveNotice = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_DRIVE)));

        // We cannot time this similar to the normal flow as the upload time can vary due to it being a large file. As a result, this will test whether the google drive upload process occurs
        // the existence of this field automatically confirms that the image is below 25mb
        if (!driveNotice.isDisplayed()) {  // If no string it means attachment wasn't properly added
            System.out.println("Image was not successfully uploaded");
        }

        try {
            while (driveNotice.isDisplayed()) {
            }
        } catch (StaleElementReferenceException e) {
            driveNotice = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_DRIVE)));
            while (driveNotice.isDisplayed()) {
            }
        }


    }

    @And("^I press the send button to send the large attachment$")
    public void sendEmail() {
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();

    }


    @Then("^the email will be sent to the recipient and will include access to the attachment via Google Drive")
    public void verifyLargeEmail() {

        WebElement emailConfirmation;

        try {
            emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
        }

        if (emailConfirmation.getText().equals("Message sent.")) {
            System.out.println("Email sent successfully.");
        }


    }


    // error flow


    // initial set up


    /**
     * Signs into the google account
     */
    private void logIn() {

        try {
            driver.findElement(By.className(ACTION_SIGN_IN_FIELD)).sendKeys(USER_EMAIL);


            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SIGN_IN_NEXT))).click();


            WebElement welcome = (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.className("jXeDnc")));

            try {
                while (!welcome.getText().equals("Welcome")) {
                    // do nothing
                }
            }
            catch(StaleElementReferenceException e) {

            }


            driver.findElement(By.className(ACTION_SIGN_IN_FIELD)).sendKeys(USER_PASSWORD);

            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SIGN_IN_NEXT))).click();


        } catch (NoSuchElementException e) {
            System.out.println("Already signed in. Skipping sign in.");
        }
    }


    // Common set up logic


    /**
     * Sets up the chrome driver if not already set up
     *
     * @throws MalformedURLException
     */
    private void instantiateWebDrivers() throws MalformedURLException {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", PATH_CHROME_DRIVER);
            driver = new ChromeDriver();
        }
    }


    /**
     * Opens a specific url
     *
     * @param url the specific url to open
     */
    private void readLink(String url) {
        if (driver != null) {
            driver.get(url);
        }
    }


}
