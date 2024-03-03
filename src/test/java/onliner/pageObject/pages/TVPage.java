package onliner.pageObject.pages;

import framework.Browser;
import framework.elements.*;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

public class TVPage extends BaseOnlinerPage {
    SoftAssert softAssert = new SoftAssert();
    private static final String PAGE_LOCATOR = "//h1[@class='catalog-form__title catalog-form__title_big-alter' " +
            "and contains(., '%s')]";
    private static final String MANUFACTURE = "//div[@class='catalog-form__checkbox-sign' and text()='%s']";
    private static final String CHECK_RESOLUTION = "//div[@class='catalog-form__checkbox-sign' and text()='%s']";
    private static final String APPLIED_FILTER = "//div[@class='catalog-form__tag-list']//div[text()='%s']";
    public static final TextBox SET_PRICE_TO = new TextBox(By.xpath("//input[@type='text' and @placeholder='до']"));
    private static final TextBox CB_DIAGONAL_FROM = new TextBox(By.xpath("//div[contains(text(),'Диагональ')]" +
            "//ancestor::div//child::div[contains(@class,'catalog-form__input-combo_width_full')]/div[1]//select"));
    private static final TextBox TXB_DIAGONAL_TO = new TextBox(By.xpath("//div[contains(text(),'Диагональ')]" +
            "//ancestor::div//child::div[contains(@class,'catalog-form__input-combo_width_full')]/div[2]//select"));
    private static final TextBox TXT_ITEM = new TextBox(By.xpath("//div[@class='catalog-form__offers-item " +
            "catalog-form__offers-item_primary']//div[contains(@class,'catalog-form__description_base-additional')]/a"));
    private static final TextBox TXT_DESCRIPTION_FIRST_LINE = new TextBox((By.xpath(
            "//div[@class='catalog-form__offers-item catalog-form__offers-item_primary']" +
                    "//div[contains(@class,'catalog-form__parameter-part_1')]/div[1]")));
    private static final TextBox PRICES = new TextBox(By.xpath(
            "//div[contains(@class,'offers-item_primary')]//div[contains(@class,'weight_bold')]"));

    public TVPage(String pageTitle) {
        super(By.xpath(String.format(PAGE_LOCATOR, pageTitle)), pageTitle);
    }

    @Step("Check manufacturer checkbox")
    public void selectManufacture(String manufacture) {
        CheckBox checkFilter = new CheckBox(By.xpath(String.format(MANUFACTURE, manufacture)));
        checkFilter.scrollIntoView();
        checkFilter.clickViaJS();
    }

    @Step("Set price TO")
    public void setPriceTo(double priceTo) {
        SET_PRICE_TO.sendKeys(String.valueOf(priceTo));
    }

    @Step("Check resolution checkbox")
    public void selectResolution(String resolution) {
        CheckBox chbResolution = new CheckBox(By.xpath(String.format(CHECK_RESOLUTION, resolution)));
        chbResolution.scrollIntoView();
        chbResolution.clickViaJS();
    }

    @Step("Set diagonal From")
    public void selectFromDiagonal(String diagonalFrom) {
        CB_DIAGONAL_FROM.selectByVisibleText(diagonalFrom);
    }

    @Step("Set diagonal To")
    public void selectToDiagonal(String diagonalTo) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TXB_DIAGONAL_TO.selectByValue(String.valueOf(Integer.parseInt(diagonalTo.replace("\"", "")) * 10));
    }

    @Step("Check if manufacturer filter applied")
    public void checkManufacturerFilterApplied(String manufacture) {
        TextBox textBox = new TextBox(By.xpath(String.format(APPLIED_FILTER, manufacture)));
        softAssert.assertTrue(textBox.isDisplayed(),
                String.format("Expected result: %s. Actual result: %b", manufacture, textBox.isDisplayed()));
    }

    @Step("Check if resolution filter applied")
    private void checkResolutionFilterApplied(String resolution) {
        TextBox textBox = new TextBox(By.xpath(String.format(APPLIED_FILTER, resolution)));
        softAssert.assertTrue(textBox.isDisplayed(),
                String.format("Expected result: %s. Actual result: %b", resolution, textBox.isDisplayed()));
    }

    @Step("Assertion: manufacturer is equal to parameter")
    private void checkIfManufacturerMatch(String manufacturer) {
        List<WebElement> listOFItems = TXT_ITEM.getElements();
        for (WebElement w : listOFItems) {
            softAssert.assertTrue(w.getText().contains(manufacturer),
                    "Expected result: " + manufacturer + ". Actual result: " + w.getText());
        }
    }

    @Step("Assertion: Resolution is equal to parameter")
    private void checkResolution(String resolution) {
        List<String> FILTERED_DIAGONAL_AND_RESOLUTION = TXT_DESCRIPTION_FIRST_LINE.getElements().stream().map(s -> s.getText()).collect(Collectors.toList());
        for (String w : FILTERED_DIAGONAL_AND_RESOLUTION) {
            softAssert.assertTrue(w.contains(resolution),
                    "Expected result: " + resolution + ". Actual result: " + w);
        }
    }

    @Step("Assertion: Diagonal is equal to parameter")
    private void checkDiagonal(String diagonalFrom, String diagonalTo) {
        List<WebElement> FILTERED_DIAGONAL_AND_RESOLUTION = TXT_DESCRIPTION_FIRST_LINE.getElements();
        for (WebElement w : FILTERED_DIAGONAL_AND_RESOLUTION) {
            double diagonal = Double.parseDouble(w.getText().substring(0, 2).replace("\"", ""));
            double dDTo = Double.parseDouble(diagonalTo.replace("\"", ""));
            double dDFrom = Double.parseDouble(diagonalFrom.replace("\"", ""));
            softAssert.assertTrue(diagonal >= dDFrom && diagonal <= dDTo,
                    String.format("Diagonal does not correspond to the specified range. Expected: from %s to %s but found %.1f", diagonalFrom, diagonalTo, diagonal));
        }
    }

    @Step("Assertion: check prices")
    private void checkPrices(double priceTo) {
        List<String> list = PRICES.getElements().stream().map(s -> s.getText()).collect(Collectors.toList());
        for (String w : list) {
            double prices = Double.parseDouble(w.replaceAll("[\\s.а-я]", "").replaceAll(",", ".").trim());
            System.out.println(prices);
            softAssert.assertTrue(prices <= priceTo, "Expected result: " + priceTo + ". Actual result: " + prices);
        }
    }

    public void validationOfAllFilters(String manufacture, String resolution, double price, String diagonalFrom, String diagonalTo) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        checkManufacturerFilterApplied(manufacture);
        checkResolutionFilterApplied(resolution);
        checkIfManufacturerMatch(manufacture);
        checkResolution(resolution);
        checkDiagonal(diagonalFrom, diagonalTo);
        checkPrices(price);
        softAssert.assertAll();
    }
}
