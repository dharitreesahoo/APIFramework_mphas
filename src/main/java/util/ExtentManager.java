package util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class ExtentManager {
	static String day;
	static String timestamp;
	public static String downloadFilePath;
	
	private static ExtentReports extent;
	static String location;
	
	public static ExtentReports getInstance(){
		if(extent == null){
			day = new SimpleDateFormat("MMM dd").format(new Date());
			timestamp = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			location = System.getProperty("user.dir")+"\\extent_report\\"+day+"\\"+timestamp+"executionreport.html";
			File file = new File(System.getProperty("user.dir")+"\\extent_report\\"+day+"\\"+timestamp);
			file.mkdir();
			return createInstance(location);
		}
		else{
			return extent;
		}
	}
	
	public static ExtentReports createInstance(String fileName){
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Automation Report");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		return extent;
	}

}
