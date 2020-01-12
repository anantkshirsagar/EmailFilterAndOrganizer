package com.emailfilter.services.run;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.AppConstants;
import com.emailfilter.constants.RunStatus;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.dbservices.RunManagerDBService;
import com.emailfilter.model.FilterRunContext;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.GmailMessage;
import com.emailfilter.model.RunDetails;
import com.emailfilter.services.LabelService;
import com.emailfilter.services.MessageService;
import com.emailfilter.utils.AppUtils;
import com.emailfilter.utils.RunUtils;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;

public class LabelFilterRun implements IFilterRun {

	private static final Logger LOG = LoggerFactory.getLogger(LabelFilterRun.class);
	private final FilterRunContext filterContext;
	private String runName;

	public LabelFilterRun(FilterRunContext filterContext) {
		this.filterContext = filterContext;
	}

	@Override
	public void run() {
		try {
			runName = Thread.currentThread().getName();
			createRun(filterContext.getFilterWrapper().getFilterName(), runName, RunStatus.PROCESSING);
			execute();
		} catch (Exception e) {
			getCurrentRunAndUpdate(RunStatus.FAILED, e.getMessage());
			LOG.error(" Exception occured while executing LabelFilterRun {}", e);
		}
	}

	@Override
	public void execute() throws Exception {
		LOG.debug("filterWrapper {}, userEmailId {}", filterContext.getFilterWrapper(), filterContext.getUserEmailId());
		if (filterContext.getFilterWrapper().isEmailFilter()) {
			LOG.debug("Email filter in process...");
			emailFilter(filterContext.getFilterWrapper(), filterContext.getUserEmailId());
		}

		if (filterContext.getFilterWrapper().isBodyFilter()) {
			LOG.debug("Body filter in process...");
			bodyFilter(filterContext.getFilterWrapper(), filterContext.getUserEmailId());
		}

		if (filterContext.getFilterWrapper().isSubjectFilter()) {
			LOG.debug("Subject filter in process...");
			subjectFilter(filterContext.getFilterWrapper(), filterContext.getUserEmailId());
		}
		getCurrentRunAndUpdate(RunStatus.COMPLETE, "");
	}

	@Override
	public void emailFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {
		MessageService messageService = filterContext.getMessageService();

		List<Message> messages = messageService.getMessagesByLabelIds(Arrays.asList(AppConstants.INBOX_ID));
		LOG.debug("Messages size: {}", AppUtils.isCollectionNotEmpty(messages) ? messages.size() : 0);

		for (Message message : messages) {
			MimeMessage mimeMessage = messageService.getMimeMessage(message.getId());
			GmailMessage gmailMessage = messageService.getGmailMessage(mimeMessage);
			List<Address> allRecipients = gmailMessage.getAllRecipients();
			List<Address> sentFrom = gmailMessage.getFrom();
			List<Address> sentTo = gmailMessage.getReplyTo();
			List<Address> allEmailAddress = new ArrayList<Address>();
			allEmailAddress.addAll(allRecipients);
			allEmailAddress.addAll(sentFrom);
			allEmailAddress.addAll(sentTo);

			if (RunUtils.isReceipientContainsFilterMailAddresses(allEmailAddress, filterWrapper.getEmailIds())) {
				LOG.debug("Email ids matched with message id [{}], subject {}", message.getId(),
						gmailMessage.getSubject());
				moveMessages(filterWrapper.getLabel(), message);
			}
		}
		
	}

	@Override
	public void bodyFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {
		MessageService messageService = filterContext.getMessageService();

		List<Message> messages = messageService.getMessagesByLabelIds(Arrays.asList(AppConstants.INBOX_ID));
		LOG.debug("Messages size: {}", AppUtils.isCollectionNotEmpty(messages) ? messages.size() : 0);

		for (Message message : messages) {
			MimeMessage mimeMessage = messageService.getMimeMessage(message.getId());
			GmailMessage gmailMessage = messageService.getGmailMessage(mimeMessage);
			String body = gmailMessage.getBody();
			List<String> bodyKeywords = filterWrapper.getBodyKeywords();
			LOG.debug("bodyKeywords {}", bodyKeywords);
			if (RunUtils.isStringContainsKeywords(body, bodyKeywords)) {
				LOG.debug("Body keywords found in message id [{}] with subject {}", message.getId(),
						gmailMessage.getSubject());
				moveMessages(filterWrapper.getLabel(), message);
			}
		}
	}

	@Override
	public void subjectFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {
		MessageService messageService = filterContext.getMessageService();

		List<Message> messages = messageService.getMessagesByLabelIds(Arrays.asList(AppConstants.INBOX_ID));
		LOG.debug("Messages size: {}", AppUtils.isCollectionNotEmpty(messages) ? messages.size() : 0);

		for (Message message : messages) {
			MimeMessage mimeMessage = messageService.getMimeMessage(message.getId());
			GmailMessage gmailMessage = messageService.getGmailMessage(mimeMessage);

			String subject = gmailMessage.getSubject();
			List<String> subjectKeywords = filterWrapper.getSubjectKeywords();
			if (RunUtils.isStringContainsKeywords(subject, subjectKeywords)) {
				LOG.debug("Subject keywords found in message id [{}] with subject {}", message.getId(),
						gmailMessage.getSubject());
				moveMessages(filterWrapper.getLabel(), message);
			}
		}
	}

	/**
	 * This method moves mails from source to destination label, when filter
	 * condition matches with the gmail message.
	 * 
	 * @param filterLabel
	 * @param message
	 * @throws Exception
	 */
	private void moveMessages(String filterLabel, Message message) throws Exception {
		LabelService labelService = filterContext.getLabelService();
		Label gmailLabel = AppUtils.matchLabelNameAndGetGmailLabel(filterLabel, labelService.getFilteredLabel());
		List<String> labelsToAdd = Arrays.asList(gmailLabel.getId());
		List<String> labelsToRemove = AppConstants.getLabelsToRemove();
		LOG.debug("labelsToRemove {}, labelsToAdd {}", labelsToRemove, labelsToAdd);
		filterContext.getMessageService().moveMessageFromLabel(message.getId(), labelsToAdd, labelsToRemove);
		LOG.debug("Message [id: {}] moved from label [{}] to [{}] ", message.getId(), labelsToRemove.get(0),
				labelsToAdd.get(0));
	}

	private RunDetails getRunDetailsObject(int labelFilterId, String runName, RunStatus runStatus,
			String failureReason) {
		RunDetails runDetails = new RunDetails();
		runDetails.setLabelFilterId(labelFilterId);
		runDetails.setRunName(runName);
		runDetails.setRunStatus(runStatus);
		runDetails.setFailureReason(failureReason);
		return runDetails;
	}

	private void createRun(String filterName, String runName, RunStatus runStatus)
			throws ClassNotFoundException, SQLException, IOException {
		RunManagerDBService runService = filterContext.getRunManagerService();
		LabelFilterDBService labelFilterService = filterContext.getLabelFilterService();
		FilterWrapper labelFilterWrapper = labelFilterService.getLabelFilterByFilterName(filterName,
				filterContext.getUserEmailId());
		RunDetails runDetails = getRunDetailsObject(labelFilterWrapper.getId(), runName, runStatus, null);
		runService.saveRun(runDetails, true);
		LOG.debug("Run created with status: {}", runStatus);
	}

	private void getCurrentRunAndUpdate(RunStatus runStatus, String failureReason) {
		RunManagerDBService runService = filterContext.getRunManagerService();
		RunDetails currentRunDetails = null;
		try {
			currentRunDetails = runService.getCurrentLabelFilterRunDetails(filterContext.getFilterWrapper().getFilterName(),
					runName, filterContext.getUserEmailId());
			currentRunDetails.setRunStatus(runStatus);
			currentRunDetails.setFailureReason(failureReason);
			runService.updateRun(currentRunDetails, currentRunDetails.getId(), true);
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("getCurrentRunAndUpdate {}", e);
		}
	}
}
