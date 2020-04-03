package com.feng.project.util;


import com.feng.project.domain.Datasource;
import com.feng.project.domain.Job;
import com.feng.project.service.DatasourceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JobUtil {

	public static void getJsonfile(Datasource sdatasource,Datasource tdatasource,Job job) throws IOException {
		String reader= null;
		String sjdbcUrl= null;

		if("mysql".equalsIgnoreCase(sdatasource.getType())){
			reader = "mysqlreader";
			sjdbcUrl = Constants.MYSQL_JDBC_NAME+sdatasource.getIp()+":"+sdatasource.getPort()+"/"+sdatasource.getDbname();
		}else if("oracle".equalsIgnoreCase(sdatasource.getType())){
			reader = "oraclereader";
			sjdbcUrl = Constants.ORACLE_THIN_JDBC_NAME+sdatasource.getIp()+":"+sdatasource.getPort()+"/"+sdatasource.getDbname();
		}

		String writer= null;
		String tjdbcUrl= null;
		if("mysql".equalsIgnoreCase(tdatasource.getType())){
			writer = "mysqlwriter";
			tjdbcUrl = Constants.MYSQL_JDBC_NAME+tdatasource.getIp()+":"+tdatasource.getPort()+"/"+tdatasource.getDbname();
		}else if("oracle".equalsIgnoreCase(tdatasource.getType())){
			writer = "oraclewriter";
			tjdbcUrl = Constants.ORACLE_THIN_JDBC_NAME+tdatasource.getIp()+":"+tdatasource.getPort()+"/"+tdatasource.getDbname();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{\"job\": {\"setting\": {\"speed\": {\"channel\": 3},\"errorLimit\": {\"record\": 0,\"percentage\": 0.02}}," +
				"\"content\": [{ \"reader\":{\"name\": \"").append(reader);
		sb.append("\",\"parameter\":{\"username\":\"").append(sdatasource.getUsername());
		sb.append("\",\"password\":\"").append(sdatasource.getPassword());
		sb.append("\",\"column\": [\"*\"],");
		sb.append("\"connection\": [{\"table\": [\"").append(job.getReaderTable());
		sb.append("\"],\"jdbcUrl\": [\"").append(sjdbcUrl);
		sb.append("\"]}]}},");
		sb.append("\"writer\": {\"name\": \"").append(writer);
		sb.append("\",\"parameter\":{\"username\":\"").append(tdatasource.getUsername());
		sb.append("\",\"password\":\"").append(tdatasource.getPassword());
		sb.append("\",\"column\": [\"*\"],");
		sb.append("\"connection\": [{\"table\": [\"").append(job.getWriterTable());
		sb.append("\"],\"jdbcUrl\": \"").append(tjdbcUrl);
		sb.append("\"}]}}}]}}");
	
		File file = new File("src/main/resources/static/file/"+job.getName()+".json");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(sb.toString().getBytes());
		bos.close();
	}
}
