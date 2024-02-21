package framework.elements;

import org.openqa.selenium.By;

public class Button extends BaseElements {
    public Button(By by) {
        super(by);
    }

    @Override
    protected String getElementType() {
        return getLoc("log.button");
    }
}