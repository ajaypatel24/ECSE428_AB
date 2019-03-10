Feature: Sending email with image attachment action 

    Scenario: Sending an email with a regular image attachment 
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter a recipient, a subject and the email body
        When I press the paperclip icon and select a standard image
        Then I upload an attachment that is under maximum size limit
        And I press the send button  
        Then the email will be sent to the recipient and will include a regular attachment

    Scenario: Sending an email with a large image attachment
        Given I am a user on the Gmail page
        And I click the compose button 
        And I enter a recipient, a subject and the email body
        When I press the paperclip icon and select an image whose size is over the maximum limit   
        Then my image will be uploaded to Google Drive and a link to it will be provided in the email
        And I press the send button to send the large attachment 
        Then the email will be sent to the recipient and will include access to the attachment via Google Drive


    Scenario: Sending an email with a regular image attachment to multiple recipients
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter more than one recipient, a subject and the email body
        When I press the paperclip icon and select an image to send
        Then I upload an attachment
        And I press the send button to send to multiple recipients  
        Then the email will be sent to the recipients and will include a regular attachment

    Scenario: Sending an email with multiple regular image attachments 
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter a recipient, a subject and the email body
        When I press the paperclip icon and select multiple images
        Then I upload multiple standard image attachments
        And I press the send button to send an email with multiple attachments
        Then the email will be sent to the recipient and will include multiple attachments

    Scenario: Sending an email with a large attachment with not enough space on Google Drive
        Given I am a user on the Gmail page
        And I click the compose button
        And I enter a recipient, a subject and the email body
        When I press the paperclip icon and select a large image when there is not enough available storage on my Google Drive
        Then an error will pop up that says that the upload was rejected and no image is attached 
