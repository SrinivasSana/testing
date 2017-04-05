package com.realpage.productname.utilities;

import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.MobileElement;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
//import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.realpage.productname.utilities.*;

public class CommonUtils 
{
	public static ExtentReports report;
    public static ExtentTest logger;
    public enum Direction {LEFT, RIGHT, UP, DOWN}
	
    public static boolean verifyPresenceOfElement(WebElement element)
    {    
    	boolean blnRetVal = false;
    	try
    	{
    		if (element != null && element.isDisplayed() && element.isEnabled())
    			blnRetVal = true;    		
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Class CommonUtils | Method verifyElement | Exception desc : " + ex.getMessage());
    	}
		return blnRetVal;
    }
    
	public static void setExtentReport()
	{
		try
		{			
			Calendar cal = Calendar.getInstance();
			Constants.strDirectory = Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString(cal.get(Calendar.MONTH) + 1) + Integer.toString(cal.get(Calendar.DATE));
			createDirectory(Constants.strReportFilePath + Constants.strDirectory);
			report = new ExtentReports(Constants.strReportFilePath + Constants.strDirectory + "\\SeniorLiving_" + getRandomVal() + ".html", false);
			report.loadConfig(new File(Constants.strReportFilePath + "extent-config.xml"));
			report.loadConfig(ExtentReports.class, Constants.strReportFilePath + "extent-config.xml");			
		}
		catch(Exception ex)
		{
			System.out.println("Class CommonUtils | Method setExtentReport | Exception desc : " + ex.getMessage());
		}
	}
	
	public static void createDirectory(String strDirName)
	{
		try
		{
			File strDir = new File(strDirName);
			if (!strDir.exists()) 											
				strDir.mkdir();
		}
		catch(Exception ex)
		{
			System.out.println("Class CommonUtils | Method createDirectory | Exception desc : " + ex.getMessage());
		}
	}
	
	public static String takeScreenShot(WebDriver scDriver, String strFileName)
    {		
		String strRetVal = "";
		try
		{
            File scrFile = ((TakesScreenshot)scDriver).getScreenshotAs(OutputType.FILE);     
			strRetVal = Constants.strScreenshotsPath + Constants.strDirectory + "//" + strFileName + "_"
					+ getRandomVal() + ".jpeg";
            FileUtils.copyFile(scrFile, new File(strRetVal));
		}
		catch(Exception ex)
		{
			System.out.println("Class CommonUtils | Method takeScreenShot | Exception desc : " + ex.getMessage());
		}
		return strRetVal;
    }
	
	public static String getRandomVal() 
	{
		String strRamdom = "";
		try 
		{			
			Calendar cal = Calendar.getInstance();

			strRamdom = Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString(cal.get(Calendar.MONTH) + 1)
					+ Integer.toString(cal.get(Calendar.DATE)) + "_"
					+ ((cal.get(Calendar.AM_PM) == Calendar.AM) ? Integer.toString(cal.get(Calendar.HOUR))
							: (Integer.toString(cal.get(Calendar.HOUR) + 12)))
					+ Integer.toString(cal.get(Calendar.MINUTE)) + Integer.toString(cal.get(Calendar.SECOND));
		} catch (Exception ex) {
			System.out.println("Class CommonUtils | Method getRandomVal | Exception desc : " + ex.getMessage());
		}
		return strRamdom;
	}
	
	public static void setPinCode()
	{
		try 
		{
			Thread.sleep(1000);
			Runtime.getRuntime().exec("adb shell input keyevent "+"KEYCODE_0");
		}
		catch (Exception ex) 
		{
			System.out.println("Class CommonUtils | Method setPinCode | Exception desc : " + ex.getMessage());
		}
	}
	
	public static void explicitWait(WebDriver driver, WebElement element, long sec) 
	{
		try 
		{
			new WebDriverWait(driver, sec).until(ExpectedConditions.visibilityOf(element));
		}
		catch (Exception ex) 
		{
			System.out.println("Class CommonUtils | Method explicitWait | Exception desc : " + ex.getMessage());
		}
	}
	
	public static void fillTextBox(WebElement element, String strValue) 
	{
		try 
		{
			element.clear();
			element.click();
			element.sendKeys(strValue);
		}
		catch (Exception ex) 
		{
			System.out.println("Class CommonUtils | Method fillTextBox | Exception desc : " + ex.getMessage());
		}
	}
	
	public static void setSuiteLevelConsts() 
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;		
		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("Config.xml");
			doc.getDocumentElement().normalize();
			String strRootPath = System.getProperty("user.dir");		
			Constants.strURL = getNodeValue(doc, "URL");
			Constants.strReportFilePath = strRootPath + getNodeValue(doc, "Reports");
			Constants.strScreenshotsPath = strRootPath + getNodeValue(doc, "ScreenShots");
			Constants.strPathForPageObjectConfigs = strRootPath + getNodeValue(doc, "PathForPageObjectConfigs");
			//Set Constants for DesiredCapabilities 
			Constants.strDeviceName = getNodeValue(doc, "DeviceName");
			Constants.strPlatformName = getNodeValue(doc, "PlatformName");
			Constants.strPlatformVersion = getNodeValue(doc, "PlatformVersion");
			Constants.strAppBuildPath = strRootPath + getNodeValue(doc, "AppBuildPath");
			Constants.strAppPackage = getNodeValue(doc, "AppPackage");
			Constants.strAppActivity = getNodeValue(doc, "AppActivity");
			Constants.strNewCmdTimeOut = Integer.parseInt(getNodeValue(doc, "newCommandTimeout"));
			//Pgolla
			Constants.strNodePath = getNodeValue(doc, "NodePath");
			Constants.strAppiumJSPath = getNodeValue(doc, "AppiumJSPath");
		} 
		catch (Exception ex) 
		{
			System.out.println("Class commonUtils | Method setSuiteLevelConsts | Exception desc : " + ex.getMessage());
		}
	}
		
	private static String getNodeValue(Document doc, String strNodeName)
	{		
		Node nNode;
		NodeList nodeList;
		String strRetVal = "";
		try
		{
			nodeList = doc.getElementsByTagName(strNodeName);
			nNode = nodeList.item(0);
			strRetVal = nNode.getTextContent();
		} 
		catch (Exception ex) 
		{
			System.out.println("Class commonUtils | Method getNodeValue | Exception desc : " + ex.getMessage());
		}
		return strRetVal;
	}
	
	public static WebElement getChildFromParent(WebDriver driver, WebElement parentElement, String strLocatorAndXpath)
	{
		WebElement element = null;
		try
		{
			String[] strArray = strLocatorAndXpath.split("=", 2);
			element = processWebElementBasedOnLocator(driver, parentElement, strArray);
		}
		catch (Exception ex) {
			System.out.println("Class Utils | Method findElementByLocator | Exception desc : " + ex.getMessage());
		}
		return element;
	}
	
	private static WebElement processWebElementBasedOnLocator(WebDriver driver, WebElement parentElement, String[] strArray)
	{
		WebElement element = null;
		String strLocator = strArray[0];
		String strLocatorValue = strArray[1];
		try
		{
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			switch (strLocator) {
			case "xpath":				
				element = parentElement.findElement(By.xpath(strLocatorValue));
				break;
			case "id":				
				element = parentElement.findElement(By.id(strLocatorValue));
				break;
			case "name":				
				element = parentElement.findElement(By.name(strLocatorValue));
				break;
			case "linkText":				
				element = parentElement.findElement(By.linkText(strLocatorValue));
				break;
			case "partialLinkText":				
				element = parentElement.findElement(By.partialLinkText(strLocatorValue));
				break;
			case "className":				
				element = parentElement.findElement(By.className(strLocatorValue));
				break;
			case "cssSelector":				
				element = parentElement.findElement(By.cssSelector(strLocatorValue));
				break;
			case "tagName":
				element = parentElement.findElement(By.tagName(strLocatorValue));
				break;
			default:
				break;
			}
		}
		catch(Exception ex)
		{
			System.out.println("Class Utils | Method processWebElementBasedOnLocator | Exception desc : " + ex.getMessage());
		}
		return element;
	}
	
	public static WebElement findElementByLocator(String strLocatorAndXpath, WebDriver driver)
	{
		WebElement element = null;
		try
		{
			String[] strArray = strLocatorAndXpath.split("=", 2);
			element = processWebElementBasedOnLocator(strArray, driver);
		}
		catch (Exception ex) {
			System.out.println("Class Utils | Method findElementByLocator | Exception desc : " + ex.getMessage());
		}
		return element;
	}
	
	private static WebElement processWebElementBasedOnLocator(String[] strArray, WebDriver driver)
	{
		WebElement element = null;
		String strLocator = strArray[0];
		String strLocatorValue = strArray[1];
		try
		{
			switch (strLocator) {
			case "xpath":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.xpath(strLocatorValue)));
				element = driver.findElement(By.xpath(strLocatorValue));
				break;
			case "id":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.id(strLocatorValue)));
				element = driver.findElement(By.id(strLocatorValue));
				break;
			case "name":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.name(strLocatorValue)));
				element = driver.findElement(By.name(strLocatorValue));
				break;
			case "linkText":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.linkText(strLocatorValue)));
				element = driver.findElement(By.linkText(strLocatorValue));
				break;
			case "partialLinkText":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(strLocatorValue)));
				element = driver.findElement(By.partialLinkText(strLocatorValue));
				break;
			case "className":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.className(strLocatorValue)));
				element = driver.findElement(By.className(strLocatorValue));
				break;
			case "cssSelector":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(strLocatorValue)));
				element = driver.findElement(By.cssSelector(strLocatorValue));
				break;
			case "tagName":
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.tagName(strLocatorValue)));
				element = driver.findElement(By.tagName(strLocatorValue));
				break;
			default:
				break;
			}
		}
		catch (Exception ex) {
			System.out.println("Class Utils | Method processWebElementBasedOnLocator | Exception desc : " + ex.getMessage());
		}
		return element;
	}
	
	public static List<WebElement> findElementListByLocator(String strLocatorAndXpath, WebDriver driver)
	{
		List<WebElement> elementList = null;
		try
		{
			String[] strArray = strLocatorAndXpath.split("=", 2);
			elementList = processElementListBasedOnLocator(strArray, driver);
		}
		catch (Exception ex) {
			System.out.println("Class Utils | Method findElementByLocator | Exception desc : " + ex.getMessage());
		}
		return elementList;
	}
	
	private static List<WebElement> processElementListBasedOnLocator(String[] strArray, WebDriver driver)
	{
		List<WebElement> elementList = null;
		String strLocator = strArray[0];
		String strLocatorValue = strArray[1];
		try
		{
			switch (strLocator) {
			case "xpath":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(strLocatorValue)));
				elementList = driver.findElements(By.xpath(strLocatorValue));
				break;
			case "id":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id(strLocatorValue)));
				elementList = driver.findElements(By.id(strLocatorValue));
				break;
			case "name":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.name(strLocatorValue)));
				elementList = driver.findElements(By.name(strLocatorValue));
				break;
			case "linkText":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.linkText(strLocatorValue)));
				elementList = driver.findElements(By.linkText(strLocatorValue));
				break;
			case "partialLinkText":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(strLocatorValue)));
				elementList = driver.findElements(By.partialLinkText(strLocatorValue));
				break;
			case "className":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.className(strLocatorValue)));
				elementList = driver.findElements(By.className(strLocatorValue));
				break;
			case "cssSelector":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(strLocatorValue)));
				elementList = driver.findElements(By.cssSelector(strLocatorValue));
				break;
			case "tagName":
				new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.tagName(strLocatorValue)));
				elementList = driver.findElements(By.tagName(strLocatorValue));
				break;
			default:
				break;
			}
		}
		catch (Exception ex) {
			System.out.println("Class Utils | Method processElementListBasedOnLocator | Exception desc : " + ex.getMessage());
		}
		return elementList;
	}
	
/*	private static boolean waitForSyncStateChange(WebElement element)
	{		
		boolean blnRetVal = false;
		try
		{			
			int i = 0;
			do 
			{
				Thread.sleep(30000);				
				System.out.println("Waiting for Sync state to change, Elapsed - " + (++i * 30) + " Seconds");
			} while (i < 20 && element.getText().equalsIgnoreCase(SliderMenu.getSyncTextFromConfig())); //Max wait time is 10 minutes
			if(i < 20)
				blnRetVal = true;
		}
		catch (Exception ex) 
		{
			System.out.println("Class Utils | Method waitForSyncComplete | Exception desc : " + ex.getMessage());
		}
		return blnRetVal;
	}*/
	
/*	public static boolean waitForSyncStateChange(ExtentTest tempLogger)
	{
		boolean retVal = false;
		try
		{			
			tempLogger.log(LogStatus.INFO, "Wait for Sync to Complete");
			if(SliderMenuActions.openSyncFromSliderMenu(tempLogger))
			{				
				Thread.sleep(2000);
				if(CommonUtils.waitForSyncStateChange(SliderMenu.getSyncText()))
				{
					retVal = true;
					tempLogger.log(LogStatus.INFO, "Sync state changed from SYNCING");
				}
			}
		}
		catch(Exception ex)
		{			
			System.out.println("Exception occured in Class - Utils | Method - waitForSyncStateChange | Error - " + ex.getMessage());			
		}
		return retVal;
	}
*/	
	
	/*public static void swipeUP(AppiumDriver driver, MobileElement element, int duration) 
	{
         int offset = 1;
         Point p = element.getCenter();
         Point location = element.getLocation();
         Dimension size = element.getSize();
         driver.swipe(p.getX(), location.getY() + size.getHeight() + offset, p.getX(), location.getY(), duration);
     }
	
	public static void swipeDown(AppiumDriver driver, MobileElement element, int duration) 
	{
        int offset = 1;
        Point p = element.getCenter();
        Point location = element.getLocation();
        Dimension size = element.getSize();
        driver.swipe(p.getX(), location.getY(), p.getX() - offset, location.getY() + size.getHeight(), duration);
    }*/
	
	public static void swipeUP(WebElement element, AppiumDriver driver, int offset, int duration)
	{
		try
		{
			int x =  element.getLocation().x;
			int y =  element.getLocation().y - 100;
			driver.swipe(x, y, x, y - offset, duration);
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured in Class - Utils | Method - swipeUP | Error - " + ex.getMessage());
		}		
	}
	
	public static void swipe(AppiumDriver driver, Direction direction, int duration)
	{
		try
		{		
			Dimension dimension = driver.manage().window().getSize();
			int width = dimension.getWidth();
			int height = dimension.getHeight();
			switch(direction)
			{
			case RIGHT : 
				driver.swipe((int) (width*(0.20)), (int) (height*(0.50)), (int)(width*(0.80)), (int) (height*(0.50)), duration);
				break;
			case LEFT : 
				driver.swipe((int) (width*(0.80)), (int) (height*(0.50)), (int) (width*(0.20)), (int) (height*(0.50)), duration);
				break;
			case UP : 
				driver.swipe((int) (width*(0.50)), (int) (height*(0.70)), (int) (width*(0.50)), (int) (height*(0.30)), duration);
				break;
			case DOWN : 
				driver.swipe((int) (width*(0.50)), (int) (height*(0.30)), (int) (width*(0.50)), (int) (height*(0.70)), duration);
				break;
			default:
				break;
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured in Class - Utils | Method - swipe | Error - " + ex.getMessage());
		}
	}
	
}
