package com.feng.project.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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




public class MysqlUtil {

	/**
	 * @param datasource
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static boolean isConn(Datasource datasource){
		Connector connector = new MysqlConnector();
		Connection conn = null;
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
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static Connection getConn(Datasource datasource){
		Connector connector = new MysqlConnector();
		Connection conn = null;
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
	 * @param
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,Integer> getTables(Connection conn){
		if(conn == null ) {return null;}
		DatabaseMetaData metaData = getMetaDate(conn);
		Map<String,Integer> map = new HashMap<String,Integer>();
		ResultSet tables;
		try {
			tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (tables.next()) {
				String TABLE_NAME = tables.getString("TABLE_NAME");
				Integer count = execute(conn, "select * from "+TABLE_NAME);
				map.put(TABLE_NAME, count);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
				sb.append(entry.getKey()+" "+entry.getValue()+" primary key )");
			}else if(keys.contains(entry.getKey()) && count < map.size()) {
				sb.append(entry.getKey()+" "+entry.getValue()+" primary key,");
			}else if(!keys.contains(entry.getKey()) && count == map.size()) {
				sb.append(entry.getKey()+" "+entry.getValue()+")");
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
			stmt.executeUpdate(createSql);
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
	
	public static Integer execute(Connection conn, String createSql) {
		int count = 0;
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(createSql);
            ResultSet rs = stmt.executeQuery(createSql);
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            return 0;
        }finally {
        	return count;
        }
	}


}
