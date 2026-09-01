package com.anup.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = "tr[id^='product-']")
    private List<WebElement> cartRows;

    @FindBy(css = "a.check_out")
    private WebElement proceedToCheckoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return cartRows.size();
    }

    public boolean isProductInCart(String productName) {
        return cartRows.stream()
                .anyMatch(row -> row.findElement(By.cssSelector("td.cart_description h4 a"))
                        .getText()
                        .toLowerCase()
                        .contains(productName.toLowerCase()));
    }

    public String getQuantityForRow(int rowIndex) {
        WebElement row = cartRows.get(rowIndex);
        return row.findElement(By.cssSelector("td.cart_quantity button")).getText();
    }

    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckoutButton);
        return new CheckoutPage(driver);
    }
}
