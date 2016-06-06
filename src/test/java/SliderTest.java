import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by heidy.cespedes on 6/1/2016.
 */
public class SliderTest {
   RemoteWebDriver driver;

   @BeforeTest
   public void setUp(){
      driver = new FirefoxDriver();
      driver.get("https://www.nuskin.com/content/nuskin/en_BE/ageloc-me-assessment.html#/skin-sensitivity");
   }

   @Test
   public void sliderTest(){
      WebElement knob = driver.findElement(By.xpath("//div[@class='portal']//div[@class='slide']//canvas"));
      WebElement slider = driver.findElement(By.xpath("//div[@class='portal']//div[@class='slide']//input[@style]"));
      Actions move = new Actions(driver);
      System.out.println("The size of the slider is " + slider.getSize());
      driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
            slider, "style", "display: ; width: 55px; visibility: visible;");

      slider.sendKeys("\b");
      slider.sendKeys("30");
      slider.sendKeys(Keys.RETURN);
      knob.click();

      //Action action = (Action) move.dragAndDropBy(slider, 250, 100).build();
      //action.perform();

      try {
         Thread.sleep(20000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

   }

   @AfterTest
   public void tearDown(){
      driver.close();
   }
}
