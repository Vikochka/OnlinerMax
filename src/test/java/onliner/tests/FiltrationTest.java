package onliner.tests;

import framework.BaseTest;
import io.qameta.allure.Description;
import onliner.pageObject.pages.*;
import org.testng.annotations.*;

public class FiltrationTest extends BaseTest {
    @Test
    @Description("Test description")
    @Parameters({"manufacturer", "resolution", "priceTo", "diagonalFrom", "diagonalTo"})
    public void checkFiltration(String manufacturer, String resolution, double priceTo, String diagonalFrom, String diagonalTo) throws InterruptedException {
        HomePage homePage = new HomePage();
        homePage.header.mainMenuNavigation("Каталог");

        CataloguePage cataloguePage = new CataloguePage("Каталог");
        cataloguePage.catalogueNavigation("Электроника");
        cataloguePage.clickOnItemGroup("Телевидение");
        cataloguePage.clickOnSubItemGroup("Телевизоры");

        TVPage tvPage = new TVPage("Телевизоры");
        tvPage.selectManufacture(manufacturer);
        tvPage.setPriceTo(priceTo);
        tvPage.selectResolution(resolution);
        tvPage.selectFromDiagonal(diagonalFrom);
        tvPage.selectToDiagonal(diagonalTo);
        tvPage.validationOfAllFilters(manufacturer, resolution, priceTo, diagonalFrom, diagonalTo);
    }
}