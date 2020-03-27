package com.feng.project.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.feng.project.connector.Connector;
import com.feng.project.connector.MysqlConnector;
import com.feng.project.connector.OracleConnector;
import com.feng.project.domain.Datasource;




public class ConnectionUtil {

	/**
	 * @param datasource
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static boolean isConn(Datasource datasource, String type){
		Connector connector = null;
		Connection conn = null;
		if(type.equalsIgnoreCase("mysql")) {
			connector = new MysqlConnector();
		}else if(type.equalsIgnoreCase("oracle")) {
			connector = new OracleConnector();
		}
		try {
			Class.forName(connector.getDriver());
			conn = DriverManager.getConnection(connector.getUrl(datasource),datasource.getUsername(),datasource.getPassword());
			if(!conn.isClosed()) {
				conn.isClosed();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param database
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Connection getConn(Datasource datasource,String type){
		Connector connector = null;
		Connection conn = null;
		if(type.equalsIgnoreCase("mysql")) {
			connector = new MysqlConnector();
		}else if(type.equalsIgnoreCase("oracle")) {
			connector = new OracleConnector();
		}
		try {
			Class.forName(connector.getDriver());
			conn = DriverManager.getConnection(connector.getUrl(datasource),datasource.getUsername(),datasource.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static DatabaseMetaData getMetaDate(Connection conn){
		if(conn  == null) {return null;}
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = conn.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return databaseMetaData;
	}
	
	/**
	 * @param metaData
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTables(DatabaseMetaData metaData){
		if(metaData == null ) {return null;}
		List<String> tableNameList = new ArrayList<String>();
		ResultSet tables;
		try {
			tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (tables.next()) {
				String TABLE_NAME = tables.getString("TABLE_NAME") ;
				tableNameList.add(TABLE_NAME);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableNameList;
	}
	
	/**
	 * @param metaData
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,String> getColumn(DatabaseMetaData metaData,String tableName){
		if(metaData == null ) {return null;}
		Map<String,String> map = new HashMap<>();
		ResultSet columns;
		try {
			columns = metaData.getColumns(null, "%", tableName, "%");
			while (columns.next()) {
				String COLUMN_NAME = columns.getString("COLUMN_NAME");
				String TYPE_NAME = columns.getString("TYPE_NAME");
				map.put(COLUMN_NAME,TYPE_NAME);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @param metaData
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getPrimaryKey(DatabaseMetaData metaData,String tableName){
		if(metaData == null ) {return null;}
		List<String> PrimaryKeysist = new ArrayList<String>();	
		ResultSet PrimaryKeys;
		try {
			PrimaryKeys = metaData.getPrimaryKeys(null, "%", tableName);
			while (PrimaryKeys.next()) {			
				String COLUMN_NAME = PrimaryKeys.getString("COLUMN_NAME");	
				PrimaryKeysist.add(COLUMN_NAME);	
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return PrimaryKeysist;
	}
	
	/**
	 * @param name
	 * @param map
	 * @return
	 * @throws SQLException 
	 */
	public static String createTable(List<String> keys,String name,Map<String,String> map){
		int count = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("create table "+name+" (");
		for(Entry<String, String> entry : map.entrySet()) {
			count++;
			if(keys.contains(entry.getKey()) && count == map.size()) {
				sb.append(entry.getKey()+" "+entry.getValue()+" primary key );");
			}else if(keys.contains(entry.getKey()) && count < map.size()) {
				sb.append(entry.getKey()+" "+entry.getValue()+" primary key,");
			}else if(!keys.contains(entry.getKey()) && count == map.size()) {
				sb.append(entry.getKey()+" "+entry.getValue()+");");
			}else{
				sb.append(entry.getKey()+" "+entry.getValue()+",");
			}
		}
		return sb.toString().replace("varchar", "varchar(255)")
				.replace("VARCHAR", "VARCHAR(255)");
	}
	
	/**
	 * @param conn
	 * @param createSql
	 * @return
	 */
	public static boolean executeSQL(Connection conn, String createSql) {
		if(conn == null) { return false;}
		Statement stmt;
		try {
			stmt = conn.createStatement(); 
			stmt.execute(createSql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Datasource database = new Datasource("192.144.129.188","3306","test","root","123456");
		System.out.println(isConn(database, "mysql"));
		Connection conn = getConn(database, "mysql");
		
		DatabaseMetaData metaData = conn.getMetaData();
		
		List<String> tablesList = getTables(metaData);
		Object[] arr = tablesList.toArray();
		System.out.println(Arrays.toString(arr));
		
		/*
		for(int i = 0;i<arr.length;i++) {
			Map<String,String> map = getColumn(metaData, (String)arr[i]);
			List<String> keys = getPrimaryKey(metaData, (String)arr[i]);
			System.out.println(createTable(keys,"test",map));
		}
		*/
		Map<String,String> map = getColumn(metaData, (String)arr[0]);
		List<String> keys = getPrimaryKey(metaData, (String)arr[0]);
		System.out.println(createTable(keys,"test",map));
		
		System.out.println(executeSQL(conn, createTable(keys,"test",map)));
	}

}
