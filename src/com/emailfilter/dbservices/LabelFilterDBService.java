package com.emailfilter.dbservices;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.objectify.Objectify;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.utils.DatabaseUtils;

public class LabelFilterDBService extends AbstractDBService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LabelFilterDBService.class);

	public void saveLabelFilter(FilterWrapper filterWrapper) throws ClassNotFoundException, SQLException, IOException {
		LOG.debug("filterWrapper {}", filterWrapper);
		connectionSettings.build();
		int labelId = DatabaseUtils.getLabelIdByLabelName(connectionSettings, filterWrapper.getLabel());

		Timestamp creationTimestamp = new Timestamp(new Date().getTime());
		String query = "insert into label_filter(label_id, is_email_id_filter, email_id_keywords, is_subject_filter, subject_keywords, is_body_filter, body_keywords, creation_date) values(?,?,?,?,?,?,?,?)";

		connectionSettings.getConnection().setAutoCommit(false);
		LOG.debug("Auto commit {}", connectionSettings.getConnection().getAutoCommit());
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, labelId);
		prepareStatement.setBoolean(2, filterWrapper.isEmailFilter());
		prepareStatement.setObject(3,
				filterWrapper.getEmailIds() != null ? Objectify.serialize(filterWrapper.getEmailIds()) : null);
		prepareStatement.setBoolean(4, filterWrapper.isSubjectFilter());
		prepareStatement.setObject(5,
				filterWrapper.getSubjectKeywords() != null ? Objectify.serialize(filterWrapper.getSubjectKeywords())
						: null);
		prepareStatement.setBoolean(6, filterWrapper.isBodyFilter());
		prepareStatement.setObject(7,
				filterWrapper.getBodyKeywords() != null ? Objectify.serialize(filterWrapper.getBodyKeywords()) : null);
		prepareStatement.setTimestamp(8, creationTimestamp);

		LOG.debug("Query {}", prepareStatement);
		LOG.debug("saveLabelFilter {}", prepareStatement.executeUpdate());
		connectionSettings.getConnection().setAutoCommit(true);
		connectionSettings.closeConnection();
	}

	public FilterWrapper getLabelFilterById(int id) throws SQLException, ClassNotFoundException, IOException {
		connectionSettings.build();
		String query = "select * from label_filter where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, id);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		FilterWrapper filterWrapper = new FilterWrapper();
		if (resultSet.next()) {
			filterWrapper.setId(resultSet.getInt("id"));
			filterWrapper.setEmailFilter(resultSet.getBoolean("is_email_id_filter"));

			InputStream emailStream = resultSet.getBinaryStream("email_id_keywords");
			filterWrapper.setEmailIds(
					emailStream != null ? (List<String>) Objectify.deserialize(IOUtils.toByteArray(emailStream))
							: null);

			InputStream subjectStream = resultSet.getBinaryStream("subject_keywords");
			filterWrapper.setSubjectKeywords(
					subjectStream != null ? (List<String>) Objectify.deserialize(IOUtils.toByteArray(subjectStream))
							: null);

			InputStream bodyStream = resultSet.getBinaryStream("body_keywords");
			filterWrapper.setBodyKeywords(
					bodyStream != null ? (List<String>) Objectify.deserialize(IOUtils.toByteArray(bodyStream)) : null);

			filterWrapper.setEmailFilter(resultSet.getBoolean("is_email_id_filter"));
			filterWrapper.setSubjectFilter(resultSet.getBoolean("is_subject_filter"));
			filterWrapper.setBodyFilter(resultSet.getBoolean("is_body_filter"));
			int labelId = resultSet.getInt("label_id");
			filterWrapper.setLabel(DatabaseUtils.getLabelNameByLabelId(connectionSettings, labelId));
		}
		LOG.debug("filterWrapper {}", filterWrapper);
		connectionSettings.closeConnection();
		return filterWrapper;
	}

	public void updateLabelFilter(FilterWrapper filterWrapper)
			throws ClassNotFoundException, SQLException, IOException {
		connectionSettings.build();
		String query = "update label_filter set label_id = ?, is_email_id_filter = ?, email_id_keywords = ?, is_subject_filter = ?, subject_keywords = ?, is_body_filter = ?, body_keywords = ?, creation_date = ? where id = ?";
		Timestamp creationTimestamp = new Timestamp(new Date().getTime());
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, DatabaseUtils.getLabelIdByLabelName(connectionSettings, filterWrapper.getLabel()));
		prepareStatement.setBoolean(2, filterWrapper.isEmailFilter());
		prepareStatement.setObject(3,
				filterWrapper.getEmailIds() != null ? Objectify.serialize(filterWrapper.getEmailIds()) : null);
		prepareStatement.setBoolean(4, filterWrapper.isSubjectFilter());
		prepareStatement.setObject(5,
				filterWrapper.getSubjectKeywords() != null ? Objectify.serialize(filterWrapper.getSubjectKeywords())
						: null);
		prepareStatement.setBoolean(6, filterWrapper.isBodyFilter());
		prepareStatement.setObject(7,
				filterWrapper.getBodyKeywords() != null ? Objectify.serialize(filterWrapper.getBodyKeywords()) : null);
		prepareStatement.setTimestamp(8, creationTimestamp);
		prepareStatement.setInt(9, filterWrapper.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("updateLabelFilter {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}
}