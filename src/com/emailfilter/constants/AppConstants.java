package com.emailfilter.constants;

import java.io.File;

public class AppConstants {

	private AppConstants() {

	}

	public static final String DATABASE_CONFIG_PATH = "email-app-configurations.properties";
	public static final String CREATE_MYSQL_TABLES = "resources" + File.separator + "database" + File.separator
			+ "email_filter_mysql_tables.sql";

	public static final String CREATE_PSQL_TABLES = "resources" + File.separator + "database" + File.separator
			+ "email_filter_psql_tables.sql";
	
	public enum DATABASE_TYPE {
		MYSQL, PSQL
	}
	
	public static final String COMMA = ",";
	public static final String MINUS_ONE = "-1";
}