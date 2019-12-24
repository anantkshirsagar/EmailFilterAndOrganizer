package com.emailfilter.dbservices;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.LabelStatus;
import com.emailfilter.exceptions.DuplicateLabelException;
import com.emailfilter.exceptions.ErrorCode;
import com.emailfilter.model.LabelGridWrapper;
import com.emailfilter.model.LabelProperty;
import com.emailfilter.utils.AppUtils;
import com.emailfilter.utils.DatabaseUtils;

public class LabelDBService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(LabelDBService.class);

	public void saveLabel(LabelProperty labelProperty) throws ClassNotFoundException, SQLException {
		if (isDuplicateLabel(labelProperty)) {
			throw new DuplicateLabelException("Duplicate label [" + labelProperty.getLabel() + "] is not allowed!",
					ErrorCode.DUPLICATE_LABEL);
		}
		connectionSettings.build();
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
	 * 
	 * @param labelProperty
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateLabel(LabelProperty labelProperty) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update core_label_metadata set name = ?, creation_time = ? from core_user_profile userProfile where core_label_metadata.id = userProfile.label_id and userProfile.label_id = ? and userProfile.user_email_id = ?";
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

	public List<LabelGridWrapper> getLabelGridInfo(String userEmailId, String labelNameSearchStr)
			throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		List<LabelGridWrapper> gridInfoList = new ArrayList<LabelGridWrapper>();
		String query = null;
		boolean flag = false;
		if (AppUtils.isStringNotEmpty(labelNameSearchStr)) {
			flag = true;
			labelNameSearchStr = labelNameSearchStr.concat("%");
			query = "select * from core_label_metadata where id in (select id from core_user_profile where user_email_id = ?) and lower(name) like ? order by creation_time desc";
		} else {
			query = "select * from core_label_metadata where id in (select id from core_user_profile where user_email_id = ?) order by creation_time desc";
		}
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		if (flag) {
			prepareStatement.setString(1, userEmailId);
			prepareStatement.setString(2, labelNameSearchStr.toLowerCase());
		} else {
			prepareStatement.setString(1, userEmailId);
		}
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			LabelGridWrapper gridLabelWrapper = new LabelGridWrapper();
			gridLabelWrapper.setId(resultSet.getInt("id"));
			gridLabelWrapper.setLabelName(resultSet.getString("name"));
			gridLabelWrapper.setCreationDate(resultSet.getDate("creation_time"));
			gridInfoList.add(gridLabelWrapper);
		}
		connectionSettings.closeConnection();
		return gridInfoList;
	}

	public List<LabelProperty> getAllLabels(String userEmailId) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from core_label_metadata where id in (select label_id from core_user_profile where user_email_id = ?);";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, userEmailId);
		ResultSet resultSet = prepareStatement.executeQuery();
		List<LabelProperty> labelList = new ArrayList<LabelProperty>();
		while (resultSet.next()) {
			LabelProperty labelProperty = new LabelProperty();
			labelProperty.setId(resultSet.getInt("id"));
			labelProperty.setLabel(resultSet.getString("name"));
			labelProperty.setUserEmailId(userEmailId);
			labelList.add(labelProperty);
		}
		LOG.debug("getAllLabels {}", labelList);
		connectionSettings.closeConnection();
		return labelList;
	}

	public void deleteLabels(List<LabelProperty> deleteLabelList) throws ClassNotFoundException, SQLException {
		LOG.debug("deleteLabels {}", deleteLabelList);
		for (LabelProperty labelProperty : deleteLabelList) {
			deleteLabel(labelProperty);
		}
	}

	public void saveLabels(List<LabelProperty> saveLabelList) throws ClassNotFoundException, SQLException {
		LOG.debug("saveLabels {}", saveLabelList);
		for (LabelProperty labelProperty : saveLabelList) {
			if (!isDuplicateLabel(labelProperty)) {
				saveLabel(labelProperty);
			}
		}
	}

	public void checkLabelStatusAndUpdateDatabase(LabelStatus labelStatus,
			Map<LabelStatus, List<LabelProperty>> labelDataMap) throws ClassNotFoundException, SQLException {
		LOG.debug("checkLabelStatusAndUpdateDatabase LabelStatus {}", labelStatus);

		if (labelDataMap == null || labelDataMap.size() == 0) {
			LOG.debug("No data found for sync");
			return;
		}

		switch (labelStatus) {
		case BOTH_CONTAINS_DATA:
			saveLabels(labelDataMap.get(LabelStatus.ADD_TO_DATABASE));
			deleteLabels(labelDataMap.get(LabelStatus.DELETE_FROM_DATABASE));
			break;
		case ONLY_DB_CONTAINS_DATA:
			deleteLabels(labelDataMap.get(LabelStatus.DELETE_FROM_DATABASE));
			break;
		case ONLY_GMAIL_CONTAINS_DATA:
			saveLabels(labelDataMap.get(LabelStatus.ADD_TO_DATABASE));
			break;
		}
	}

	public boolean isDuplicateLabel(LabelProperty labelProperty) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "select coreLabel.name as name from core_label_metadata coreLabel join core_user_profile coreUser on coreLabel.id = coreUser.id where coreLabel.name = ? and coreUser.user_email_id = ?";
		PreparedStatement preparedStatement = connectionSettings.getConnection().prepareStatement(query);
		preparedStatement.setString(1, labelProperty.getLabel());
		preparedStatement.setString(2, labelProperty.getUserEmailId());
		LOG.debug("Query {}", preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();
		boolean isExists = resultSet.next();
		LOG.debug("Is label [{}] already exists: {}", labelProperty.getLabel(), isExists ? "Yes" : "No");
		connectionSettings.closeConnection();
		return isExists;
	}

	public LabelProperty getLabelPropertyFromGridLabelWrapper(LabelGridWrapper gridLabelWrapper, String userEmailId)
			throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		int labelId = DatabaseUtils.getLabelIdByLabelName(connectionSettings, gridLabelWrapper.getOldLabelName(),
				userEmailId);
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setId(labelId);
		labelProperty.setLabel(gridLabelWrapper.getLabelName());
		labelProperty.setUserEmailId(userEmailId);
		connectionSettings.closeConnection();
		return labelProperty;
	}
}
