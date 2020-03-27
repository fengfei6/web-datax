package com.feng.project.connector;

import com.feng.project.domain.Datasource;
import com.feng.project.util.Constants;


public class OracleConnector extends Connector{

    public String getDriver() {
        return Constants.ORACLE_DRIVER_NAME;
    }

  
    public String getUrl(Datasource ds) {
        String ip = ds.getIp();
        String port = ds.getPort();
        String sid = ds.getDbname();
        StringBuilder url = new StringBuilder();
        url.append(Constants.ORACLE_THIN_JDBC_NAME).append(ip.trim()).
            append(":").append(port.trim()).append("/").append(sid);
        return url.toString();
    }
}
