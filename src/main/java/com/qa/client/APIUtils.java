package com.qa.client;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import com.qa.base.TestBase;

import helper.ExcelHelper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIUtils {

	CloseableHttpResponse response;
	CloseableHttpClient httpclient;
	RequestConfig config;
	HttpHost target;
	HttpHost proxy;
	ExcelHelper excelHelper;
	HttpGet httpGet;
	HttpPut httpPut;

	/**
	 * @author Dharitree Sahoo
	 * @param proxyMap
	 * @param targetMap
	 */

	public void setProxy(HashMap<String, String> proxyMap, HashMap<String, String> targetMap) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(proxyMap.get("proxyURL"), 8080),
				new UsernamePasswordCredentials(proxyMap.get("proxUserName"), proxyMap.get("proxyPassword")));
		httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		target = new HttpHost(targetMap.get("baseURL"), Integer.parseInt(targetMap.get("port")),
				targetMap.get("CMMType"));
		proxy = new HttpHost(proxyMap.get("proxyURL"), 8080);
		config = RequestConfig.custom().setProxy(proxy).build();
	}

	public void setProxyWithoutPort(HashMap<String, String> proxyMap, HashMap<String, String> targetMap) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(proxyMap.get("proxyURL"), 8080),
				new UsernamePasswordCredentials(proxyMap.get("proxUserName"), proxyMap.get("proxyPassword")));
		httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		target = new HttpHost(targetMap.get("baseURL"));
		proxy = new HttpHost(proxyMap.get("proxyURL"), 8080);
		config = RequestConfig.custom().setProxy(proxy).build();
	}

	/**
	 * @author Dharitree Sahoo
	 * @param requestType
	 * @param targetMap
	 * @param headerMap
	 * @param entityString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public CloseableHttpResponse sendRequest(String requestType, HashMap<String, String> targetMap,
			HashMap<String, String> headerMap, String entityString, HashMap<String, String> paramMap, String queryParam)
			throws ClientProtocolException, IOException, URISyntaxException {
		switch (requestType) {
		case "GET":
			if (queryParam.equalsIgnoreCase("Yes")) {
				URIBuilder urlBuilder = new URIBuilder(targetMap.get("endPoint"));
				for (String key : paramMap.keySet()) {
					urlBuilder.addParameter(key, paramMap.get(key));
				}
				httpGet = new HttpGet(urlBuilder.build());
			} else {
				httpGet = new HttpGet(targetMap.get("endPoint"));
			}

			for (Entry<String, String> entry : headerMap.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
			httpGet.setConfig(config);
			try {
				response = httpclient.execute(target, httpGet);
			} catch (Exception e) {
				TestBase.logExtentReportValidationHighlight("Response is not getting " + response, "fail");
			}
			break;
		case "GETWithParam":

			URIBuilder urlBuilder = new URIBuilder(targetMap.get("endPoint"));
			for (String key : paramMap.keySet()) {
				urlBuilder.addParameter(key, paramMap.get(key));
			}
			// builder.setParameter("Ids", "").setParameter("startDt",
			// "05\\/01\\/2018").setParameter("IsNewRequest", "1");
			httpGet = new HttpGet(urlBuilder.build());

			for (Entry<String, String> entry : headerMap.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
			httpGet.setConfig(config);
			try {
				response = httpclient.execute(target, httpGet);
			} catch (Exception e) {
				TestBase.logExtentReportValidationHighlight("Response is not getting " + response, "fail");
			}
			break;
		case "POST":
			HttpPost httppost = new HttpPost(targetMap.get("endPoint"));
			for (Entry<String, String> entry : headerMap.entrySet()) {
				httppost.addHeader(entry.getKey(), entry.getValue());
			}
			httppost.setConfig(config);
			httppost.setEntity(new StringEntity(entityString)); // for payload
			try {
				response = httpclient.execute(target, httppost);
			} catch (Exception e) {
				TestBase.logExtentReportValidationHighlight("Response is not getting " + response, "fail");
			}
			break;
		case "PUT":

			httpPut = new HttpPut(targetMap.get("endPoint"));

			for (Entry<String, String> entry : headerMap.entrySet()) {
				httpPut.addHeader(entry.getKey(), entry.getValue());
			}
			httpPut.setConfig(config);
			response = httpclient.execute(target, httpPut);
			break;

		case "DELETE":
			HttpDelete httpDelete = new HttpDelete(targetMap.get("endPoint"));
			for (Entry<String, String> entry : headerMap.entrySet()) {
				httpDelete.addHeader(entry.getKey(), entry.getValue());
			}
			httpDelete.setConfig(config);
			response = httpclient.execute(target, httpDelete);
			break;
		default:
			break;
		}
		return response;
	}

	/**
	 * @author Dharitree Sahoo
	 * @param requestType
	 * @param token
	 * @param proxy
	 * @param targetURL
	 * @param entityString
	 * @return response as String
	 * @throws IOException
	 */
	public Response sendRequestWithRestAssured(String requestType, String token, String proxy, String targetURL,
			String entityString, HashMap<String, String> headerMap) throws IOException {

		Response responseData = null;
		JSONObject jsonObj;
		String response = null;

		try {
			switch (requestType) {
			case "GET":
				responseData = given().proxy(proxy).relaxedHTTPSValidation().headers("Authorization", "Bearer " + token)
						.headers("Authorization", "Bearer " + token).headers("Authorization", "Bearer " + token)
						.headers("Authorization", "Bearer " + token).when().get(targetURL).then().statusCode(200)
						.contentType("application/json; charset=utf-8").extract().response();
				jsonObj = new JSONObject(responseData.prettyPrint());
				responseData.prettyPrint();
				response = responseData.asString();
				System.out.println(response);
				break;
			case "POST":
				try {
					responseData = given().proxy("http://z_eaglelending_prox:H8s8d%3D%23spL@vsproxynp2:8080")
							.relaxedHTTPSValidation().headers("Authorization", "Bearer " + token)
							.headers("Content-Type", "application/json").headers("channelId", "lending")
							.headers("correlationId", "1313").body(entityString).when().post(targetURL).then()
							.contentType(ContentType.JSON).extract().response();
				} catch (Exception e) {
					TestBase.logExtentReportValidationHighlight("Could not get any response", "fail");
				}

				break;

			default:
				break;
			}
		} catch (JSONException e) {
			TestBase.logExtentReportValidationHighlight("Could not get any response", "fail");
		}
		return responseData;

	}

}
