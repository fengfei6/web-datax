package com.feng.project.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static Map<String,String> testComplexJSONStrToJSONObject(String json) {
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject job = jsonObject.getJSONObject("job");
		Map<String,String> map = new HashMap<String, String>();
	     //获取JSONObject中的数据
//	    JSONObject setting = job.getJSONObject("setting");
//	    JSONObject speed = setting.getJSONObject("speed");   
//	    String channel = speed.getString("channel");
//	    JSONObject errorLimit = setting.getJSONObject("errorLimit");   
//	    Integer record = errorLimit.getInteger("record");
//	    Float percentage = errorLimit.getFloat("percentage");
	    JSONArray content = job.getJSONArray("content");
	    JSONObject object = (JSONObject)content.get(0);
//	    map.put("tabler",getTable(object,"reader"));
//		map.put("tablew",getTable(object,"writer"));
//	    map.put("reader",getParams(object,"reader"));
//	    map.put("writer",getParams(object,"writer"));
	    map.put("reader", getDbType(object, "reader"));
	    map.put("writer", getDbType(object, "writer"));
	    return map;
	}

	private static String getDbType(JSONObject object,String type){
		JSONObject reader = object.getJSONObject(type);
		String dbtype = reader.getString("name");
		return dbtype;
	}
	
	private static String getParams(JSONObject object,String type){
		JSONObject reader = object.getJSONObject(type);
		String readerType = reader.getString("name");

		JSONObject param1 = reader.getJSONObject("parameter");
		String username = param1.getString("username");
		String password = param1.getString("password");

		JSONObject connection = param1.getJSONArray("connection").getJSONObject(0);
		String table = connection.getJSONArray("table").getString(0);

		String jdbcUrl = "";
		if(type.equalsIgnoreCase("reader")) {
			jdbcUrl = connection.getJSONArray("jdbcUrl").getString(0);
		}
		if(type.equalsIgnoreCase("writer")) {
			jdbcUrl = connection.getString("jdbcUrl");
		}
		return splitUrl(jdbcUrl);
	}

	private static String getTable(JSONObject object,String type){
		JSONObject reader = object.getJSONObject(type);
		String readerType = reader.getString("name");

		JSONObject param1 = reader.getJSONObject("parameter");
		String username = param1.getString("username");
		String password = param1.getString("password");

		JSONObject connection = param1.getJSONArray("connection").getJSONObject(0);
		String table = connection.getJSONArray("table").getString(0);
		return table;
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

	public static String[] getParamsArray(String params){
		String[] array = params.split(",");
		return array;
	}

	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("D://test.json");
	     StringBuilder sb = new StringBuilder();
	    	int ch = 0;
	    	while((ch = fr.read())!=-1 ){
	    		sb.append((char)ch);
	    	}
		Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(sb.toString());

	    for(Map.Entry<String,String> entey:map.entrySet()){
	    	System.out.println(entey.getKey()+" "+entey.getValue());
		}
	}
}
