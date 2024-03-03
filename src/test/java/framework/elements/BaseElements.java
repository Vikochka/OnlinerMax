package framework.elements;

import framework.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static framework.Browser.*;
import static framework.PropertyReader.*;

public abstract class BaseElements {

    private By by;

    private WebElement element;

    private List<WebElement> elements;

    public BaseElements(By by) {
        this.by = by;
    }


    public static String getLoc(final String key) {
        return getProperty(key);
    }

    protected abstract String getElementType();

    public boolean isElementPresent() {
        try {
            getDriver().manage().timeouts().implicitlyWait(new PropertyReader("config.properties").getIntProperty("timeout"), TimeUnit.SECONDS);
            element = getDriver().findElement(by);
            return element.isDisplayed();
        } catch (Exception e) {
            System.out.println(getElementType() + ": " + by + " - is not present. Exception");
            e.printStackTrace();
        }
        return false;
    }

    public boolean areElementsPresent() {
        try {
            getDriver().manage().timeouts().implicitlyWait(new PropertyReader("config.properties").getIntProperty("timeout"), TimeUnit.SECONDS);
            elements = getDriver().findElements(by);
            return !elements.isEmpty();
        } catch (Exception e) {
            System.out.println(getElementType() + ": " + by + " - are not present. Exception");
            e.printStackTrace();
        }
        return false;
    }

    public WebElement getElement() {
        isElementPresent();
        return element;
    }

    public List<WebElement> getElements() {
        areElementsPresent();
        return elements;
    }

    public String getText() {
        isElementPresent();
        return element.getText();
    }

    public void sendKeys(String sendKeys) {
        isElementPresent();
        getElement().sendKeys(sendKeys);
    }

    public void selectByValue(String v) {
        isElementPresent();
        Select select = new Select(getElement());
        select.selectByValue(v);
    }

    public void selectByVisibleText(String value) {
        isElementPresent();
        Select drpDown = new Select(getElement());
        drpDown.selectByVisibleText(value);
    }

    public boolean isSelected() {
        isElementPresent();
        System.out.println((getProperty("log.select") + getText()) + " is selected status: " + element.isSelected());
        return element.isSelected();
    }

    public boolean isDisplayed() {
        isElementPresent();
        return element.isDisplayed();
    }

    public void click() {
        isElementPresent();
        if (getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid orange'", element);
            isElementClickable();
            getElement().click();
        }
        System.out.println(getProperty("log.click") + " : " + getElementType() + " : " + by);
    }

    public void clickViaJS() {
        if (isElementPresent()) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid blue'", element);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
        }
    }

    public void clickAndWait() {
        isElementPresent();
        isElementClickable();
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
        getElement().click();
        waitForPageLoaded();
    }

    public void moveAndClickByAction() {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(getElement())
                .click()
                .perform();
    }

    public void moveToElement() {
        Actions actions = new Actions(getDriver());
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid green'", element);
        actions.moveToElement(getElement());
    }

    public void scrollIntoView() {
        isElementPresent();
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String getAttribute(String attrName) {
        return getElement().getAttribute(attrName);
    }

    public boolean isElementSelectable() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(getIntProperty("element.timeout"))).
                    until(ExpectedConditions.elementToBeSelected(getElement()));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Element isn't selectable:" + getElementType() + ": " + by);
            return false;
        }
    }

    public void waitForElementLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(PropertyReader.getIntProperty("element.load.timeout")));
    }

    public boolean isElementClickable() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(getIntProperty("element.timeout"))).
                    until(ExpectedConditions.elementToBeClickable(by));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Element isn't clickable:" + getElementType() + ": " + by);
            return false;
        }
    }
}