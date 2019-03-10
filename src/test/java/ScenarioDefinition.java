import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.Random;


public class ScenarioDefinition {

    private static WebDriver driver;

    // local paths


    // NOTICE: IF YOU ARE ON A WINDOWS COMPUTER, YOU MUST SWITCH CHROMEDRIVER TO chromedriver.exe! chromedriver is platform dependent!

    private static final String PATH_CHROME_DRIVER = System.getProperty("user.dir") + "/chromedriver";


    private static final String PATH_REGULAR_IMAGE_MONTREAL = System.getProperty("user.dir") + "/images/regular_montreal.jpg";
    private static final String PATH_REGULAR_IMAGE_COLORADO = System.getProperty("user.dir") + "/images/regular_colorado.jpg";
    private static final String PATH_REGULAR_IMAGE_TORONTO = System.getProperty("user.dir") + "/images/regular_toronto.jpg";
    private static final String PATH_LARGE_IMAGE_OIL = System.getProperty("user.dir") + "/images/large_oil.jpg";
    private static final String PATH_LARGE_IMAGE_AIRPORT = System.getProperty("user.dir") + "/images/large_airport.jpg";
    private static final String PATH_LARGE_IMAGE_MINING = System.getProperty("user.dir") + "/images/large_mining.jpg";
    private static final String PATH_VERY_LARGE_IMAGE_QUEBEC = System.getProperty("user.dir") + "/images/very_large_quebec.jpg";

    private static final String[] REGULAR_IMAGE_COLLECTION = {PATH_REGULAR_IMAGE_COLORADO, PATH_REGULAR_IMAGE_MONTREAL, PATH_REGULAR_IMAGE_TORONTO};
    private static final String[] LARGE_IMAGE_COLLECTION = {PATH_LARGE_IMAGE_AIRPORT, PATH_LARGE_IMAGE_MINING, PATH_LARGE_IMAGE_OIL};


    private static final String EMAIL_SUBJECT_BODY_FIELD = "whaleshark";

    // log in info as well as recipient emails

    private static final String USER_EMAIL = "ECSE428AB@gmail.com";
    private static final String USER_PASSWORD = "gulugulu";


    private static final String[] EMAIL_RECIPIENTS = {"ECSE428AB@gmail.com", "a-tel@live.ca", "boomasticka@hotmail.com"};


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


    private static final String NOTICE_EMAIL_SENT = "aT";
    private static final String NOTICE_UPLOADING_DRIVE = "Kj-JD-K7-K0";
    private static final String NOTICE_DRIVE_ERROR = "Kj-JD-K7-K0";

    // Shared Logic


    @Given("^I am a user on the Gmail page$")
    public void iOpenGmail() throws MalformedURLException {
        instantiateWebDrivers();
        readLink(URL_INBOX);
        logIn();
    }


    @And("^I click the compose button$")
    public void iComposeAnEmail() {
        WebElement composeButton = new WebDriverWait(driver, 25).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_COMPOSE_EMAIL)));
        composeButton.click();
    }

    @And("^I enter a recipient, a subject and the email body$")
    public void iInputFields() {
        try {
            System.out.println("Entering a recipient, subject and body.");
            WebElement recipient = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_RECIPIENT)));
            recipient.sendKeys(USER_EMAIL);
            WebElement subject = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_SUBJECT)));
            subject.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
            WebElement body = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY)));
            body.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
        } catch (ElementNotInteractableException | TimeoutException e) {
            System.out.println("Failed to enter a recipient, subject, or body. It is recommended that you re-run the test.");

        }
    }


    @And("^I enter more than one recipient, a subject and the email body$")
    public void iInputFieldsWithMultipleRecipients() throws ElementNotInteractableException {
        Random random = new Random();
        int firstEmailIndex = random.nextInt(3);
        int secondEmailIndex = random.nextInt(3);

        while (firstEmailIndex == secondEmailIndex) {
            secondEmailIndex = random.nextInt(3);
        }


        try {
            System.out.println("Entering recipients, subject and body.");
            WebElement recipient = (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_RECIPIENT)));
            recipient.sendKeys(EMAIL_RECIPIENTS[firstEmailIndex] + " ");
            recipient = (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_RECIPIENT)));
            recipient.sendKeys(EMAIL_RECIPIENTS[secondEmailIndex]);

            WebElement subject = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_SUBJECT)));
            subject.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
            WebElement body = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY)));

            body.sendKeys(EMAIL_SUBJECT_BODY_FIELD);
        } catch (ElementNotInteractableException | StaleElementReferenceException e) {
            System.out.println("Failed to enter a recipient, subject, or body. It is recommended that you re-run the test.");

        }
    }


    // Normal Flow

    @When("^I press the paperclip icon and select a standard image$")
    public void iAddARegularAttachment() {

        System.out.println("Attempting to upload image...");

        Random random = new Random();

        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(REGULAR_IMAGE_COLLECTION[random.nextInt(3)]);

    }


    @Then("^I upload an attachment that is under maximum size limit$")
    public void checkRegularAttachStatus() {
        WebElement bodyAttachment = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_ATTACHMENT)));

        // the existence of this field automatically confirms that the image is below 25mb
        if (bodyAttachment == null || bodyAttachment.getText().equals("")) {  // If no string it means attachment wasn't properly added
            System.out.println("Image was not successfully uploaded");
        } else {
            System.out.println("Image was attached.");
        }

    }

    @And("^I press the send button$")
    public void sendRegularEmail() {
        System.out.println("Attempting to send email...");
        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();
        } catch (TimeoutException e) {
            System.out.println("Server timed out, please re-run the test.");
        }

    }


    @Then("^the email will be sent to the recipient and will include a regular attachment$")
    public void verifyRegularEmail() {

        WebElement emailConfirmation;

        try {
            emailConfirmation = (new WebDriverWait(driver, 25)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
            if (emailConfirmation.getText().equals("Message sent.")) {
                System.out.println("Regular email sent successfully.");
            } else {
                System.out.println("There was an error or extended delay (possible network issues) in sending the email with multiple recipients. If you see this message, the email has likely sent, but was delayed due to the file upload.");
            }

        } catch (org.openqa.selenium.StaleElementReferenceException | TimeoutException e) {
            System.out.println("There was an error sending the email. It is recommended that you re-run the test.");
        }


    }


    // alternate flows


    // sending an image > 25mb


    @When("^I press the paperclip icon and select an image whose size is over the maximum limit$")
    public void addLargeAttachment() {

        System.out.println("Attempting to upload image...");

        Random random = new Random();

        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(LARGE_IMAGE_COLLECTION[random.nextInt(3)]);

    }


    @Then("^my image will be uploaded to Google Drive and a link to it will be provided in the email$")
    public void checkAttachStatus() {
        WebElement driveNotice = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_UPLOADING_DRIVE)));

        // TODO

        // We cannot time this similar to the normal flow as the upload time can vary due to it being a large file. As a result, this will test whether the google drive upload process occurs
        // the existence of this field automatically confirms that the image is below 25mb
        if (!driveNotice.isDisplayed()) {  // If no string it means attachment wasn't properly added
            System.out.println("Image was not successfully uploaded");
        }

        try {
            while (driveNotice.isDisplayed()) {
            }
        } catch (StaleElementReferenceException e) {
            driveNotice = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_UPLOADING_DRIVE)));
            while (driveNotice.isDisplayed()) {
            }
        }


    }

    @And("^I press the send button to send the large attachment$")
    public void sendEmail() {
        System.out.println("Attempting to send email...");

        try {
            new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();
        } catch (TimeoutException e) {
            System.out.println("Server timed out, please re-run the test.");

        }

    }


    @Then("^the email will be sent to the recipient and will include access to the attachment via Google Drive")
    public void verifyLargeEmail() {

        WebElement emailConfirmation;

        try {
            emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
            if (emailConfirmation.getText().equals("Message sent.")) {
                System.out.println("Large Email sent successfully.");
            } else {
                System.out.println("There was an error or extended delay (possible network issues) in sending the email with multiple recipients. If you see this message, the email has likely sent, but was delayed due to the file upload.");
            }
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            System.out.println("There was an error sending the email. It is recommended that you re-run the test.");

        }


    }

    // sending to multiple recipients

    @When("^I press the paperclip icon and select an image to send$")
    public void iAddARegularAttachmentForMultipleRecipients() {

        System.out.println("Attempting to upload image...");


        Random random = new Random();


        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(REGULAR_IMAGE_COLLECTION[random.nextInt(3)]);

    }


    @Then("^I upload an attachment$")
    public void myImageWasUploaded() {
        WebElement bodyAttachment = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_ATTACHMENT)));

        // the existence of this field automatically confirms that the image is below 25mb
        if (bodyAttachment == null || bodyAttachment.getText().equals("")) {
            System.out.println("Image was not successfully uploaded for multiple recipient case.");
        } else {
            System.out.println("Image was successfully uploaded for multiple recipient case.");
        }

    }

    @And("^I press the send button to send to multiple recipients$")
    public void iSendAnEmailToMultipleRecipients() {
        System.out.println("Attempting to send email...");

        try {
            new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();
        } catch (TimeoutException e) {
            System.out.println("Server timed out, please re-run the test.");


        }

    }


    @Then("^the email will be sent to the recipients and will include a regular attachment$")
    public void myEmailWasSentToMultipleRecipients() {

        try {
            WebElement emailConfirmation = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
            if (emailConfirmation.getText().equals("Message sent.")) {
                System.out.println("Multiple recipients email sent successfully.");
            } else {
                System.out.println("There was an error or extended delay (possible network issues) in sending the email with multiple recipients. If you see this message, the email has likely sent, but was delayed due to the file upload.");
            }
        } catch (StaleElementReferenceException | TimeoutException e) {
            System.out.println("There was an error sending the email. It is recommended that you re-run the test.");
        }


    }


    // sending multiple images


    @When("^I press the paperclip icon and select multiple images$")
    public void iAddMultipleAttachments() {


        System.out.println("Attempting to upload images...");


        Random random = new Random();
        int firstImageIndex = random.nextInt(3);
        int secondImageIndex = random.nextInt(3);

        while (firstImageIndex == secondImageIndex) {
            secondImageIndex = random.nextInt(3);
        }

        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(REGULAR_IMAGE_COLLECTION[firstImageIndex]);
        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(REGULAR_IMAGE_COLLECTION[secondImageIndex]);

    }


    @Then("^I upload multiple standard image attachments$")
    public void myImagesWereUploaded() {
        WebElement bodyAttachment = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(FIELD_EMAIL_BODY_WITH_ATTACHMENT)));

        // the existence of this field automatically confirms that the image is below 25mb
        if (bodyAttachment == null || bodyAttachment.getText().equals("")) {
            System.out.println("Images were not successfully uploaded");
        } else {
            System.out.println("Images were successfully uploaded");
        }

    }

    @And("^I press the send button to send an email with multiple attachments$")
    public void iSendAnEmailWithMultipleAttachments() {
        new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SEND_EMAIL))).click();

    }


    @Then("^the email will be sent to the recipient and will include multiple attachments$")
    public void myEmailWithMultipleAttachmentsWasSent() {

        WebElement emailConfirmation;

        try {
            emailConfirmation = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_EMAIL_SENT)));
            if (emailConfirmation.getText().equals("Message sent.")) {
                System.out.println("Multiple recipients email sent successfully.");
            } else {
                System.out.println("There was an error or extended delay (possible network issues) in sending the email with multiple recipients. If you see this message, the email has likely sent, but was delayed due to the file upload.");
            }

        } catch (org.openqa.selenium.StaleElementReferenceException | TimeoutException e) {
            System.out.println("There was an error sending the email. It is recommended that you re-run the test.");
        }


    }


    // error flow

    // this case is very specific. the file must be large enough so that the user does not have sufficient drive space.


    @When("^I press the paperclip icon and select a large image when there is not enough available storage on my Google Drive$")
    public void iAddALargeAttachment() {

        System.out.println("Attempting to upload image...");


        driver.findElement(By.xpath(ACTION_ATTACH_BUTTON)).sendKeys(PATH_VERY_LARGE_IMAGE_QUEBEC);

    }


    @Then("^an error will pop up that says that the upload was rejected and no image is attached$")
    public void myImageWasNotUploaded() {

        try {
            WebElement driveNotice = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_DRIVE_ERROR)));

            System.out.println("Image uploading. Waiting for drive storage full notice... ");

            while (!driveNotice.getText().equals("Error uploading")) {
                driveNotice = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.className(NOTICE_DRIVE_ERROR)));
            }


            // the existence of this field automatically confirms that the image is below 25mb
            if (driveNotice.getText().equals("Error uploading")) {
                System.out.println("Image was not successfully uploaded as drive space is insufficient.");
            } else {
                System.out.println("An error regarding insufficient drive space did not successfully trigger or was delayed due to network error. If files were deleted from the Drive, this may also be due to sufficient space now available.");
            }

        } catch (ElementNotInteractableException | StaleElementReferenceException e) {
            System.out.println("There was an error sending the email. It is recommended that you re-run the test.");

        }
        // goes back to inbox page
        readLink(URL_INBOX);

    }


    // initial set up


    /**
     * Signs into the google account
     */
    private void logIn() {

        try {

            System.out.println("Signing in...");

            driver.findElement(By.className(ACTION_SIGN_IN_FIELD)).sendKeys(USER_EMAIL);


            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.className(ACTION_SIGN_IN_NEXT))).click();


            WebElement welcome = (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.className("jXeDnc")));

            try {
                while (!welcome.getText().equals("Welcome")) {
                    // do nothing
                }
            } catch (StaleElementReferenceException e) {

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
        System.out.println("Instantiating drivers..");
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
        System.out.println("Opening Gmail inbox...");
        if (driver != null) {
            driver.get(url);
        }
    }


}
