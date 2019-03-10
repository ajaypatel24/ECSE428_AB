Feature: Sending email with image attachment action 

    Scenario: Sending an email with a regular image attachment 
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter a recipient and subject
        When I press the paperclip icon and select an image
        Then I upload an attachment that is under maximum size limit
        And I press the send button  
        Then the email will be sent to the recipient and will include a regular attachment

    Scenario: Sending an email with a large image attachment
        Given I am a user on the Gmail page
        And I click the compose button 
        And I enter a recipient and subject
        When I press the paperclip icon and select an image whose size is over the maximum limit   
        Then my image will be uploaded to Google Drive and a link to it will be provided in the email
        And I press the send button to send the large attachment 
        Then the email will be sent to the recipient and will include access to the attachment via Google Drive