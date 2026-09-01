package com.anup.framework.tests;

import com.anup.framework.base.BaseTest;
import com.anup.framework.model.AccountDetails;
import com.anup.framework.pages.HomePage;
import com.anup.framework.pages.LoginPage;
import com.anup.framework.pages.SignupPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.anup.framework.listeners.TestListener.class)
public class LoginTest extends BaseTest {

    @Test(description = "New user can register an account and lands logged in on the home page")
    public void newUserCanSignUpAndIsLoggedIn() {
        AccountDetails newUser = AccountDetails.randomTestUser();

        LoginPage loginPage = homePage.goToLoginPage();
        SignupPage signupPage = loginPage.signup(newUser.name(), newUser.email());

        signupPage.fillAccountInformation(newUser).clickCreateAccount();
        Assert.assertTrue(signupPage.isAccountCreated(), "Account created confirmation was not shown");

        HomePage home = signupPage.clickContinue();
        Assert.assertTrue(home.isUserLoggedIn(), "User should be logged in immediately after signup");
    }

    @Test(description = "Login with an invalid email/password combination shows a validation error")
    public void invalidLoginShowsErrorMessage() {
        LoginPage loginPage = homePage.goToLoginPage();
        loginPage.login("not-a-real-user-" + System.currentTimeMillis() + "@mailinator.com", "WrongPassword123");

        String errorMessage = loginPage.getLoginErrorMessage();
        Assert.assertTrue(
                errorMessage.toLowerCase().contains("incorrect"),
                "Expected an 'incorrect email or password' style error, but got: " + errorMessage);
    }
}
