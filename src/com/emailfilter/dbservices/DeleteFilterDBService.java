package com.emailfilter.dbservices;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.objectify.Objectify;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.LabelFilterGridWrapper;
import com.emailfilter.utils.AppUtils;
import com.emailfilter.utils.DatabaseUtils;

public class DeleteFilterDBService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteFilterDBService.class);

	public void saveDeleteFilter(FilterWrapper filterWrapper, String userEmailId)
			throws ClassNotFoundException, SQLException, IOException {
		LOG.debug("filterWrapper {}", filterWrapper);
		connectionSettings.build();
		int labelId = DatabaseUtils.getLabelIdByLabelName(connectionSettings, filterWrapper.getLabel(), userEmailId);

		Timestamp creationTimestamp = new Timestamp(new Date().getTime());
		String query = "insert into delete_filter(label_id, is_email_id_filter, email_id_keywords, is_subject_filter, subject_keywords, is_body_filter, body_keywords, creation_date, filter_name) values(?,?,?,?,?,?,?,?,?)";

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
		prepareStatement.setString(9, filterWrapper.getFilterName());

		LOG.debug("Query {}", prepareStatement);
		LOG.debug("saveLabelFilter {}", prepareStatement.executeUpdate());
		connectionSettings.getConnection().setAutoCommit(true);
		connectionSettings.closeConnection();
	}

	public FilterWrapper getDeleteFilterById(int id) throws SQLException, ClassNotFoundException, IOException {
		connectionSettings.build();
		String query = "select * from delete_filter where id = ?";
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
			filterWrapper.setFilterName(resultSet.getString("filter_name"));
			filterWrapper.setBodyFilter(resultSet.getBoolean("is_body_filter"));
			int labelId = resultSet.getInt("label_id");
			filterWrapper.setLabel(DatabaseUtils.getLabelNameByLabelId(connectionSettings, labelId));
		}
		LOG.debug("filterWrapper {}", filterWrapper);
		connectionSettings.closeConnection();
		return filterWrapper;
	}

	public List<LabelFilterGridWrapper> getDeleteFilterGridInfo(String userEmailId, String filterNameSearchStr)
			throws ClassNotFoundException, SQLException, IOException {
		connectionSettings.build();
		List<LabelFilterGridWrapper> labelFilterGridInfoList = new ArrayList<LabelFilterGridWrapper>();
		String query = null;
		if (AppUtils.isStringNotEmpty(filterNameSearchStr)) {
			filterNameSearchStr = filterNameSearchStr.concat("%");
			query = "select filter.id, labelMetadata.name, filter.filter_name, filter.creation_date,"
					+ "filter.is_email_id_filter, filter.email_id_keywords, filter.is_subject_filter,"
					+ "filter.subject_keywords, filter.is_body_filter,"
					+ "filter.body_keywords from core_label_metadata labelMetadata "
					+ "join delete_filter filter on labelMetadata.id = filter.label_id "
					+ "join core_user_profile userProfile on labelMetadata.id = userProfile.label_id "
					+ "where userProfile.user_email_id = ? and filter_name like ?";
		} else {
			query = "select filter.id, labelMetadata.name, filter.filter_name, filter.creation_date,"
					+ "filter.is_email_id_filter, filter.email_id_keywords, filter.is_subject_filter,"
					+ "filter.subject_keywords, filter.is_body_filter,"
					+ "filter.body_keywords from core_label_metadata labelMetadata "
					+ "join delete_filter filter on labelMetadata.id = filter.label_id "
					+ "join core_user_profile userProfile on labelMetadata.id = userProfile.label_id "
					+ "where userProfile.user_email_id = ?";
		}

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		if (AppUtils.isStringNotEmpty(filterNameSearchStr)) {
			prepareStatement.setString(1, userEmailId);
			prepareStatement.setString(2, filterNameSearchStr);
		} else {
			prepareStatement.setString(1, userEmailId);
		}

		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();

		while (resultSet.next()) {
			LabelFilterGridWrapper labelFilterGrid = new LabelFilterGridWrapper();
			labelFilterGrid.setId(resultSet.getInt("id"));
			labelFilterGrid.setLabel(resultSet.getString("name"));
			labelFilterGrid.setFilterName(resultSet.getString("filter_name"));
			labelFilterGrid.setCreationDate(resultSet.getDate("creation_date"));
			labelFilterGrid.setEmailFilter(resultSet.getBoolean("is_email_id_filter"));

			InputStream emailStream = resultSet.getBinaryStream("email_id_keywords");
			labelFilterGrid
					.setEmailIds(emailStream != null
							? AppUtils.listToStringConvertor(
									(List<String>) Objectify.deserialize(IOUtils.toByteArray(emailStream)), ",")
							: null);

			labelFilterGrid.setSubjectFilter(resultSet.getBoolean("is_subject_filter"));

			InputStream subjectStream = resultSet.getBinaryStream("subject_keywords");
			labelFilterGrid
					.setSubjectKeywords(subjectStream != null
							? AppUtils.listToStringConvertor(
									(List<String>) Objectify.deserialize(IOUtils.toByteArray(subjectStream)), ",")
							: null);

			labelFilterGrid.setBodyFilter(resultSet.getBoolean("is_body_filter"));

			InputStream bodyStream = resultSet.getBinaryStream("body_keywords");
			labelFilterGrid
					.setBodyKeywords(
							bodyStream != null
									? AppUtils.listToStringConvertor(
											(List<String>) Objectify.deserialize(IOUtils.toByteArray(bodyStream)), ",")
									: null);
			labelFilterGridInfoList.add(labelFilterGrid);
		}
		LOG.debug("deleteFilterGridInfoList {}", labelFilterGridInfoList);
		connectionSettings.closeConnection();
		return labelFilterGridInfoList;
	}

	public void updateDeleteFilter(FilterWrapper filterWrapper, String userEmailId)
			throws ClassNotFoundException, SQLException, IOException {
		connectionSettings.build();
		String query = "update delete_filter set label_id = ?, is_email_id_filter = ?, email_id_keywords = ?, is_subject_filter = ?, subject_keywords = ?, is_body_filter = ?, body_keywords = ?, creation_date = ?, filter_name = ? where id = ?";
		Timestamp creationTimestamp = new Timestamp(new Date().getTime());
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1,
				DatabaseUtils.getLabelIdByLabelName(connectionSettings, filterWrapper.getLabel(), userEmailId));
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
		prepareStatement.setString(9, filterWrapper.getFilterName());
		prepareStatement.setInt(10, filterWrapper.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("updateDeleteFilter {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}
	
	public FilterWrapper getDeleteFilterByFilterName(String filterName, String userEmailId)
			throws SQLException, ClassNotFoundException, IOException {
		connectionSettings.build();
		String query = "select deleteFilter.id, deleteFilter.filter_name, deleteFilter.label_id, "
				+ "deleteFilter.is_email_id_filter, deleteFilter.email_id_keywords, "
				+ "deleteFilter.is_subject_filter, deleteFilter.subject_keywords, deleteFilter.is_body_filter, "
				+ "deleteFilter.body_keywords, deleteFilter.creation_date from delete_filter deleteFilter "
				+ "join core_label_metadata label on label.id = deleteFilter.label_id "
				+ "join core_user_profile userProfile on userProfile.label_id = label.id "
				+ "where userProfile.user_email_id = ? and deleteFilter.filter_name = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, userEmailId);
		prepareStatement.setString(2, filterName);
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
			filterWrapper.setFilterName(resultSet.getString("filter_name"));
			filterWrapper.setBodyFilter(resultSet.getBoolean("is_body_filter"));
			int labelId = resultSet.getInt("label_id");
			filterWrapper.setLabel(DatabaseUtils.getLabelNameByLabelId(connectionSettings, labelId));
		}
		LOG.debug("filterWrapper {}", filterWrapper);
		connectionSettings.closeConnection();
		return filterWrapper;
	}
	
	public void deleteFilter(int filterId) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "delete from delete_filter where id = ?";

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, filterId);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("deleteFilter {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}
}