package onliner.pageObject.pages;

import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CataloguePage extends BaseOnlinerPage {

    private static final String PAGE_LOCATOR = "//div[@class='catalog-navigation__title' and text()='%s']";
    private static final String NAV_CATALOG_ITEM = "//span[@class='catalog-navigation-classifier__item-title-wrapper' and text()='%s']";

    private static final String NAV_CATALOGUE_SUBITEM = "//div[@class='catalog-navigation-list__aside-item' and contains(., '%s')]";

    private static final String NAV_SUBITEM_GROUP = "//div[@data-id='1']//span[@class='catalog-navigation-list__dropdown-title' and contains(., '%s')]";

    public CataloguePage(String title) {
        super(By.xpath(String.format(PAGE_LOCATOR,title)), "Catalogue Page");
    }

    @Step("Navigate to Item group")
    public void catalogueNavigation(String item) {
        Label lblCatalogueSection = new Label(By.xpath(String.format(NAV_CATALOG_ITEM, item)));
        lblCatalogueSection.click();
    }

    @Step("Click on item group")
    public void clickOnItemGroup(String itemGroup) {
        Label lblItemGroup = new Label(By.xpath(String.format(NAV_CATALOGUE_SUBITEM, itemGroup)));
        lblItemGroup.click();
    }

    @Step("Click on subItem")
    public void clickOnSubItemGroup(String subItemGroup) {
        Label lblSubItemGroup = new Label(By.xpath(String.format(NAV_SUBITEM_GROUP, subItemGroup)));
        lblSubItemGroup.click();
    }
}