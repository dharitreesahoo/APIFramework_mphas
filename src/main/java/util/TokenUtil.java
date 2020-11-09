package util;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import com.qa.client.APIUtils;

import io.restassured.response.Response;

public class TokenUtil {
public String resp_TokenID;
public String final_resp_TokenID;
APIUtils apiUtil;
public String getToken(String tokenURL , HashMap<String,String> bodyContent) throws JSONException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
String CONTEXT_PATH=tokenURL;

Response response = given().
contentType("application/x-www-form-urlencoded").
formParams(bodyContent).
when().
post(CONTEXT_PATH).
then().
statusCode(200).
contentType("application/json;charset=utf-8").
extract().
response();
JSONObject jsonObj = new JSONObject(response.prettyPrint());
        String access_tk = jsonObj.getString("access_token");
        response.prettyPrint();
String responseData = response.asString();
int code=response.getStatusCode();
Assert.assertEquals(code, 200);
resp_TokenID=responseData;
        String[] dataStr = resp_TokenID.split(":");
        String[] dataFinal = dataStr[1].split(",");
        final_resp_TokenID=access_tk;
return final_resp_TokenID;

}
public void getTokenNew(String tokenURL , HashMap<String,String> bodyContent) throws JSONException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
String CONTEXT_PATH=tokenURL;
HttpClient httpClient = HttpClients
           .custom()
           .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
           .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
           .build();
HttpPost postRequest = new HttpPost("https://cidmspngfd.corp.frbnp2.com:9031/as/token.oauth2");
       
        //Set the API media type in http content-type header
        postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
         
        //Set the request post body
        postRequest.setEntity((HttpEntity) bodyContent);
         
        //Send the request; It will immediately return the response in HttpResponse object if any
        HttpResponse response = httpClient.execute(postRequest);
         
        //verify the valid error code first
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

}
}

