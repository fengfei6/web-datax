package com.feng.project.connector;
import com.feng.project.domain.Datasource;

public abstract class Connector {

	public abstract String getDriver();
	public abstract String getUrl(Datasource ds);
}
