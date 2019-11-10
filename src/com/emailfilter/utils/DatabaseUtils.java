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
import com.emailfilter.model.LabelProperty;

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

	public static boolean isDuplicateLabel(AbstractConnectionSettings connectionSettings, LabelProperty labelProperty)
			throws SQLException {
		String query = "select name from core_label_metadata where name = ?";
		PreparedStatement preparedStatement = connectionSettings.getConnection().prepareStatement(query);
		preparedStatement.setString(1, labelProperty.getLabel());
		LOG.debug("Query {}", preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();
		boolean isExists = resultSet.next();
		LOG.debug("Is label [{}] already exists: {}", labelProperty.getLabel(), isExists ? "Yes" : "No");
		return isExists;
	}

	public static int getLabelIdByLabelName(AbstractConnectionSettings connectionSettings, String labelName)
			throws SQLException {
		String query = "select id from core_label_metadata where name = ?";
		PreparedStatement preparedStatement = connectionSettings.getConnection().prepareStatement(query);
		preparedStatement.setString(1, labelName);
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

}
