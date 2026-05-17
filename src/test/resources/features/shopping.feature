@shopping @regression
Feature: Shopping Cart and Checkout
  As a logged-in user
  I want to add products to cart and complete checkout
  So that I can purchase items

  Background:
    Given I am logged in as "standard_user" with password "secret_sauce"
    And I am on the home page

  @smoke @cart
  Scenario: Add a single product to cart
    When I add product "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"
    And the cart should contain "Sauce Labs Backpack"

  @cart
  Scenario: Add multiple products to cart
    When I add product "Sauce Labs Backpack" to the cart
    And I add product "Sauce Labs Bike Light" to the cart
    Then the cart badge should show "2"

  @cart
  Scenario: Remove a product from cart
    When I add product "Sauce Labs Backpack" to the cart
    And I remove product "Sauce Labs Backpack" from the cart
    Then the cart badge should not be visible

  @sort
  Scenario Outline: Sort products on home page
    When I sort products by "<sortOption>"
    Then the products should be sorted by "<sortOption>"

    Examples:
      | sortOption          |
      | Name (A to Z)       |
      | Name (Z to A)       |
      | Price (low to high) |
      | Price (high to low) |

  @checkout @smoke
  Scenario: Complete full checkout flow
    When I add product "Sauce Labs Backpack" to the cart
    And I go to the cart
    And I proceed to checkout
    And I fill shipping details with first name "John" last name "Doe" postal code "12345"
    And I click continue
    And I click finish
    Then the order should be complete with message "Thank you for your order!"

  @product
  Scenario: View product details
    When I click on product "Sauce Labs Backpack"
    Then I should see the product detail page
    And the product name should be "Sauce Labs Backpack"
    And the product image should be displayed
