package com.emailfilter.utils;

import java.util.List;

import javax.mail.Address;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.dbservices.DeleteFilterDBService;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.dbservices.RunManagerDBService;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.FilterRunContext;
import com.emailfilter.services.GmailService;
import com.emailfilter.services.LabelService;
import com.emailfilter.services.MessageService;
import com.google.api.services.gmail.Gmail;

public class RunUtils {

	private RunUtils() {

	}

	private static final Logger LOG = LoggerFactory.getLogger(RunUtils.class);

	public static FilterRunContext getLabelFilterRunContext(FilterWrapper filterWrapper, String userEmailId)
			throws Exception {
		LOG.debug("Creating label filter run context");
		FilterRunContext runContext = new FilterRunContext();
		Gmail gmailService = new GmailService().getGmailService();
		runContext.setFilterWrapper(filterWrapper);
		runContext.setUserEmailId(userEmailId);
		runContext.setLabelFilterService(new LabelFilterDBService());
		runContext.setMessageService(new MessageService(userEmailId, gmailService));
		runContext.setRunManagerService(new RunManagerDBService());
		runContext.setLabelService(new LabelService(userEmailId, gmailService));
		return runContext;
	}

	public static FilterRunContext getDeleteFilterRunContext(FilterWrapper filterWrapper, String userEmailId)
			throws Exception {
		LOG.debug("Creating label filter run context");
		FilterRunContext runContext = new FilterRunContext();
		Gmail gmailService = new GmailService().getGmailService();
		runContext.setFilterWrapper(filterWrapper);
		runContext.setUserEmailId(userEmailId);
		runContext.setDeleteFilterService(new DeleteFilterDBService());
		runContext.setMessageService(new MessageService(userEmailId, gmailService));
		runContext.setRunManagerService(new RunManagerDBService());
		runContext.setLabelService(new LabelService(userEmailId, gmailService));
		return runContext;
	}

	public static boolean isReceipientContainsFilterMailAddresses(List<Address> receipients, List<String> emailIds) {
		List<Address> filterEmailIds = AppUtils.convertStringToAddress(emailIds);
		return CollectionUtils.containsAny(receipients, filterEmailIds);
	}

	public static boolean isStringContainsKeywords(String string, List<String> filterKeywords) {
		for (String keyword : filterKeywords) {
			if (string.toLowerCase().contains(keyword.trim().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
