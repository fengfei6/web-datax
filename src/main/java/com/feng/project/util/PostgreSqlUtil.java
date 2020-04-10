package com.feng.project.util;

import java.sql.*;
import java.util.*;

import com.feng.project.connector.Connector;
import com.feng.project.connector.MysqlConnector;
import com.feng.project.connector.PostgreSQLConnector;
import com.feng.project.domain.Datasource;

public class PostgreSqlUtil {

	/**
	 * @param datasource
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static boolean isConn(Datasource datasource){
		Connector connector = new PostgreSQLConnector();
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
		Connector connector = new PostgreSQLConnector();
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

	public static Map<String,Integer> getTables(Connection conn) {
		Map<String, Integer> map = new HashMap<>();
		PreparedStatement stmt;
		String createSql = "select relname as table_name from pg_class c where  relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname";
		try {
			 stmt = conn.prepareStatement(createSql);
	         ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("table_name");
				int count = getTablesCount(conn,name);
				map.put(name,count);
			}
		} catch (SQLException e) {
			e.getMessage();
		}finally {
			return map;
		}
	}

	public static Integer getTablesCount(Connection conn,String name) {
		PreparedStatement stmt;
		String createSql = "SELECT count(1) as cou from "+name;
		int count = 0;
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("cou");
			}
		} catch (SQLException e) {
			return count;
		}finally {
			return count;
		}
	}

	public static Map<String,String> getColumn(Connection conn,String tableName) {
		Map<String,String> map = new HashMap<>();
		PreparedStatement stmt;
		String createSql = "select column_name ,udt_name  from information_schema.columns where table_name= '"+tableName+"'";
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("column_name");
				String type = rs.getString("udt_name");
				map.put(name,type);
			}
		} catch (SQLException e) {
			return map;
		}finally {
			return map;
		}
	}

	/**
	 * @param
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getPrimaryKey(Connection conn,String tableName){
		List<String> PrimaryKeysist = new ArrayList<String>();
		String createSql = "select pg_attribute.attname as colname from \n" +
				"pg_constraint  inner join pg_class \n" +
				"on pg_constraint.conrelid = pg_class.oid \n" +
				"inner join pg_attribute on pg_attribute.attrelid = pg_class.oid \n" +
				"and  pg_attribute.attnum = pg_constraint.conkey[1]\n" +
				"inner join pg_type on pg_type.oid = pg_attribute.atttypid\n" +
				"where pg_constraint.contype='p'\n" +
				"and pg_class.relname = '"+tableName+"'";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String COLUMN_NAME = rs.getString("colname");
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
	public static String createTable(List<String> keys, String name, Map<String,String> map){
		int count = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("create table "+name+" (");
		for(Map.Entry<String, String> entry : map.entrySet()) {
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
		return sb.toString().replace("varchar","varchar(255)")
				.replace("int4","int");
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

	public static void main(String[] args) {
		Datasource ds = new Datasource("192.144.129.188","5432","postgres","postgres","FFei916#");
		System.out.println(isConn(ds));

		Connection conn = getConn(ds);


		Map<String,Integer> map = getTables(conn);
		for(Map.Entry<String,Integer> entry:map.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}

		Map<String,String> map1 = getColumn(conn,"user");
		for(Map.Entry<String,String> entry:map1.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}

		List<String> list = getPrimaryKey(conn,"user");
		System.out.println(Arrays.toString(list.toArray()));

		System.out.println(createTable(list,"user",map1));

		executeSQL(conn,createTable(list,"user2",map1));
	}
}
