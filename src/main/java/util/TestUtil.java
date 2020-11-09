package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
ObjectMapper mapper;;
public static String getValueByJPath(JSONObject responsejson, String jpath){
Object obj = responsejson;
for(String s : jpath.split("/"))
if(!s.isEmpty())
if(!(s.contains("[") || s.contains("]")))
obj = ((JSONObject) obj).get(s);
else if(s.contains("[") || s.contains("]"))
obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0])).get(Integer.parseInt(s.split("\\[")[1].replace("]", "")));
return obj.toString();
}
public static boolean compareJSONResponse(String actualResponse , String expectedJSON) throws JsonProcessingException, IOException
{
JSONObject firmOfferCreditJson = new JSONObject(actualResponse);
JSONObject expectedJson = new JSONObject(expectedJSON);
ObjectMapper mapper = new ObjectMapper();

JsonNode tree1 = mapper.readTree(firmOfferCreditJson.toString());
JsonNode tree2 = mapper.readTree(expectedJson.toString());
if(tree1.equals(tree2))
{
return true;
}
return false;
}
public String getJSONPayload(String strPayLoad , String filePath) throws JsonGenerationException, JsonMappingException, IOException
{
//String filePath = System.getProperty("user.dir") + "/src/main/java/com/qa/payLoadData/InstaBase_Payload.json";
mapper = new ObjectMapper();
mapper.writeValue(new File(filePath),strPayLoad);
File file = new File(filePath);
FileInputStream fis = new FileInputStream(file);
byte[] data = new byte[(int) file.length()];
fis.read(data);
fis.close();
String inputPayload = new String(data, "UTF-8");
//String inputPayloadJson1 = inputPayload.replaceAll("^\"|\"$", "");
//System.out.println(inputPayloadJson1);

String inputPayloadJson = StringEscapeUtils.unescapeJava(inputPayload).replaceAll("^\"|\"$", "");
return inputPayloadJson;
}

}

