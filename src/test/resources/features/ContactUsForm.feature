@ContactUsForm
Feature: Contact Us Form

  Background: User is on the Event Management System homepage
    Given the user navigates to the Event Management System homepage
    
  @Regression @Smoke @TC_011
  Scenario: Verify successful submission of the Contact Us form with valid data
    When the user enters valid data for the contact us form from "ContactUsData" and row 1
    And the user clicks the "Send Message Now" button
    Then a contact success message "Your message has been sent !" should be displayed

  @Regression @Negative @TC_012
  Scenario: Verify validation messages when submitting empty Contact Us form
  When the user enters contact form data with empty fields from "ContactUsData" and row 2
  And the user clicks the "Send Message Now" button
  Then appropriate validation messages should be displayed for all required contact fields

