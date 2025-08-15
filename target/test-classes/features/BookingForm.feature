@BookingForm
Feature: Event Booking Form

  Background: User is on the Event Management System homepage
    Given the user navigates to the Event Management System homepage

  @Regression @Smoke @TC_002
  Scenario: Verify successful booking with valid data
    When the user enters valid data for the booking form from "BookingData" and row 1
    And the user clicks the "Book Now" button
    Then a success message "Your Booking has been Confirmed !" should be displayed

  @Regression @Negative @TC_005
  Scenario: Verify email field validation with invalid format
    When the user enters booking form data with an invalid email from "BookingData" and row 3
    And the user clicks the "Book Now" button
    Then an appropriate validation message should be displayed for the invalid field

  @Regression @Negative @TC_007
  Scenario: Verify error on empty required fields
  	When the user enters booking form data with empty fields from "BookingData" and row 2
    And the user clicks the "Book Now" button
    Then appropriate error messages should be displayed for all required booking fields
   
    @Regression @Negative @TC_009
  Scenario: Verify phone number validation with invalid format
    When the user enters booking form data with an invalid phone number from "BookingData" and row 4
    And the user clicks the "Book Now" button
    Then an appropriate validation message should be displayed for the invalid field
   
    
