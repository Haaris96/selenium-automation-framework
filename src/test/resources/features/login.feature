@login @regression
Feature: Login Functionality
  As a registered user
  I want to log into the application
  So that I can access my account

  Background:
    Given I am on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be on the home page
    And the page heading should be "Products"

  @negative
  Scenario: Login fails with invalid username
    When I enter username "invalid_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"

  @negative
  Scenario: Login fails with locked out user
    When I enter username "locked_out_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Sorry, this user has been locked out."

  @negative
  Scenario: Login fails with empty credentials
    When I click the login button
    Then I should see an error message "Epic sadface: Username is required"

  @smoke @positive
  Scenario Outline: Login with multiple valid users
    When I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should be on the home page

    Examples:
      | username          | password     |
      | standard_user     | secret_sauce |
      | problem_user      | secret_sauce |
      | performance_glitch_user | secret_sauce |
