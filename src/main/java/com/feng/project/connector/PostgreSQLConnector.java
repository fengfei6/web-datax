package com.feng.project.connector;
import com.feng.project.domain.Datasource;
import com.feng.project.util.Constants;


public class PostgreSQLConnector extends Connector{

	
	public String getDriver() {
        return Constants.POSTGRESQL_DRIVER_NAME;
    }
	
	public String getUrl(Datasource ds) {
        StringBuilder url = new StringBuilder();
        url.append(Constants.POSTGRESQL_JDBC_NAME)
            .append(ds.getIp().trim())
            .append(":")
            .append(ds.getPort().trim())
            .append("/")
            .append(ds.getDbname());
        return url.toString();
    }
}
