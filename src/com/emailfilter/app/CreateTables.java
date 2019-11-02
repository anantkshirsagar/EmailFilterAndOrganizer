package com.emailfilter.app;

import java.io.IOException;
import java.sql.SQLException;

import com.emailfilter.constants.AppConstants;
import com.emailfilter.manager.SQLTableManager;

public class CreateTables {
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		SQLTableManager databaseService = new SQLTableManager(AppConstants.DATABASE_TYPE.MYSQL);
		databaseService.createTables();
	}
}