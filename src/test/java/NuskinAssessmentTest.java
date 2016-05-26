import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import testSupport.SpreadSheetData;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


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

   @DataProvider (name = "GetExcelData")
   public Object[][] getData(Method method) {

      SpreadSheetData spreadSheetData = new SpreadSheetData();
      Object [][] data;
      data = new Object[][]{new String[]{""}};
      // Retrieve data from spreadSheet depending on what parameter is specified
      if (method.getName().equals("accessTestAssessment")) {
         try {
            data = spreadSheetData.getData(excelFilePath, "Language");
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      return data;
   }

   @Test(dataProvider = "GetExcelData")
   public void accessTestAssessment(String parameter, String language) {
      String title = "";
      if (language.equals("English")) {
         driver.get("https://www.nuskin.com/content/nuskin/en_BE/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "ageLOC Me Assessment");
      } else if (language.equals("Czech")) {
         driver.get("https://www.nuskin.com/content/nuskin/cs_CZ/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "Analýza pleti ageLOC Me");
      } else if (language.equals("Danish")) {
         driver.get(" https://www.nuskin.com/content/nuskin/da_DK/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "ageLOC Me Vurdering");
      }

      System.out.println("The title of the page is: " + title);
   }

   @AfterTest
   public void closeSelenium() {
      // Shutdown the browser
      driver.close();
   }
}