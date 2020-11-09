package com.qa.client;

import org.testng.annotations.DataProvider;

import helper.ExcelHelper;

public class DataProviderUtil {
ExcelHelper excelHelper;
@DataProvider(name = "verifyStatusCodeInGET")
public Object[][] dataProviderMethodForGET() throws Exception {
excelHelper = new ExcelHelper(System.getProperty("user.dir") + "/resources/CLS.xlsx");
Object[][] userData = excelHelper.getDataAsMap("dataProviderGET");
return userData;
}
@DataProvider(name = "verifyStatusCodeInPOST")
public Object[][] dataProviderMethodForPOST() throws Exception {
excelHelper = new ExcelHelper(System.getProperty("user.dir") + "/resources/CLS.xlsx");
Object[][] userData = excelHelper.getDataAsMap("dataProviderPOST");
return userData;
}


}

