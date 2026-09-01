package com.anup.framework.tests;

import com.anup.framework.base.BaseTest;
import com.anup.framework.pages.CartPage;
import com.anup.framework.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.anup.framework.listeners.TestListener.class)
public class AddToCartTest extends BaseTest {

    @Test(description = "Adding a product from the products page places it in the cart")
    public void addingProductShowsItInCart() {
        ProductsPage productsPage = homePage.goToProductsPage();
        productsPage.addProductToCartByIndex(0);

        CartPage cartPage = productsPage.goToCartFromModal();
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Expected exactly one item in the cart after adding one product");
    }

    @Test(description = "Adding two different products results in two cart line items")
    public void addingTwoProductsResultsInTwoCartItems() {
        ProductsPage productsPage = homePage.goToProductsPage();
        productsPage.addProductToCartByIndex(0);
        productsPage.continueShoppingFromModal();
        productsPage.addProductToCartByIndex(1);

        CartPage cartPage = productsPage.goToCartFromModal();
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Expected two items in the cart after adding two distinct products");
    }
}
