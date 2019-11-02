package com.emailfilter.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.GmailAuth;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Labels;
import com.google.api.services.gmail.Gmail.Users.Labels.Create;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

public class LabelService {

	private static final Logger LOG = LoggerFactory.getLogger(LabelService.class.getName());
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
		LOG.debug("Creating label...");
		Label label = new Label();
		label.setName(newLabelName);
		label.setLabelListVisibility("labelShow");
		label.setMessageListVisibility("show");
		Labels labels = gmailService.users().labels();
		Create create = labels.create(userEmailId, label);
		return create.execute();
	}

	public Label patchLabel(String labelId, Label labelPatch) throws IOException {
		Label patchedLabel = gmailService.users().labels().patch(userEmailId, labelId, labelPatch).execute();
		LOG.debug("Label with id {} patched sucessfully.", labelId);
		LOG.debug(patchedLabel.toPrettyString());
		return patchedLabel;
	}

	public void updateLabel(String labelId, String newLabelName, boolean showInMessageList, boolean showInLabelList)
			throws Exception {
		LOG.debug("Updating label...");
		String msgVisibility = showInMessageList ? GmailAuth.MessageVisibility.SHOW.name()
				: GmailAuth.MessageVisibility.HIDE.name();
		String labelVisibility = showInLabelList ? GmailAuth.LabelListVisibility.labelShow.name()
				: GmailAuth.LabelListVisibility.labelHide.name();
		Label newLabel = new Label().setName(newLabelName).setMessageListVisibility(msgVisibility)
				.setLabelListVisibility(labelVisibility);
		newLabel.setId(labelId);
		newLabel = gmailService.users().labels().update(userEmailId, labelId, newLabel).execute();
		LOG.debug("Label id {}", newLabel.getId());
		LOG.debug(newLabel.toPrettyString());
	}

	public void deleteLabel(String labelId) throws Exception {
		LOG.debug("Deleting label...");
		gmailService.users().labels().delete(userEmailId, labelId).execute();
		LOG.debug("Label with id {} deleted successfully.", labelId);
	}
}
