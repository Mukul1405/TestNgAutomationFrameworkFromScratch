package org.orangehrmautomation.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class Base {

    private static final Logger logger = LogManager.getLogger(Base.class);

    public WebDriver driver;
    public Properties prop;
    public ExtentSparkReporter extentSparkReporter;
    public ExtentReports extentReports;
    public ExtentTest extentTest;

    public Base() {
        try {
            prop = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/configs.properties");
            prop.load(fileInputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Before Suite");
        try {
            extentSparkReporter  = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/extentReport.html");
            extentReports = new ExtentReports();
            extentReports.attachReporter(extentSparkReporter);

            extentSparkReporter.config().setDocumentTitle("Simple Automation Report");
            extentSparkReporter.config().setReportName("Test Report");
            extentSparkReporter.config().setTheme(Theme.DARK);
            extentSparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("Extent Report setup complete.");
    }

    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("chrome") String browser, Method method) {
        System.out.println("Before Method");

        // Initialize extentTest for the current test
        extentTest = extentReports.createTest(method.getName());

        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            logger.info("Firefox Driver Setup complete.");
        } else {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            logger.info("Chrome Driver Setup complete.");
        }

        String url = prop.getProperty("url");
        driver.manage().window().maximize();
        driver.get(url);
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (extentTest != null) {
            if(result.getStatus() == ITestResult.FAILURE) {
                extentTest.log(Status.FAIL, result.getThrowable());
            }
            else if(result.getStatus() == ITestResult.SUCCESS) {
                extentTest.log(Status.PASS, result.getMethod().getMethodName());
            }
            else {
                extentTest.log(Status.SKIP, result.getMethod().getMethodName());
            }
        }

        if (result.getThrowable() != null) {
            logger.info(result.getThrowable().getMessage());
        }
    }

    @AfterTest
    public void afterTest() {
        if (driver != null) {
            driver.quit();
            logger.info("Driver closed after test.");
        }

        extentReports.flush();
        logger.info("Flushing extent-report");
    }

    @AfterSuite
    public void teardownSuite() {
        System.out.println("After Suite");
    }
}
