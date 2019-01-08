package utils;

import java.io.File;
import java.sql.Driver;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WinUtils {

	static ConfigReader config = new ConfigReader();

	XlsUtil xls = new XlsUtil(config.getexcelpath());
	String sheet = "New Production View";
	int column = 6;

	public static boolean isElementPresent(WebDriver driver, By by) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
	}

	public void switchIfWindowsAre(WebDriver driver, int num) throws Exception {

		if (driver.getWindowHandles().size() == num) {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				Thread.sleep(1000);
			}

		} else {
			Thread.sleep(1000);
		}
	}

	public void getscreenshot(WebDriver driver, String path) throws Exception {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// The below method will save the screen shot in d drive with name
		// "screenshot.png"
		FileUtils.copyFile(scrFile, new File(path + "\\screenshot.png"));
	}

	// Validate and pass result to excel sheet if it is positive scenario.
	public void passIfPresent(WebDriver rdriver, int result, int row) {

		if (result > 0) {

			xls.setCellData(sheet, column, row, "Pass");
			xls.setCellData(sheet, column - 1, row,
					System.getProperty("user.name"));

		} else {

			xls.setCellData(sheet, column, row, "Fail");
			xls.setCellData(sheet, column - 1, row,
					System.getProperty("user.name"));
		}

	}

	public void passIfAbsent(WebDriver rdriver, int result, int row) {

		if (result > 0) {

			xls.setCellData(sheet, column, row, "Fail");
			xls.setCellData(sheet, column - 1, row,
					System.getProperty("user.name"));

		} else {

			xls.setCellData(sheet, column, row, "Pass");
			xls.setCellData(sheet, column - 1, row,
					System.getProperty("user.name"));
		}

	}

	public boolean isAlertPresent(WebDriver driver2) {

		try {
			driver2.switchTo().alert();
			return true;
		} catch (NoAlertPresentException ex) {
			return false;
		}

	}

	public void alerthandle(WebDriver driver3) throws Exception {

		if (isAlertPresent(driver3)) {
			
            Thread.sleep(2000);
			Alert alert = driver3.switchTo().alert();
			alert.accept();

		}

	}

}
