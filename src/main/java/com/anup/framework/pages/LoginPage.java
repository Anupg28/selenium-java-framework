package com.anup.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(css = "input[data-qa='login-email']")
    private WebElement loginEmailInput;

    @FindBy(css = "input[data-qa='login-password']")
    private WebElement loginPasswordInput;

    @FindBy(css = "button[data-qa='login-button']")
    private WebElement loginButton;

    @FindBy(css = "form[action='/login'] p")
    private WebElement loginErrorMessage;

    @FindBy(css = "input[data-qa='signup-name']")
    private WebElement signupNameInput;

    @FindBy(css = "input[data-qa='signup-email']")
    private WebElement signupEmailInput;

    @FindBy(css = "button[data-qa='signup-button']")
    private WebElement signupButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String email, String password) {
        type(loginEmailInput, email);
        type(loginPasswordInput, password);
        click(loginButton);
        return new HomePage(driver);
    }

    public String getLoginErrorMessage() {
        return getText(loginErrorMessage);
    }

    public SignupPage signup(String name, String email) {
        type(signupNameInput, name);
        type(signupEmailInput, email);
        click(signupButton);
        return new SignupPage(driver);
    }
}
