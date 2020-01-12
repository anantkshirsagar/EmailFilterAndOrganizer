package com.emailfilter.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.dbservices.DeleteFilterDBService;
import com.emailfilter.dbservices.LabelDBService;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.LabelGridWrapper;
import com.emailfilter.model.LabelProperty;

public class DatabaseTest {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseTest.class);

	public static void main(String[] args) {
		try {
			checkLabelIsAlreadyPresent();
//			 saveLabelTest();
//			 deleteLabelTest();
//			saveLabelFilterTest();
//			updateLabel();
//			updateLabelFilterTest();
//			fetchLabelFilterDataById();
//			getLabelGridInfo();
//			getLabelFilterByFilterName();
			getDeletelFilterByFilterName();
			
		} catch (Exception e) {
			LOG.error("Exception {}", e);
		}
	}

	public static void checkLabelIsAlreadyPresent() throws ClassNotFoundException, SQLException, IOException {
		LabelProperty labelProperty = new LabelProperty();
		labelProperty.setLabel("Label2");
		labelProperty.setUserEmailId("icbm.iot@gmail.com");
		LabelDBService dbService = new LabelDBService();
		boolean duplicateLabel = dbService.isDuplicateLabel(labelProperty);
		LOG.debug("isDuplicateLabel {}", duplicateLabel);
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
		labelFilterService.saveLabelFilter(filterWrapper, "icbm.iot@gmail.com");
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
		labelFilterService.updateLabelFilter(filterWrapper, "icbm.iot@gmail.com");
	}

	public static void getLabelGridInfo() throws ClassNotFoundException, SQLException {
		String userEmailId = "icbm.iot@gmail.com";
		LabelDBService labelService = new LabelDBService();
		List<LabelGridWrapper> labelGridInfo = labelService.getLabelGridInfo(userEmailId, "s");
		LOG.debug("labelGridInfo {}", labelGridInfo);
	}

	public static void getLabelFilterByFilterName() throws ClassNotFoundException, SQLException, IOException {
		LabelFilterDBService filterService = new LabelFilterDBService();
		FilterWrapper filterWrapper = filterService.getLabelFilterByFilterName("Label filter by body", "icbm.iot@gmail.com");
		LOG.debug("filterWrapper {}", filterWrapper);
	}
	
	public static void getDeletelFilterByFilterName() throws ClassNotFoundException, SQLException, IOException {
		DeleteFilterDBService filterService = new DeleteFilterDBService();
		FilterWrapper filterWrapper = filterService.getDeleteFilterByFilterName("Delete filter by subject", "icbm.iot@gmail.com");
		LOG.debug("filterWrapper {}", filterWrapper);
	}
}
