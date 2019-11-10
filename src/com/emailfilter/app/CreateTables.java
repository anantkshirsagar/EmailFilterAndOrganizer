package com.emailfilter.app;

import java.io.IOException;
import java.sql.SQLException;

import com.emailfilter.constants.AppConstants;
import com.emailfilter.manager.SQLTableManager;

public class CreateTables {

	/**
	 * <strong>Default types of database</strong> <br>
	 * 1) AppConstants.DATABASE_TYPE.MYSQL <br>
	 * 2) AppConstants.DATABASE_TYPE.PSQL
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		SQLTableManager tableManager = new SQLTableManager(AppConstants.DATABASE_TYPE.PSQL);
		tableManager.createTables();
	}
}