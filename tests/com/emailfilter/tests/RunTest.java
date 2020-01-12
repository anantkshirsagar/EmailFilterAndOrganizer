package com.emailfilter.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.RunStatus;
import com.emailfilter.dbservices.RunManagerDBService;
import com.emailfilter.model.RunDetails;
import com.emailfilter.model.RunDetailsWrapper;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class RunTest {

	private static final Logger LOG = LoggerFactory.getLogger(RunTest.class);

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
//		saveRun();
//		updateRun();
		updateRunStatus();
//		printRunById();
//		deleteRun();
//		getAllRunDetails();
//		getAllRunDetailsByUserEmail();
//		convertRunDetailsListIntoWrapperList();
	}

	public static void saveRun() throws ClassNotFoundException, SQLException {
		LOG.debug("saveRun");
		RunManagerDBService runService = new RunManagerDBService();
		runService.saveRun(getRunDetails(-1, 1, false), false);
	}

	public static void printRunById() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		RunDetails runDetails = runService.getRunDetailsById(2);
		LOG.debug(" runDetails {}", runDetails);
	}

	public static void updateRun() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		RunDetails runDetails = getRunDetails(1, -1, true);
		runService.updateRun(runDetails, 2, true);
	}

	public static void updateRunStatus() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		runService.updateRunStatus(5, RunStatus.FAILED);
	}

	public static void getAllRunDetails() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		LOG.debug("runDetailsList {}", runService.getAllRunDetails());
	}

	public static void deleteRun() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		runService.deleteRun(3);
	}

	public static void getAllRunDetailsByUserEmail() throws ClassNotFoundException, SQLException {
		RunManagerDBService runService = new RunManagerDBService();
		List<RunDetails> runDetailsList = runService.getAllRunDetailsByUserEmail("icbm.iot@gmail.com");
		LOG.debug("runDetailsList {}", runDetailsList);
	}

	public static RunDetails getRunDetails(int labelFilterId, int deleteFilterId, boolean isLabelFilter) {
		RunDetails runDetails = new RunDetails();
		if (isLabelFilter) {
			runDetails.setLabelFilterId(labelFilterId);
		} else {
			runDetails.setDeleteFilterId(deleteFilterId);
		}
		runDetails.setFailureReason(null);
		runDetails.setRunStatus(RunStatus.PROCESSING);
		runDetails.setRunName("Init-Thread-101");
		return runDetails;
	}

	public static void convertRunDetailsListIntoWrapperList() throws ClassNotFoundException, SQLException, IOException {
		RunManagerDBService runService = new RunManagerDBService();
		List<RunDetails> allRunDetails = runService.getAllRunDetails();
		List<RunDetailsWrapper> runDetailsWrapperList = runService.convertRunDetailsListIntoWrapperList(allRunDetails);
		Gson gson = AppUtils.getGsonInstance();
		String json = gson.toJson(runDetailsWrapperList);
		LOG.debug(" Json {}", json);
		LOG.debug("runDetailsWrapper {}", runDetailsWrapperList);
	}
}
