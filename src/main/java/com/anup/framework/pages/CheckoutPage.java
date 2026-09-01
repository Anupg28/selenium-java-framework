package com.anup.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(css = "textarea[name='message']")
    private WebElement orderCommentTextArea;

    @FindBy(css = "a[href='/payment']")
    private WebElement placeOrderButton;

    @FindBy(css = "#address_delivery")
    private WebElement deliveryAddressBlock;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterOrderComment(String comment) {
        type(orderCommentTextArea, comment);
        return this;
    }

    public String getDeliveryAddressText() {
        return getText(deliveryAddressBlock);
    }

    public PaymentPage placeOrder() {
        click(placeOrderButton);
        return new PaymentPage(driver);
    }
}
