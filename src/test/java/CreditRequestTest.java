import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.APIUtils;
import com.qa.payLoadData.BorrowerDetails;
import com.qa.payLoadData.CreditRequest_POJO;

import helper.ExcelHelper;
import util.TestUtil;
import util.TokenUtil;

public class CreditRequestTest extends TestBase {
	HashMap<String, String> proxyMap;
	HashMap<String, String> targetMap;
	HashMap<String, String> headerMap;
	HashMap<String, String> paramMap;
	HashMap<String, String> tokenDetails;

	ExcelHelper excelHelper;
	ObjectMapper mapper;
	TokenUtil tokenUtil;
	TestUtil testUtil;
	JSONObject responseJson;
	APIUtils apiUtils;
	CloseableHttpResponse closebaleHttpResponse;
	String authToken;
	String sheetName = "CreditRequest";
	String inputPayloadJson;
	String strPayLoad;
	String tokenURL;
	String requestType;
	String expectedVal;
	String responseString;
	String queryParam;
	String filePath = System.getProperty("user.dir") + "/src/main/java/com/qa/payLoadData/CreditRequest_Payload.json";
	int row;

	@BeforeClass
	void startClass() throws Exception {
		excelHelper = new ExcelHelper(System.getProperty("user.dir") + "/resources/PLP.xlsx");
		tokenUtil = new TokenUtil();
		tokenDetails = new HashMap<String, String>();
		tokenDetails.put("grant_type", "");
		tokenDetails.put("client_secret", "");
		tokenDetails.put("scope", "");
		tokenDetails.put("client_id", "");
		tokenDetails.put("Content-Type", "application/x-www-form-urlencoded");
		tokenURL = "";
		authToken = tokenUtil.getToken(tokenURL, tokenDetails);

		proxyMap = new HashMap<String, String>();
		proxyMap.put("proxyURL", prop.getProperty("proxyURL"));
		proxyMap.put("proxUserName", prop.getProperty("proxUserName"));
		proxyMap.put("proxyPassword", prop.getProperty("proxyPassword"));

		headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("correlationId", "");
		headerMap.put("Authorization", "Bearer" + " " + authToken);

		targetMap = new HashMap<String, String>();
		apiUtils = new APIUtils();
		testUtil = new TestUtil();
	}

	@Test(priority = 1, enabled = true)
	public void verifyPrerequiSite() throws ClientProtocolException, IOException, URISyntaxException {
		childTest = test.createNode("TC1-Prerequisite - verify  response Status code");
		row = excelHelper.getCellRowNum(sheetName, "TCID", "TC1");
		strPayLoad = excelHelper.getXLValue(sheetName, "payLoad", row);
		requestType = excelHelper.getXLValue(sheetName, "HTTP Method Type", row);
		expectedVal = excelHelper.getXLValue(sheetName, "ExpectedVal", row);
		targetMap = getTargetURL(sheetName, row);
		if (!strPayLoad.equalsIgnoreCase("NA")) {
			inputPayloadJson = testUtil.getJSONPayload(strPayLoad, filePath);
		} else {
			inputPayloadJson = "";
		}
		apiUtils.setProxy(proxyMap, targetMap);
		closebaleHttpResponse = apiUtils.sendRequest(requestType, targetMap, headerMap, inputPayloadJson, paramMap,
				queryParam);
		responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		responseJson = new JSONObject(responseString);
		System.out.println("The response from API is:" + responseJson);
		int actualStatusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println(actualStatusCode);
		if (actualStatusCode == Integer.parseInt(expectedVal)) {
			TestBase.logExtentReportValidationHighlight(
					"Actual Status code>>" + actualStatusCode + " and Expected status code>>" + expectedVal, "pass");
		} else if (actualStatusCode == 407) {
			TestBase.logExtentReportValidationHighlight(
					"Actual Status code>>" + actualStatusCode + " and Expected status code>>" + expectedVal, "fail");
			TestBase.logExtentReport(
					"Client error codes indicating that there was a problem with the request>> 'Could not get any response'",
					"fail");
			Assert.assertTrue(false);
		} else {
			TestBase.logExtentReportValidationHighlight("Actual Status code>>" + actualStatusCode, "fail");
		}
	}

	@Test(priority = 2, enabled = true, dependsOnMethods = "verifyPrerequiSite")
	public void verifyResponsePayload() throws ClientProtocolException, IOException, URISyntaxException {
		childTest = test.createNode("TC2- Display  success response");
		apiUtils = new APIUtils();

		row = excelHelper.getCellRowNum(sheetName, "TCID", "TC2");
		strPayLoad = excelHelper.getXLValue(sheetName, "payLoad", row);
		requestType = excelHelper.getXLValue(sheetName, "HTTP Method Type", row);

		targetMap = getTargetURL(sheetName, row);
		if (!strPayLoad.equalsIgnoreCase("NA")) {
			inputPayloadJson = testUtil.getJSONPayload(strPayLoad, filePath);
		} else {
			inputPayloadJson = "";
		}

		apiUtils.setProxy(proxyMap, targetMap);
		closebaleHttpResponse = apiUtils.sendRequest(requestType, targetMap, headerMap, inputPayloadJson, paramMap,
				queryParam);
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("The response from API is:" + responseJson);
		TestBase.logExtentReport("Success JSON response as expected >>" + responseJson, "pass");
	}

	@Test(priority = 3, enabled = true, dependsOnMethods = "verifyPrerequiSite")
	public void verifyResponsePayloadWithInvalidToken()
			throws ClientProtocolException, IOException, URISyntaxException {
		childTest = test.createNode("TC3- Verify response with Invalid token");
		apiUtils = new APIUtils();

		row = excelHelper.getCellRowNum(sheetName, "TCID", "TC3");
		strPayLoad = excelHelper.getXLValue(sheetName, "payLoad", row);
		requestType = excelHelper.getXLValue(sheetName, "HTTP Method Type", row);
		expectedVal = excelHelper.getXLValue(sheetName, "ExpectedVal", row);
		headerMap.put("Authorization", "Bearer" + " " + authToken + "invalidToken");

		targetMap = getTargetURL(sheetName, row);
		if (!strPayLoad.equalsIgnoreCase("NA")) {
			inputPayloadJson = testUtil.getJSONPayload(strPayLoad, filePath);
		} else {
			inputPayloadJson = "";
		}

		apiUtils.setProxy(proxyMap, targetMap);
		closebaleHttpResponse = apiUtils.sendRequest(requestType, targetMap, headerMap, inputPayloadJson, paramMap,
				queryParam);
		responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		responseJson = new JSONObject(responseString);
		System.out.println("The response from API is:" + responseJson);
		TestBase.logExtentReport("JSON response for Invalid token as expected >>" + responseJson, "pass");
		int actualStatusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		if (actualStatusCode == Integer.parseInt(expectedVal)) {
			TestBase.logExtentReportValidationHighlight(
					"Actual Status code>>" + actualStatusCode + " and Expected status code>>" + expectedVal, "pass");
		} else {
			TestBase.logExtentReportValidationHighlight(
					"Actual Status code>>" + actualStatusCode + " and Expected status code>>" + expectedVal, "fail");
		}
	}

	// Sample code for POJO type
	@Test(priority = 1, enabled = false)
	public void verifyPrerequiSiteNew() throws ClientProtocolException, IOException, URISyntaxException {
		childTest = test.createNode("Prerequisite - verify Request Status");
		apiUtils = new APIUtils();
		String creditReportPayLoadStr;
		int row = excelHelper.getCellRowNum(sheetName, "TCID", "TC1");
		String strPayLoad = excelHelper.getXLValue(sheetName, "payLoad", row);
		requestType = excelHelper.getXLValue(sheetName, "HTTP Method Type", row);
		expectedVal = excelHelper.getXLValue(sheetName, "ExpectedVal", row);
		targetMap = getTargetURL(sheetName, row);
		if (!strPayLoad.equalsIgnoreCase("NA")) {
			mapper = new ObjectMapper();
			BorrowerDetails objBorrower = new BorrowerDetails("NICK", "J", "BLANK", "666682881", "1944-03-01",
					"HOPKINSVILLE", "42240", "1647 PYLE LN", "KY");

			CreditRequest_POJO objCreditReport = new CreditRequest_POJO("Submit", "Other", objBorrower);

			mapper.writeValue(new File(System.getProperty("user.dir")
					+ "/src/main/java/com/qa/payLoadData/CreditRequestPayLoad_JSON.json"), objCreditReport);
			creditReportPayLoadStr = mapper.writeValueAsString(objCreditReport);
			apiUtils.setProxy(proxyMap, targetMap);
			closebaleHttpResponse = apiUtils.sendRequest(requestType, targetMap, headerMap, creditReportPayLoadStr,
					paramMap, queryParam);
			responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");

			responseJson = new JSONObject(responseString);
			System.out.println("The response from API is:" + responseJson);
			TestBase.logExtentReport("Success JSON response as expected >>" + responseJson, "pass");

			int actualStatusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
			System.out.println(actualStatusCode);
		}
	}

	// *********************************************************************************************

	/**
	 *
	 * @param sheetName
	 * @param row
	 * @return
	 */
	public HashMap<String, String> getTargetURL(String sheetName, int row) {
		HashMap<String, String> targetMap = new HashMap<String, String>();
		String baseURL = excelHelper.getXLValue(sheetName, "baseURL", row);
		String endPoint = excelHelper.getXLValue(sheetName, "End Point", row);
		String port = excelHelper.getXLValue(sheetName, "port", row);
		String CMMType = excelHelper.getXLValue(sheetName, "CMMType", row);
		targetMap.put("baseURL", baseURL);
		targetMap.put("endPoint", endPoint);
		targetMap.put("port", port);
		targetMap.put("CMMType", CMMType);
		return targetMap;
	}
}
