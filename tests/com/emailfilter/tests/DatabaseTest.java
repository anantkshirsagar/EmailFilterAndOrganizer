package com.emailfilter.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.util.DBUtils;
import com.emailfilter.dbservices.LabelDBService;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.exceptions.DuplicateLabelException;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.GridLabelWrapper;
import com.emailfilter.model.LabelProperty;
import com.emailfilter.utils.DatabaseUtils;

public class DatabaseTest {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseTest.class);

	public static void main(String[] args) {
//		checkLabelIsAlreadyPresent();
		try {
//			 saveLabelTest();
//			 deleteLabelTest();
//			saveLabelFilterTest();
//			updateLabel();
//			updateLabelFilterTest();
//			fetchLabelFilterDataById();
			getLabelGridInfo();
		} catch (Exception e) {
			LOG.error("Exception {}", e);
		}
	}

	public static void checkLabelIsAlreadyPresent() throws ClassNotFoundException, SQLException, IOException {
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setLabel("Label2");
		DatabaseUtils.isDuplicateLabel(DatabaseUtils.getConnectionSettings(
				"F:\\eclipse-nsg-workspace\\EmailFilterAndOrganizer\\resources\\sql_properties\\psql.properties"),
				labelProperty);
	}

	public static void saveLabelTest() throws ClassNotFoundException, SQLException {
		LabelDBService databaseService = new LabelDBService();
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setLabel("Label-2");
		labelProperty.setUserEmailId("anantnitinkshirsagar@gmail.com");
		databaseService.saveLabel(labelProperty);
	}

	public static void deleteLabelTest() throws ClassNotFoundException, SQLException {
		LabelDBService databaseService = new LabelDBService();
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setId(3);
		labelProperty.setUserEmailId("anantnitinkshirsagar@gmail.com");
		labelProperty.setLabel("AKNewLabel1");
		databaseService.deleteLabel(labelProperty);
	}

	public static void saveLabelFilterTest() throws ClassNotFoundException, SQLException, IOException {
		LabelFilterDBService labelFilterService = new LabelFilterDBService();
		FilterWrapper filterWrapper = new FilterWrapper();
		filterWrapper.setId(101);
		filterWrapper.setBodyFilter(true);
		filterWrapper.setBodyKeywords(Arrays.asList("bodykey1", "bodykey2", "bodykey3"));
		filterWrapper.setEmailFilter(true);
		filterWrapper.setEmailIds(Arrays.asList("anant@gmail.com", "suyog@gmail.com", "suyog@gmail.com"));
		filterWrapper.setLabel("Label1");
		filterWrapper.setSubjectFilter(false);
		filterWrapper.setSubjectKeywords(Arrays.asList("subkey1, subkey2, subkey3"));
		labelFilterService.saveLabelFilter(filterWrapper);
	}

	public static void fetchLabelFilterDataById() throws ClassNotFoundException, SQLException, IOException {
		LabelFilterDBService labelFilterService = new LabelFilterDBService();
		labelFilterService.getLabelFilterById(2);
	}
	
	public static void updateLabel() throws ClassNotFoundException, SQLException {
		LabelDBService labelDBService = new LabelDBService();
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setId(4);
		labelProperty.setLabel("Label-1");
		labelProperty.setUserEmailId("anantnitinkshirsagar@gmail.com");
		labelDBService.updateLabel(labelProperty);
	}

	public static void updateLabelFilterTest() throws ClassNotFoundException, SQLException, IOException {
		LabelFilterDBService labelFilterService = new LabelFilterDBService();
		FilterWrapper filterWrapper = new FilterWrapper();
		filterWrapper.setId(2);
		filterWrapper.setLabel("Label1");
		filterWrapper.setBodyFilter(true);
		filterWrapper.setBodyKeywords(Arrays.asList("key1", "key2", "key3"));
		filterWrapper.setEmailFilter(true);
		filterWrapper.setEmailIds(Arrays.asList("aaaaa@gmail.com", "bbbb@gmail.com", "cccccc@gmail.com"));
		filterWrapper.setSubjectFilter(false);
		filterWrapper.setSubjectKeywords(Arrays.asList("sub3, sub2, sub1"));
		labelFilterService.updateLabelFilter(filterWrapper);
	}
	
	public static void getLabelGridInfo() throws ClassNotFoundException, SQLException {
		String userEmailId = "icbm.iot@gmail.com";
		LabelDBService labelService = new LabelDBService();
		List<GridLabelWrapper> labelGridInfo = labelService.getLabelGridInfo(userEmailId, "s");
		LOG.debug("labelGridInfo {}", labelGridInfo);
	}
}
