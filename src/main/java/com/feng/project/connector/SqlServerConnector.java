package com.feng.project.connector;
import com.feng.project.domain.Datasource;
import com.feng.project.util.Constants;


public class SqlServerConnector extends Connector{

	
	public String getDriver() {
        return Constants.MSSQL_DRIVER_NAME;
    }
	
	public String getUrl(Datasource ds) {
	    String ip = ds.getIp();
	    String port = ds.getPort();
	    String dbname = ds.getDbname();
	    StringBuilder url = new StringBuilder();
	    url.append(Constants.MSSQL_JDBC_NAME).append(ip.trim()).
	         append(":").append(port.trim()).append(";DatabaseName=").append(dbname);
	    return url.toString();
	 }

}
