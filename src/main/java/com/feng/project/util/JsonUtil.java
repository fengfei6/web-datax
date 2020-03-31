package com.feng.project.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static void testComplexJSONStrToJSONObject(String json) {
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject job = jsonObject.getJSONObject("job");
	     //获取JSONObject中的数据
//	    JSONObject setting = job.getJSONObject("setting");
//	    JSONObject speed = setting.getJSONObject("speed");   
//	    String channel = speed.getString("channel");
//	    JSONObject errorLimit = setting.getJSONObject("errorLimit");   
//	    Integer record = errorLimit.getInteger("record");
//	    Float percentage = errorLimit.getFloat("percentage");
	    JSONArray content = job.getJSONArray("content");
	    JSONObject object = (JSONObject)content.get(0);
	    getParams(object,"reader");
	    getParams(object,"writer");
	}

	private static Map<String,String> getParams(JSONObject object,String type){
		Map<String,String> map = new HashMap<String, String>();
		JSONObject reader = object.getJSONObject(type);
		String readerType = reader.getString("name");
		map.put("name", readerType);
		JSONObject param1 = reader.getJSONObject("parameter");
		String username = param1.getString("username");
		map.put("username", username);
		String password = param1.getString("password");
		map.put("password", password);
		JSONObject connection = param1.getJSONArray("connection").getJSONObject(0);
		String table = connection.getJSONArray("table").getString(0);
		map.put("table", table);
		String jdbcUrl = connection.getJSONArray("jdbcUrl").getString(0);
		map.put("jdbcUrl", splitUrl(jdbcUrl));
		return map;
	}
	
	private static String splitUrl(String url) {
		StringBuilder result = new StringBuilder();
		String[] array1 = url.split("/");
		result.append(array1[3]+",");
		String[] array2 = array1[2].split(":");
		result.append(array2[0]+",");
		result.append(array2[1]);
		return result.toString();
	}
	
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("D:\\test.json"); 
	     StringBuilder sb = new StringBuilder();
	    	int ch = 0;  
	    	while((ch = fr.read())!=-1 ){   
	    		sb.append((char)ch);   
	    	} 
		JsonUtil.testComplexJSONStrToJSONObject(sb.toString());
	}
}
