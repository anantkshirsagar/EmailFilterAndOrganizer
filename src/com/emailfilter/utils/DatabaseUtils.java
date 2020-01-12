package com.emailfilter.utils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.connection.setting.AbstractConnectionSettings;
import com.dbmanager.connection.setting.ConnectionSettings;
import com.dbmanager.property.util.PropertyReader;

public class DatabaseUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtils.class);

	private DatabaseUtils() {
	}

	public static AbstractConnectionSettings getConnectionSettings(String filePath) throws IOException {
		PropertyReader propertyReader = new PropertyReader(new File(filePath));
		return new ConnectionSettings(propertyReader.propertiesReader());
	}

	public static int getLatestRecordId(AbstractConnectionSettings connectionSettings, String tableName)
			throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select id from " + tableName + " order by id desc limit 1";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt("id");
		}
		connectionSettings.closeConnection();
		return -1;
	}

	public static int getLabelIdByLabelName(AbstractConnectionSettings connectionSettings, String labelName,
			String userEmailId) throws SQLException {
		String query = "select coreLabel.id as id from core_label_metadata coreLabel join core_user_profile coreUser on coreLabel.id = coreUser.id where coreLabel.name = ? and coreUser.user_email_id = ?";
		PreparedStatement preparedStatement = connectionSettings.getConnection().prepareStatement(query);
		preparedStatement.setString(1, labelName);
		preparedStatement.setString(2, userEmailId);
		LOG.debug("Query {}", preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt("id");
		}
		return 0;
	}

	public static String getLabelNameByLabelId(AbstractConnectionSettings connectionSettings, int labelId)
			throws SQLException {
		String query = "select name from core_label_metadata where id = ?";
		PreparedStatement preparedStatement = connectionSettings.getConnection().prepareStatement(query);
		preparedStatement.setInt(1, labelId);
		LOG.debug("Query {}", preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getString("name");
		}
		return "";
	}
			
	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.close();
	}
}
