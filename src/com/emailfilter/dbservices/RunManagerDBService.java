package com.emailfilter.dbservices;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.RunStatus;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.RunDetails;
import com.emailfilter.model.RunDetailsWrapper;
import com.emailfilter.utils.AppUtils;
import com.emailfilter.utils.DatabaseUtils;

public class RunManagerDBService extends AbstractDBService {
	private static final Logger LOG = LoggerFactory.getLogger(RunManagerDBService.class);

	public void saveRun(RunDetails runDetails, boolean isLabelFilter) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "";
		if (isLabelFilter) {
			query = "insert into core_run_manager(label_filter_id, run_name, status, failure_reason) values(?,?,?,?)";
		} else {
			query = "insert into core_run_manager(delete_filter_id, run_name, status, failure_reason) values(?,?,?,?)";
		}
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		if (isLabelFilter) {
			prepareStatement.setInt(1, runDetails.getLabelFilterId());
		} else {
			prepareStatement.setInt(1, runDetails.getDeleteFilterId());
		}
		prepareStatement.setString(2, runDetails.getRunName());
		prepareStatement.setInt(3, runDetails.getRunStatus().ordinal());
		prepareStatement.setString(4, runDetails.getFailureReason());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to core_run_manager {}", prepareStatement.executeUpdate());
		DatabaseUtils.closePreparedStatement(prepareStatement);
		connectionSettings.closeConnection();
	}

	public void updateRun(RunDetails runDetails, int runId, boolean isLabelFilter)
			throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "";
		if (isLabelFilter) {
			query = "update core_run_manager set label_filter_id = ?, run_name = ?, status = ?, failure_reason = ? where id = ?";
		} else {
			query = "update core_run_manager set delete_filter_id = ?, run_name = ?, status = ?, failure_reason = ? where id = ?";
		}
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		if (isLabelFilter) {
			prepareStatement.setInt(1, runDetails.getLabelFilterId());
		} else {
			prepareStatement.setInt(1, runDetails.getDeleteFilterId());
		}
		prepareStatement.setString(2, runDetails.getRunName());
		prepareStatement.setInt(3, runDetails.getRunStatus().ordinal());
		prepareStatement.setString(4, runDetails.getFailureReason());
		prepareStatement.setInt(5, runId);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("updateRun entry to core_run_manager {}", prepareStatement.executeUpdate());
		DatabaseUtils.closePreparedStatement(prepareStatement);
		connectionSettings.closeConnection();
	}

	public void updateRunStatus(int runId, RunStatus runStatus) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "update core_run_manager set status = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, runStatus.ordinal());
		prepareStatement.setInt(2, runId);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("updateRunStatus entry to core_run_manager {}", prepareStatement.executeUpdate());
		DatabaseUtils.closePreparedStatement(prepareStatement);
		connectionSettings.closeConnection();
	}

	public void deleteRun(int runId) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		String query = "delete from core_run_manager where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, runId);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("deleteRun entry to core_run_manager {}", prepareStatement.executeUpdate());
		DatabaseUtils.closePreparedStatement(prepareStatement);
		connectionSettings.closeConnection();
	}

	public RunDetails getRunDetailsById(int runId) throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		RunDetails runDetails = new RunDetails();
		String query = "select * from core_run_manager where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, runId);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			runDetails = getRunDetailsFromResultSet(resultSet);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetails;
	}

	public List<RunDetails> getAllRunDetails() throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		List<RunDetails> runDetailsList = new ArrayList<RunDetails>();
		String query = "select * from core_run_manager";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			RunDetails runDetails = getRunDetailsFromResultSet(resultSet);
			runDetailsList.add(runDetails);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetailsList;
	}

	public List<RunDetails> getAllRunDetailsByUserEmail(String userEmailId)
			throws SQLException, ClassNotFoundException {
		List<RunDetails> runDetailsList = getLabelFilterRunDetailsByUserEmail(userEmailId);
		runDetailsList.addAll(getDeleteFilterRunDetailsByUserEmail(userEmailId));
		return runDetailsList;
	}

	public List<RunDetails> getLabelFilterRunDetailsByUserEmail(String userEmailId)
			throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		List<RunDetails> runDetailsList = new ArrayList<RunDetails>();
		String query = "select runManager.id, runManager.label_filter_id,"
				+ "runManager.delete_filter_id, runManager.run_name, runManager.status, runManager.failure_reason "
				+ "from core_run_manager runManager join label_filter labelFilter "
				+ "on runManager.label_filter_id = labelFilter.id join core_label_metadata label "
				+ "on label.id = labelFilter.label_id join core_user_profile userProfile "
				+ "on userProfile.label_id = label.id where userProfile.user_email_id = ?";

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, userEmailId);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			RunDetails runDetails = getRunDetailsFromResultSet(resultSet);
			runDetailsList.add(runDetails);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetailsList;
	}

	public List<RunDetails> getDeleteFilterRunDetailsByUserEmail(String userEmailId)
			throws SQLException, ClassNotFoundException {
		connectionSettings.build();
		List<RunDetails> runDetailsList = new ArrayList<RunDetails>();
		String query = "select runManager.id, runManager.label_filter_id,"
				+ "runManager.delete_filter_id, runManager.run_name, runManager.status, runManager.failure_reason "
				+ "from core_run_manager runManager join delete_filter deleteFilter "
				+ "on runManager.delete_filter_id = deleteFilter.id join core_label_metadata label "
				+ "on label.id = deleteFilter.label_id join core_user_profile userProfile "
				+ "on userProfile.label_id = label.id where userProfile.user_email_id = ?";

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, userEmailId);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			RunDetails runDetails = getRunDetailsFromResultSet(resultSet);
			runDetailsList.add(runDetails);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetailsList;
	}

	public List<RunDetailsWrapper> convertRunDetailsListIntoWrapperList(List<RunDetails> runDetails)
			throws ClassNotFoundException, SQLException, IOException {
		LOG.debug("runDetails {}", runDetails);
		List<RunDetailsWrapper> runDetailsWrapper = new ArrayList<RunDetailsWrapper>();
		LabelFilterDBService labelFilterService = new LabelFilterDBService();
		DeleteFilterDBService deleteFilterService = new DeleteFilterDBService();

		if (AppUtils.isCollectionNotEmpty(runDetails)) {
			for (RunDetails run : runDetails) {
				int labelFilterId = run.getLabelFilterId();
				int deleteFilterId = run.getDeleteFilterId();
				if (labelFilterId != 0) {
					FilterWrapper labelFilterWrapper = labelFilterService.getLabelFilterById(labelFilterId);
					runDetailsWrapper
							.add(getRunDetailsWrapperFromRunDetails(run, labelFilterWrapper.getFilterName(), null));
				} else if (deleteFilterId != 0) {
					FilterWrapper deleteFilterWrapper = deleteFilterService.getDeleteFilterById(deleteFilterId);
					runDetailsWrapper
							.add(getRunDetailsWrapperFromRunDetails(run, null, deleteFilterWrapper.getFilterName()));
				}
			}
		}
		return runDetailsWrapper;
	}

	public RunDetails getCurrentLabelFilterRunDetails(String filterName, String runName, String userEmailId)
			throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select runManager.id, runManager.label_filter_id, runManager.delete_filter_id, "
				+ "runManager.run_name, runManager.status, runManager.failure_reason "
				+ "from core_run_manager runManager join label_filter labelFilter  "
				+ "on runManager.label_filter_id = labelFilter.id join core_label_metadata label "
				+ "on label.id = labelFilter.label_id join core_user_profile userProfile "
				+ "on label.id = userProfile.label_id where labelFilter.filter_name = ?"
				+ "and userProfile.user_email_id = ? and runManager.run_name = ?";

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, filterName);
		prepareStatement.setString(2, userEmailId);
		prepareStatement.setString(3, runName);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		RunDetails runDetails = new RunDetails();
		if (resultSet.next()) {
			runDetails = getRunDetailsFromResultSet(resultSet);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetails;
	}
	
	public RunDetails getCurrentDeleteFilterRunDetails(String filterName, String runName, String userEmailId)
			throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select runManager.id, runManager.label_filter_id, runManager.delete_filter_id, "
				+ "runManager.run_name, runManager.status, runManager.failure_reason "
				+ "from core_run_manager runManager join delete_filter deleteFilter  "
				+ "on runManager.delete_filter_id = deleteFilter.id join core_label_metadata label "
				+ "on label.id = deleteFilter.label_id join core_user_profile userProfile "
				+ "on label.id = userProfile.label_id where deleteFilter.filter_name = ?"
				+ "and userProfile.user_email_id = ? and runManager.run_name = ?";

		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, filterName);
		prepareStatement.setString(2, userEmailId);
		prepareStatement.setString(3, runName);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		RunDetails runDetails = new RunDetails();
		if (resultSet.next()) {
			runDetails = getRunDetailsFromResultSet(resultSet);
		}
		connectionSettings.closeConnection();
		DatabaseUtils.closePreparedStatement(prepareStatement);
		return runDetails;
	}

	private RunDetails getRunDetailsFromResultSet(ResultSet resultSet) throws SQLException {
		RunDetails runDetails = new RunDetails();
		runDetails.setId(resultSet.getInt("id"));
		runDetails.setLabelFilterId(resultSet.getInt("label_filter_id"));
		runDetails.setDeleteFilterId(resultSet.getInt("delete_filter_id"));
		runDetails.setFailureReason(resultSet.getString("failure_reason"));
		runDetails.setRunName(resultSet.getString("run_name"));
		runDetails.setRunStatus(RunStatus.values()[resultSet.getInt("status")]);
		return runDetails;
	}

	private RunDetailsWrapper getRunDetailsWrapperFromRunDetails(RunDetails runDetails, String labelFilterName,
			String deleteFilterName) {
		RunDetailsWrapper runDetailsWrapper = new RunDetailsWrapper();
		runDetailsWrapper.setId(runDetails.getId());
		runDetailsWrapper.setLabelFilterName(labelFilterName);
		runDetailsWrapper.setDeleteFilterName(deleteFilterName);
		runDetailsWrapper.setFailureReason(runDetails.getFailureReason());
		runDetailsWrapper.setRunStatus(runDetails.getRunStatus());
		runDetailsWrapper.setRunName(runDetails.getRunName());
		return runDetailsWrapper;
	}
}
