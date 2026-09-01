package com.anup.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ProductsPage extends BasePage {

    @FindBy(id = "search_product")
    private WebElement searchInput;

    @FindBy(id = "submit_search")
    private WebElement searchButton;

    @FindBy(css = "h2.title.text-center")
    private WebElement searchedProductsHeading;

    @FindBy(css = "div.product-image-wrapper")
    private List<WebElement> productCards;

    @FindBy(css = "div.product-image-wrapper .productinfo p")
    private List<WebElement> productNames;

    @FindBy(css = "#cartModal a[href='/view_cart']")
    private WebElement viewCartModalLink;

    @FindBy(css = "#cartModal button.close-modal")
    private WebElement continueShoppingButton;

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage searchProduct(String productName) {
        type(searchInput, productName);
        click(searchButton);
        waitForVisible(searchedProductsHeading);
        return this;
    }

    public String getSearchResultsHeadingText() {
        return getText(searchedProductsHeading);
    }

    public int getVisibleProductCount() {
        return productCards.size();
    }

    public boolean anyProductNameContains(String keyword) {
        return productNames.stream()
                .map(WebElement::getText)
                .anyMatch(text -> text.toLowerCase().contains(keyword.toLowerCase()));
    }

    public ProductsPage addProductToCartByIndex(int index) {
        WebElement card = productCards.get(index);
        WebElement addToCartButton = card.findElement(By.cssSelector(".product-overlay .add-to-cart"));
        // Element is only clickable on hover; scroll it into view first.
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartButton);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", addToCartButton);
        waitForVisible(viewCartModalLink);
        return this;
    }

    public CartPage goToCartFromModal() {
        click(viewCartModalLink);
        return new CartPage(driver);
    }

    public ProductsPage continueShoppingFromModal() {
        click(continueShoppingButton);
        return this;
    }
}
