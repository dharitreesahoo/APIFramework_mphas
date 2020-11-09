package com.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import util.ExtentManager;

public class TestBase {
	public int RESPONSE_STATUS_CODE_200 = 200;
	public int RESPONSE_STATUS_CODE_500 = 500;
	public int RESPONSE_STATUS_CODE_400 = 400;
	public int RESPONSE_STATUS_CODE_401 = 401;
	public int RESPONSE_STATUS_CODE_201 = 201;

	public Properties prop;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ExtentTest childTest;
	
	
	public TestBase(){
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/qa/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@BeforeSuite
	public void beforeSuite()
	{
		extent = ExtentManager.getInstance();
	}
	@BeforeTest
	public void beforeTest()
	{
		test = extent.createTest(getClass().getSimpleName());
	}
	@AfterMethod
	public void afterMethod(ITestResult result)
	{
		if(result.getStatus()==ITestResult.FAILURE)
		{
			childTest.log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName()+"failed",ExtentColor.RED));
		}else if(result.getStatus()==ITestResult.SUCCESS)
		{
			
		}else if(result.getStatus()==ITestResult.SKIP)
		{
			test.log(Status.SKIP, result.getThrowable());
		}
		extent.flush();
	}
	
	public static void logExtentReport(String steps , String status){
		if(status.toUpperCase().equals("PASS"))
		{
			childTest.log(Status.PASS, MarkupHelper.createLabel(steps, ExtentColor.BLUE));
		}else{
			childTest.log(Status.PASS, MarkupHelper.createLabel(steps, ExtentColor.RED));
		}
		
	}
	public static void logExtentReportValidationHighlight(String steps , String status){
		if(status.toUpperCase().equals("PASS"))
		{
			childTest.log(Status.PASS, MarkupHelper.createLabel(steps, ExtentColor.GREEN));
		}else{
			childTest.log(Status.PASS, MarkupHelper.createLabel(steps, ExtentColor.RED));
		}
		
	}

}
