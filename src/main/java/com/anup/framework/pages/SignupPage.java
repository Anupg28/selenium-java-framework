package com.anup.framework.pages;

import com.anup.framework.model.AccountDetails;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends BasePage {

    @FindBy(id = "id_gender1")
    private WebElement titleMrRadio;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "days")
    private WebElement daysDropdown;

    @FindBy(id = "months")
    private WebElement monthsDropdown;

    @FindBy(id = "years")
    private WebElement yearsDropdown;

    @FindBy(id = "first_name")
    private WebElement firstNameInput;

    @FindBy(id = "last_name")
    private WebElement lastNameInput;

    @FindBy(id = "address1")
    private WebElement addressInput;

    @FindBy(id = "country")
    private WebElement countryDropdown;

    @FindBy(id = "state")
    private WebElement stateInput;

    @FindBy(id = "city")
    private WebElement cityInput;

    @FindBy(id = "zipcode")
    private WebElement zipcodeInput;

    @FindBy(id = "mobile_number")
    private WebElement mobileNumberInput;

    @FindBy(css = "button[data-qa='create-account']")
    private WebElement createAccountButton;

    @FindBy(css = "h2[data-qa='account-created']")
    private WebElement accountCreatedHeading;

    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public SignupPage fillAccountInformation(AccountDetails details) {
        click(titleMrRadio);
        type(passwordInput, details.password());
        new Select(daysDropdown).selectByValue(details.birthDay());
        new Select(monthsDropdown).selectByValue(details.birthMonth());
        new Select(yearsDropdown).selectByValue(details.birthYear());

        type(firstNameInput, details.firstName());
        type(lastNameInput, details.lastName());
        type(addressInput, details.address());
        new Select(countryDropdown).selectByVisibleText(details.country());
        type(stateInput, details.state());
        type(cityInput, details.city());
        type(zipcodeInput, details.zipcode());
        type(mobileNumberInput, details.mobileNumber());
        return this;
    }

    public SignupPage clickCreateAccount() {
        click(createAccountButton);
        return this;
    }

    public boolean isAccountCreated() {
        return isDisplayed(accountCreatedHeading);
    }

    public HomePage clickContinue() {
        click(continueButton);
        return new HomePage(driver);
    }
}
