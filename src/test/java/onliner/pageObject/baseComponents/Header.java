package onliner.pageObject.baseComponents;

import framework.elements.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class Header {

    private static final String NAV_MENU_ITEM = "//span[@class='b-main-navigation__text' and text()='%s']";

    @Step("Open Catalogue")
    public void mainMenuNavigation(String item) {
        Label lblNavMenuSection = new Label(By.xpath(String.format(NAV_MENU_ITEM, item)));
        lblNavMenuSection.clickAndWait();
    }


}