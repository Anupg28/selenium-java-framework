package com.anup.framework.tests;

import com.anup.framework.base.BaseTest;
import com.anup.framework.model.AccountDetails;
import com.anup.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.anup.framework.listeners.TestListener.class)
public class CheckoutTest extends BaseTest {

    @Test(description = "A logged-in user can add a product, check out, pay, and receive an order confirmation")
    public void loggedInUserCanCompleteCheckout() {
        AccountDetails newUser = AccountDetails.randomTestUser();

        LoginPage loginPage = homePage.goToLoginPage();
        SignupPage signupPage = loginPage.signup(newUser.name(), newUser.email());
        signupPage.fillAccountInformation(newUser).clickCreateAccount();
        HomePage home = signupPage.clickContinue();
        Assert.assertTrue(home.isUserLoggedIn(), "Precondition failed: user was not logged in after signup");

        ProductsPage productsPage = home.goToProductsPage();
        productsPage.addProductToCartByIndex(0);
        CartPage cartPage = productsPage.goToCartFromModal();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Expected one product in cart before checkout");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(
                checkoutPage.getDeliveryAddressText().contains(newUser.firstName()),
                "Delivery address on checkout page did not reflect the account holder's name");

        checkoutPage.enterOrderComment("Automated order placed by Selenium test suite - please ignore.");

        PaymentPage paymentPage = checkoutPage.placeOrder();
        paymentPage.fillPaymentDetails(
                newUser.firstName() + " " + newUser.lastName(),
                "4111111111111111",
                "123",
                "12",
                "2030");
        paymentPage.confirmOrder();

        Assert.assertTrue(paymentPage.isOrderPlaced(), "Order confirmation heading was not displayed");
        Assert.assertEquals(paymentPage.getOrderPlacedHeadingText().trim().toLowerCase(), "order placed!",
                "Unexpected order confirmation heading text");
    }
}
