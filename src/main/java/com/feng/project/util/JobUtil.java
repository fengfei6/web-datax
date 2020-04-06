package com.feng.project.util;


import com.feng.project.domain.CronJob;
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
		sb.append("{\"job\": {\"setting\": {\"speed\": {\"channel\": 3},\"errorLimit\": {\"record\": 5,\"percentage\": 0.02}}," +
				"\"content\": [{ \"reader\":{\"name\": \"").append(reader);
		sb.append("\",\"parameter\":{\"username\":\"").append(sdatasource.getUsername());
		sb.append("\",\"password\":\"").append(sdatasource.getPassword()).append("\",");
		if(job.getReaderTable() != null && !job.getReaderTable().trim().equals("null")) {
			sb.append("\"column\": [\"*\"],");
		}
		sb.append("\"connection\": [{");
		if(job.getReaderTable() != null && !job.getReaderTable().trim().equals("null")) {
			sb.append("\"table\": [\"").append(job.getReaderTable()).append("\"],");
		}
		if(job.getQuerySql() != null && job.getQuerySql().trim() != ""){
			sb.append("\"querySql\": [\"").append(job.getQuerySql()).append("\"],");
		}
		sb.append("\"jdbcUrl\": [\"").append(sjdbcUrl);
		sb.append("\"]}]}},");
		sb.append("\"writer\": {\"name\": \"").append(writer);
		sb.append("\",\"parameter\":{\"username\":\"").append(tdatasource.getUsername());
		sb.append("\",\"password\":\"").append(tdatasource.getPassword()).append("\",");
		sb.append("\"column\": [\"*\"],");
		if(job.getWriterPresql() != null) {
			sb.append("\"preSql\": [\"").append(job.getWriterPresql()).append("\"],");
		}
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

	public static void getJsonFileByContent(Job job) throws IOException {
		File file = new File("src/main/resources/static/file/"+job.getName()+".json");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(job.getJsonContent().getBytes());
		bos.close();
	}


	public static void getJsonfileForCronJob(Datasource sdatasource, Datasource tdatasource, CronJob cronJob) throws IOException {
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
		sb.append("{\"job\": {\"setting\": {\"speed\": {\"channel\": 3},\"errorLimit\": {\"record\": 5,\"percentage\": 0.02}}," +
				"\"content\": [{ \"reader\":{\"name\": \"").append(reader);
		sb.append("\",\"parameter\":{\"username\":\"").append(sdatasource.getUsername());
		sb.append("\",\"password\":\"").append(sdatasource.getPassword()).append("\",");
		if(cronJob.getReaderTable() != null) {
			sb.append("\"column\": [\"*\"],");
		}
		sb.append("\"connection\": [{");
		if(cronJob.getReaderTable() != null) {
			sb.append("\"table\": [\"").append(cronJob.getReaderTable()).append("\"],");
		}
		if(cronJob.getQuerySql() != null){
			sb.append("\"querySql\": [\"").append(cronJob.getQuerySql()).append("\"],");
		}
		sb.append("\"jdbcUrl\": [\"").append(sjdbcUrl);
		sb.append("\"]}]}},");
		sb.append("\"writer\": {\"name\": \"").append(writer);
		sb.append("\",\"parameter\":{\"username\":\"").append(tdatasource.getUsername());
		sb.append("\",\"password\":\"").append(tdatasource.getPassword()).append("\",");
		sb.append("\"column\": [\"*\"],");
		if(cronJob.getWriterPresql() != null) {
			sb.append("\"preSql\": [\"").append(cronJob.getWriterPresql()).append("\"],");
		}
		sb.append("\"connection\": [{\"table\": [\"").append(cronJob.getWriterTable());
		sb.append("\"],\"jdbcUrl\": \"").append(tjdbcUrl);
		sb.append("\"}]}}}]}}");

		File file = new File("src/main/resources/static/file/"+cronJob.getName()+".json");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(sb.toString().getBytes());
		bos.close();
	}
}
