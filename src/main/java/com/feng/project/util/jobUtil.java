package com.feng.project.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class jobUtil {
	public static void main(String[] args) throws IOException {
		String username = "root";
		String password = "root";
		String table = "user";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/database";
		getJsonfile(username,password,table,jdbcUrl);
	}
	private static void getJsonfile(String username,String password,String table,String jdbcUrl) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"job\": {\"setting\": {\"speed\": {\"channel\": 3},\"errorLimit\": {\"record\": 0,\"percentage\": 0.02}},\"content\": [{ \"reader\":{\"name\": \"mysqlreader\",\"parameter\":{");
		sb.append("\"username\":\"").append(username);
		sb.append("\",\"password\":\"").append(password);
		sb.append("\",\"column\": [\"*\"],\"splitPk\": \"id\",");
		sb.append("\"connection\": [{\"table\": [\"").append(table);
		sb.append("\"],\"jdbcUrl\": [\"").append(jdbcUrl);
		sb.append("\"]}]}},");
		sb.append("\"writer\": {\"name\": \"mysqlwriter\",\"parameter\":{");
		sb.append("\"username\":\"").append(username);
		sb.append("\",\"password\":\"").append(password);
		sb.append("\",\"column\": [\"*\"],\"splitPk\": \"id\",");
		sb.append("\"connection\": [{\"table\": [\"").append(table);
		sb.append("\"],\"jdbcUrl\": [\"").append(jdbcUrl);
		sb.append("\"]}]}}}]}}");
	
		File file = new File("d:/test.json");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(sb.toString().getBytes());
		bos.close();
	}
}
