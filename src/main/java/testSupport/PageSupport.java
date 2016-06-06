package testSupport;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by heidy.cespedes on 6/1/2016.
 */
public class PageSupport {

      public boolean clickOn(WebDriver driver, WebElement currentElement, String elementName, String fileName) {
      // Try to find the element a few times while it loads
      int counter = 0;
      while ((!currentElement.isDisplayed()) && (counter < 20)) {
         counter++;
      }
      // Click on the element once it is present
      if (currentElement.isDisplayed()) {
         currentElement.click();
         return true;
      } else {
         System.out.println("The " + elementName + " is not available");
         CaptureScreenShot.takeScreenShot(driver, fileName);
         return false;
      }
   }

   public boolean fillOut(WebDriver driver, WebElement currentElement, String data, String elementName, String fileName) {
      // Try to find the element a few times while it loads
      int counter = 0;
      while ((!currentElement.isDisplayed()) && (counter < 20)) {
         counter++;
      }
      // Fill out form once the element is present
      if (currentElement.isDisplayed()) {
         currentElement.sendKeys(data);
         currentElement.sendKeys(Keys.RETURN);
         return true;
      } else {
         System.out.println("The " + elementName + " is not available");
         CaptureScreenShot.takeScreenShot(driver, fileName);
         return false;
      }
   }

   public WebElement findElement(WebDriver driver, String xpath) {
      int counter = 0;
      WebElement element = null;
      while (element == null) {
         element = driver.findElement(By.xpath(xpath));
         counter++;
         if (counter == 20) {
            break;
         }
      }
      return element;
   }
}
