Feature: Sending email with image attachment action 

    Scenario: Sending an email with an image attachment 
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter a recipient
        And I press the paperclip icon to add an attachment 
        Then I upload an attachment that is under maximum size limit
        And press the send button  
        Then the email will be sent to the recipient 

    Scenario: Sending an email with a large image attachment
        Given I am a user on the Gmail page
        And I click the compose button 
        And I enter a recipient 
        And I press the paperclip icon to add an attachment 
        Then I upload an attachment that is over the maximum size limit
        Then my image will be uploaded to Google Drive and a link to it will be provided in the email
        Then the email will be sent to the recipient 