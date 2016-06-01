import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import testSupport.CaptureScreenShot;
import testSupport.PageSupport;
import testSupport.SpreadSheetData;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class NuskinAssessmentTest {

   // Declare the browser
   WebDriver driver;
   PageSupport pageSupport = new PageSupport();
   private Properties props = new Properties();
   private String excelFilePath;
   private Boolean testStatus;

   @BeforeTest
   public void setupSelenium() {
      // Start the browser (Firefox in this case)
      driver = new FirefoxDriver();
      // Adds implicit timeouts to the driver
      driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
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
      String testName = method.getName();

      // Define what parameters are needed based on what test step is run
      switch (testName) {
         case "accessTestAssessment":
            parameter = "Language";
            break;
         case "b_termsOfUse":
            parameter = "Terms Of Use Text";
            break;
         case "c_personalInfo":
            parameter = "Personal Info (First Name; Age; Female/Male)";
            break;
         case "d_ethnicity":
            parameter = "Ethnicity";
            break;
         case "e_location":
            parameter = "Location";
            break;
         case "f_chemicalExposure":
            parameter = "Chemical Exposure";
            break;
         case "g_sunExposure":
            parameter = "Sun Exposure (hrs)";
            break;
         case "h_skinType":
            parameter = "Skin Type";
            break;
         case "i_skinIrritability":
            parameter = "Skin Irritability";
            break;
         case "j_reactionToAHAS":
            parameter = "Reaction to AHAS";
            break;
         case "k_ageSpots":
            parameter = "Age Spots";
            break;
         case "l_eyesLines&Wrinkles":
            parameter = "Eyes Lines & Wrinkles";
            break;
         case "m_mouthLines&Wrinkles":
            parameter = "Mouth Lines & Wrinkles";
            break;
         case "n_foreheadLines&Wrinkles":
            parameter = "Forehead Lines & Wrinkles";
            break;
         case "o_poreAppearance":
            parameter = "Pore Appearance";
            break;
         case "p_skinFirmness":
            parameter = "Skin Firmness";
            break;
         case "q_skinRadiance":
            parameter = "Skin Radiance";
            break;
         case "r_skinTexture":
            parameter = "Skin Texture";
            break;
         case "s_dayMoisturiserFragance":
            parameter = "Day Moisturiser Fragance";
            break;
         case "t_dayMoisturiserPreference":
            parameter = "Day Moisturiser Preference";
            break;
         case "u_nightMoisturiserFragance":
            parameter = "Night Moisturiser Fragance";
            break;
         case "v_nightMoisturiserPreference":
            parameter = "Night Moisturiser Preference";
            break;
         default:
            System.out.println("Parameter Type is not recognized");
            break;
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
      WebElement assessmentLink = pageSupport.findElement(driver, "//div[@class='app-page']//ul//li[1]//h2[@translate='home-new-assessment']");

      testStatus = pageSupport.clickOn(driver, assessmentLink, "Assessment Link", "AssessmentLink");
   }

   @Test(dataProvider = "GetExcelData")
   public void b_termsOfUse(String parameter, String englishText) {
      testStatus = true;
      boolean textTestStatus = true;
      WebElement element;
      String currentUrl = driver.getCurrentUrl();
      String line;

      // Check that the string is not English when testing other languages....
      if (currentUrl.contains("cs_CZ") || currentUrl.contains("da_DK")) {
         line = pageSupport.findElement(driver, "//div[@class='popup-scroll-text full ng-binding']").getText();
         line = line.replace("\n"," ");
         englishText = englishText.replace("\n", " ");
         if (line.equals(englishText)) {
            System.out.println("The Terms of Use Agreement is in English");
            CaptureScreenShot.takeScreenShot(driver, "TermsOfUseLanguage");
            textTestStatus = false;
         }
      }

      // Agree with Terms of Use once the terms of use language has been verified:

      // Get the element for the checkbox
      element = pageSupport.findElement(driver, "//p[@class='checkbox']//label[@translate='terms-conditions-agree']");
      // Click the checkbox
      testStatus = pageSupport.clickOn(driver, element, "Checkbox", "Checkbox");
      // Get the element for the continue button
      element = pageSupport.findElement(driver, "//div[@class='popup']//div//button[@translate='continue-btn-text']");
      // Click on the continue button
      if (testStatus) {
         testStatus = pageSupport.clickOn(driver, element, "Continue Button", "ContinueButton");
      } else {
         pageSupport.clickOn(driver, element, "Continue Button", "ContinueButton");
      }
      // Get the next Continue button
      element = pageSupport.findElement(driver, "//div[@class='footer start-page start']//button[@translate='continue-btn-text']");
      // Click on the continue button
      if (testStatus) {
         testStatus = pageSupport.clickOn(driver, element, "Continue Button", "ContinueButton2");
      } else {
         pageSupport.clickOn(driver, element, "Continue Button", "ContinueButton2");
      }
      // Make sure the final test status is correct
      if (testStatus && !textTestStatus) {
         testStatus = textTestStatus;
      }
   }

   @Test(dataProvider = "GetExcelData")
   public void c_personalInfo(String parameter, String info) {
      testStatus = true;
      String[] parts = info.split(";");
      WebElement name = pageSupport.findElement(driver, "//input[@id='name-text']");
      WebElement age = pageSupport.findElement(driver, "//input[@id='age-text']");
      WebElement female = pageSupport.findElement(driver, "//button[@translate='you-details-female']");
      WebElement male = pageSupport.findElement(driver, "//button[@translate='you-details-male']");
      WebElement nextButton = pageSupport.findElement(driver, "//button[@translate='next-btn-text']");

      // Fill out form
      testStatus = pageSupport.fillOut(driver, name, parts[0], "Name element", "Name");
      // If the testStatus has changed to "failed" we want to keep that result for the final report
      if (testStatus) {
         testStatus = pageSupport.fillOut(driver, age, parts[1], "Age element", "Age");
      } else {
         pageSupport.fillOut(driver, age, parts[1], "Age element", "Age");
      }
      if (parts[2].equals("Female")) {
         if (testStatus) {
            testStatus = pageSupport.clickOn(driver, female, "Female button", "Female");
         } else {
            pageSupport.clickOn(driver, female, "Female button", "Female");
         }
      } else {
         if (testStatus) {
            testStatus = pageSupport.clickOn(driver, male, "Male button", "Male");
         } else {
            pageSupport.clickOn(driver, male, "Male button", "Male");
         }
      }

      // Go to next page
      if (testStatus) {
         testStatus = pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      } else {
         pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      }
   }

   @Test(dataProvider = "GetExcelData")
   public void d_ethnicity(String parameter, String ethnicity) {
      testStatus = true;
      WebElement element;
      WebElement nextButton;

      switch (ethnicity) {
         case "African":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[1]");
            break;
         case "Caucasian (European)":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[2]");
            break;
         case "Chinese":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[3]");
            break;
         case "Filipino":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[4]");
            break;
         case "Hispanic":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[5]");
            break;
         case "Indian":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[6]");
            break;
         case "Japanese":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[7]");
            break;
         case "Javanese":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[8]");
            break;
         case "Korean":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[9]");
            break;
         case "Malaysian":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[10]");
            break;
         case "Mediterranean":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[11]");
            break;
         case "Middle Eastern":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[12]");
            break;
         case "Native American":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[13]");
            break;
         case "South Pacific Islander":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[14]");
            break;
         case "Thai":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[15]");
            break;
         case "Vietnamese":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[16]");
            break;
         case "Other":
            element = pageSupport.findElement(driver, "//ul[@class='button-list']//li[17]");
            break;
         default:
            element = null;
            break;
      }

      // Click on the correct element and click on arrow to continue
      testStatus = pageSupport.clickOn(driver, element, "Ethnicity", "Ethnicity");
      nextButton = pageSupport.findElement(driver, "//div[@class='footer']//button[@translate='next-btn-text']");
      // Go to next page
      if (testStatus) {
         testStatus = pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      } else {
         pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      }
   }

   @Test(dataProvider = "GetExcelData")
   public void e_location(String parameter, String location) {
      testStatus = true;
      WebElement element;
      WebElement nextButton;

      element = pageSupport.findElement(driver, "//input[@id='locationText']");
      testStatus = pageSupport.fillOut(driver, element, location, "Location", "Location");
      nextButton = pageSupport.findElement(driver, "//div[@class='footer']//button[@translate='next-btn-text']");
      // Go to next page
      if (testStatus) {
         testStatus = pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      } else {
         pageSupport.clickOn(driver, nextButton, "Next Button", "NextButton");
      }
   }

   @AfterMethod
   public void tearDown(ITestResult result, ITestContext testContext) {

      if (!testStatus) {
         result.setStatus(2);
      }
      // Takes screenshot if test failed
      if (result.FAILURE == result.getStatus()) {
         CaptureScreenShot.takeScreenShot(driver, result.getName()); //To do....Add date to differentiate names
      }

      System.out.println("Test name is " + result.getName());
      if (result.getStatus() == 1) {
         System.out.println("Test has passed");
      } else if (result.getStatus() == 2){
         System.out.println("Test has failed");
      }
   }

   @AfterTest
   public void closeSelenium() {
      // Shutdown the browser
      driver.close();
   }

}