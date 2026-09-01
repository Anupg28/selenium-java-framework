package com.anup.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PaymentPage extends BasePage {

    @FindBy(css = "input[data-qa='name-on-card']")
    private WebElement nameOnCardInput;

    @FindBy(css = "input[data-qa='card-number']")
    private WebElement cardNumberInput;

    @FindBy(css = "input[data-qa='cvc']")
    private WebElement cvcInput;

    @FindBy(css = "input[data-qa='expiry-month']")
    private WebElement expiryMonthInput;

    @FindBy(css = "input[data-qa='expiry-year']")
    private WebElement expiryYearInput;

    @FindBy(css = "button[data-qa='pay-button']")
    private WebElement payAndConfirmButton;

    @FindBy(css = "h2[data-qa='order-placed']")
    private WebElement orderPlacedHeading;

    @FindBy(css = "#success_message .alert-success, div.alert-success p")
    private WebElement orderConfirmationMessage;

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public PaymentPage fillPaymentDetails(String nameOnCard, String cardNumber, String cvc,
                                           String expiryMonth, String expiryYear) {
        type(nameOnCardInput, nameOnCard);
        type(cardNumberInput, cardNumber);
        type(cvcInput, cvc);
        type(expiryMonthInput, expiryMonth);
        type(expiryYearInput, expiryYear);
        return this;
    }

    public PaymentPage confirmOrder() {
        click(payAndConfirmButton);
        return this;
    }

    public boolean isOrderPlaced() {
        return isDisplayed(orderPlacedHeading);
    }

    public String getOrderPlacedHeadingText() {
        return getText(orderPlacedHeading);
    }
}
