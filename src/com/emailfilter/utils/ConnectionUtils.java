package com.emailfilter.utils;

import java.io.File;
import java.io.IOException;

import com.dbmanager.connection.setting.AbstractConnectionSettings;
import com.dbmanager.connection.setting.ConnectionSettings;
import com.dbmanager.property.util.PropertyReader;

public class ConnectionUtils {
	private static PropertyReader propertyReader;

	public static AbstractConnectionSettings getConnectionSettings(String filePath) throws IOException {
		propertyReader = new PropertyReader(new File(filePath));
		return new ConnectionSettings(propertyReader.propertiesReader());
	}
}
