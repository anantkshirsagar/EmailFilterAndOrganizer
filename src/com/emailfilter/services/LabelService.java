package com.emailfilter.services;

import java.util.logging.Logger;

import com.emailfilter.constants.GmailAuth;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Labels;
import com.google.api.services.gmail.Gmail.Users.Labels.Create;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

public class LabelService {

	private static final Logger LOG = Logger.getLogger(LabelService.class.getName());
	private final String userEmailId;
	private final Gmail gmailService;

	public LabelService(String userEmailId, Gmail gmailService) {
		this.userEmailId = userEmailId;
		this.gmailService = gmailService;
	}

	public java.util.List<Label> getLabels() throws Exception {
		ListLabelsResponse listResponse = gmailService.users().labels().list(userEmailId).execute();
		return listResponse.getLabels();
	}

	public Label createLabel(String newLabelName) throws Exception {
		LOG.info("Creating label...");
		Label label = new Label();
		label.setName(newLabelName);
		label.setLabelListVisibility("labelShow");
		label.setMessageListVisibility("show");
		Labels labels = gmailService.users().labels();
		Create create = labels.create(userEmailId, label);
		return create.execute();
	}

	public void updateLabel(String labelId, String newLabelName, boolean showInMessageList, boolean showInLabelList)
			throws Exception {
		LOG.info("Updating label...");
		String msgVisibility = showInMessageList ? GmailAuth.MessageVisibility.SHOW.name()
				: GmailAuth.MessageVisibility.HIDE.name();
		String labelVisibility = showInLabelList ? GmailAuth.LabelListVisibility.labelShow.name()
				: GmailAuth.LabelListVisibility.labelHide.name();
		Label newLabel = new Label().setName(newLabelName).setMessageListVisibility(msgVisibility)
				.setLabelListVisibility(labelVisibility);
		newLabel.setId(labelId);
		newLabel = gmailService.users().labels().update(userEmailId, labelId, newLabel).execute();
		LOG.info("Label id: " + newLabel.getId());
		LOG.info(newLabel.toPrettyString());
	}

	public void deleteLabel(String labelId) throws Exception {
		LOG.info("Deleting label...");
		gmailService.users().labels().delete(userEmailId, labelId).execute();
		LOG.info("Label with id: " + labelId + " deleted successfully.");
	}
}
