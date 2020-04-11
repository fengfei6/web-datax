package com.feng.project.util;

import java.sql.*;
import java.util.*;

import com.feng.project.connector.Connector;
import com.feng.project.connector.PostgreSQLConnector;
import com.feng.project.connector.SqlServerConnector;
import com.feng.project.domain.Datasource;

public class SqlServerUtil {

	/**
	 * @param datasource
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static boolean isConn(Datasource datasource){
		Connector connector = new SqlServerConnector();
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
		Connector connector = new SqlServerConnector();
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

	public static Map<String,Integer> getTables(Connection conn,String dbname) {
		Map<String, Integer> map = new HashMap<>();
		PreparedStatement stmt;
		String createSql = "SELECT name FROM "+dbname+"..sysobjects Where xtype='U' ORDER BY name";
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
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
		String createSql = "SELECT count(1) as cou from ["+name+"]";
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
		String createSql = "SELECT a.name name,b.name type\n" +
				"FROM  syscolumns a \n" +
				"left join systypes b on a.xtype=b.xusertype  \n" +
				"inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' \n" +
				"where b.name is not null and d.name='"+tableName+"'";
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String type = rs.getString("type");
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
		String createSql = "select name from (SELECT a.name name,\n" +
				"(case when (SELECT count(*) FROM sysobjects  \n" +
				"WHERE (name in (SELECT name FROM sysindexes  \n" +
				"WHERE (id = a.id) AND (indid in  \n" +
				"(SELECT indid FROM sysindexkeys  \n" +
				"WHERE (id = a.id) AND (colid in  \n" +
				"(SELECT colid FROM syscolumns WHERE (id = a.id) AND (name = a.name)))))))  \n" +
				"AND (xtype = 'PK'))>0 then 'y' else 'n' end) pk\n" +
				"FROM  syscolumns a \n" +
				"left join systypes b on a.xtype=b.xusertype  \n" +
				"inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' \n" +
				"where b.name is not null and d.name='"+tableName+"') x where x.pk='y'";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(createSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String COLUMN_NAME = rs.getString("name");
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
		return sb.toString().replace("varchar", "varchar(255)");
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
		Datasource ds = new Datasource("192.144.129.188","1433","demo","sa","FFei916#");
		System.out.println(isConn(ds));


		Connection conn = getConn(ds);


		Map<String,Integer> map = getTables(conn,ds.getDbname());
		for(Map.Entry<String,Integer> entry:map.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}

		Map<String,String> map1 = getColumn(conn,"student");
		for(Map.Entry<String,String> entry:map1.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}

		List<String> list = getPrimaryKey(conn,"student");
		System.out.println(Arrays.toString(list.toArray()));

		System.out.println(createTable(list,"student2",map1));

		//executeSQL(conn,createTable(list,"user2",map1));
	}
}
