package com.anup.framework.tests;

import com.anup.framework.base.BaseTest;
import com.anup.framework.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.anup.framework.listeners.TestListener.class)
public class SearchProductTest extends BaseTest {

    @Test(description = "Searching for a known keyword returns matching products")
    public void searchingForKnownProductReturnsResults() {
        ProductsPage productsPage = homePage.goToProductsPage();
        productsPage.searchProduct("Dress");

        Assert.assertEquals(
                productsPage.getSearchResultsHeadingText().trim().toLowerCase(),
                "searched products",
                "Search results heading was not shown as expected");

        Assert.assertTrue(productsPage.getVisibleProductCount() > 0,
                "Expected at least one product in the search results");

        Assert.assertTrue(productsPage.anyProductNameContains("dress"),
                "None of the returned products contained the searched keyword 'dress'");
    }

    @Test(description = "Searching for a nonsense keyword returns no products")
    public void searchingForUnknownProductReturnsNoResults() {
        ProductsPage productsPage = homePage.goToProductsPage();
        productsPage.searchProduct("zzz_nonexistent_product_zzz");

        Assert.assertEquals(productsPage.getVisibleProductCount(), 0,
                "Expected zero products for a nonsense search term");
    }
}
