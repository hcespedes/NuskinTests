import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import testSupport.SpreadSheetData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class NuskinAssessmentTest {

   // Declare the browser
   WebDriver driver;
   private Properties props = new Properties();
   private String excelFilePath;

   @BeforeTest
   public void setupSelenium() {
      // Start the browser (Firefox in this case)
      driver = new FirefoxDriver();
      // Adds implicit timeouts to the driver
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      // Get the excel file name which contains the parameters
      try {
         props.load(new FileInputStream("c://Users/heidy.cespedes/IdeaProjects/NuskinTests/src/main/resources/parameters.properties"));
         excelFilePath = props.getProperty("excelFile");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testAssessment() {
      driver.get("https://www.nuskin.com/content/nuskin/en_BE/ageloc-me-assessment.html#/home");
      String title = driver.getTitle();
      Assert.assertEquals(title, "ageLOC Me Assessment");
      System.out.println("The title of the page is: " + title);

      SpreadSheetData spreadSheetData = new SpreadSheetData();
      //Read data from spreadSheet
      Object [][] data = null;
      // Trying the spreadsheet
      try {
         data = spreadSheetData.getData(excelFilePath);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @AfterTest
   public void closeSelenium() {
      // Shutdown the browser
      driver.close();
   }
}