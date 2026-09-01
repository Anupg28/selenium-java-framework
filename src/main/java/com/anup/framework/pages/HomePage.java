package com.anup.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(css = "a[href='/login']")
    private WebElement signupLoginLink;

    @FindBy(css = "a[href='/products']")
    private WebElement productsLink;

    @FindBy(css = "a[href='/view_cart']")
    private WebElement cartLink;

    @FindBy(css = "a[href='/logout']")
    private WebElement logoutLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public LoginPage goToLoginPage() {
        click(signupLoginLink);
        return new LoginPage(driver);
    }

    public ProductsPage goToProductsPage() {
        click(productsLink);
        return new ProductsPage(driver);
    }

    public CartPage goToCartPage() {
        click(cartLink);
        return new CartPage(driver);
    }

    public boolean isUserLoggedIn() {
        return isDisplayed(logoutLink);
    }
}
