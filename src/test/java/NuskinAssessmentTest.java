import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class NuskinAssessmentTest {

   // Declare the browser
   WebDriver driver;

   @BeforeTest
   public void setupSelenium() {
      // Start the browser (Firefox in this case)
      driver = new FirefoxDriver();
      // Adds implicit timeouts to the driver
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
   }

   @Test
   public void testAssessment() {
      driver.get("https://www.nuskin.com/content/nuskin/en_BE/ageloc-me-assessment.html#/home");
      String title = driver.getTitle();
      Assert.assertEquals(title, "ageLOC Me Assessment");
      System.out.println("The title of the page is: " + title);
   }

   @AfterTest
   public void closeSelenium() {
      // Shutdown the browser
      driver.close();
   }
}