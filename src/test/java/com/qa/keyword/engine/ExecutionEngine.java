package com.qa.keyword.engine;
import com.qa.keyword.base.WebDriverManager;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
Executes the keyword action from the excel sheet . The action is a corresponding selenium webdriver call
UI action
 */
public class ExecutionEngine {
    public WebDriver driver;
    public Properties configProp;

    public void startExecution(String excelFilePath,String sheetName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(excelFilePath);
        XSSFWorkbook excelBook = new XSSFWorkbook(fis);
        int totalSheets = excelBook.getNumberOfSheets();
        for(int i =0 ; i< totalSheets;i++) {
            if(sheetName!=null)
            {
                if(!(sheetName.isEmpty()) && (excelBook.getSheetAt(i).getSheetName().equals(sheetName))) {
                    XSSFSheet sheet = excelBook.getSheetAt(i);
                    executeActionsInSheet(sheet);
                    break;
                }
            } else {
                // execute the teststeps for all sheets
                XSSFSheet sheet = excelBook.getSheetAt(i);
                executeActionsInSheet(sheet);
            }
        }
    }

    public void executeActionsInSheet(XSSFSheet sheet) throws IOException, ClassNotFoundException{
        String locatorType=null;
        String locatorTypeValue=null;
        String testStep=null;
        System.out.println("Sheet which is used for execution : "+sheet.getSheetName());
        int lastRow = sheet.getLastRowNum();
        int k=0;
        for(int j =1;j <=lastRow;j++) {   //  iterator for loop traversing all the rows// skipping first row since it contains headers of the excel
            System.out.println("TestStep performed : "+sheet.getRow(j).getCell(k).getStringCellValue());
            testStep = sheet.getRow(j).getCell(k).getStringCellValue();
            String locatorColValue = sheet.getRow(j).getCell(k+1).getStringCellValue();
            if(!locatorColValue.equalsIgnoreCase("NA")) {
                String[] arrOfLocatorAttributes = locatorColValue.split("=",2); // example xpath=<xpathValue> in excel file
                locatorType = arrOfLocatorAttributes[0];
                locatorTypeValue = arrOfLocatorAttributes[1];
            }
            String action = sheet.getRow(j).getCell(k+2).getStringCellValue();
            String value  = sheet.getRow(j).getCell(k+3).getStringCellValue();
            switch(action) {
                case "open_browser":
                    driver = WebDriverManager.initializeDriver(value);
                    configProp = WebDriverManager.prop;
                    System.out.println("Initialized driver and config properties");
                    break;
                case "enter_url":
                    if(value.isEmpty() || value.equalsIgnoreCase("NA")) {
                        driver.get(configProp.getProperty("url"));
                    } else {
                        driver.get(value);
                    }
                    System.out.println("Entered URL successfully");
                    break;
                case "enter_text":
                    if(value.isEmpty() || value.equalsIgnoreCase("NA")) {
                        Assert.fail("undefined or null string value to sendKeys command. " +
                                "Please pass the correct value to sendKeys() method");
                    }
                    returnWebElementForGivenLocatorValue(driver,locatorType,locatorTypeValue).sendKeys(value);
                    System.out.println("entered Valued successfully");
                    break;
                case "click":
                    try {
                        returnWebElementForGivenLocatorValue(driver, locatorType, locatorTypeValue).click();
                    } catch (NoSuchElementException ex) {
                        if(testStep.contains("click Login")) {
                            returnWebElementForGivenLocatorValue(driver,"xpath"
                                    ,"//button[@name=\"login\"]").click();
                        }
                    }
                    System.out.println("clicked Webelement successfully");
                    break;
                case "is_enabled":
                    if(returnWebElementForGivenLocatorValue(driver,locatorType,locatorTypeValue).isEnabled())
                        System.out.println("Webelement is enabled successfully");
                    break;
                case "closeBrowser":
                    driver.close();
                    System.out.println("close browser");
                    break;
                default:
                    if(driver!=null) {
                        driver.quit();
                        System.out.println("quit all browser instances");
                    } else {
                        System.out.println("driver object already Null, not tearDown needed");
                    }
                    break;
            } // switch case according to actions
        } // close for all rows
    }

    public WebElement returnWebElementForGivenLocatorValue(WebDriver driver , String locatorType,String locatorTypeValue) {
        WebElement returnElement=null;
        if(locatorType.equals("id")) {
            returnElement = driver.findElement(By.id(locatorTypeValue));
        } else if(locatorType.equals("name")) {
            returnElement = driver.findElement(By.name(locatorTypeValue));
        } else if(locatorType.equals("xpath")) {
            returnElement = driver.findElement(By.xpath(locatorTypeValue));
        } else if(locatorType.equals("linkText")) {
            returnElement = driver.findElement(By.linkText(locatorTypeValue));
        } else {
            Assert.fail("Provide proper Locator value for the webelement");
        }
        return returnElement;
    }
}
