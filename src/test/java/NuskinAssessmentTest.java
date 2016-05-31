import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import testSupport.CaptureScreenShot;
import testSupport.SpreadSheetData;

import javax.xml.crypto.Data;
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
   private Boolean testStatus;

   @BeforeTest
   public void setupSelenium() {
      // Start the browser (Firefox in this case)
      driver = new FirefoxDriver();
      // Adds implicit timeouts to the driver
      //driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
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
      String parameter = "";

      // Define what parameters are needed based on what test step is run
      if (method.getName().equals("accessTestAssessment")) {
         parameter = "Language";
      } else if (method.getName().equals("c_personalInfo")) {
         parameter = "Personal Info (First Name; Age; Female/Male)";
      } else if (method.getName().equals("ethnicity")) {
         parameter = "Ethnicity";
      } else if (method.getName().equals("location")) {
         parameter = "Location";
      } else if (method.getName().equals("chemicalExposure")) {
         parameter = "Chemical Exposure";
      } else if (method.getName().equals("sunExposure")) {
         parameter = "Sun Exposure (hrs)";
      } else if (method.getName().equals("skinType")) {
         parameter = "Skin Type";
      } else if (method.getName().equals("skinIrritability")) {
         parameter = "Skin Irritability";
      } else if (method.getName().equals("reactionToAHAS")) {
         parameter = "Reaction to AHAS";
      } else if (method.getName().equals("ageSpots")) {
         parameter = "Age Spots";
      } else if (method.getName().equals("eyesLines&Wrinkles")) {
         parameter = "Eyes Lines & Wrinkles";
      } else if (method.getName().equals("mouthLines&Wrinkles")) {
         parameter = "Mouth Lines & Wrinkles";
      } else if (method.getName().equals("foreheadLines&Wrinkles")) {
         parameter = "Forehead Lines & Wrinkles";
      } else if (method.getName().equals("poreAppearance")) {
         parameter = "Pore Appearance";
      } else if (method.getName().equals("skinFirmness")) {
         parameter = "Skin Firmness";
      } else if (method.getName().equals("skinRadiance")) {
         parameter = "Skin Radiance";
      } else if (method.getName().equals("skinTexture")) {
         parameter = "Skin Texture";
      } else if (method.getName().equals("dayMoisturiserFragance")) {
         parameter = "Day Moisturiser Fragance";
      } else if (method.getName().equals("dayMoisturiserPreference")) {
         parameter = "Day Moisturiser Preference";
      } else if (method.getName().equals("nightMoisturiserFragance")) {
         parameter = "Night Moisturiser Fragance";
      } else if (method.getName().equals("nightMoisturiserPreference")) {
         parameter = "Night Moisturiser Preference";
      } else {
         System.out.println("Parameter Type is not recognized");
      }

      // Retrieve data from spreadSheet depending on what parameter is specified
      try {
         data = spreadSheetData.getData(excelFilePath, parameter);
      } catch (IOException e) {
         e.printStackTrace();
      }

      return data;
   }

   @Test(dataProvider = "GetExcelData")
   public void accessTestAssessment(String parameter, String language) {
      String title = "";
      testStatus = true;
      if (language.equals("English")) {
         driver.get("https://www.nuskin.com/content/nuskin/en_BE/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "ageLOC Me Assessment", "The Page title is not correct");
      } else if (language.equals("Czech")) {
         driver.get("https://www.nuskin.com/content/nuskin/cs_CZ/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "Analýza pleti ageLOC Me", "The Page title is not correct");
      } else if (language.equals("Danish")) {
         driver.get(" https://www.nuskin.com/content/nuskin/da_DK/ageloc-me-assessment.html#/home");
         title = driver.getTitle();
         Assert.assertEquals(title, "ageLOC Me Vurdering", "The Page title is not correct");
      }
      driver.manage().window().maximize();
      System.out.println("The title of the page is: " + title);
      WebElement assessmentLink = driver.findElement(By.xpath("//div[@class='app-page']//ul//li[1]//h2[@translate='home-new-assessment']"));

      testStatus = clickOn(assessmentLink, "Assessment Link");
   }

   @Test
   public void b_termsOfUse() {
      testStatus = true;
      WebElement element;
      // Check that the string is not English when testing other languages....
      // Code goes here .....

      // Agree with Terms of Use:
      // Get the element for the checkbox
      element = findElement("//p[@class='checkbox']//label[@translate='terms-conditions-agree']");
      // Click the checkbox
      testStatus = clickOn(element, "Checkbox");
      // Get the element for the continue button
      element = findElement("//div[@class='popup']//div//button[@translate='continue-btn-text']");
      // Click on the continue button
      testStatus = clickOn(element, "Continue Button");
      // Get the next Continue button
      element = findElement("//div[@class='footer start-page start']//button[@translate='continue-btn-text']");
      // Click on the continue button
      testStatus = clickOn(element, "Continue Button");

   }

   @Test(dataProvider = "GetExcelData")
   public void c_personalInfo(String parameter, String info) {
      testStatus = true;
      String[] parts = info.split(";");
      WebElement name = findElement("//input[@id='name-text']");
      WebElement age = findElement("//input[@id='age-text']");
      WebElement female = findElement("//button[@translate='you-details-female']");
      WebElement male = findElement("//button[@translate='you-details-male']");
      WebElement nextButton = findElement("//button[@translate='next-btn-text']");

      // Fill out form
      testStatus = fillOut(name, parts[0], "Name element");
      testStatus = fillOut(age, parts[1], "Age element");
      if (parts[2].equals("Female")) {
         testStatus = clickOn(female, "Female button");
      } else {
         testStatus = clickOn(male, "Male button");
      }
      // Go to next page
      nextButton.click();

   }


   @AfterMethod
   public void tearDown(ITestResult result, ITestContext testContext) {

      if (!testStatus) {
         result.setStatus(2);
      }
      System.out.println("Test status is " + result.getStatus());
      System.out.println("Test name is " + result.getName());

      if (result.FAILURE == result.getStatus()) {
         CaptureScreenShot.takeScreenShot(driver, testContext.getName());
      }
   }

   @AfterTest
   public void closeSelenium() {
      // Shutdown the browser
      driver.close();
   }

   public boolean clickOn(WebElement currentElement, String elementName) {
      if ( currentElement.isDisplayed()) {
         currentElement.click();
         return true;
      } else {
         System.out.println("The " + elementName + " is not available");
         return false;
      }
   }

   public boolean fillOut(WebElement currentElement, String data, String elementName) {
      if ( currentElement.isDisplayed()) {
         currentElement.sendKeys(data);
         return true;
      } else {
         System.out.println("The " + elementName + " is not available");
         return false;
      }
   }

   public WebElement findElement(String xpath) {
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