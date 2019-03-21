package ProductionRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import utils.ConfigReader;
import utils.WinUtils;
import utils.XlsUtil;
import utils.smlogin;

public class Production_Execution {

	WebDriver driver;
	smlogin login = new smlogin();
	
	static ConfigReader config = new ConfigReader();
	WinUtils util = new WinUtils();
	StringBuffer showAllError;
	WebDriverWait wait;
	XlsUtil xls = new XlsUtil(config.getexcelpath());
	String sheet = "New Production View";
	int column = 6;
	SoftAssert softAssert = new SoftAssert();
	String prodreqname;

	@BeforeMethod
	public String RandomNo() {

		prodreqname = "Auto Test Request " + ((int) (Math.random() * 10000));
		return prodreqname;
	}
	
	@Test
	public void RunRequest() throws Exception {			

		try {
			System.out.println("Starts Production execution");
			System.setProperty("webdriver.chrome.driver", config.getChromePath());
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Get the ShowMan URL to be tested
			driver.get(config.getShowManagerURL());
			driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);

			// Login to ShowMan and enter show code
			login.smloginChandra(driver);
			System.out.println(" Browser opended");

			driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(config.getshowcode());
			driver.findElement(By.xpath("//input[@value='Go!']")).click();

			System.out.println("Logged in and entered the showcode");

			// Go to Production Request tab
			driver.findElement(By.xpath("//*[contains(text(),'Production')]")).click();
			// driver.findElement(By.xpath("//*[contains(text(),'New Production View')]")).click();
			System.out.println(" Clicked on New Production view");
			// Creating explicit wait.
			wait = new WebDriverWait(driver, 100);

//			int a = driver.findElements(By.tagName("iframe")).size();
//			System.out.println("Size of Frame: " + a);
			driver.switchTo().frame(0);
			// Validate the Production Request Page and write it to Excel sheet
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Add New Production Request')]")));																						 
			int newproduction = driver.findElements(By.xpath("//button[contains(.,'Add New Production Request')]")).size();
			util.passIfPresent(driver,newproduction, 2);
			
			Assert.assertTrue(newproduction > 0, "Failed to launch new production page");

			// Click 'Add New Production Request' page
			driver.findElement(By.xpath("//button[contains(.,'Add New Production Request')]")).click();
			System.out.println("Clicked on New Production Request");
			// Validate the Company Search Page and write it to Excel sheet
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//button[contains(.,'Save Production Request')]")));
			int productionRequest = driver.findElements(By.xpath("//button[contains(.,'Save Production Request')]")).size();
			util.passIfPresent(driver,productionRequest, 3);
			Assert.assertTrue(productionRequest > 0, "Failed to launch new production Request");

			// Enter Production Request details
			// String Productionname = "Auto Test Request " + RND;
			System.out.println("Production Request Name: " + prodreqname);
			driver.findElement(By.name("productionRequestName")).sendKeys(prodreqname);
			driver.findElement(By.xpath("//input[@class='typeahead-input ng-untouched ng-pristine ng-valid']")).sendKeys("kulandasamy", Keys.ENTER);
			Thread.sleep(2000);
			driver.findElement(By.name("requestorPhone")).sendKeys("123 1234 123");
			driver.findElement(By.xpath("//button[contains(.,'Save Production Request')]")).click();
			System.out.println("Saved new production request");
			int ProdRequest = driver.findElements(By.xpath("//button[contains(.,'Add New Print Request ')]")).size();
			util.passIfPresent(driver,ProdRequest, 4);
					
			// Click on Add New Print Request

			// wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(.,'Add New Print Request ')]")));
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Add New Print Request ')]")).click();
			System.out.println("Clicked on Add New Print Request");
			int printRequest = driver.findElements(By.name("printRequestName")).size();
			util.passIfPresent(driver,printRequest, 5);
					
			// Save Print Request
			driver.findElement(By.name("printRequestName")).sendKeys("Auto Print Request");
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(2000);
			int isenabled = driver.findElements(By.xpath("//button[contains(.,'Add New Print Item ')]")).size();
			util.passIfPresent(driver,isenabled, 6);
			
			// click on Add New Print Item				
			driver.findElement(By.xpath("//button[contains(.,'Add New Print Item ')]")).click();
			int printitem = driver.findElements(By.name("printItemTypeCode")).size();
			util.passIfPresent(driver,printitem, 7);
			System.out.println("Clicked on Add New Print Item");
			Thread.sleep(2000);

			// Select request and report type and save.
			Select requesttype = new Select(driver.findElement(By.name("printItemTypeCode")));
			requesttype.selectByValue("Confirm");
			Select mediatype = new Select(driver.findElement(By.name("mediaTypeCode")));
			mediatype.selectByVisibleText("White");
			new Select(driver.findElement(By.name("reportDefinitionID"))).selectByVisibleText("HTML Confirmation - Attendee");
//			selectrep.selectByVisibleText("HTML Confirmation");
			driver.findElement(By.xpath("//button[contains(.,'Save ')]")).click();
			System.out.println("New Print Item Added");
			// validate whether Print item is saved or not

			int printsaved = driver.findElements(By.linkText("Confirm")).size();
			util.passIfPresent(driver,printsaved, 8);
			
//		Delete Print item
			driver.findElement(By.xpath("//button[contains(.,'Delete ')]")).click();
			util.alerthandle(driver);
			System.out.println("Print Item deleted");

			// wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.xpath("//button[contains(.,'Delete ')]"))));
			Thread.sleep(2000);
//		Assert.assertFalse(driver.findElements(By.xpath("//button[contains(.,'Delete ')]")).size() > 0,"Delete button still appeared");
			int deleteprintitem = driver.findElements(By.xpath("//div/em[contains(.,'There are no print items.')]")).size();
			util.passIfPresent(driver,deleteprintitem, 9);
					
			// After delete Again add new Print Item
			driver.findElement(By.xpath("//button[contains(.,'Add New Print Item ')]")).click();
			Thread.sleep(2000);
			Select requesttype1 = new Select(driver.findElement(By.name("printItemTypeCode")));
			requesttype1.selectByValue("Confirm");
			Select mediatype1 = new Select(driver.findElement(By.name("mediaTypeCode")));
			mediatype1.selectByVisibleText("White");
			Select selectrep1 = new Select(driver.findElement(By.name("reportDefinitionID")));
			selectrep1.selectByVisibleText("HTML Confirmation - Attendee");
			driver.findElement(By.xpath("//button[contains(.,'Save ')]")).click();
			Thread.sleep(2000);
			System.out.println("New Print Item Added again");
			driver.findElement(By.xpath("//button[contains(.,'Back ')]")).click();
				 
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//button[contains(.,'Add New Print Request ')]")));
			int printrequest = driver.findElements(By.xpath("(//button[contains(.,'Save and Continue')])[2]")).size();
			util.passIfPresent(driver,printrequest, 10);
					
//			Click on Sort accordion
			driver.findElement(By.xpath("(//button[contains(.,'Save and Continue')])[2]")).click();
			Thread.sleep(2000);		
			int sort = driver.findElements(By.xpath("//button[contains(.,' Add Rule ')]")).size();
			util.passIfPresent(driver,sort, 11);
			
			//Add Sorting 
			driver.findElement(By.xpath("//button[contains(.,' Add Rule ')]")).click();
			int addrule =driver.findElements(By.name("sortDirection")).size();
			util.passIfPresent(driver,addrule, 12);		
			driver.findElement(By.xpath("(//button[contains(.,'Save and Continue')])[3]")).click();
			Thread.sleep(2000);
			System.out.println("Sorting added");

			// Click on Quick Selection Criteria		
			int quicksel = driver.findElements(By.xpath("(//button[contains(.,'Add Rule ')])[2]")).size();
			util.passIfPresent(driver,quicksel, 13);
			driver.findElement(By.xpath("(//button[contains(.,'Add Rule ')])[2]")).click();
			Select fieldname = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[2]")));
			fieldname.selectByVisibleText("FirstName");
			Select comparsion = new Select(driver.findElement(By.name("operator")));
			comparsion.selectByVisibleText("contains");
			
			driver.findElement(By.name("value")).sendKeys("test");
			int txtvalue = driver.findElements(By.name("value")).size();
			util.passIfPresent(driver,txtvalue, 14);
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//button[contains(.,' Add Rule ')])[2]")).click();

			// Delete Rule
			driver.findElement(By.xpath("(//button[contains(.,'Remove Rule ')])[2]")).click();
			util.alerthandle(driver);		
			int rueldelete = driver.findElements(By.xpath("//label[contains(text(),'Logical Operator')]")).size();
			util.passIfAbsent(driver,rueldelete, 15);			

 
			// Again Add Rule
			driver.findElement(By.xpath("(//button[contains(.,' Add Rule ')])[2]")).click();
			Select fieldname1 = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[3]")));
			fieldname1.selectByVisibleText("LastName");
			Select comparsion1 = new Select(driver.findElement(By.xpath("(//select[@name='operator'])[2]")));
			comparsion1.selectByVisibleText("contains");
			driver.findElement(By.xpath("(//input[@name='value'])[2]")).sendKeys("LN test");

			// Add and delete Group
			driver.findElement(By.xpath("//button[contains(.,' Add Group ')]")).click();
			int addgrp =driver.findElements(By.xpath("(//button[contains(.,'Remove Group ')])[2]")).size();
			util.passIfPresent(driver,addgrp, 16);
			driver.findElement(By.xpath("(//button[contains(.,'Remove Group ')])[2]")).click();
			util.alerthandle(driver);
			int deletegrp = driver.findElements(By.xpath("(//button[contains(.,'Remove Group ')])[2]")).size();
			util.passIfAbsent(driver,deletegrp, 17);
			
			/*
			driver.findElement(By.xpath("//button[contains(.,' Add Group ')]")).click();
			Select fieldname2 = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[4]")));
			fieldname2.selectByVisibleText("ZipCode");
			Select comparsion2 = new Select(driver.findElement(By.xpath("(//select[@name='operator'])[3]")));
			comparsion2.selectByVisibleText("equals");
			driver.findElement(By.xpath("(//input[@name='value'])[3]")).sendKeys("22222");
			driver.findElement(By.xpath("(//button[contains(.,' Add Rule ')])[3]")).click();
			Select fieldname3 = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[5]")));
			fieldname3.selectByVisibleText("Address");
			Thread.sleep(3000);
			Select comparsion3 = new Select(driver.findElement(By.xpath("(//select[@name='operator'])[4]")));
			comparsion3.selectByVisibleText("is not blank/empty");*/
			driver.findElement(By.xpath("(//button[contains(.,'Save and Continue')])[4]")).click();
			System.out.println("Quick Criteria Selection sorting is added");
			Thread.sleep(2000);					

			// Started 'Advanced Criteria Selection'
			if (!driver.findElement(By.xpath("//li/a[contains(.,'Cancelled')]")).isDisplayed())
				driver.findElement(By.xpath("//a[contains(.,'Advanced Criteria Selection')]")).click();
			Thread.sleep(2000);
			int advcri = driver.findElements(By.xpath("//li/a[contains(.,'Cancelled')]")).size();
			util.passIfPresent(driver,advcri, 18);
			
			// driver.findElement(By.xpath("//a[contains(.,'Advanced Criteria Selection')]")).click();
			System.out.println("Clicked on 'Advanced Criteria Selection' accordion");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//li/a[contains(.,'Cancelled')]")).click();

			js.executeScript("arguments[0].scrollIntoView();",
			driver.findElement(By.xpath("//a[contains(.,'Advanced Criteria Selection')]")));

			Select andor = new Select(driver.findElement(By.xpath("//div/label[contains(.,'And/Or')]/child::select")));
			andor.selectByValue("OR");
			Thread.sleep(2000);
			
			Select fieldname4 = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[4]")));
			fieldname4.selectByVisibleText("Address");
			Thread.sleep(2000);
			
			Select comparsion4 = new Select(driver.findElement(By.xpath("(//select[@name='operator'])[3]")));
			comparsion4.selectByVisibleText("is not blank/empty");
			
			driver.findElement(By.xpath("(//button[contains(.,'Add ')])[5]")).click();
			driver.findElement(By.xpath("//a[contains(.,'Advanced Criteria Selection')]/following-sibling::div/div/button[2]")).click();
			int advquicksave = driver.findElements(By.xpath("//a[contains(.,'Advanced Criteria Selection')]/following-sibling::div/div/button[2]")).size();
			util.passIfPresent(driver,advquicksave, 19);
			Thread.sleep(2000);
			System.out.println("'Advanced Criteria Selection' is saved");

			// Schedule Type, Start Date & End Date, Status
			Select schd = new Select(driver.findElement(By.xpath("(//select[@name='scheduleTypeCode'])[2]")));
			schd.selectByValue("DLY");
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("(//input[@class='datepicker-input'])[3]")));
			driver.findElement(By.xpath("(//input[@class='datepicker-input'])[3]")).click();

			Date dt = new Date();
			int today = dt.getDate();				
			Calendar c = Calendar.getInstance();		
			c.setTime(dt);		
			c.add(Calendar.DATE, 1);
			int tomorrowdate = c.getTime().getDate();		
			
			System.out.println("Tomorrow Date Method-1:"+tomorrowdate);	
			
			driver.findElement(By.xpath("(//div[contains(@class,'column day text-center ng-star-inserted')])[" + tomorrowdate + "]")).click();

			driver.findElement(By.xpath("(//input[@class='datepicker-input'])[4]")).click();
			driver.findElement(By.xpath("//div[contains(@class,'column day text-center ng-star-inserted')]")).click();
			System.out.println("Entered Schedule Type, Start & End date");

			driver.findElement(By.xpath("(//button[contains(.,'Save Production Request')])[2]")).click();
			Thread.sleep(2000);
//			driver.findElement(By.xpath("//span[contains(@class,'ng-tns-c3')]")).click();
			driver.findElement(By.xpath("(//button[contains(.,'Save Production Request')])[2]")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(.,'Save Production Request')])[2]")));
			Thread.sleep(3000);
			util.alerthandle(driver); // handling alert		
						
			Thread.sleep(2000);
			int saverequest = driver.findElements(By.xpath("(//button[contains(.,'Back ')])[2]")).size();
			util.passIfPresent(driver,saverequest, 20);
			driver.findElement(By.xpath("(//button[contains(.,'Back ')])[2]")).click();
			
//		// Search Production Request
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),'Request Name')]/input")));

			int search = driver.findElements(By.xpath("//input[@type='search']")).size();
			util.passIfPresent(driver,search, 21);

			driver.findElement(By.xpath("//label[contains(text(),'Request Name')]/input")).sendKeys(prodreqname);
			Select statustype = new Select(driver.findElement(By.xpath("//label[contains(.,'Status Type')]/select")));
			statustype.selectByValue("C");
			Thread.sleep(2000);
			String expectedName = driver.findElement(By.xpath("//a[@role='button']")).getText();
					
			if (expectedName.equalsIgnoreCase(prodreqname)) {
				
				 xls.setCellData(sheet, column, 22, "Pass");
			     xls.setCellData(sheet, column - 1, 22,	System.getProperty("user.name"));
			
			} else { 
				
				 xls.setCellData(sheet,  column, 22, "Fail"); 
			     xls.setCellData(sheet, column - 1, 22,  System.getProperty("user.name"));
			     }		
			
			driver.findElement(By.xpath("//a[@role='button']")).click();
			System.out.println("Searched production request and clicked on the link.");

			// Change Status to 'Do Not Run' to 'Run'.
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//button[contains(.,'Save Production Request')])[2]")));
			Thread.sleep(2000);

			Select statuschange = new Select(driver.findElement(By.xpath("(//select[@name='statusTypeCode'])[2]")));
			statuschange.selectByValue("0: O");		
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//button[contains(.,'Save Production Request')])[2]")).click();
			Thread.sleep(2000);
			util.alerthandle(driver);
//		Select statuschange1 = new Select(driver.findElement(By.xpath("(//label[contains(.,'Status')]/select)[4]")));
			Select statuschange1 = new Select(driver.findElement(By.xpath("(//select[@name='statusTypeCode'])[2]")));
//		System.out.println("Status change text: "+statuschange1.getFirstSelectedOption().getText()+"lenth:"+ statuschange1.getAllSelectedOptions().size());
			
			if (statuschange1.getFirstSelectedOption().getText().equals(" Run ")){
				
				 xls.setCellData(sheet, column, 23, "Pass");
			     xls.setCellData(sheet, column - 1, 23,	System.getProperty("user.name"));
			
			} else { 
			
			 xls.setCellData(sheet,  column, 23, "Fail"); 
			     xls.setCellData(sheet, column - 1, 23,  System.getProperty("user.name"));
			     }		
			
			System.out.println("The Production Job Status changed to 'Do Not Run' to 'Run'");
			Thread.sleep(2000);			

			// Click on Quick Count		
			driver.findElement(By.xpath("//exl-accordion-item[@itemtitle='Advanced Criteria Selection']/a")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//button[@class='button secondary'])[2]")).click();
			Thread.sleep(2000);
			// driver.findElement(By.xpath("//exl-accordion-item[@itemtitle='Quick Criteria Selection']/a")).click();
			driver.findElement(By.xpath("(//button[contains(.,' Add Rule ')])[2]")).click();
			Select fieldname5 = new Select(driver.findElement(By.xpath("(//select[@name='fieldName'])[2]")));
			fieldname5.selectByVisibleText("Address");
			Select comparsion5 = new Select(driver.findElement(By.xpath("(//select[@name='operator'])[1]")));
			comparsion5.selectByVisibleText("is not blank/empty");
			driver.findElement(By.xpath("(//button[contains(.,'Save and Continue')])[4]")).click();
			Thread.sleep(2000);
			util.alerthandle(driver);
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//a[contains(.,'Details')]")).click();
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//a[contains(.,'Details')]")));
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(.,'Quick Count')]"))));		
			driver.findElement(By.xpath("//button[contains(.,'Quick Count')]")).click();
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("(//div[@class='ng-star-inserted'])[1]")));
			int quickcount = driver.findElements(By.xpath("(//div[@class='ng-star-inserted'])[1]")).size();
			util.passIfPresent(driver,quickcount, 24);		
			System.out.println("Quick Count page displayed");
			
			// CLick on each of the 4 links
			List<WebElement> links = driver.findElements(By.xpath("((//td[contains(.,'#Print items this run ')])[1]/following::td/a)"));
			System.out.println("List Quick Count: " +links.size());
			
			for(int i=0;i<links.size();i++){
			
				List<WebElement> links1 = driver.findElements(By.xpath("((//td[contains(.,'#Print items this run ')])[1]/following::td/a)"));

			if (!links1.get(i).getAttribute("class").equalsIgnoreCase("disabled")) {
				links1.get(i).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p/button")));
			int exporttable = driver.findElements(By.xpath("//p/button")).size();
			util.passIfPresent(driver,exporttable, 25);
			driver.findElement(By.xpath("//a[contains(text(),'quickcount')]")).click();
				}
			}
			System.out.println("Clicked on all 4 links in each section");	
			

			// Click on Preview Report
			driver.findElement(By.xpath("//a[normalize-space(text())='" + prodreqname + "']")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//a[contains(.,' Print Requests')]")).click();
			driver.findElement(By.xpath("//button[contains(.,'Preview ')]")).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(.,'Send Test Confirmations ')]"))));
			int previewrpt = driver.findElements(By.xpath("//button[contains(.,'Send Test Confirmations ')]")).size();
			util.passIfPresent(driver,previewrpt, 26);
			

			// Click on Preview Report and Refresh and Send Test Confirmations
			driver.findElement(By.xpath("//button[contains(.,'Refresh ')]")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Send Test Confirmations ')]")));
			int refresh = driver.findElements(By.xpath("//button[contains(.,'Refresh ')]")).size();
			util.passIfPresent(driver,refresh, 27);
			driver.findElement(By.name("email")).sendKeys(config.getemailid());
			driver.findElement(By.xpath("//button[contains(.,'Send Test Confirmations ')]")).click();
			wait.until(ExpectedConditions.elementToBeClickable(
					driver.findElement(By.xpath("//button[contains(.,'Send Test Confirmations ')]"))));
			util.passIfPresent(driver,previewrpt, 28);
			driver.findElement(By.xpath("//button[contains(.,'Back')]")).click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(.,'Auto Print Request')]")));
			System.out.println("Preview page loaded and Sent Confirmation");

			//Click Preview Button on Print Item Page.
			Thread.sleep(2000);
			driver.findElement(By.xpath("//a[contains(.,'Auto Print Request')]")).click();
			driver.findElement(By.xpath("(//button[contains(.,'Preview Report')])[1]")).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(.,'Send Test Confirmations ')]"))));
//		int previewrpt = driver.findElements(By.xpath("//button[contains(.,'Send Test Confirmations ')]")).size();
			util.passIfPresent(driver,previewrpt, 29);
			System.out.println("Clicked on Print Item Preview.");
			
				
			// CLick on 'Job History'
			driver.findElement(By.xpath("//a[normalize-space(text())='" + prodreqname + "']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[contains(.,'Details')]"))));
			driver.findElement(By.xpath("//a[contains(.,'Details')]")).click();
			Thread.sleep(2000);
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//a[contains(.,'Details')]")));
			driver.findElement(By.xpath("//button[contains(.,'View Job History ')]")).click();
			int jobhistory = driver.findElements(By.xpath("//span[contains(.,'jobhistory')]")).size();
			util.passIfPresent(driver,jobhistory, 30);
			System.out.println("Reached Job History page");
			
			// Click on 'Create Duplicate'
			driver.findElement(By.xpath("//a[normalize-space(text())='" + prodreqname + "']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(.,'Create Duplicate ')]"))));
			driver.findElement(By.xpath("//button[contains(.,'Create Duplicate ')]")).click();
			int duplicate = driver.findElements(By.xpath("//span[contains(.,'Duplicate of')]")).size();
			util.passIfPresent(driver,duplicate, 31);
			System.out.println("Duplicated record created");
			System.out.println("Completed New Production Automation Testing :-) ");
		
			} catch (Exception e) {
			e.printStackTrace();
		}		

	}

	@AfterTest(enabled = false)
	public void quitBrowser() {
		 driver.quit();
	}

	@AfterClass(enabled = true)
	public void sendEmailReport() {

		utils.emailReport rept = new utils.emailReport();
		rept.sendEmail(this.getClass().getSimpleName(), "New Production View");

	}
}