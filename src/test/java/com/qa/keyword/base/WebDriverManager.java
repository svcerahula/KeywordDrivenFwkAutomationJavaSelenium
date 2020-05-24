package com.qa.keyword.base;

import com.qa.keyword.utils.FileOpsUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/*
WebDriverManager class to initialize WebDriver object and config properties file required for driver configurations.
 */
public class WebDriverManager {
    public static ThreadLocal<WebDriver> driver=new ThreadLocal<>();
    public static String browserName;
    public static Properties prop = new Properties();

    public static WebDriver initializeDriver(String browserValue) throws IOException, ClassNotFoundException {
        loadProps(System.getProperty("user.dir")
                        +"\\src\\test\\resources\\config\\config.properties");

        // if browser value is not passed, pick from config properties file.
        browserName = (browserValue!=null) ? browserValue : prop.getProperty("browser");

        if(browserName.equals("chrome")) {
             System.setProperty("webdriver.chrome.driver",prop.getProperty("chromeDriverPath"));
             driver.set(new ChromeDriver());
        }

        driver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get().manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
        driver.get().manage().window().maximize();
        return driver.get();
    }

    public static void loadProps(String filePath) throws IOException, ClassNotFoundException {
         FileInputStream fis = new FileInputStream(filePath);
        prop.load(fis);
    }

}
