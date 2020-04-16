package com.feng.project.util;

import com.feng.project.connector.Connector;
import com.feng.project.connector.OracleConnector;
import com.feng.project.domain.Datasource;

import java.sql.*;
import java.util.*;

public class OracleUtil {
    public static boolean isConn(Datasource datasource){
        Connector connector = new OracleConnector();
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
     * @return
     * @throws Exception
     */
    public static Connection getConn(Datasource datasource){
        Connector connector = new OracleConnector();
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

    public static Map<String,Integer> getTables(Connection conn,String username) {
        Map<String, Integer> map = new HashMap<>();
        PreparedStatement stmt;
        String createSql = "select table_name,num_rows from all_tables a where a.OWNER = upper('"+username+"')";
        try {
            stmt = conn.prepareStatement(createSql);
            ResultSet rs = stmt.executeQuery(createSql);
            while (rs.next()) {
                String name = rs.getString("table_name");
                int count = getTablesCount(conn,name);
                map.put(name,count);
            }
        } catch (SQLException e) {
            return map;
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
            ResultSet rs = stmt.executeQuery(createSql);
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
        String createSql = "select column_name,data_type from all_tab_columns c where c.TABLE_NAME like '"+tableName+"'";
        try {
            stmt = conn.prepareStatement(createSql);
            ResultSet rs = stmt.executeQuery(createSql);
            while (rs.next()) {
                String name = rs.getString("column_name");
                String type = rs.getString("data_type");
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
        String createSql = "select column_name from user_cons_columns u,user_constraints c where u.table_name = '"+tableName+"' and u.constraint_name = c.index_name and c.constraint_type ='P'";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(createSql);
            ResultSet rs = stmt.executeQuery(createSql);
            while (rs.next()) {
                String COLUMN_NAME = rs.getString("column_name");
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
        if(keys.size() <= 1) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                count++;
                if (keys.contains(entry.getKey()) && count == map.size()) {
                    sb.append(entry.getKey() + " " + entry.getValue() + " primary key )");
                } else if (keys.contains(entry.getKey()) && count < map.size()) {
                    sb.append(entry.getKey() + " " + entry.getValue() + " primary key,");
                } else if (!keys.contains(entry.getKey()) && count == map.size()) {
                    sb.append(entry.getKey() + " " + entry.getValue() + ")");
                } else {
                    sb.append(entry.getKey() + " " + entry.getValue() + ",");
                }
            }
        }else{
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey() + " " + entry.getValue() + ",");
            }
            sb.append("primary key("+getPrimaryList(keys)+"))");
        }
        return sb.toString().replace("VARCHAR2","VARCHAR2(255)");
    }

    private static String getPrimaryList(List<String> keys){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(String str : keys){
            count++;
            if(count == keys.size()){
                sb.append(str);
            }else{
                sb.append(str+",");
            }
        }
        return sb.toString();
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

	public static List<List<Object>> getTableData(Connection conn,String name){
		if(conn == null) {return null;}
		List<List<Object>> result = new ArrayList<>();
		List<Object> list = new ArrayList<>();
		for(String column:getColumn(conn, name).keySet()) {
        	list.add(column);
        }
		result.add(list);
		list = new ArrayList<>();
		PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select rownum,x.* from "+name+" x where rownum <=10");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                for(String col:getColumn(conn, name).keySet()) {
                	Object data = rs.getObject(col);
                	list.add(data);
                }
                result.add(list);
                list = new ArrayList<>();
            }
        } catch (SQLException e) {
            return null;
        }finally {
        	return result;
        }
	}
    
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Datasource database = new Datasource("192.144.129.188","1521","xe","FENGFEI","FFei916#");
        System.out.println(isConn(database));
        Connection conn = getConn(database);

        DatabaseMetaData metaData = conn.getMetaData();

        Map<String,Integer> tablemap = getTables(conn,"SYSTEM");


        for(Map.Entry<String, Integer> entry : tablemap.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());
        }

        Map<String,String> map1 = getColumn(conn,"test");
        for(Map.Entry<String,String> entry:map1.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

        List<String> list = getPrimaryKey(conn,"test");
        System.out.println(Arrays.toString(list.toArray()));


        System.out.println(createTable(list,"student2",map1));
    }
}
