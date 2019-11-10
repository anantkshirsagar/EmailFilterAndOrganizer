package com.emailfilter.dbservices;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.exceptions.DuplicateLabelException;
import com.emailfilter.exceptions.ErrorCode;
import com.emailfilter.model.LabelProperty;
import com.emailfilter.utils.DatabaseUtils;
import com.google.api.services.gmail.model.Label;

public class LabelDBService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(LabelDBService.class);

	public void saveLabel(LabelProperty labelProperty) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		if (DatabaseUtils.isDuplicateLabel(connectionSettings, labelProperty)) {
			throw new DuplicateLabelException("Duplicate label [" + labelProperty.getLabel() + "] is not allowed!",
					ErrorCode.DUPLICATE_LABEL);
		}
		String query = "insert into core_label_metadata(name, creation_time) values(?,?)";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, labelProperty.getLabel());
		java.sql.Timestamp sqlDateFormat = new java.sql.Timestamp(new Date().getTime());
		prepareStatement.setTimestamp(2, sqlDateFormat);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to core_label_metadata {}", prepareStatement.executeUpdate());

		int latestRecordId = DatabaseUtils.getLatestRecordId(connectionSettings, "core_label_metadata");
		LOG.debug("latestRecordId {}", latestRecordId);

		query = "insert into core_user_profile(user_email_id, label_id) values(?,?)";
		prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, labelProperty.getUserEmailId());
		prepareStatement.setLong(2, latestRecordId);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to core_user_profile {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void deleteLabel(LabelProperty labelProperty) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "delete from core_user_profile where label_id = ? and user_email_id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, labelProperty.getId());
		prepareStatement.setString(2, labelProperty.getUserEmailId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("core_user_profile Record removed {}", prepareStatement.executeUpdate());

		query = "delete from core_label_metadata where id = ?";
		prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, labelProperty.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("core_label_metadata Record removed {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	/**
	 * TODO Update Query is not working this part is pending
	 * @param labelProperty
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateLabel(LabelProperty labelProperty) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update core_label_metadata set core_label_metadata.name = ?, core_label_metadata.creation_time = ? from core_label_metadata coreLabel join core_user_profile userProfile on coreLabel.id = userProfile.id where coreLabel.id = ? and userProfile.user_email_id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, labelProperty.getLabel());
		Timestamp sqlDateFormat = new Timestamp(new Date().getTime());
		prepareStatement.setTimestamp(2, sqlDateFormat);
		prepareStatement.setInt(3, labelProperty.getId());
		prepareStatement.setString(4, labelProperty.getUserEmailId());

		LOG.debug("Query {}", prepareStatement);
		LOG.debug("core_label_metadata updated {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public int getDatabaseParentLabelId(String labelName) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		int labelId = DatabaseUtils.getLabelIdByLabelName(connectionSettings, labelName);
		connectionSettings.closeConnection();
		LOG.debug("Database label id: {}", labelId);
		return labelId;
	}

	public void syncGmailLabelAndSave(List<Label> gmailLabelList) {

	}
}
