package testSupport;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

/**
 * Created by heidy.cespedes on 5/26/2016.
 */
public class CaptureScreenShot {
   public static void takeScreenShot(WebDriver driver, String screenShotName) {
      TakesScreenshot takesScreenShot = (TakesScreenshot)driver;
      File source = takesScreenShot.getScreenshotAs(OutputType.FILE);
      try {
         FileUtils.copyFile(source, new File("./Screenshots/" + screenShotName + ".png"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
