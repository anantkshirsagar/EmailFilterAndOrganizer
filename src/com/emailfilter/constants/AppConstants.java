package com.emailfilter.constants;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	public static final String INBOX_ID = "INBOX";
	public static final String USER_HOME = "user.home";
	public static final String APP_FOLDER_NAME = "email_filter_and_organizer";

	public static final List<String> getLabelsToRemove() {
		return Collections.unmodifiableList(Arrays.asList("INBOX"));
	}
}